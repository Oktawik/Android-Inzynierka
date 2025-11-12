package com.example.pierwszawersjaapki.FDC_API;

import com.google.gson.annotations.SerializedName;

public class FoodNutrientDetail {
    @SerializedName("nutrient")
    private Nutrient nutrient;

    @SerializedName("amount")
    private double amount;

    public Nutrient getNutrient() {
        return nutrient;
    }

    public double getAmount() {
        return amount;
    }
}
