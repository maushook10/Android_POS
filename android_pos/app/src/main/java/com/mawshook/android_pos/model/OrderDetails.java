package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class OrderDetails {



    @SerializedName("order_details_id")
    private String orderDetailsId;

    @SerializedName("invoice_id")
    private String invoiceId;

    @SerializedName("product_order_date")
    private String productOrderDate;
    @SerializedName("product_name")
    private String productName;

    @SerializedName("product_quantity")
    private String productQuantity;

    @SerializedName("product_weight")
    private String productWeight;

    @SerializedName("product_price")
    private String productPrice;

    @SerializedName("value")
    private String value;

    @SerializedName("product_image")
    private String productImage;


    public String getInvoiceId() {
        return invoiceId;
    }


    public String getProductName() {
        return productName;
    }

    public String getProductOrderDate() {
        return productOrderDate;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }


    public String getValue() {
        return value;
    }

    public String getProductImage() {
        return productImage;
    }


    public String getProductWeight() {
        return productWeight;
    }


    public String getOrderDetailsId() {
        return orderDetailsId;
    }





}