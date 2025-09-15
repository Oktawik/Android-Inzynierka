package com.example.pierwszawersjaapki;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        // Domyślny fragment
        loadFragment(new DziennikFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.dziennik) {
                fragment = new DziennikFragment();
            } else if (itemId == R.id.postep) {
                fragment = new PostepFragment();
            } else if (itemId == R.id.edukacja) {
                fragment = new EdukacjaFragment();
            } else if (itemId == R.id.przepisy) {
                fragment = new PrzepisyFragment();
            } else if (itemId == R.id.wiecej) {
                fragment = new WiecejFragment();
            }
            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}