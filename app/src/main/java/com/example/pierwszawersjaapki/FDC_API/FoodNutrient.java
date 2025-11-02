package com.example.pierwszawersjaapki.FDC_API;

import com.google.gson.annotations.SerializedName;

public class FoodNutrient {
    @SerializedName("nutrientName")
    private String nutrientName;

    @SerializedName("value")
    private double value;

    @SerializedName("unitName")
    private String unitName;

    public String getNutrientName() {
        return nutrientName;
    }

    public double getValue() {
        return value;
    }

    public String getUnitName() {
        return unitName;
    }
}
