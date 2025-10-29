package com.example.pierwszawersjaapki.CaloriesJournal;

public class WaterCup {
    int id;
    int image;
    boolean isFUll;

    public WaterCup(int id, int image, boolean isFUll) {
        this.id = id;
        this.image = image;
        this.isFUll = isFUll;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isFUll() {
        return isFUll;
    }

    public void setFUll(boolean FUll) {
        isFUll = FUll;
    }
}
