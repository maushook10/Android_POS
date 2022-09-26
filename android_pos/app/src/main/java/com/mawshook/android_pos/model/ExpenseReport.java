package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class ExpenseReport {


    @SerializedName("total_expense_price")
    private String totalExpensePrice;



    @SerializedName("value")
    private String value;


    public String getTotalExpensePrice() {
        return totalExpensePrice;
    }



    public String getValue() {
        return value;
    }

}