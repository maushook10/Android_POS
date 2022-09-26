package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class Category {


    @SerializedName("product_category_id")
    private String productCategoryId;


    @SerializedName("product_category_name")
    private String productCategoryName;


    @SerializedName("value")
    private String value;


    public String getProductCategoryId() {
        return productCategoryId;
    }



    public String getProductCategoryName() {
        return productCategoryName;
    }


    public String getValue() {
        return value;
    }


}