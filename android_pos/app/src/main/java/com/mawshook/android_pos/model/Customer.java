package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class Customer {


    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("customer_name")
    private String customerName;
    @SerializedName("customer_email")
    private String customerEmail;

    @SerializedName("customer_cell")
    private String customerCell;

    @SerializedName("customer_address")
    private String customerAddress;

    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerCell() {
        return customerCell;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }


    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }
}