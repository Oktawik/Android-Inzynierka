package com.example.pierwszawersjaapki.FDC_API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodSearchResponse {
    @SerializedName("totalHits")
    private int totalHits;

    @SerializedName("currentPage")
    private int currentPage;

    @SerializedName("foods")
    private List<Food> foods;

    public int getTotalHits() {
        return totalHits;
    }

    public List<Food> getFoods() {
        return foods;
    }
}
