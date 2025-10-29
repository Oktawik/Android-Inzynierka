package com.example.pierwszawersjaapki;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.pierwszawersjaapki.Pomoc.PomocAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PomocFragment extends Fragment {

    private ExpandableListView expandableListView;
    private List<String> pytania;
    private HashMap<String, String> odpowiedzi;
    private PomocAdapter adapter;

    public PomocFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pomoc, container, false);

        expandableListView = view.findViewById(R.id.elv_pomoc);
        pytania = new ArrayList<>();
        odpowiedzi = new HashMap<>();

        pytania.add("Problem z logowaniem na konto.");
        odpowiedzi.put("Problem z logowaniem na konto.", "Sprawdź czy twoje urządzenie jest podłączone do internetu." +
                " jeśli masz połączenie z internetem, to sprawdź czy internet faktycznie jest" +
                "wejdź na przeglądarkę np. Google i wyszukaj czegoś. Jeśli się nie wyszukuje "+
                "to oznacza, że nie masz internetu. Bez internetu nie da się zalogować."+
                " Jeśli masz internet sprawdź czy nie masz włączonego trybu oszczędzania danych "+
                "bądź trybu oszczędzania baterii, jeśli masz włączone, wyłącz i uruchom aplikację" +
                " ponownie. Jeśli to nie zadziała, to spróbuj uruchomić ponownie telefon. "+
                "W przypadku, gdy ponowne uruchomienie telefonu dalej nie pomogło, prosimy" +
                "wejść w aplikacji w opcję więcej na pasku nawigacyjnym dolnym, a następnie "+
                "zjechać na dół wejść w sekcję Pomoc, zjechać na sam dół i wypełnić formularz "+
                "i opisać problem.");

        pytania.add("Czy Przemek pojedzie nad morze?");
        odpowiedzi.put("Czy Przemek pojedzie nad morze?", "Nie może, bo we wakacje pracuje");

        adapter = new PomocAdapter(requireContext(), pytania, odpowiedzi);
        expandableListView.setAdapter(adapter);

        return view;
    }
}