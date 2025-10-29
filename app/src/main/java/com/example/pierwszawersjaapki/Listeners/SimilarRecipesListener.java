package com.example.pierwszawersjaapki.Listeners;

import com.example.pierwszawersjaapki.Models.SimilarRecipeResponse;

import java.util.List;

public interface SimilarRecipesListener {
    void didFetch(List<SimilarRecipeResponse> response, String message);
    void didError(String message);
}
