package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class Suppliers {


    @SerializedName("suppliers_id")
    private String suppliersId;

    @SerializedName("suppliers_name")
    private String suppliersName;
    @SerializedName("suppliers_email")
    private String suppliersEmail;

    @SerializedName("suppliers_cell")
    private String suppliersCell;

    @SerializedName("suppliers_address")
    private String suppliersAddress;

    @SerializedName("suppliers_contact_person")
    private String suppliersContactPerson;

    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public String getSuppliersId() {
        return suppliersId;
    }

    public String getSuppliersName() {
        return suppliersName;
    }

    public String getSuppliersEmail() {
        return suppliersEmail;
    }

    public String getSuppliersCell() {
        return suppliersCell;
    }

    public String getSuppliersAddress() {
        return suppliersAddress;
    }


    public String getSuppliersContactPerson() {
        return suppliersContactPerson;
    }


    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }
}