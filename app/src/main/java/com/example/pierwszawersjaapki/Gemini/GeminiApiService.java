package com.example.pierwszawersjaapki.Gemini;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeminiApiService {

    // Używamy v1 zamiast v1beta
    @POST("v1/models/gemini-2.5-flash:generateContent")
    Call<GeminiResponse> sendMessage(
            @Body GeminiRequest request,
            @Query("key") String apiKey
    );
}