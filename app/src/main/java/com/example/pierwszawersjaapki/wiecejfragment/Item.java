package com.example.pierwszawersjaapki.wiecejfragment;

public class Item {
    String name;
    int image;
    String opis;

    public Item(String name, int image, String opis) {
        this.name = name;
        this.image = image;
        this.opis = opis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}
