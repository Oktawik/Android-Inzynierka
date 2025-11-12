package com.example.pierwszawersjaapki.FDC_API; // Użyj swojego pakietu

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FoodDetailsResponse {

    @SerializedName("description")
    private String description; // Nazwa produktu

    @SerializedName("fdcId")
    private long fdcId;

    @SerializedName("ingredients")
    private String ingredients; // Składniki

    @SerializedName("foodNutrients")
    private List<FoodNutrientDetail> foodNutrients; // Pełne wartości odżywcze

    // --- NOWE POLA DO OBSŁUGI PORCJI Z PRODUKTÓW MARKOWYCH ---

    @SerializedName("servingSize")
    private double servingSize; // np. 30.0

    @SerializedName("servingSizeUnit")
    private String servingSizeUnit; // np. "g"

    @SerializedName("householdServingFullText")
    private String householdServingFullText; // np. "0.25 cup"

    @SerializedName("packageWeight")
    private String packageWeight;

    // --- Gettery ---

    public String getDescription() {
        return description;
    }

    public long getFdcId() {
        return fdcId;
    }

    public String getIngredients() {
        return ingredients;
    }

    public List<FoodNutrientDetail> getFoodNutrients() {
        return foodNutrients;
    }

    // --- Gettery dla nowych pól ---

    public double getServingSize() {
        return servingSize;
    }

    public String getServingSizeUnit() {
        return servingSizeUnit;
    }

    public String getHouseholdServingFullText() {
        return householdServingFullText;
    }

    public String getPackageWeight() {
        return packageWeight;
    }
}