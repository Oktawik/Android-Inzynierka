package com.example.pierwszawersjaapki;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Modele
import com.example.pierwszawersjaapki.CaloriesJournal.CircularProgressBarView;
import com.example.pierwszawersjaapki.CaloriesJournal.DishFragmentAdapter;
import com.example.pierwszawersjaapki.CaloriesJournal.DishItem;
import com.example.pierwszawersjaapki.CaloriesJournal.MacroAdapter;
import com.example.pierwszawersjaapki.CaloriesJournal.MacroItem;
import com.example.pierwszawersjaapki.CaloriesJournal.WaterCup;
import com.example.pierwszawersjaapki.CaloriesJournal.WaterFragmentAdapter;
import com.example.pierwszawersjaapki.model.DailyData;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

public class DziennikFragment extends Fragment implements DishFragmentAdapter.OnCameraAddClickListener, DishFragmentAdapter.OnAddClickListener, DishFragmentAdapter.OnMealTimingClickListener {

    // Zmienne ogolne
    private View rootView;
    private ImageButton btn_notifications;

    // Zmienne kalendarza
    private View selectedDayView = null; // przechowuje poprzednio zaznaczony dzień

    // Zmienne podsumowania
    private CircularProgressBarView pb_calories_left;
    private List<MacroItem> macroItemList;
    private RecyclerView rv_macros;
    private LinearLayout ll_calories_summary;

    private DailyData currentDayData = new DailyData();

    // Zmienne Odżywianie
    private RecyclerView rv_meal_timings;
    private List<DishItem> dishItemList;
    int ilosc_posilkow;

    // Zmienne Woda
    private RecyclerView rv_cups_of_water;
    private List<WaterCup> waterCupList;
    private int waterIntake;
    private int waterCupsAmount;
    private TextView tv_water_intake_current;
    private float waterDrank;

