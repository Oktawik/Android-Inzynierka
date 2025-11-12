package com.example.pierwszawersjaapki.FDC_API; // Użyj swojego pakietu

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Model reprezentujący JEDEN produkt na liście wyników wyszukiwania
 * (z endpointu /foods/search)
 */
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
    private List<FoodNutrient> foodNutrients; // Używa Twojej starej klasy FoodNutrient

    // ⭐ NOWE POLE (1)
    @SerializedName("householdServingFullText")
    private String householdServingFullText; // np. "0.25 cup"

    // ⭐ NOWE POLE (2)
    @SerializedName("packageWeight")
    private String packageWeight; // np. "1.64 kg"

    // --- Gettery ---

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

    // --- NOWE GETTERY ---

    public String getHouseholdServingFullText() {
        return householdServingFullText;
    }

    public String getPackageWeight() {
        return packageWeight;
    }
}