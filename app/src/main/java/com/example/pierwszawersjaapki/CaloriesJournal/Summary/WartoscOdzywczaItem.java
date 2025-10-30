package com.example.pierwszawersjaapki.CaloriesJournal.Summary;

public class WartoscOdzywczaItem {
    int id;
    String nazwa;
    int ilosc;
    int cel;
    int progres;
    String rodzaj;
    String jednostka;

    public WartoscOdzywczaItem(int id, String nazwa, int ilosc, int cel, int progres, String rodzaj, String jednostka) {
        this.id = id;
        this.nazwa = nazwa;
        this.ilosc = ilosc;
        this.cel = cel;
        this.progres = progres;
        this.rodzaj = rodzaj;
        this.jednostka = jednostka;
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

    public int getIlosc() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }

    public int getCel() {
        return cel;
    }

    public void setCel(int cel) {
        this.cel = cel;
    }

    public int getProgres() {
        return progres;
    }

    public void setProgres(int progres) {
        this.progres = progres;
    }

    public String getRodzaj() {
        return rodzaj;
    }

    public void setRodzaj(String rodzaj) {
        this.rodzaj = rodzaj;
    }

    public String getJednostka() {
        return jednostka;
    }

    public void setJednostka(String jednostka) {
        this.jednostka = jednostka;
    }
}
