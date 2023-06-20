package com.example.asepto.ui.main.karyawan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.asepto.R;
import com.example.asepto.ui.main.karyawan.home.KaryawanHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class KaryawanMainActivty extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan_main_activty);
        bottomNavigationView = findViewById(R.id.bottom_bar);
        lisetener();
        replace(new KaryawanHomeFragment());
    }

    private void lisetener() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    replace(new KaryawanHomeFragment());
                    return true;
                }
                return false;
            }
        });
    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameKaryawn, fragment)
                .commit();

    }
}