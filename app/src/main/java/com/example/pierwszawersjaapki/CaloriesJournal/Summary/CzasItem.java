package com.example.pierwszawersjaapki.CaloriesJournal.Summary;

public class CzasItem {
    int id;
    String text;

    public CzasItem(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
