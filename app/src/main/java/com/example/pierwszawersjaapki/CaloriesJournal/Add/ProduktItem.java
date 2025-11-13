package com.example.pierwszawersjaapki.CaloriesJournal.Add; // Użyj swojego pakietu

import com.example.pierwszawersjaapki.FDC_API.FoodNutrient;

import java.io.Serializable;
import java.util.List;

public class ProduktItem implements Serializable {
    long id;
    String nazwa;
    int ilosc_kalorii;
    int bialko;
    int weglowodany;
    int tluszcze;
    int jedna_porcja; // Możesz to usunąć, jeśli nie używasz
    String householdServing; // cup
    double servingSize; // g
    String servingSizeUnit; // g
    String packageWeight; // kg
    List<FoodNutrient> foodNutrients;

    public ProduktItem(long id, String nazwa, int ilosc_kalorii, int bialko, int weglowodany, int tluszcze,
                       String householdServing, double servingSize, String servingSizeUnit, String packageWeight, List<FoodNutrient> foodNutrients) {
        this.id = id;
        this.nazwa = nazwa;
        this.ilosc_kalorii = ilosc_kalorii;
        this.bialko = bialko;
        this.weglowodany = weglowodany;
        this.tluszcze = tluszcze;
        this.householdServing = householdServing;
        this.servingSize = servingSize;
        this.servingSizeUnit = servingSizeUnit;
        this.packageWeight = packageWeight;
        this.foodNutrients = foodNutrients;
    }
    public long getId() { return id; }
    public String getNazwa() { return nazwa; }
    public int getIlosc_kalorii() { return ilosc_kalorii; }
    public int getBialko() { return bialko; }
    public int getWeglowodany() { return weglowodany; }
    public int getTluszcze() { return tluszcze; }
    public String getHouseholdServing() { return householdServing; }
    public double getServingSize() { return servingSize; }
    public String getServingSizeUnit() { return servingSizeUnit; }
    public String getPackageWeight() { return packageWeight; }
    public List<FoodNutrient> getFoodNutrients() {
        return foodNutrients;
    }
}