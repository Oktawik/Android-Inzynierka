package com.example.pierwszawersjaapki.Gemini;

public class PopularQuestionsItem {

    String popular_question_text;
    int popular_question_image;

    public PopularQuestionsItem(String popular_question_text, int popular_question_image) {
        this.popular_question_text = popular_question_text;
        this.popular_question_image = popular_question_image;
    }

    public String getPopular_question_text() {
        return popular_question_text;
    }

    public void setPopular_question_text(String popular_question_text) {
        this.popular_question_text = popular_question_text;
    }

    public int getPopular_question_image() {
        return popular_question_image;
    }

    public void setPopular_question_image(int popular_question_image) {
        this.popular_question_image = popular_question_image;
    }
}
