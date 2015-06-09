package com.challenge.match.impl;

import com.challenge.entities.Listing;
import com.challenge.entities.Product;
import com.challenge.entities.Result;
import com.challenge.executer.TasksConcurrentExecutor;
import com.challenge.executer.impl.TasksConcurrentExecutorImpl;
import com.challenge.match.ListingMatcher;
import com.challenge.search.SearchEngine;
import com.challenge.search.impl.SearchConfiguration;
import com.challenge.search.impl.SearchEngineImpl;
import com.challenge.utils.CommonMethods;
import com.challenge.utils.json.JSONUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YULIAT on 06/07/2015.
 */
public class ListingsMatcherImpl implements ListingMatcher {

    private final static String TOKENS_SEPARATOR = " ";
    private final static String TITLE_PROPERTY = "TITLE";

    private int coreThreadsPool=5;
    private int maxThreadsPool=7;

    private final SearchEngine engine;

    public ListingsMatcherImpl()
    {
        SearchConfiguration configuration = new SearchConfiguration();
        configuration.setTermsSeparator(TOKENS_SEPARATOR);
        engine = new SearchEngineImpl<Product>(configuration);
    }

    @Override
    public Map<String, Result> executeMatch(BufferedReader productsReader, BufferedReader listingsReader)  {

        System.out.println("Retrieving Products Catalog...");
        long startTime = System.currentTimeMillis();
        /************************/
        uploadProducts(productsReader);
        /***********************/
        System.out.println("Finished Retrieving Products Catalog...took " + (System.currentTimeMillis()-startTime) + " msec");

        System.out.println("********************************");
        System.out.println("Started Matching Listings to retrieved Products...");
        long startMatch = System.currentTimeMillis();
        /***********************/
        Map<String, Result> product2Results = matchListingsToProducts(listingsReader);
        /***********************/
        System.out.println("Finished matching Listings...took " + (System.currentTimeMillis()-startMatch) + " msec");

        return product2Results;
    }


    private void uploadProducts (BufferedReader bufferedReader)  {
        String input;

        int counter = 0;
        try {
            while ((input = bufferedReader.readLine()) != null) {
                Product product = JSONUtils.convertJsonStringToObject(input, Product.class);
                registerProduct(product);
                counter ++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Uploaded " + counter +" products.");
        System.out.println(engine.getTermStatisticsSummary());
    }

    private Map<String, Result> matchListingsToProducts (BufferedReader bufferedReader)  {
        String listingString;
        TasksConcurrentExecutor<SingleMatchingResult, Map<String, Result>> concurrentMatcher = new TasksConcurrentExecutorImpl(coreThreadsPool,maxThreadsPool);
        concurrentMatcher.start();
        try {

            while ((listingString = bufferedReader.readLine()) != null) {
                concurrentMatcher.submitTask(new MatchingTaskUnit(listingString, engine));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }

        System.out.println("Submitted Matching Tasks to MatchingExecutor. Collecting results from MatchingExecutor...");
        long start = System.currentTimeMillis();
        /************************/
        Map<String, Result> resultMap =  concurrentMatcher.collectResults(new MatchingResultsCollector());
        /************************/
        System.out.println("Finished collecting results from Matching Executors... " + (System.currentTimeMillis()-start) + " msec");

        concurrentMatcher.shutdown();
        return resultMap;
    }


    private void registerProduct (Product product)
    {
        String normalizedName = CommonMethods.normalizeString(product.getProductName(), TOKENS_SEPARATOR);
        engine.register(product, Collections.singletonMap(TITLE_PROPERTY, normalizedName));
    }



    class MatchingResultsCollector implements  TasksConcurrentExecutor.ResultsCollector<SingleMatchingResult, Map<String, Result>>
    {
        private Map<String, Result> product2Results = new HashMap<>();
        private int unmatched = 0;
        private int matched = 0;

        @Override
        public void collectResult(SingleMatchingResult result) {

                Product product = result.getMatchedProduct();
                Listing listing = result.getListing();

                if (product == null) {
                    unmatched++;
                    return;
                }
                if (product2Results.get(product.getProductName()) == null) {
                    product2Results.put(product.getProductName(), new Result(product.getProductName()));
                }
                product2Results.get(product.getProductName()).getListings().add(listing);
                matched++;
            }

        @Override
        public Map<String, Result> getCollectedResults() {
            System.out.println(String.format("Matched Listings: %s, Unmatched Listings: %s", matched, unmatched));
            return product2Results;
        }

    }

}