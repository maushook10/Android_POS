package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class MonthData {


    @SerializedName("jan")
    private String jan;

    @SerializedName("feb")
    private String feb;

    @SerializedName("mar")
    private String mar;

    @SerializedName("apr")
    private String apr;

    @SerializedName("may")
    private String may;

    @SerializedName("jun")
    private String jun;

    @SerializedName("jul")
    private String jul;

    @SerializedName("aug")
    private String aug;

    @SerializedName("sep")
    private String sep;

    @SerializedName("oct")
    private String oct;

    @SerializedName("nov")
    private String nov;

    @SerializedName("dec")
    private String dec;


    @SerializedName("value")
    private String value;


    @SerializedName("total_order_price")
    private float totalOrderPrice;


    @SerializedName("total_tax")
    private float totalTax;


    @SerializedName("total_discount")
    private float totalDiscount;


    public String getJan() {
        return jan;
    }
    public String getFeb() {
        return feb;
    }
    public String getMar() {
        return mar;
    }
    public String getApr() {
        return apr;
    }
    public String getMay() {
        return may;
    }
    public String getJun() {
        return jun;
    }
    public String getJul() {
        return jul;
    }
    public String getAug() {
        return aug;
    }
    public String getSep() {
        return sep;
    }
    public String getOct() {
        return oct;
    }
    public String getNov() {
        return nov;
    }
    public String getDec() {
        return dec;
    }


    public float getTotalOrderPrice() {
        return totalOrderPrice;
    }
    public float getTotalTax() {
        return totalTax;
    }
    public float getTotalDiscount() {
        return totalDiscount;
    }



    public String getValue() {
        return value;
    }

}