package com.example.pierwszawersjaapki.CaloriesJournal.Summary;

import java.util.List;

public class SekcjaWartosciOdzywcze {
    String naglowek;
    List<WartoscOdzywczaItem> listaSkladnikow;

    public SekcjaWartosciOdzywcze(String naglowek, List<WartoscOdzywczaItem> listaSkladnikow) {
        this.naglowek = naglowek;
        this.listaSkladnikow = listaSkladnikow;
    }

    public String getNaglowek() {
        return naglowek;
    }

    public void setNaglowek(String naglowek) {
        this.naglowek = naglowek;
    }

    public List<WartoscOdzywczaItem> getListaSkladnikow() {
        return listaSkladnikow;
    }

    public void setListaSkladnikow(List<WartoscOdzywczaItem> listaSkladnikow) {
        this.listaSkladnikow = listaSkladnikow;
    }
}
