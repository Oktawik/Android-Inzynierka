package com.example.pierwszawersjaapki;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView tvPosiadaszJuzKonto;
    private Button btnZalozKonto;
    private TextInputEditText etEmail;
    private TextInputEditText etHaslo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvPosiadaszJuzKonto = findViewById(R.id.tv_posiadasz_juz_konto);
        btnZalozKonto = findViewById(R.id.btn_zaloz_konto);
        etEmail = findViewById(R.id.et_email);
        etHaslo = findViewById(R.id.et_haslo);
        auth = FirebaseAuth.getInstance();

        // Po wcisnieciu posiadasz juz konto, przenosi na stronę logowania
        tvPosiadaszJuzKonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnZalozKonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etEmail.getText().toString().trim();
                String pass = etHaslo.getText().toString().trim();

                if (user.isEmpty()) {
                    etEmail.setError("Pole email nie może być puste");
                } else if (pass.isEmpty()) {
                    etHaslo.setError("Pole hasło nie może być puste");
                } else if (pass.length() < 3 || pass.length() > 12) {
                    etHaslo.setError("Hasło musi mieć od 3 do 12 znaków");
                }
                else {
                    if (Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                        auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Pomyślnie utworzono konto", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                } else {
                                    etEmail.setError("Istnieje już konto o tym emailu");
                                    // Toast.makeText(RegisterActivity.this, "Podczas tworzenia konta wystąpił błąd " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        etEmail.setError("Podano niepoprawny adres email.");
                        // Toast.makeText(RegisterActivity.this, "Podaj poprawny adres email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}