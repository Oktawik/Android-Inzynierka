package com.example.pierwszawersjaapki;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pierwszawersjaapki.CaloriesJournal.Add.FoodMapper;
import com.example.pierwszawersjaapki.CaloriesJournal.Add.ProduktAdapter;
import com.example.pierwszawersjaapki.CaloriesJournal.Add.ProduktItem;
import com.example.pierwszawersjaapki.CaloriesJournal.DishItem;
import com.example.pierwszawersjaapki.FDC_API.Food;
import com.example.pierwszawersjaapki.FDC_API.FoodDataService;
import com.example.pierwszawersjaapki.FDC_API.FoodSearchResponse;
import com.example.pierwszawersjaapki.FDC_API.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DziennikDodaj extends Fragment implements ProduktAdapter.onBtnProduktListener, ProduktAdapter.onProduktClickListener {

    TextView tv_pora_posilku;
    SearchView sv_dziennik_dodaj;
    TextView tv_dziennik_dodaj;
    RecyclerView rv_dziennik_dodaj;
    String mealName;
    String api_key;
    List<ProduktItem> lista_produktow;
    private ProduktAdapter produktAdapter;

    public DziennikDodaj() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mealName = getArguments().getString("meal_name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dziennik_dodaj, container, false);

        tv_pora_posilku = view.findViewById(R.id.tv_pora_posilku);
        tv_pora_posilku.setText(mealName.toString());
        sv_dziennik_dodaj = view.findViewById(R.id.sv_dziennik_dodaj);
        tv_dziennik_dodaj = view.findViewById(R.id.tv_dziennik_dodaj);
        tv_dziennik_dodaj.setVisibility(View.GONE);
        rv_dziennik_dodaj = view.findViewById(R.id.rv_dziennik_dodaj);
        rv_dziennik_dodaj.setVisibility(View.GONE);
        api_key = getString(R.string.food_data_central_key);
        lista_produktow = new ArrayList<>();

        produktAdapter = new ProduktAdapter(getContext(),lista_produktow,this, this);
        rv_dziennik_dodaj.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_dziennik_dodaj.setAdapter(produktAdapter);

        sv_dziennik_dodaj.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Naklikniecie szukaj na klawiaturze
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    searchProducts(query);
                    // schowanie klawiatury
                    sv_dziennik_dodaj.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Reaguje na każdą zmianę
                // na tą chwilę niepotrzebny
                return false;
            }
        });

        return view;
    }

    private void searchProducts(String query) {
        // Serwis
        FoodDataService service = RetrofitClient.getClient().create(FoodDataService.class);

        // Zapytanie
        Call<FoodSearchResponse> call = service.searchFoods(api_key, query, 150);

        tv_dziennik_dodaj.setVisibility(View.VISIBLE);
        tv_dziennik_dodaj.setText("Szukam produktów dla: " + query + "...");
        rv_dziennik_dodaj.setVisibility(View.GONE);

        call.enqueue(new Callback<FoodSearchResponse>() {
            @Override
            public void onResponse(Call<FoodSearchResponse> call, Response<FoodSearchResponse> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    FoodSearchResponse searchResponse = response.body();
                    List<Food> foods = searchResponse.getFoods();

                    if (foods!=null && !foods.isEmpty()) {
                        // Czyscimy starą listę
                        lista_produktow.clear();

                        // Mapujemy obiekty z API na ProduktItem
                        for (Food food : foods) {
                            ProduktItem produkt = FoodMapper.mapFoodToProduktItem(food);
                            if (produkt!=null) {
                                lista_produktow.add(produkt);
                            }
                        }

                        // Informujemy adapter o zmianach, czyli odswiezamy
                        produktAdapter.notifyDataSetChanged();

                        StringBuilder resultsText = new StringBuilder();
                        resultsText.append("Znaleziono: ").append(foods.size()).append(" produktów.");

                        tv_dziennik_dodaj.setText(resultsText.toString());
                        rv_dziennik_dodaj.setVisibility(View.VISIBLE);
                    } else {
                        tv_dziennik_dodaj.setText("Nie znaleziono produktów dla: "+query);
                        lista_produktow.clear();
                        produktAdapter.notifyDataSetChanged();
                    }
                } else {
                    tv_dziennik_dodaj.setText("Błąd serwera "+response.code());
                }
            }

            @Override
            public void onFailure(Call<FoodSearchResponse> call, Throwable t) {
                tv_dziennik_dodaj.setText("Błąd sieci. Sprawdź połączenie.");
            }
        });

    }

    @Override
    public void onProduktClicked(ProduktItem produkt) {
        Toast.makeText(getContext(),"Przechodzimy w szczegóły "+produkt.getNazwa(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProduktAdded(ProduktItem produkt) {
        Toast.makeText(getContext(),"Dodano produkt "+produkt.getNazwa(), Toast.LENGTH_SHORT).show();
    }
}