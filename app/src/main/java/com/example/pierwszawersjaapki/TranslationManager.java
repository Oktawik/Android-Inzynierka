package com.example.pierwszawersjaapki;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;

public class TranslationManager {
    private static final String TAG = "TranslationManager";
    private Context context;
    private Translator translator;
    private boolean isModelDownloaded = false;

    public TranslationManager(Context context) {
        this.context = context;
        initializeTranslator();
    }

    private void initializeTranslator() {
        // Konfiguracja tłumaczenia z angielskiego na polski
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.POLISH)
                .build();

        translator = Translation.getClient(options);
        Log.d(TAG, "Translator initialized");
    }

    public interface TranslationListener {
        void onTranslationComplete(String translatedText);
        void onTranslationError(String error);
    }

    public interface ModelDownloadListener {
        void onModelDownloaded();
        void onModelDownloadError(String error);
    }

    // Metoda do pobierania modelu przed tłumaczeniem
    public void ensureModelDownloaded(ModelDownloadListener listener) {
        // Sprawdź najpierw czy model już istnieje
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()  // Wymagaj WiFi - model jest duży
                .build();

        Log.d(TAG, "Checking if model needs download...");

        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(unused -> {
                    isModelDownloaded = true;
                    Log.d(TAG, "Model is ready (downloaded or already exists)");
                    listener.onModelDownloaded();
                })
                .addOnFailureListener(e -> {
                    isModelDownloaded = false;
                    Log.e(TAG, "Model download/check failed: " + e.getMessage());
                    e.printStackTrace();
                    listener.onModelDownloadError("Błąd: " + e.getMessage());
                });
    }

    // Alternatywna metoda - bez wymagania pobrania modelu
    public void translateTextWithoutModelCheck(String text, TranslationListener listener) {
        if (text == null || text.isEmpty()) {
            Log.d(TAG, "Text is null or empty");
            listener.onTranslationComplete(text);
            return;
        }

        Log.d(TAG, "Attempting translation without model check: " + text);

        translator.translate(text)
                .addOnSuccessListener(translatedText -> {
                    Log.d(TAG, "Translation successful: " + text + " -> " + translatedText);
                    isModelDownloaded = true; // Model działa
                    listener.onTranslationComplete(translatedText);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Translation failed: " + e.getMessage());
                    e.printStackTrace();
                    // Zwróć oryginalny tekst zamiast błędu
                    listener.onTranslationComplete(text);
                });
    }

    public void translateText(String text, TranslationListener listener) {
        if (text == null || text.isEmpty()) {
            Log.d(TAG, "Text is null or empty");
            listener.onTranslationComplete(text);
            return;
        }

        Log.d(TAG, "Translating: " + text);

        translator.translate(text)
                .addOnSuccessListener(translatedText -> {
                    Log.d(TAG, "Translation successful: " + text + " -> " + translatedText);
                    listener.onTranslationComplete(translatedText);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Translation failed: " + e.getMessage());
                    listener.onTranslationError("Błąd tłumaczenia: " + e.getMessage());
                });
    }

    // Metoda do tłumaczenia listy tekstów
    public void translateTexts(List<String> texts, TranslationListener listener) {
        if (texts == null || texts.isEmpty()) {
            listener.onTranslationComplete("");
            return;
        }

        List<Task<String>> tasks = new ArrayList<>();
        for (String text : texts) {
            if (text != null && !text.isEmpty()) {
                tasks.add(translator.translate(text));
            }
        }

        Tasks.whenAllSuccess(tasks)
                .addOnSuccessListener(results -> {
                    StringBuilder combined = new StringBuilder();
                    for (Object result : results) {
                        combined.append(result.toString()).append(", ");
                    }
                    String finalResult = combined.length() > 2
                            ? combined.substring(0, combined.length() - 2)
                            : combined.toString();
                    listener.onTranslationComplete(finalResult);
                })
                .addOnFailureListener(e -> {
                    listener.onTranslationError("Błąd tłumaczenia listy: " + e.getMessage());
                });
    }

    public boolean isModelDownloaded() {
        return isModelDownloaded;
    }

    public void close() {
        if (translator != null) {
            translator.close();
            Log.d(TAG, "Translator closed");
        }
    }

}