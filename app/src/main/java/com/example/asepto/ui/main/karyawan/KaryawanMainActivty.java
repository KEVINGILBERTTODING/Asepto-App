package com.example.asepto.ui.main.karyawan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.asepto.R;
import com.example.asepto.ui.main.karyawan.home.KaryawanHomeFragment;

public class KaryawanMainActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan_main_activty);
        replace(new KaryawanHomeFragment());
    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameKaryawn, fragment)
                .commit();

    }
}