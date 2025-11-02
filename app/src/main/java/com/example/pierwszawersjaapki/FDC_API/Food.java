package com.example.pierwszawersjaapki.FDC_API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Food {
    @SerializedName("fdcId")
    private long fdcId;

    @SerializedName("description")
    private String description;

    @SerializedName("brandOwner")
    private String brandOwner;

    @SerializedName("brandName")
    private String brandName;

    @SerializedName("ingredients")
    private String ingredients;

    @SerializedName("servingSize")
    private double servingSize;

    @SerializedName("servingSizeUnit")
    private String servingSizeUnit;

    @SerializedName("foodNutrients")
    private List<FoodNutrient> foodNutrients;

    public long getFdcId() {
        return fdcId;
    }

    public String getDescription() {
        return description;
    }

    public String getBrandOwner() {
        return brandOwner;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public double getServingSize() {
        return servingSize;
    }

    public String getServingSizeUnit() {
        return servingSizeUnit;
    }

    public List<FoodNutrient> getFoodNutrients() {
        return foodNutrients;
    }
}
