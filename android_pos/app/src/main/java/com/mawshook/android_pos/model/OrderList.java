package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class OrderList {



    @SerializedName("order_id")
    private String orderId;

    @SerializedName("invoice_id")
    private String invoiceId;

    @SerializedName("order_date")
    private String orderDate;
    @SerializedName("order_time")
    private String orderTime;

    @SerializedName("order_type")
    private String orderType;


    @SerializedName("order_price")
    private String orderPrice;


    @SerializedName("order_payment_method")
    private String orderPaymentMethod;

    @SerializedName("discount")
    private String discount;

    @SerializedName("tax")
    private String tax;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("order_note")
    private String orderNote;


    @SerializedName("served_by")
    private String servedBy;

    @SerializedName("value")
    private String value;


    public String getInvoiceId() {
        return invoiceId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getOrderPaymentMethod() {
        return orderPaymentMethod;
    }


    public String getValue() {
        return value;
    }

    public String getServedBy() {
        return servedBy;
    }

    public String getTax() {
        return tax;
    }

    public String getDiscount() {
        return discount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public String getOrderPrice() {
        return orderPrice;
    }




}