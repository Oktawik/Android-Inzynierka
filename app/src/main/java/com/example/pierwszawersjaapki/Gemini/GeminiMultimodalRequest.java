package com.example.pierwszawersjaapki.Gemini;

import java.util.Collections;
import java.util.List;

public class GeminiMultimodalRequest {
    public List<Content> contents;

    /**
     * Konstruktor dla żądania multimodalnego (tekst + obraz).
     * @param prompt Tekst do wysłania.
     * @param base64Image Obraz zakodowany w Base64.
     * @param mimeType Typ MIME obrazu (np. "image/jpeg").
     */
    public GeminiMultimodalRequest(String prompt, String base64Image, String mimeType) {
        // Tworzymy listę części: tekst + obraz
        Part textPart = new Part(prompt);
        Part imagePart = new Part(new InlineData(mimeType, base64Image));

        // Musimy stworzyć nową listę, ponieważ nie możemy użyć Collections.singletonList
        List<Part> parts = new java.util.ArrayList<>();
        parts.add(textPart);
        parts.add(imagePart);

        // Tworzymy zawartość
        Content content = new Content(parts);

        // Tworzymy listę zawartości
        this.contents = Collections.singletonList(content);
    }

    public static class Content {
        public List<Part> parts;

        public Content(List<Part> parts) {
            this.parts = parts;
        }
    }

    public static class Part {
        public String text;
        public InlineData inlineData;

        // Konstruktor dla części tekstowej (prompt)
        public Part(String text) {
            this.text = text;
            this.inlineData = null;
        }

        // Konstruktor dla części obrazkowej (Base64)
        public Part(InlineData inlineData) {
            this.text = null;
            this.inlineData = inlineData;
        }
    }

    public static class InlineData {
        public String mimeType;
        public String data; // String Base64

        public InlineData(String mimeType, String data) {
            this.mimeType = mimeType;
            this.data = data;
        }
    }
}