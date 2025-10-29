package com.example.pierwszawersjaapki.CaloriesJournal;

public class MacroItem {
    String name;
    int caloriesEaten;
    int caloriesGoal;
    int progress;
    int color;

    public MacroItem(String name, int caloriesEaten, int caloriesGoal, int progress, int color) {
        this.name = name;
        this.caloriesEaten = caloriesEaten;
        this.caloriesGoal = caloriesGoal;
        this.progress = progress;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCaloriesEaten() {
        return caloriesEaten;
    }

    public void setCaloriesEaten(int caloriesEaten) {
        this.caloriesEaten = caloriesEaten;
    }

    public int getCaloriesGoal() {
        return caloriesGoal;
    }

    public void setCaloriesGoal(int caloriesGoal) {
        this.caloriesGoal = caloriesGoal;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
