package com.mawshook.android_pos.model;

import com.google.gson.annotations.SerializedName;

public class WeightUnit {


    @SerializedName("weight_unit_id")
    private String weightUnitId;


    @SerializedName("weight_unit_name")
    private String weightUnitName;


    @SerializedName("value")
    private String value;


    public String getWeightUnitId() {
        return weightUnitId;
    }



    public String getWeightUnitName() {
        return weightUnitName;
    }


    public String getValue() {
        return value;
    }


}