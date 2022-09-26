package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class SalesReport {


    @SerializedName("total_order_price")
    private String totalOrderPrice;

    @SerializedName("total_tax")
    private String totalTax;
    @SerializedName("total_discount")
    private String totalDiscount;


    @SerializedName("value")
    private String value;


    public String getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }


    public String getValue() {
        return value;
    }

}