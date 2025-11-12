package com.example.pierwszawersjaapki;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pierwszawersjaapki.FDC_API.FoodDataService;
import com.example.pierwszawersjaapki.FDC_API.FoodDetailsResponse;
import com.example.pierwszawersjaapki.FDC_API.FoodMeasure;
import com.example.pierwszawersjaapki.FDC_API.FoodNutrientDetail;
import com.example.pierwszawersjaapki.FDC_API.Nutrient;
import com.example.pierwszawersjaapki.FDC_API.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DziennikDodajItem extends Fragment {

    // --- Komponenty ---
    private TextView tv_pora_posilku_item, tv_data_item, tv_item_nazwa, tv_item_custom_kcal;
    private ImageButton btn_cofnij_item, btn_item_custom;
    private EditText et_item_custom_ilosc;
    private Spinner sp_item_custom_miara;

    // --- Dane z Bundle ---
    private long fdcId;
    private String mealName, selectedDate, api_key;
    private String householdServing, servingSizeUnit, packageWeightStr;
    private double servingSize;

    // --- Dane z API ---
    private List<FoodMeasure> listaMiar = new ArrayList<>();
    private double caloriesPer100g = 0;

    private int caloriesFromSearch = 0;

    public DziennikDodajItem() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fdcId = getArguments().getLong("fdcId");
            mealName = getArguments().getString("mealName");
            selectedDate = getArguments().getString("selectedDate");
            householdServing = getArguments().getString("householdServing");
            servingSize = getArguments().getDouble("servingSize");
            servingSizeUnit = getArguments().getString("servingSizeUnit");
            packageWeightStr = getArguments().getString("packageWeight");
            caloriesFromSearch = getArguments().getInt("calories");
        }
        api_key = getString(R.string.food_data_central_key);
        Log.d("DEBUG_API", "Odebrano w onCreate fdcId: " + fdcId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dziennik_dodaj_item, container, false);
        initializeComponents(view);
        setComponentsValues();
        fetchProductDetails(fdcId);
        return view;
    }

    private void setComponentsValues() {
        tv_pora_posilku_item.setText(mealName);
        if (selectedDate != null) tv_data_item.setText(sprawdzDate(selectedDate));
        btn_cofnij_item.setOnClickListener(v -> cofnij());
        btn_item_custom.setOnClickListener(v -> dodajProduktDoDziennika());

        et_item_custom_ilosc.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { updateKcalDisplay(); }
        });

        sp_item_custom_miara.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) { updateKcalDisplay(); }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });
    }

    private void fetchProductDetails(long fdcId) {
        Log.d("DEBUG_API", "fetchProductDetails: Wysyłam zapytanie dla fdcId: " + fdcId);
        tv_item_nazwa.setText("Ładowanie danych...");

        FoodDataService service = RetrofitClient.getClient().create(FoodDataService.class);
        Call<FoodDetailsResponse> call = service.getFoodDetails(fdcId, api_key, "abridged");

        call.enqueue(new Callback<FoodDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<FoodDetailsResponse> call, @NonNull Response<FoodDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FoodDetailsResponse details = response.body();

                    tv_item_nazwa.setText(details.getDescription());
                    Log.d("DEBUG_API", "Sukces! Pobrano: " + details.getDescription());

                    caloriesPer100g = caloriesFromSearch;
                    listaMiar.clear();

                    Log.d("DEBUG_API", "--- Analiza 'servingSize' (z Bundle) ---");
                    Log.d("DEBUG_API", "householdServing: '" + householdServing + "'");
                    Log.d("DEBUG_API", "servingSize: " + servingSize);
                    Log.d("DEBUG_API", "servingSizeUnit: '" + servingSizeUnit + "'");

                    if (householdServing != null && !householdServing.isEmpty() &&
                            servingSize > 0 &&
                            ("g".equalsIgnoreCase(servingSizeUnit) || "GRM".equalsIgnoreCase(servingSizeUnit))) {

                        Log.d("DEBUG_API", "-> SUKCES: Tworzę miarę z 'servingSize'.");
                        listaMiar.add(new FoodMeasure(householdServing, servingSize));
                    } else {
                        Log.d("DEBUG_API", "-> INFO: Brak miary 'servingSize'.");
                    }

                    Log.d("DEBUG_API", "--- Analiza 'packageWeight' (z Bundle) ---");
                    Log.d("DEBUG_API", "packageWeight: '" + packageWeightStr + "'");
                    double packageGrams = parsePackageWeightToGrams(packageWeightStr);

                    if (packageGrams > 0) {
                        Log.d("DEBUG_API", "-> SUKCES: Sparsowano 'packageWeight' na: " + packageGrams + "g");
                        FoodMeasure packageMeasure = new FoodMeasure("1 opakowanie", packageGrams);

                        boolean isDuplicate = false;
                        for(FoodMeasure m : listaMiar) {
                            if(m.getGramWeight() == packageGrams) { isDuplicate = true; break; }
                        }
                        if(!isDuplicate) {
                            listaMiar.add(packageMeasure);
                        } else {
                            Log.d("DEBUG_API", "-> INFO: Miara 'packageWeight' jest duplikatem, pomijam.");
                        }
                    } else {
                        Log.d("DEBUG_API", "-> INFO: Brak miary 'packageWeight'.");
                    }

                    Log.d("DEBUG_API", "Łącznie dodano miar do listy: " + listaMiar.size());
                    updateSpinner();
                    updateKcalDisplay(); // Pierwsze wywołanie do ustawienia kalorii

                } else {
                    Log.e("DEBUG_API", "Błąd API: Kod=" + response.code() + " | URL=" + call.request().url());
                    tv_item_nazwa.setText("Nie udało się znaleźć produktu (Błąd: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<FoodDetailsResponse> call, @NonNull Throwable t) {
                Log.e("DEBUG_API", "Błąd KRYTYCZNY (onFailure): " + t.getMessage(), t);
                tv_item_nazwa.setText("Błąd sieci: " + t.getMessage());
            }
        });
    }

    // --- Metody pomocnicze ---

    void initializeComponents(View view) {
        tv_pora_posilku_item = view.findViewById(R.id.tv_pora_posilku_item);
        tv_data_item = view.findViewById(R.id.tv_data_item);
        btn_cofnij_item = view.findViewById(R.id.btn_cofnij_item);
        tv_item_nazwa = view.findViewById(R.id.tv_item_nazwa);
        et_item_custom_ilosc = view.findViewById(R.id.et_item_custom_ilosc);
        sp_item_custom_miara = view.findViewById(R.id.sp_item_custom_miara);
        tv_item_custom_kcal = view.findViewById(R.id.tv_item_custom_kcal);
        btn_item_custom = view.findViewById(R.id.btn_item_custom);
    }

    private double findCalories(List<FoodNutrientDetail> nutrients) {
        if (nutrients == null) return 0;
        for (FoodNutrientDetail nutrientDetail : nutrients) {
            Nutrient nutrient = nutrientDetail.getNutrient();
            if (nutrient != null && "Energy".equalsIgnoreCase(nutrient.getName()) && "KCAL".equalsIgnoreCase(nutrient.getUnitName())) {
                return nutrientDetail.getAmount();
            }
        }
        for (FoodNutrientDetail nutrientDetail : nutrients) {
            Nutrient nutrient = nutrientDetail.getNutrient();
            if (nutrient != null && "Energy".equalsIgnoreCase(nutrient.getName()) && "KJ".equalsIgnoreCase(nutrient.getUnitName())) {
                return nutrientDetail.getAmount() / 4.184; // Przelicz kJ na kcal
            }
        }
        Log.e("DEBUG_API", "Nie znaleziono kalorii (KCAL ani KJ) dla tego produktu.");
        return 0;
    }

    private double parsePackageWeightToGrams(String packageWeight) {
        if (packageWeight == null || packageWeight.isEmpty()) {
            return 0;
        }
        String lower = packageWeight.toLowerCase();
        Pattern patternKg = Pattern.compile("([\\d.]+) *kg");
        Matcher matcherKg = patternKg.matcher(lower);
        Pattern patternG = Pattern.compile("([\\d.]+) *g");
        Matcher matcherG = patternG.matcher(lower);
        try {
            if (matcherKg.find()) {
                String numStr = matcherKg.group(1);
                double value = Double.parseDouble(numStr);
                return value * 1000;
            }
            String lastMatchG = "";
            while (matcherG.find()) {
                lastMatchG = matcherG.group(1);
            }
            if (!lastMatchG.isEmpty()) {
                return Double.parseDouble(lastMatchG);
            }
        } catch (Exception e) {
            Log.e("DEBUG_API", "Nie udało się sparsować packageWeight: " + packageWeight, e);
            return 0;
        }
        return 0;
    }

    private void updateSpinner() {
        if (getContext() == null) return;
        List<String> unitNames = new ArrayList<>();

        // ⭐ POPRAWKA: Zmiana "100 g" na "g"
        unitNames.add("g"); // Zawsze na pozycji 0

        for (FoodMeasure measure : listaMiar) {
            String unitName = String.format(Locale.getDefault(), "%s (%.0fg)",
                    measure.getMeasureName(),
                    measure.getGramWeight());
            unitNames.add(unitName);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), R.layout.spinner_selected_item, unitNames
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_item_custom_miara.setAdapter(adapter);
    }

    private void updateKcalDisplay() {
        if (tv_item_custom_kcal == null || et_item_custom_ilosc == null || sp_item_custom_miara == null) {
            return;
        }
        double ilosc;
        try {
            String iloscStr = et_item_custom_ilosc.getText().toString();
            ilosc = (iloscStr.isEmpty() || iloscStr.equals(".")) ? 0 : Double.parseDouble(iloscStr);
        } catch (NumberFormatException e) {
            ilosc = 0;
        }
        int selectedPosition = sp_item_custom_miara.getSelectedItemPosition();
        double gramWeight;

        // ⭐ POPRAWKA: Logika dla 'g' na pozycji 0
        if (selectedPosition == 0) {
            // Wybrano "g"
            gramWeight = 1.0;
        } else if (selectedPosition > 0 && selectedPosition <= listaMiar.size()) {
            // Wybrano inną miarę (np. "1/2 cup")
            FoodMeasure wybranaMiara = listaMiar.get(selectedPosition - 1);
            gramWeight = wybranaMiara.getGramWeight();
        } else {
            gramWeight = 0;
        }

        double totalGrams = ilosc * gramWeight;
        double totalKcal = (totalGrams / 100.0) * caloriesPer100g;
        tv_item_custom_kcal.setText(String.format(Locale.getDefault(), "%.0f kcal", totalKcal));
    }

    private void dodajProduktDoDziennika() {
        double ilosc;
        try {
            String iloscStr = et_item_custom_ilosc.getText().toString();
            if (iloscStr.isEmpty() || iloscStr.equals(".")) {
                Toast.makeText(getContext(), "Wpisz poprawną ilość", Toast.LENGTH_SHORT).show();
                return;
            }
            ilosc = Double.parseDouble(iloscStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Wpisz poprawną ilość", Toast.LENGTH_SHORT).show();
            return;
        }
        int selectedPosition = sp_item_custom_miara.getSelectedItemPosition();
        double gramWeight;

        // ⭐ POPRAWKA: Logika dla 'g' na pozycji 0
        if (selectedPosition == 0) {
            gramWeight = 1.0;
        } else if (selectedPosition > 0 && selectedPosition <= listaMiar.size()) {
            FoodMeasure wybranaMiara = listaMiar.get(selectedPosition - 1);
            gramWeight = wybranaMiara.getGramWeight();
        } else {
            // Jeśli jakimś cudem nie ma miary, domyślnie 1g (bo "g" jest zawsze)
            gramWeight = (selectedPosition == 0) ? 1.0 : 0.0;
        }

        double totalGrams = ilosc * gramWeight;
        double totalKcal = (totalGrams / 100.0) * caloriesPer100g;

        // TODO: Tutaj umieść logikę zapisu do bazy danych

        Toast.makeText(getContext(), String.format(Locale.getDefault(), "Dodano %.1f g (%.0f kcal)", totalGrams, totalKcal), Toast.LENGTH_LONG).show();
        cofnij();
    }

    private void cofnij() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private String sprawdzDate(String data) {
        if (data == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        String dzisiaj = sdf.format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, -1);
        String wczoraj = sdf.format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, 2);
        String jutro = sdf.format(c.getTime());

        if (data.equals(dzisiaj)) return "Dzisiaj";
        if (data.equals(wczoraj)) return "Wczoraj";
        if (data.equals(jutro)) return "Jutro";
        return data;
    }
}