package com.example.pierwszawersjaapki.FDC_API;

import com.google.gson.annotations.SerializedName;

public class Nutrient {
    @SerializedName("name")
    private String name;

    @SerializedName("unitName")
    private String unitName;

    public String getName() {
        return name;
    }

    public String getUnitName() {
        return unitName;
    }
}
