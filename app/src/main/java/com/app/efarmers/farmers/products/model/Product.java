package com.app.efarmers.farmers.products.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;
    @SerializedName("value")
    private String value;
    @SerializedName("image")
    private String image;

    @SerializedName("stock")
    private String stock;


    @SerializedName("farmer_cell")
    private String farmer_cell;

    @SerializedName("description")
    private String description;



    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }

    public String getImage() {
        return image;
    }

    public String getValue() {
        return value;
    }


    public String getDescription() {
        return description;
    }

    public String getFarmerCell() {
        return farmer_cell;
    }

    public String getProductId() {
        return product_id;
    }


}