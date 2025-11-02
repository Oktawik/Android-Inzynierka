package com.example.pierwszawersjaapki.CaloriesJournal.Add;

import com.example.pierwszawersjaapki.FDC_API.Food;
import com.example.pierwszawersjaapki.FDC_API.FoodNutrient;

import java.util.List;

// Klasa do tłumaczenia obiektu Food z API
// na obiekt ProduktItem do recyclerView
public class FoodMapper {
    public static ProduktItem mapFoodToProduktItem(Food food) {
        if (food == null) {
            return null;
        }

        List<FoodNutrient> nutrients = food.getFoodNutrients();

        // Wyciągamy wartości odżywcze dla 100g
        int kalorie = (int) findNutrientValue(nutrients, "Energy");
        int bialko = (int) findNutrientValue(nutrients, "Protein");
        int tluszcze = (int) findNutrientValue(nutrients, "Total lipid (fat)");
        int weglowodany = (int) findNutrientValue(nutrients, "Carbohydrate, by difference");

//        // Pobieramy rozmiar porcji (i sprawdzamy czy jest w gramach)
//        int porcja = 0;
//        String unit = food.getServingSizeUnit();
//        if (unit != null && (unit.equalsIgnoreCase("g") || unit.equalsIgnoreCase("GRM"))) {
//            porcja = (int) food.getServingSize();
//        }
        int porcja = 100;

        // Tworzymy i zwracamy Twój czysty obiekt ProduktItem
        return new ProduktItem(
                (int) food.getFdcId(), // id
                food.getDescription(), // nazwa
                kalorie,
                bialko,
                weglowodany,
                tluszcze,
                porcja
        );
    }

    private static double findNutrientValue(List<FoodNutrient> nutrients, String nutrientName) {
        if (nutrients == null) {
            return 0;
        }
        for (FoodNutrient nutrient : nutrients) {
            // Sprawdzamy nazwy makroskładników
            if (nutrient.getNutrientName() != null) {
                if (nutrient.getNutrientName().equalsIgnoreCase(nutrientName)) {
                    return nutrient.getValue();
                }
                // API czasem zwraca "Energy" w "KCAL", a czasem w "kJ".
                // Upewnijmy się, że bierzemy "KCAL"
                if (nutrientName.equals("Energy") && nutrient.getNutrientName().equalsIgnoreCase("Energy") && nutrient.getUnitName().equalsIgnoreCase("KCAL")) {
                    return nutrient.getValue();
                }
            }
        }
        return 0; // Nie znaleziono
    }
}
