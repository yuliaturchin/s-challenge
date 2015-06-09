package com.challenge.entities;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by YULIAT on 06/07/2015.
 */
public class Product implements Serializable {

    private String manufacturer;
    private String model;
    private String family;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("announced-date")
    private String announcedDate;

    public String getProductName() {
        return productName;
    }

    public void setProduct_name(String product_name) {
        this.productName = product_name;
    }



    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }


    public String getAnnouncedDate() {
        return announcedDate;
    }

    public void setAnnouncedDate(String announced_date) {
        this.announcedDate = announced_date;
    }

}
