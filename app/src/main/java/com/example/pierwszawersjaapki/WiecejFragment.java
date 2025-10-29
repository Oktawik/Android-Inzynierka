package com.example.pierwszawersjaapki;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pierwszawersjaapki.wiecejfragment.WiecejFragmentAdapter;
import com.example.pierwszawersjaapki.wiecejfragment.Item;
import com.example.pierwszawersjaapki.wiecejfragment.WiecejFragmentViewHolder;

import java.util.ArrayList;
import java.util.List;

public class WiecejFragment extends Fragment {

    RecyclerView recyclerView;
    List<Item> items;

    public WiecejFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wiecej, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_wiecej);
        items = new ArrayList<>();
        items.add(new Item("Lista zakupów", R.drawable.ic_launcher_background, "Twórz i zarządzaj swoimi listami zakupów. Dodawaj produkty ręcznie lub z przepisów."));
        items.add(new Item("Nawyki", R.drawable.ic_launcher_background, "Śledź codzienne nawyki zdrowego stylu życia. Otrzymuj przypomnienia i sprawdzaj postępy."));
        items.add(new Item("Chatbot", R.drawable.ic_launcher_background, "Porozmawiaj z naszym doradcą żywieniowym. Zadaj pytania i otrzymuj wskazówki dotyczące diety."));
        items.add(new Item("Post Przerywany", R.drawable.ic_launcher_background, "Zarządzaj swoim postem przerywanym. Otrzymuj powiadomienia i śledź czas postu."));
        items.add(new Item("Diety", R.drawable.ic_launcher_background, "Przeglądaj gotowe plany dietetyczne. Wybieraj te, które pasują do Twojego celu i stylu życia."));
        items.add(new Item("Zdrowsze zamienniki", R.drawable.ic_launcher_background, "Sprawdź propozycje zdrowszych zamienników dla codziennych produktów. Ułatwia podejmowanie lepszych wyborów."));
        items.add(new Item("Ustawienia", R.drawable.ic_launcher_background, "Dostosuj aplikację do swoich potrzeb. Zmieniaj powiadomienia, jednostki i profil użytkownika."));
        items.add(new Item("Pomoc", R.drawable.ic_launcher_background, "Znajdź odpowiedzi na pytania i wskazówki dotyczące korzystania z aplikacji. Skontaktuj się z nami w razie problemów."));
        items.add(new Item("Wyloguj się", R.drawable.ic_launcher_background, "Kliknij, żeby wyjść ze swojego konta. Będziesz musiał zalogować się ponownie, jeśli zechcesz wrócić."));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        WiecejFragmentAdapter adapter = new WiecejFragmentAdapter(
                getContext(),
                items,
                item -> {
                    String nazwa = item.getName();

                    if ("Chatbot".equals(nazwa)) {
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout, new ChatbotFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                    if ("Pomoc".equals(nazwa)) {
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout, new PomocFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                    if ("Wyloguj się".equals(nazwa)) {
                        Toast.makeText(getContext(), "Wylogowano", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Kliknięto: " + nazwa, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        recyclerView.setAdapter(adapter);

    }

    // 0bsluga przyciskow jest w WiecejFragmentAdapter

}