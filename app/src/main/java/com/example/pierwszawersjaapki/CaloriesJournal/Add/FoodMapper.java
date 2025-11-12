package com.example.pierwszawersjaapki.CaloriesJournal.Add; // Użyj swojego pakietu

import com.example.pierwszawersjaapki.FDC_API.Food;
import com.example.pierwszawersjaapki.FDC_API.FoodNutrient;
import java.util.List;

public class FoodMapper {
    public static ProduktItem mapFoodToProduktItem(Food food) {
        if (food == null) {
            return null;
        }
        List<FoodNutrient> nutrients = food.getFoodNutrients();

        int kalorie = (int) findNutrientValue(nutrients, "Energy");
        int bialko = (int) findNutrientValue(nutrients, "Protein");
        int tluszcze = (int) findNutrientValue(nutrients, "Total lipid (fat)");
        int weglowodany = (int) findNutrientValue(nutrients, "Carbohydrate, by difference");

        // ⭐ POBIERZ NOWE DANE Z OBIEKTU 'food'
        String householdServing = food.getHouseholdServingFullText();
        double servingSize = food.getServingSize();
        String servingSizeUnit = food.getServingSizeUnit();
        String packageWeight = food.getPackageWeight();

        // ⭐ PRZEKAŻ DANE DO NOWEGO KONSTRUKTORA
        return new ProduktItem(
                food.getFdcId(), // Zmienione na long
                food.getDescription(),
                kalorie,
                bialko,
                weglowodany,
                tluszcze,
                householdServing, // Nowe
                servingSize,      // Nowe
                servingSizeUnit,  // Nowe
                packageWeight     // Nowe
        );
    }

    private static double findNutrientValue(List<FoodNutrient> nutrients, String nutrientName) {
        if (nutrients == null) return 0;
        for (FoodNutrient nutrient : nutrients) {
            if (nutrient.getNutrientName() != null) {
                if (nutrient.getNutrientName().equalsIgnoreCase(nutrientName)) {
                    return nutrient.getValue();
                }
                if (nutrientName.equals("Energy") && nutrient.getNutrientName().equalsIgnoreCase("Energy") && "KCAL".equalsIgnoreCase(nutrient.getUnitName())) {
                    return nutrient.getValue();
                }
            }
        }
        return 0;
    }
}