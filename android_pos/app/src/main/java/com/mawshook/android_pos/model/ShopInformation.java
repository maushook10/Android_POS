package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class ShopInformation {


    @SerializedName("shop_id")
    private String shopId;

    @SerializedName("shop_name")
    private String shopName;
    @SerializedName("shop_email")
    private String shopEmail;

    @SerializedName("shop_contact")
    private String shopContact;

    @SerializedName("shop_address")
    private String shopAddress;

    @SerializedName("value")
    private String value;

    @SerializedName("tax")
    private String tax;


    public String getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public String getShopContact() {
        return shopContact;
    }

    public String getShopAddress() {
        return shopAddress;
    }


    public String getValue() {
        return value;
    }

    public String getTax() {
        return tax;
    }
}