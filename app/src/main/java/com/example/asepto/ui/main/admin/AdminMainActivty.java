package com.example.asepto.ui.main.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.asepto.R;
import com.example.asepto.ui.main.admin.home.AdminHomeFragment;

public class AdminMainActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_activty);
        replace(new AdminHomeFragment());
    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameKaryawn, fragment)
                .commit();

    }
}