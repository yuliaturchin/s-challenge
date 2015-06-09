package com.challenge.match;

import com.challenge.entities.Result;

import java.io.BufferedReader;
import java.util.Map;

/**
 * Created by YULIAT on 06/09/2015.
 */
public interface ListingMatcher {

    /**
     * Executes the matching process based on provided products and listings.
     * Buffers are used here in order to avoid redundant big data manipulation
     * @param productsReader
     * @param listingsReader
     * @return
     */
    Map<String, Result> executeMatch(BufferedReader productsReader, BufferedReader listingsReader);
}
