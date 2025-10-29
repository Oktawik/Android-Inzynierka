package com.example.pierwszawersjaapki.Listeners;

import com.example.pierwszawersjaapki.Models.RecipeDetailsResponse;

public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);
}
