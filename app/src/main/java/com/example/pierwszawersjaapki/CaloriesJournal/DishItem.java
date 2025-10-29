package com.example.pierwszawersjaapki.CaloriesJournal;

public class DishItem {
    int id;
    String nazwa;
    int ikona;
    int kalorieSpozyte;
    int kaloriePotrzebne;
    String zjedzoneProdukty;
    int progress;

    public DishItem(int id, String nazwa, int ikona, int kalorieSpozyte, int kaloriePotrzebne, String zjedzoneProdukty, int progress) {
        this.id = id;
        this.nazwa = nazwa;
        this.ikona = ikona;
        this.kalorieSpozyte = kalorieSpozyte;
        this.kaloriePotrzebne = kaloriePotrzebne;
        this.zjedzoneProdukty = zjedzoneProdukty;
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getIkona() {
        return ikona;
    }

    public void setIkona(int ikona) {
        this.ikona = ikona;
    }

    public int getKalorieSpozyte() {
        return kalorieSpozyte;
    }

    public void setKalorieSpozyte(int kalorieSpozyte) {
        this.kalorieSpozyte = kalorieSpozyte;
    }

    public int getKaloriePotrzebne() {
        return kaloriePotrzebne;
    }

    public void setKaloriePotrzebne(int kaloriePotrzebne) {
        this.kaloriePotrzebne = kaloriePotrzebne;
    }

    public String getZjedzoneProdukty() {
        return zjedzoneProdukty;
    }

    public void setZjedzoneProdukty(String zjedzoneProdukty) {
        this.zjedzoneProdukty = zjedzoneProdukty;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
