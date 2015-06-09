package com.challenge.match.impl;

import com.challenge.entities.Listing;
import com.challenge.entities.Product;
import com.challenge.search.SearchEngine;
import com.challenge.search.TermStatistics;
import com.challenge.utils.CommonMethods;
import com.challenge.utils.json.JSONUtils;

import java.util.List;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.Callable;

/**
 * Created by YULIAT on 06/09/2015.
 */
class MatchingTaskUnit implements Callable<SingleMatchingResult>
{
    private final static String EMPTY_DELIMITER = "";
    private final static String TITLE_PROPERTY = "TITLE";

    private String listingString;
    private SearchEngine engine;
    public MatchingTaskUnit(String listing, SearchEngine engine)
    {
        this.listingString = listing;
        this.engine = engine;
    }

    @Override
    public SingleMatchingResult call() {
        final Listing listing = JSONUtils.convertJsonStringToObject(listingString, Listing.class);
        Product product = matchListingToProduct(listing);
        return new SingleMatchingResult(listing, product);

    }

    private Product matchListingToProduct(Listing listing) {
        //sort terms from the least frequent to the most frequent
        SortedSet<TermStatistics> sortedTermsByStatistics = resolveSortedTermsInListing(listing);

        //start checking products having least frequent term among the listing terms - heuristics for having better chance of not performing full-scan of terms in the listing
        for (TermStatistics termStats: sortedTermsByStatistics)
        {
            List<Product> candidateProducts = engine.search(TITLE_PROPERTY, SearchEngine.Operator.CONTAINS, termStats.getTerm());
            Product matchingProduct = findFirstMatchingProduct(listing, candidateProducts);
            if (matchingProduct!=null)
            {
                return matchingProduct;
            }
        }
        return null;
    }


    private Product findFirstMatchingProduct(Listing listing, List<Product> candidateProducts) {
        if (candidateProducts==null || listing==null)
        {
            return null;
        }

        String normalizedListingTitle = CommonMethods.normalizeString(listing.getTitle(), EMPTY_DELIMITER);
        String normalizedListingManuf = CommonMethods.normalizeString(listing.getManufacturer(), EMPTY_DELIMITER);

        for (Product candidateProduct: candidateProducts)
        {
            //check first as it is shorter
            String normalizedProductManuf = CommonMethods.normalizeString(candidateProduct.getManufacturer(), EMPTY_DELIMITER);
            boolean isListingMatchesManufacturer = isMatches (normalizedListingManuf, normalizedProductManuf);
            if (!isListingMatchesManufacturer)
            {
                continue;
            }

            String normalizedProductTitle = CommonMethods.normalizeString(candidateProduct.getProductName(), EMPTY_DELIMITER);
            boolean isListingMatchesProductName = isMatches (normalizedListingTitle, normalizedProductTitle);
            if (isListingMatchesProductName)
            {
                return candidateProduct;
            }
        }
        return null;
    }

    private boolean isMatches(String sourceString, String targetString) {

        return sourceString.contains(targetString);
    }


    /**
     * Returns SortedSet of terms in ascending order of TermStatistics.
     * @param listing
     * @return
     */
    private SortedSet<TermStatistics> resolveSortedTermsInListing(Listing listing) {
        StringTokenizer stringTokenizer = CommonMethods.getNormalizedTokens(listing.getTitle());

        SortedSet<TermStatistics> termsStatistics = new TreeSet<>();
        while (stringTokenizer.hasMoreTokens()) {
            String termToSearch = stringTokenizer.nextToken();
            TermStatistics termStatistics  = engine.getTermStatisticsByProperty(TITLE_PROPERTY, termToSearch);
            if (termStatistics!=null)
            {
                termsStatistics.add(termStatistics);
            }
        }
        return termsStatistics;
    }




}

