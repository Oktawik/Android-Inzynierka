package com.example.pierwszawersjaapki.model;

public class DailyData {
    public int caloriesNeeded = 2000;
    public int caloriesConsumed = 0;
    public int caloriesBurned = 0;
    public MacroNutrient protein = new MacroNutrient(0, 150);
    public MacroNutrient carbs = new MacroNutrient(0, 250);
    public MacroNutrient fat = new MacroNutrient(0, 67);

    public DailyData() {
    }
}
