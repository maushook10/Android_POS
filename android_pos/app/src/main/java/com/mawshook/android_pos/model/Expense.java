package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class Expense {


    @SerializedName("expense_id")
    private String expenseId;

    @SerializedName("expense_name")
    private String expenseName;

    @SerializedName("expense_note")
    private String expenseNote;

    @SerializedName("expense_amount")
    private String expenseAmount;

    @SerializedName("expense_date")
    private String expenseDate;


    @SerializedName("expense_time")
    private String expenseTime;

    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public String getExpenseId() {
        return expenseId;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public String getExpenseNote() {
        return expenseNote;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public String getExpenseTime() {
        return expenseTime;
    }


    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }
}