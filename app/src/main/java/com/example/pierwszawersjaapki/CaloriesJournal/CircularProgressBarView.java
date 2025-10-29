package com.example.pierwszawersjaapki.CaloriesJournal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class CircularProgressBarView extends View {

    // Ustawienia dla rysowania
    private final Paint backgroundPaint;
    private final Paint progressPaint;
    private final RectF rectF;

    // Zmienne dla postępu
    private int maxProgress = 100;
    private int currentProgress = 0;

    // Kąty łuku
    private final float startAngle = 135f; // Łuk zaczyna się na dole po lewej
    private final float sweepAngle = 270f; // Łuk pokrywa 270 stopni (3/4 koła)
    private final float strokeWidth = 30f; // Grubość linii

    // Konstruktory
    public CircularProgressBarView(Context context) {
        this(context, null);
    }

    public CircularProgressBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Inicjalizacja obiektów Paint dla tła
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setColor(Color.parseColor("#E0E0E0")); // Kolor tła (szary)
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND); // Okrągłe zakończenia linii

        // Inicjalizacja obiektów Paint dla postępu
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setColor(Color.parseColor("#4CAF50")); // Kolor postępu (miętowy)
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        setBackgroundColor(Color.TRANSPARENT);

        // Obszar rysowania
        rectF = new RectF();
    }

    // Metoda wywoływana, gdy zmienia się rozmiar widoku
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Obliczanie obszaru rysowania (RectF)
        int size = Math.min(w, h);
        // Odsuń, aby uwzględnić połowę grubości linii
        float offset = strokeWidth / 2;

        rectF.set(
                offset,
                offset,
                size - offset,
                size - offset
        );
    }

    // Metoda rysująca widok
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 0. Rysowanie czarnego tła (wypełnia kwadrat widoku)
        // To jest kluczowe, aby nadać kontekst wizualny jak na Twoim obrazku.
        // canvas.drawColor(Color.parseColor("#2C2C2C")); // Użyj ciemnego koloru tła

        // Sprawdź, czy widok ma wystarczający rozmiar
        if (rectF.width() > 0 && rectF.height() > 0) {
            // 1. Rysowanie Tła (szary łuk 270 stopni)
            canvas.drawArc(
                    rectF,
                    startAngle,
                    sweepAngle,
                    false, // false, bo to linia (stroke), nie wycinek
                    backgroundPaint
            );

            // 2. Rysowanie Postępu (miętowy łuk)
            // Obliczamy kąt dla aktualnego postępu: (postęp / max) * 270 stopni
            float progressSweepAngle = ((float) currentProgress / maxProgress) * sweepAngle;

            canvas.drawArc(
                    rectF,
                    startAngle,
                    progressSweepAngle,
                    false,
                    progressPaint
            );
        }
    }

    // --- Gettery i Settery dla Postępu ---

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        invalidate(); // Wymuś przerysowanie widoku
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        invalidate(); // Wymuś przerysowanie widoku
    }
}