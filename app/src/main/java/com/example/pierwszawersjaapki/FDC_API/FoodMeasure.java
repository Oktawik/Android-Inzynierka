package com.example.pierwszawersjaapki.FDC_API; // Użyj swojego pakietu

import com.google.gson.annotations.SerializedName;

public class FoodMeasure {

    // Te pola nie będą już wypełniane przez Retrofit, ale przez nas
    private String measureName;
    private double gramWeight;

    // ⭐ KLUCZOWY KONSTRUKTOR: Upewnij się, że go masz
    public FoodMeasure(String measureName, double gramWeight) {
        this.measureName = measureName;
        this.gramWeight = gramWeight;
    }

    // --- Gettery ---
    public String getMeasureName() {
        return measureName;
    }

    public double getGramWeight() {
        return gramWeight;
    }
}