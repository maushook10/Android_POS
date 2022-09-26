package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class Product {


    @SerializedName("product_id")
    private String productId;

    @SerializedName("product_name")
    private String productName;
    @SerializedName("product_code")
    private String product_code;

    @SerializedName("product_category_id")
    private String productCategoryId;

    @SerializedName("product_category_name")
    private String productCategoryName;

    @SerializedName("product_description")
    private String productDescription;


    @SerializedName("product_sell_price")
    private String productSellPrice;


    @SerializedName("product_stock")
    private String productStock;


    @SerializedName("product_weight")
    private String productWeight;


    @SerializedName("weight_unit_name")
    private String productWeightUnit;


    @SerializedName("weight_unit_id")
    private String productWeightUnitId;


    @SerializedName("product_supplier_id")
    private String productSupplierId;

    @SerializedName("suppliers_name")
    private String supplierName;


    @SerializedName("product_image")
    private String productImage;


    @SerializedName("tax")
    private String tax;

    @SerializedName("value")
    private String value;

    @SerializedName("message")
    private String massage;

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProduct_code() {
        return product_code;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public String getProductDescription() {
        return productDescription;
    }


    public String getProductSellPrice() {
        return productSellPrice;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public String getProductWeightUnit() {
        return productWeightUnit;
    }

    public String getProductSupplierID() {
        return productSupplierId;
    }

    public String getProductSupplierName() {
        return supplierName;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductStock() {
        return productStock;
    }

    public String getProductWeightUnitId() {
        return productWeightUnitId;
    }

    public String getValue() {
        return value;
    }

    public String getTax() {
        return tax;
    }


}