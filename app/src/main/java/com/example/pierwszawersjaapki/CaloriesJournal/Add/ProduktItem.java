package com.example.pierwszawersjaapki.CaloriesJournal.Add; // Użyj swojego pakietu

public class ProduktItem {
    long id; // Zmienione na long
    String nazwa;
    int ilosc_kalorii;
    int bialko;
    int weglowodany;
    int tluszcze;
    int jedna_porcja; // Możesz to usunąć, jeśli nie używasz

    // ⭐ NOWE POLA (do przekazania dalej)
    private String householdServing; // "0.25 cup"
    private double servingSize; // 41.0
    private String servingSizeUnit; // "g"
    private String packageWeight; // "1.64 kg"

    // ⭐ ZAKTUALIZOWANY KONSTRUKTOR
    public ProduktItem(long id, String nazwa, int ilosc_kalorii, int bialko, int weglowodany, int tluszcze,
                       String householdServing, double servingSize, String servingSizeUnit, String packageWeight) {
        this.id = id;
        this.nazwa = nazwa;
        this.ilosc_kalorii = ilosc_kalorii;
        this.bialko = bialko;
        this.weglowodany = weglowodany;
        this.tluszcze = tluszcze;

        this.householdServing = householdServing;
        this.servingSize = servingSize;
        this.servingSizeUnit = servingSizeUnit;
        this.packageWeight = packageWeight;
    }

    // --- Gettery ---
    public long getId() { return id; }
    public String getNazwa() { return nazwa; }
    public int getIlosc_kalorii() { return ilosc_kalorii; }
    public int getBialko() { return bialko; }
    public int getWeglowodany() { return weglowodany; }
    public int getTluszcze() { return tluszcze; }

    // ⭐ NOWE GETTERY
    public String getHouseholdServing() { return householdServing; }
    public double getServingSize() { return servingSize; }
    public String getServingSizeUnit() { return servingSizeUnit; }
    public String getPackageWeight() { return packageWeight; }
}