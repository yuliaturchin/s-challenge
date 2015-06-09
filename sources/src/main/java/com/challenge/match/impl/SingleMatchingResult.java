package com.challenge.match.impl;

import com.challenge.entities.Listing;
import com.challenge.entities.Product;

class SingleMatchingResult {

    public SingleMatchingResult(Listing listing, Product product) {
        this.listing = listing;
        this.product = product;
    }

    public Listing getListing() {
        return listing;
    }

    public Product getMatchedProduct() {
        return product;
    }

    private Listing listing;
    private Product product;

}
