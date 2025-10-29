package com.example.pierwszawersjaapki.Gemini;

public class Message {
    private String text;
    private boolean isUser;
    private boolean isLoading;

    public Message(String text, boolean isUser) {
        this.isUser = isUser;
        this.text = text;
        this.isLoading = false;
    }

    public Message(boolean isLoading) {
        this.text = "";
        this.isUser = false;
        this.isLoading = isLoading;
    }

    public String getText() {
        return text;
    }

    public boolean isUser() {
        return isUser;
    }

    public boolean isLoading() { return isLoading; }
}
