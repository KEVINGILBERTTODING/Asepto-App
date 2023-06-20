package com.example.asepto.ui.main.karyawan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.asepto.R;
import com.example.asepto.ui.main.karyawan.catatan.KaryawanCatatanFragment;
import com.example.asepto.ui.main.karyawan.home.KaryawanHomeFragment;
import com.example.asepto.ui.main.karyawan.project.KaryawanProjectFragment;
import com.example.asepto.ui.main.karyawan.project.KaryawanProjectNavbarFragment;
import com.example.asepto.ui.main.karyawan.review.KaryawanReviewFragment;
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
                }else if (item.getItemId() == R.id.menuReview) {
                    replace(new KaryawanReviewFragment());
                    return true;
                }else if (item.getItemId() == R.id.menuTambahan) {
                    replace(new KaryawanCatatanFragment());
                    return true;
                }else if (item.getItemId() == R.id.menuProject) {
                    replace(new KaryawanProjectNavbarFragment());
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