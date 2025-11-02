package com.example.pierwszawersjaapki.CaloriesJournal.Add;

public class ProduktItem {
    int id;
    String nazwa;
    // int ilosc_gramow; // domyslnie jest 100g
    int ilosc_kalorii;
    int bialko;
    int weglowodany;
    int tluszcze;
    int jedna_porcja;
    // String jednostka; // np. ml, gramy

    public ProduktItem(int id, String nazwa, int ilosc_kalorii, int bialko, int weglowodany, int tluszcze, int jedna_porcja) {
        this.id = id;
        this.nazwa = nazwa;
        this.ilosc_kalorii = ilosc_kalorii;
        this.bialko = bialko;
        this.weglowodany = weglowodany;
        this.tluszcze = tluszcze;
        this.jedna_porcja = jedna_porcja;
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

    public int getIlosc_kalorii() {
        return ilosc_kalorii;
    }

    public void setIlosc_kalorii(int ilosc_kalorii) {
        this.ilosc_kalorii = ilosc_kalorii;
    }

    public int getBialko() {
        return bialko;
    }

    public void setBialko(int bialko) {
        this.bialko = bialko;
    }

    public int getWeglowodany() {
        return weglowodany;
    }

    public void setWeglowodany(int weglowodany) {
        this.weglowodany = weglowodany;
    }

    public int getTluszcze() {
        return tluszcze;
    }

    public void setTluszcze(int tluszcze) {
        this.tluszcze = tluszcze;
    }

    public int getJedna_porcja() {
        return jedna_porcja;
    }

    public void setJedna_porcja(int jedna_porcja) {
        this.jedna_porcja = jedna_porcja;
    }
}
