package com.example.pierwszawersjaapki;

import android.graphics.Bitmap; // NOWY IMPORT
import android.graphics.BitmapFactory; // NOWY IMPORT
import android.net.Uri; // NOWY IMPORT
import android.os.Bundle;

import androidx.annotation.NonNull; // NOWY IMPORT
import androidx.annotation.Nullable; // NOWY IMPORT
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager; // NOWY IMPORT
import androidx.fragment.app.FragmentResultListener; // NOWY IMPORT

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File; // NOWY IMPORT

// Dodaj implementację FragmentResultListener na potrzeby czystej komunikacji
public class AparatResultFragment extends Fragment {

    private ImageView iv_aparat_result;
    private TextView tv_aparat_result;

    public AparatResultFragment() {
        // Required empty public constructor
    }

    // Dodaj metodę do obsługi danych przekazanych po utworzeniu widoku
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ustaw słuchacza na wynik z AparatFragment
        getParentFragmentManager().setFragmentResultListener(
                AparatFragment.REQUEST_KEY_IMAGE_RESULT, // Klucz żądania
                this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                        // 1. Odbierz dane tekstowe i ścieżkę obrazu
                        String geminiText = result.getString(AparatFragment.BUNDLE_KEY_TEXT, "Brak opisu.");
                        String imagePath = result.getString(AparatFragment.BUNDLE_KEY_IMAGE_PATH, null);

                        // 2. Ustaw tekst
                        tv_aparat_result.setText(geminiText);

                        // 3. Wczytaj i wyświetl obraz
                        if (imagePath != null) {
                            try {
                                File imgFile = new File(imagePath);
                                if (imgFile.exists()) {
                                    // Wczytaj plik jako Bitmapę i ustaw w ImageView
                                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    iv_aparat_result.setImageBitmap(myBitmap);
                                    // UWAGA: Plik jest kasowany w AparatFragment, więc trzeba to ogarnąć,
                                    // lub po prostu skasować go tutaj po wczytaniu!
                                } else {
                                    Toast.makeText(requireContext(), "Plik obrazu nie istnieje!", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(requireContext(), "Błąd wczytywania obrazu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aparat_result, container, false);

        iv_aparat_result = view.findViewById(R.id.iv_aparat_result);
        tv_aparat_result = view.findViewById(R.id.tv_aparat_result);

        // Inicjalizuj TextView domyślnym tekstem oczekiwania
        tv_aparat_result.setText("Oczekuję na wynik...");

        return view;
    }
}