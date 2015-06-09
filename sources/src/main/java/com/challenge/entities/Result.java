package com.challenge.entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YULIAT on 06/08/2015.
 */
public class Result implements Serializable {

    public Result (String product_name)
    {
        setProduct_name(product_name);
    }

    public List<Listing> getListings() {
        return listings;
    }

    public void setListings(List<Listing> listings) {
        this.listings = listings;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    String product_name;
    List<Listing> listings = new LinkedList<>();


}