    public DziennikFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_dziennik, container, false);

        LinearLayout weekDaysContainer = rootView.findViewById(R.id.weekDaysContainer);
        ImageButton calendarButton = rootView.findViewById(R.id.calendarButton);

        btn_notifications = rootView.findViewById(R.id.btn_notificaions);

        // Pobranie dzisiejszej daty
        Calendar calendar = Calendar.getInstance();
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);
        int todayMonth = calendar.get(Calendar.MONTH);
        int todayYear = calendar.get(Calendar.YEAR);

        // Wyświetlamy początkowy tydzień wokół dzisiejszej daty
        updateWeekCalendar(todayDay, todayMonth, todayYear, weekDaysContainer, inflater);

        // Obsługa kliknięcia w przycisk kalendarza
        calendarButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, year, month, dayOfMonth) -> {
                        // Aktualizacja kalendarza po wybraniu daty
                        updateWeekCalendar(dayOfMonth, month, year, weekDaysContainer, inflater);
                    },
                    todayYear, todayMonth, todayDay);
            datePickerDialog.show();
        });

        // Obsluga przycisku powiadomien
        btn_notifications.setOnClickListener(v -> {

        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sekcja Podsumowanie
        pb_calories_left = view.findViewById(R.id.pb_calories_left);
        pb_calories_left.setMaxProgress(100);
        pb_calories_left.setCurrentProgress(33);
        rv_macros = view.findViewById(R.id.rv_macros);
        macroItemList = new ArrayList<>();
        macroItemList.add(new MacroItem("Białko", 50, 100, (int)(((float)50/100) * 100), R.color.green));
        macroItemList.add(new MacroItem("Węglowodany", 94, 244, (int)(((float)94/244) * 100), R.color.gray));
        macroItemList.add(new MacroItem("Tłuszcz", 10, 68, (int)(((float)10/68) * 100), R.color.gray));

        MacroAdapter macroAdapter = new MacroAdapter(getContext(), macroItemList);
        rv_macros.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_macros.setAdapter(macroAdapter);

        ll_calories_summary = view.findViewById(R.id.ll_calories_summary);

        ll_calories_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnCaloriesSummaryClick();
            }
        });


        ilosc_posilkow = 0;

        // Sekcja odżywianie
        rv_meal_timings = view.findViewById(R.id.rv_meal_timings);
        dishItemList = new ArrayList<>();
        dishItemList.add(new DishItem(1, "Śniadanie", R.drawable.ic_chatbot, 200, 400, "Owsianka, Mleko", 400/200));
        dishItemList.add(new DishItem(1, "Lunch", R.drawable.ic_chatbot, 100, 400, "Banan, Jogurt grecki", 400/100));
        dishItemList.add(new DishItem(1, "Obiad", R.drawable.ic_chatbot, 150, 600, "Owsianka, Mleko", 600/150));
        dishItemList.add(new DishItem(1, "Kolacja", R.drawable.ic_chatbot, 150, 500, "Owsianka, Mleko", 500/150));

        // Ustawienie odpowiedniej wysokosci
        ilosc_posilkow = dishItemList.size();
        // Docelowa wysokosc
        final float TARGET_HEIGHT_DP = ilosc_posilkow * 102f;

        // 2. Pobranie współczynnika gęstości ekranu (dp)
        final float scale = getResources().getDisplayMetrics().density;

        // 3. Przeliczenie DP na piksele (px)
        // Dodanie 0.5f służy do poprawnego zaokrąglenia
        int targetHeightPx = (int) (TARGET_HEIGHT_DP * scale + 0.5f);

        // 4. Pobranie obecnych parametrów layoutu
        ViewGroup.LayoutParams params = rv_meal_timings.getLayoutParams();

        // 5. Ustawienie nowej wysokości w PIKSELACH
        params.height = targetHeightPx;

        // 6. Zastosowanie zmienionych parametrów
        rv_meal_timings.setLayoutParams(params);


        rv_meal_timings.setLayoutManager(new LinearLayoutManager(getActivity()));
        DishFragmentAdapter dishFragmentAdapter = new DishFragmentAdapter(getContext(), dishItemList, this, this, this);
        rv_meal_timings.setAdapter(dishFragmentAdapter);


        // Sekcja woda
        rv_cups_of_water = view.findViewById(R.id.rv_cups_of_water);
        waterCupList = new ArrayList<>();
        tv_water_intake_current = view.findViewById(R.id.tv_water_intake_current);

        // Pobieramy ilosc wody
        waterIntake = 2000;
        waterCupsAmount = 2000/200;
        waterDrank = 0;

        // Dodajemy ilosc szklanek wody
        for (int i=0; i<waterCupsAmount; i++) {
            waterCupList.add(new WaterCup(i, R.drawable.glass_empty, false));
        }

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
        rv_cups_of_water.setLayoutManager(flexboxLayoutManager);
        WaterFragmentAdapter waterFragmentAdapter = new WaterFragmentAdapter(
                getContext(),
                waterCupList,
                change -> {
                    waterDrank+=change;
                    tv_water_intake_current.setText(String.format("%.2f l", waterDrank/1000));
                }
        );
        rv_cups_of_water.setAdapter(waterFragmentAdapter);
    }

    // METODY KALENDARZA
    private void updateWeekCalendar(int day, int month, int year, LinearLayout weekDaysContainer, LayoutInflater inflater) {
        weekDaysContainer.removeAllViews();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        // Ustawienie początkowego dnia tygodnia na poniedziałek
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int diffToMonday = (dayOfWeek + 5) % 7; // obliczamy ile dni cofnąć do poniedziałku
        calendar.add(Calendar.DAY_OF_MONTH, -diffToMonday);

        String[] weekDays = {"Pn", "Wt", "Śr", "Cz", "Pt", "Sb", "Nd"};

        for (int i = 0; i < 7; i++) {
            View dayView = inflater.inflate(R.layout.day_item, weekDaysContainer, false);
            TextView dayName = dayView.findViewById(R.id.dayName);
            TextView dayNumber = dayView.findViewById(R.id.dayNumber);

            dayName.setText(weekDays[i]);
            int dayNumberValue = calendar.get(Calendar.DAY_OF_MONTH);
            dayNumber.setText(String.valueOf(dayNumberValue));

            // Listener kliknięcia
            dayView.setOnClickListener(v -> {
                if (selectedDayView != null) {
                    selectedDayView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    TextView prevName = selectedDayView.findViewById(R.id.dayName);
                    TextView prevNumber = selectedDayView.findViewById(R.id.dayNumber);
                    prevName.setTextColor(Color.BLACK);
                    prevNumber.setTextColor(Color.BLACK);
                }

                v.setBackgroundResource(R.drawable.selected_day_background);
                TextView newName = v.findViewById(R.id.dayName);
                TextView newNumber = v.findViewById(R.id.dayNumber);
                newName.setTextColor(Color.WHITE);
                newNumber.setTextColor(Color.WHITE);

                selectedDayView = v;
            });

            // Domyślnie zaznacz wybrany dzień
            if (dayNumberValue == day && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year) {
                dayView.setBackgroundResource(R.drawable.selected_day_background);
                dayName.setTextColor(Color.WHITE);
                dayNumber.setTextColor(Color.WHITE);
                selectedDayView = dayView;
            }

            weekDaysContainer.addView(dayView);
            calendar.add(Calendar.DAY_OF_MONTH, 1); // przechodzimy do następnego dnia
        }
    }

    // tu będę musiał przekazywać dzisiejszą datę
    private void OnCaloriesSummaryClick() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new DziennikPodsumowanieFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAddClickListener(DishItem dishItem) {

    }

    @Override
    public void onCameraAddClick(DishItem dishItem) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                        .replace(R.id.frame_layout, new AparatFragment())
                        .addToBackStack(null)
                        .commit();
    }

    @Override
    public void onMealTimingCliked(DishItem dishItem) {
        Toast.makeText(getContext(), dishItem.getNazwa(), Toast.LENGTH_SHORT).show();
    }
}