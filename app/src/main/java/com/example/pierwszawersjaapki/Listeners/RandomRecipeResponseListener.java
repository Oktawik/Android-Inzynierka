package com.example.pierwszawersjaapki.Listeners;

import com.example.pierwszawersjaapki.Models.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);
}
