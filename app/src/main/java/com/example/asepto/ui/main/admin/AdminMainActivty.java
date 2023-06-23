package com.example.asepto.ui.main.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.asepto.R;
import com.example.asepto.ui.main.admin.Review.AdminReviewFragment;
import com.example.asepto.ui.main.admin.catatan.AdminCatatanFragment;
import com.example.asepto.ui.main.admin.home.AdminHomeFragment;
import com.example.asepto.ui.main.admin.karyawan.AdminKaryawanFragment;
import com.example.asepto.ui.main.admin.project.AdminProjectNavbarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminMainActivty extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_activty);
        init();
        listener();
        replace(new AdminHomeFragment());
    }

    private void listener() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    replace(new AdminHomeFragment());
                    return true;
                }else if (item.getItemId() == R.id.menuKaryawan) {
                    replace(new AdminKaryawanFragment());
                    return true;
                }else if (item.getItemId() == R.id.menuReview) {
                    replace(new AdminReviewFragment());
                    return true;
                }else if (item.getItemId() == R.id.menuTambahan) {
                    replace(new AdminCatatanFragment());
                    return true;
                }else if (item.getItemId() == R.id.menuProject) {
                    replace(new AdminProjectNavbarFragment());
                    return true;
                }
                return false;
            }
        });
    }

    private void  init() {
        bottomNavigationView = findViewById(R.id.bottom_bar);

    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, fragment)
                .commit();

    }
}