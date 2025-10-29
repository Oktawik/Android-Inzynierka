package com.example.pierwszawersjaapki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView tvPamietaszHaslo;
    private TextInputEditText etEmailForgot;
    private Button btnPrzywrocHaslo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        tvPamietaszHaslo = findViewById(R.id.tv_pamietasz_haslo);
        etEmailForgot = findViewById(R.id.et_email_forgot);
        btnPrzywrocHaslo = findViewById(R.id.btn_przywroc_haslo);

        tvPamietaszHaslo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}