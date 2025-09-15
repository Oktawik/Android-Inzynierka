package com.example.pierwszawersjaapki;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;

// Modele
import com.example.pierwszawersjaapki.model.DailyData;
import com.example.pierwszawersjaapki.model.MacroNutrient;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DziennikFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DziennikFragment extends Fragment {

    // Zmienne kalendarza
    private View selectedDayView = null; // przechowuje poprzednio zaznaczony dzień

    // Zmienne podsumowania
    private View rootView;
    private TextView caloriesNeeded, caloriesConsumed, caloriesBurned, netCalories;
    private ProgressBar proteinProgress, carbsProgress, fatProgress;
    private TextView proteinText, carbsText, fatText;

    private DailyData currentDayData = new DailyData();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DziennikFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DziennikFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DziennikFragment newInstance(String param1, String param2) {
        DziennikFragment fragment = new DziennikFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_dziennik, container, false);

        LinearLayout weekDaysContainer = rootView.findViewById(R.id.weekDaysContainer);
        ImageButton calendarButton = rootView.findViewById(R.id.calendarButton);

        // Inicjalizacja Podsumowania
        initializeSummaryViews();

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

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Startowe dane testowe
        initializeSampleData();
        updateDailySummary();
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
                    selectedDayView.setBackgroundColor(Color.WHITE);
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

    // METODY PODSUMOWANIA
    // Aktualizacja całego podsumowania
    private void updateDailySummary() {
        updateCalories();
        updateMacronutrients();
    }

    // Aktualizacja kalorii
    private void updateCalories() {
        caloriesNeeded.setText(String.valueOf(currentDayData.caloriesNeeded));
        caloriesConsumed.setText(String.valueOf(currentDayData.caloriesConsumed));
        caloriesBurned.setText(String.valueOf(currentDayData.caloriesBurned));

        int netCals = currentDayData.caloriesNeeded - currentDayData.caloriesConsumed + currentDayData.caloriesBurned;
        netCalories.setText(String.valueOf(netCals));
    }

    // Aktualizacja makroskładników
    private void updateMacronutrients() {
        updateMacro(proteinProgress, proteinText, currentDayData.protein);
        updateMacro(carbsProgress, carbsText, currentDayData.carbs);
        updateMacro(fatProgress, fatText, currentDayData.fat);
    }

    private void updateMacro(ProgressBar progressBar, TextView textView, MacroNutrient macro) {
        int progress = (int) ((macro.consumed / (double) macro.target) * 100);
        progressBar.setProgress(Math.min(progress, 100));
        textView.setText(macro.consumed + "g / " + macro.target + "g");
    }

    private void initializeSummaryViews() {
        caloriesNeeded = rootView.findViewById(R.id.caloriesNeeded);
        caloriesConsumed = rootView.findViewById(R.id.caloriesConsumed);
        caloriesBurned = rootView.findViewById(R.id.caloriesBurned);
        netCalories = rootView.findViewById(R.id.netCalories);

        proteinProgress = rootView.findViewById(R.id.proteinProgress);
        carbsProgress = rootView.findViewById(R.id.carbsProgress);
        fatProgress = rootView.findViewById(R.id.fatProgress);
        proteinText = rootView.findViewById(R.id.proteinText);
        carbsText = rootView.findViewById(R.id.carbsText);
        fatText = rootView.findViewById(R.id.fatText);
    }

    // Dodaj spożyte kalorie
    public void addCalories(int calories, int protein, int carbs, int fat) {
        currentDayData.caloriesConsumed += calories;
        currentDayData.protein.consumed += protein;
        currentDayData.carbs.consumed += carbs;
        currentDayData.fat.consumed += fat;
        updateDailySummary();
    }

    // Dodaj spalone kalorie
    public void addBurnedCalories(int calories) {
        currentDayData.caloriesBurned += calories;
        updateDailySummary();
    }

    // Ustaw cele na dany dzień
    public void setDailyTargets(int calorieTarget, int proteinTarget, int carbsTarget, int fatTarget) {
        currentDayData.caloriesNeeded = calorieTarget;
        currentDayData.protein.target = proteinTarget;
        currentDayData.carbs.target = carbsTarget;
        currentDayData.fat.target = fatTarget;
        updateDailySummary();
    }

    private void initializeSampleData() {
        currentDayData.caloriesConsumed = 1200;
        currentDayData.caloriesBurned = 300;
        currentDayData.protein.consumed = 45;
        currentDayData.carbs.consumed = 180;
        currentDayData.fat.consumed = 40;
    }

}