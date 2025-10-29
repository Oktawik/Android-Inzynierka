package com.example.pierwszawersjaapki.Gemini;

import java.util.List;

public class GeminiMultimodalResponse {
    public List<Candidate> candidates;

    public static class Candidate {
        public Content content;
    }

    public static class Content {
        public List<Part> parts;
    }

    public static class Part {
        public String text;
    }

    // Metoda pomocnicza do łatwego wyciągania tekstu z odpowiedzi
    public String getTextOutput() {
        if (candidates != null && !candidates.isEmpty()
                && candidates.get(0).content != null
                && candidates.get(0).content.parts != null
                && !candidates.get(0).content.parts.isEmpty()) {
            return candidates.get(0).content.parts.get(0).text;
        }
        return "Nie otrzymano opisu od Gemini.";
    }
}