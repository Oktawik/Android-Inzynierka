package com.example.pierwszawersjaapki;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btnZaloguj;
    private TextInputEditText etEmailLogin;
    private TextInputEditText etHasloLogin;
    private TextView tvNiePosiadaszKonta;
    private TextView tvZapomnialemHasla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnZaloguj = findViewById(R.id.btn_zaloguj);
        etEmailLogin = findViewById(R.id.et_email_login);
        etHasloLogin = findViewById(R.id.et_haslo_login);
        tvNiePosiadaszKonta = findViewById(R.id.tv_nie_posiadasz_konta);
        tvZapomnialemHasla = findViewById(R.id.tv_zapomniales_hasla);
        auth = FirebaseAuth.getInstance();

        btnZaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailLogin.getText().toString().trim();
                String pass = etHasloLogin.getText().toString().trim();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if(!pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(email,pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(LoginActivity.this, "Pomyslnie zalogowano!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Podane dane są nieprawidłowe", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        etHasloLogin.setError("Pole hasło jest wymagane");
                    }
                } else if (email.isEmpty()) {
                    etEmailLogin.setError("Pole email jest wymagane");
                } else {
                    etEmailLogin.setError("Podano błędny email");
                }
            }
        });


        tvNiePosiadaszKonta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvZapomnialemHasla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}