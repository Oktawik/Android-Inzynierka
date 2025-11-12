package com.example.pierwszawersjaapki;

// Importy Androida
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log; // Import dla Log.d
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

// Importy Twoich klas (Adaptery, Modele)
import com.example.pierwszawersjaapki.CaloriesJournal.Add.FoodMapper;
import com.example.pierwszawersjaapki.CaloriesJournal.Add.ProduktAdapter;
import com.example.pierwszawersjaapki.CaloriesJournal.Add.ProduktItem;

// Importy FDC API
import com.example.pierwszawersjaapki.FDC_API.Food;
import com.example.pierwszawersjaapki.FDC_API.FoodDataService;
import com.example.pierwszawersjaapki.FDC_API.FoodSearchResponse;
import com.example.pierwszawersjaapki.FDC_API.RetrofitClient;

// Importy Javy
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Importy Retrofit
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DziennikDodaj extends Fragment implements ProduktAdapter.onBtnProduktListener, ProduktAdapter.onProduktClickListener {

    // Definicje widoków
    TextView tv_pora_posilku;
    TextView tv_data;
    ImageButton btn_cofnij;
    SearchView sv_dziennik_dodaj;
    TextView tv_dziennik_dodaj;
    RecyclerView rv_dziennik_dodaj;

    // Zmienne
    String mealName;
    String selectedDate;
    String koncowaData;
    String api_key;
    List<ProduktItem> lista_produktow;
    private ProduktAdapter produktAdapter;

    public DziennikDodaj() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Odbieranie danych (nazwa posiłku i data)
        if (getArguments() != null) {
            mealName = getArguments().getString("meal_name");
            selectedDate = getArguments().getString("selected_date"); // Upewnij się, że klucz to "selected_date"
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dziennik_dodaj, container, false);

        // Inicjalizacja widoków
        tv_pora_posilku = view.findViewById(R.id.tv_pora_posilku);
        tv_data = view.findViewById(R.id.tv_data);
        btn_cofnij = view.findViewById(R.id.btn_cofnij);
        sv_dziennik_dodaj = view.findViewById(R.id.sv_dziennik_dodaj);
        tv_dziennik_dodaj = view.findViewById(R.id.tv_dziennik_dodaj);
        rv_dziennik_dodaj = view.findViewById(R.id.rv_dziennik_dodaj);

        // Ustawienie wartości początkowych
        tv_pora_posilku.setText(mealName);
        if (selectedDate != null) {
            koncowaData = sprawdzDate(selectedDate);
            tv_data.setText(koncowaData);
        } else {
            // Wartość domyślna, gdyby data była nullem
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault());
            String dzisiejszaData = sdf.format(Calendar.getInstance().getTime());
            koncowaData = sprawdzDate(dzisiejszaData); // To zwróci "Dzisiaj"
            tv_data.setText(koncowaData);
        }

        // Ustawienie listenera dla przycisku cofania
        btn_cofnij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cofnij();
            }
        });

        // Ustawienia RecyclerView
        tv_dziennik_dodaj.setVisibility(View.GONE);
        rv_dziennik_dodaj.setVisibility(View.GONE);
        api_key = getString(R.string.food_data_central_key);
        lista_produktow = new ArrayList<>();

        produktAdapter = new ProduktAdapter(getContext(), lista_produktow, this, this);
        rv_dziennik_dodaj.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_dziennik_dodaj.setAdapter(produktAdapter);

        // Ustawienie listenera dla paska wyszukiwania
        sv_dziennik_dodaj.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    searchProducts(query);
                    sv_dziennik_dodaj.clearFocus(); // Ukryj klawiaturę
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false; // Nie robimy nic przy zmianie tekstu
            }
        });

        return view;
    }

    /**
     * Metoda wyszukująca produkty w API FDC
     */
    private void searchProducts(String query) {
        FoodDataService service = RetrofitClient.getClient().create(FoodDataService.class);

        // Wywołanie API
        Call<FoodSearchResponse> call = service.searchFoods(api_key, query, 150);

        tv_dziennik_dodaj.setVisibility(View.VISIBLE);
        tv_dziennik_dodaj.setText("Szukam produktów dla: " + query + "...");
        rv_dziennik_dodaj.setVisibility(View.GONE);

        call.enqueue(new Callback<FoodSearchResponse>() {
            @Override
            public void onResponse(Call<FoodSearchResponse> call, Response<FoodSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FoodSearchResponse searchResponse = response.body();
                    List<Food> foods = searchResponse.getFoods();

                    if (foods != null && !foods.isEmpty()) {
                        lista_produktow.clear();

                        // Mapowanie Food -> ProduktItem
                        for (Food food : foods) {
                            ProduktItem produkt = FoodMapper.mapFoodToProduktItem(food);
                            if (produkt != null) {
                                lista_produktow.add(produkt);
                            }
                        }

                        produktAdapter.notifyDataSetChanged();
                        tv_dziennik_dodaj.setText("Znaleziono: " + foods.size() + " produktów.");
                        rv_dziennik_dodaj.setVisibility(View.VISIBLE);
                    } else {
                        tv_dziennik_dodaj.setText("Nie znaleziono produktów dla: " + query);
                        lista_produktow.clear();
                        produktAdapter.notifyDataSetChanged();
                    }
                } else {
                    tv_dziennik_dodaj.setText("Błąd serwera: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<FoodSearchResponse> call, Throwable t) {
                tv_dziennik_dodaj.setText("Błąd sieci. Sprawdź połączenie.");
            }
        });
    }

    /**
     * Wywoływane po kliknięciu na element listy (interfejs z ProduktAdapter)
     */
    @Override
    public void onProduktClicked(ProduktItem produkt) {
        DziennikDodajItem fragmentSzczegolow = new DziennikDodajItem();

        Log.d("DEBUG_API", "Kliknięto produkt. Przekazuję fdcId: " + produkt.getId());

        Bundle args = new Bundle();

        // --- PRZEKAZYWANIE DANYCH ---
        args.putLong("fdcId", produkt.getId());
        args.putString("mealName", mealName);
        args.putString("selectedDate", selectedDate);

        // ⭐ PRZEKAŻ NOWE DANE O PORCJACH
        args.putString("householdServing", produkt.getHouseholdServing());
        args.putDouble("servingSize", produkt.getServingSize());
        args.putString("servingSizeUnit", produkt.getServingSizeUnit());
        args.putString("packageWeight", produkt.getPackageWeight());

        args.putInt("calories", produkt.getIlosc_kalorii());
        // --- KONIEC PRZEKAZYWANIA DANYCH ---

        fragmentSzczegolow.setArguments(args);

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragmentSzczegolow)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Wywoływane po kliknięciu przycisku "dodaj" na liście (interfejs z ProduktAdapter)
     */
    @Override
    public void onProduktAdded(ProduktItem produkt) {
        Toast.makeText(getContext(), "Dodano produkt " + produkt.getNazwa(), Toast.LENGTH_SHORT).show();
        // TODO: Tutaj logika szybkiego dodawania
    }

    /**
     * Metoda do obsługi przycisku "Wstecz"
     */
    private void cofnij() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * Metoda zamieniająca datę na "Dzisiaj", "Wczoraj" itp.
     */
    private String sprawdzDate(String data) {
        if (data == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        Calendar calendarDzisiaj = Calendar.getInstance();
        String dzisiejszaData = sdf.format(calendarDzisiaj.getTime());

        Calendar calendarWczoraj = Calendar.getInstance();
        calendarWczoraj.add(Calendar.DAY_OF_MONTH, -1);
        String wczorajszaData = sdf.format(calendarWczoraj.getTime());

        Calendar calendarJutro = Calendar.getInstance();
        calendarJutro.add(Calendar.DAY_OF_MONTH, 1);
        String jutrzejszaData = sdf.format(calendarJutro.getTime());

        if (data.equals(dzisiejszaData)) {
            return "Dzisiaj";
        } else if (data.equals(wczorajszaData)) {
            return "Wczoraj";
        } else if (data.equals(jutrzejszaData)) {
            return "Jutro";
        } else {
            return data;
        }
    }
}