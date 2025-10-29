package com.example.pierwszawersjaapki.Listeners;

import com.example.pierwszawersjaapki.Models.InstructionsResponse;

import java.util.List;

public interface InstructionsListener {
    void didFetch(List<InstructionsResponse> response, String message);
    void didError(String message);
}
