package com.example.pierwszawersjaapki;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pierwszawersjaapki.CaloriesJournal.Summary.CzasAdapter;
import com.example.pierwszawersjaapki.CaloriesJournal.Summary.CzasItem;
import com.example.pierwszawersjaapki.CaloriesJournal.Summary.WartoscOdzywczaAdapter;
import com.example.pierwszawersjaapki.CaloriesJournal.Summary.WartoscOdzywczaItem;

import java.util.ArrayList;
import java.util.List;

public class DziennikPodsumowanieFragment extends Fragment implements CzasAdapter.OnCzasClickListener {

    private RecyclerView rv_podsumowanie_czas;
    private List<CzasItem> czasItemList;
    private LinearLayout ll_dziennik_podsumowanie_dzien;
    private LinearLayout ll_dziennik_podsumowanie_tydzien;
    private LinearLayout ll_dziennik_podsumowanie_miesiac;
    private RecyclerView rv_dziennik_podsumowanie_wartosci_odzywcze;
    private List<WartoscOdzywczaItem> wartoscOdzywczaList;

    public DziennikPodsumowanieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dziennik_podsumowanie, container, false);

        rv_podsumowanie_czas = view.findViewById(R.id.rv_podsumowanie_czas);
        czasItemList = new ArrayList<>();
        czasItemList.add(new CzasItem(1, "Dzień"));
        czasItemList.add(new CzasItem(2,"Tydzień"));
        czasItemList.add(new CzasItem(3, "Miesiąc"));

        CzasAdapter czasAdapter = new CzasAdapter(getContext(),czasItemList,this::OnCzasClicked);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), czasItemList.size());
        rv_podsumowanie_czas.setLayoutManager(layoutManager);
        rv_podsumowanie_czas.setAdapter(czasAdapter);

        ll_dziennik_podsumowanie_dzien = view.findViewById(R.id.ll_dziennik_podsumowanie_dzien);
        ll_dziennik_podsumowanie_tydzien = view.findViewById(R.id.ll_dziennik_podsumowanie_tydzien);
        ll_dziennik_podsumowanie_miesiac = view.findViewById(R.id.ll_dziennik_podsumowanie_miesiac);

        rv_dziennik_podsumowanie_wartosci_odzywcze = view.findViewById(R.id.rv_dziennik_podsumowanie_wartosci_odzywcze);
        if (rv_dziennik_podsumowanie_wartosci_odzywcze == null) {
            Toast.makeText(getContext(),"Pusty",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(),"Znaleziono",Toast.LENGTH_SHORT).show();
        }
        wartoscOdzywczaList = new ArrayList<>();
        wartoscOdzywczaList.add(new WartoscOdzywczaItem(1, "Białka", 170, 2000, 50, "Wartości odżywcze", " (g) "));
        rv_dziennik_podsumowanie_wartosci_odzywcze.setLayoutManager(new LinearLayoutManager(getContext()));
        WartoscOdzywczaAdapter wartoscOdzywczaAdapter = new WartoscOdzywczaAdapter(getContext(),wartoscOdzywczaList);
        rv_dziennik_podsumowanie_wartosci_odzywcze.setAdapter(wartoscOdzywczaAdapter);

        return view;
    }

    @Override
    public void OnCzasClicked(CzasItem item) {
        String nazwa = item.getText().toString().toLowerCase();

        if ("dzień".equals(nazwa)) {
            ll_dziennik_podsumowanie_dzien.setVisibility(View.VISIBLE);
            ll_dziennik_podsumowanie_tydzien.setVisibility(View.GONE);
            ll_dziennik_podsumowanie_miesiac.setVisibility(View.GONE);
        }

        if ("tydzień".equals(nazwa)) {
            ll_dziennik_podsumowanie_dzien.setVisibility(View.GONE);
            ll_dziennik_podsumowanie_tydzien.setVisibility(View.VISIBLE);
            ll_dziennik_podsumowanie_miesiac.setVisibility(View.GONE);
        }

        if ("miesiąc".equals(nazwa)) {
            ll_dziennik_podsumowanie_dzien.setVisibility(View.GONE);
            ll_dziennik_podsumowanie_tydzien.setVisibility(View.GONE);
            ll_dziennik_podsumowanie_miesiac.setVisibility(View.VISIBLE);
        }
    }
}