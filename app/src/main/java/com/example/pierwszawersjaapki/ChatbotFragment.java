package com.example.pierwszawersjaapki;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pierwszawersjaapki.Gemini.ChatAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.pierwszawersjaapki.Gemini.GeminiApiService;
import com.example.pierwszawersjaapki.Gemini.GeminiRequest;
import com.example.pierwszawersjaapki.Gemini.GeminiResponse;
import com.example.pierwszawersjaapki.Gemini.Message;
import com.example.pierwszawersjaapki.Gemini.PopularQuestionsAdapter;
import com.example.pierwszawersjaapki.Gemini.PopularQuestionsItem;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatbotFragment extends Fragment {

    private RecyclerView rv_chatbot;
    private ChatAdapter adapter;
    private List<Message> messages = new ArrayList<>();
    private EditText et_message_input;
    private ImageButton btn_send_message;
    private TextView tv_chatbot_datetime;
    private Date now;
    private RecyclerView rv_chatbot_popularne_pytania;
    private List<PopularQuestionsItem> popularne_pytania = new ArrayList<>();
    private PopularQuestionsAdapter popularQuestionsAdapter;
    private LinearLayout ll_popular_questions;
    private LinearLayout ll_chatbot_widok_powitalny;
    private List<PopularQuestionsItem> randomThreeQuestions;
    private ImageButton btn_nowy_czat;

    private GeminiApiService apiService;

    // WAŻNE: Zamień na swój prawdziwy klucz API z Google AI Studio
    private static final String API_KEY = "AIzaSyC1su4bq1zcc5_ZtmJiMdTXZeyR4C_S4qQ";

    public ChatbotFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatbot, container, false);

        rv_chatbot_popularne_pytania = view.findViewById(R.id.rv_chatbot_popularne_pytania);
        ll_popular_questions = view.findViewById(R.id.ll_popular_questions);
        ll_chatbot_widok_powitalny = view.findViewById(R.id.ll_chatbot_widok_powitalny);
        btn_nowy_czat = view.findViewById(R.id.btn_nowy_chat);

        rv_chatbot = view.findViewById(R.id.rv_chatbot);
        et_message_input = view.findViewById(R.id.et_message_input);
        btn_send_message = view.findViewById(R.id.btn_send_message);

        // tv_chatbot_datetime = view.findViewById(R.id.tv_chatbot_datetime);
        // now = new Date();
        // Formatowanie
        // SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm", new Locale("pl"));
        // String formattedDate = sdf.format(now);
        // tv_chatbot_datetime.setText(formattedDate);

        // Obsluga recyclerview do popularnych pytan
        popularne_pytania.add(new PopularQuestionsItem("Jakie są zdrowe przekąski?", R.drawable.ic_chatbot));
        popularne_pytania.add(new PopularQuestionsItem("Jak skutecznie przygotowywać posiłki?", R.drawable.ic_chatbot));
        popularne_pytania.add(new PopularQuestionsItem("Korzyści z diety roślinnej", R.drawable.ic_chatbot));
        popularne_pytania.add(new PopularQuestionsItem("Pomysły na niskowęglowodanowe kolacje", R.drawable.ic_chatbot));
        popularne_pytania.add(new PopularQuestionsItem("Porady na zbilansowane śniadanie", R.drawable.ic_chatbot));
        popularne_pytania.add(new PopularQuestionsItem("Ile wody pić każdego dnia?", R.drawable.ic_chatbot));

        // Losowanie 3 pytań
        // Tasujemy całą listę
        Collections.shuffle(popularne_pytania);
        randomThreeQuestions = popularne_pytania.subList(0,3);

        rv_chatbot_popularne_pytania.setLayoutManager(new LinearLayoutManager(getContext()));
        popularQuestionsAdapter = new PopularQuestionsAdapter(
                getContext(),
                randomThreeQuestions,
                pytanie -> {
                    String nazwa = pytanie.getPopular_question_text();
                    ll_popular_questions.setVisibility(View.GONE);
                    ll_chatbot_widok_powitalny.setVisibility(View.GONE);
                    rv_chatbot.setVisibility(View.VISIBLE);

                    addMessage(nazwa, true);
                    sendToGemini(nazwa);

                }
        );
        rv_chatbot_popularne_pytania.setAdapter(popularQuestionsAdapter);

        // Obsluga recyclerview do wiadomosci
        adapter = new ChatAdapter(messages);
        rv_chatbot.setAdapter(adapter);
        rv_chatbot.setLayoutManager(new LinearLayoutManager(getContext()));

        // Klient OkHttp i zwiększenie limitu do 30 sekund
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS) // Limit czasu na nawiązanie połączenia
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)    // Limit czasu na odczyt danych z serwera
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)   // Limit czasu na wysłanie danych do serwera (mniej istotne dla GET/krótkich POST)
                .build();

        // Inicjalizacja Retrofit - poprawiony base URL
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GeminiApiService.class);

        btn_send_message.setOnClickListener(v -> {
            String text = et_message_input.getText().toString().trim();
            if (!text.isEmpty()) {
                ll_popular_questions.setVisibility(View.GONE);
                ll_chatbot_widok_powitalny.setVisibility(View.GONE);
                rv_chatbot.setVisibility(View.VISIBLE);
                addMessage(text, true);
                et_message_input.setText("");
                sendToGemini(text);
            }
        });

        btn_nowy_czat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, new ChatbotFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void addMessage(String text, boolean isUser) {
        // Logowanie dla celów debugowania
        Log.d("ChatbotUI", "Próba dodania wiadomości: " + (isUser ? "USER" : "BOT") + ": " + text);

        // Używamy runOnUiThread, aby upewnić się, że aktualizacja UI następuje w wątku głównym.
        // Jest to kluczowe, ponieważ metoda ta jest wywoływana z wątku Retrofit.
        if (isAdded()) {
            requireActivity().runOnUiThread(() -> {
                messages.add(new Message(text, isUser));

                // Używamy adapter, nie rv_chatbot! W Twoim kodzie było to dobrze.
                adapter.notifyItemInserted(messages.size() - 1);

                // Przewinięcie widoku do nowej wiadomości
                rv_chatbot.scrollToPosition(messages.size() - 1);

                Log.d("ChatbotUI", "Wiadomość " + (isUser ? "USER" : "BOT") + " dodana do UI.");
            });
        }
    }

    private void sendToGemini(String text) {
        Log.d("Gemini", "Wysyłam wiadomość: " + text);

        String polishText = "Odpowiadaj WYŁĄCZNIE w języku polskim. Pytanie: " + text;
        GeminiRequest request = new GeminiRequest(polishText);

        // 1. DODAJ WIADOMOŚĆ ŁADOWANIA
        // (Robimy to zanim wyślemy zapytanie)
        addLoadingMessage();

        Call<GeminiResponse> call = apiService.sendMessage(request, API_KEY);
        Log.d("Gemini", "URL zapytania: " + call.request().url());

        call.enqueue(new retrofit2.Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, retrofit2.Response<GeminiResponse> response) {
                // 2. USUŃ WIADOMOŚĆ ŁADOWANIA (niezależnie od sukcesu)
                removeLoadingMessage();

                Log.d("Gemini", "Otrzymano odpowiedź. Kod: " + response.code());
                if (!isAdded()) {
                    Log.w("Gemini", "Fragment nie jest już dodany, ignoruję odpowiedź.");
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    // ... (reszta logiki sukcesu bez zmian)
                    GeminiResponse responseBody = response.body();
                    String botReply = responseBody.getText_output();

                    Log.d("Gemini", "Liczba kandydatów: " + (responseBody.candidates != null ? responseBody.candidates.size() : "0"));
                    Log.d("Gemini", "Tekst odpowiedzi: " + (botReply.isEmpty() ? "<PUSTY TEKST>" : botReply));

                    if (botReply != null && !botReply.isEmpty()) {
                        addMessage(botReply, false);
                    } else {
                        addMessage("Otrzymano pustą odpowiedź. Możliwa blokada bezpieczeństwa API.", false);
                    }
                } else {
                    // ... (reszta logiki błędu bez zmian)
                    Log.e("Gemini", "Error response: " + response.code() + " " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e("Gemini", "Error body: " + errorBody);
                            addMessage("Błąd API (" + response.code() + "): " + errorBody, false);
                        } else {
                            addMessage("Błąd API: Brak treści błędu (Kod: " + response.code() + ")", false);
                        }
                    } catch (Exception e) {
                        Log.e("Gemini", "Błąd odczytu errorBody", e);
                        addMessage("Przepraszam, wystąpił błąd: " + response.code(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                // 3. USUŃ WIADOMOŚĆ ŁADOWANIA (również przy błędzie sieci)
                removeLoadingMessage();

                Log.e("Gemini", "Request failed: " + t.getClass().getName() + " - " + t.getMessage(), t);
                if (!isAdded()) {
                    Log.w("Gemini", "Fragment nie jest już dodany, ignoruję błąd połączenia.");
                    return;
                }
                addMessage("Błąd połączenia: " + t.getMessage(), false);
            }
        });
    }

    // Metoda do dodawania wskaźnika ładowania
    private void addLoadingMessage() {
        if (isAdded()) {
            requireActivity().runOnUiThread(() -> {
                // Używamy nowego konstruktora Message(true)
                messages.add(new Message(true));
                adapter.notifyItemInserted(messages.size() - 1);
                rv_chatbot.scrollToPosition(messages.size() - 1);
                Log.d("ChatbotUI", "Dodano wskaźnik ładowania.");
            });
        }
    }

    // Metoda do usuwania wskaźnika ładowania
    private void removeLoadingMessage() {
        if (isAdded()) {
            requireActivity().runOnUiThread(() -> {
                int lastIndex = messages.size() - 1;
                // Sprawdzamy, czy ostatnia wiadomość NA PEWNO jest wskaźnikiem ładowania
                if (lastIndex >= 0 && messages.get(lastIndex).isLoading()) {
                    messages.remove(lastIndex);
                    adapter.notifyItemRemoved(lastIndex);
                    Log.d("ChatbotUI", "Usunięto wskaźnik ładowania.");
                }
            });
        }
    }
}