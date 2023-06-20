package com.example.asepto.ui.main.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asepto.R;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.AuthService;
import com.example.asepto.data.model.ResponseModel;
import com.example.asepto.ui.main.admin.AdminMainActivty;
import com.example.asepto.ui.main.karyawan.KaryawanMainActivty;
import com.example.asepto.util.Constans;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AuthService authService;
    private AlertDialog progressDialog;
    private EditText etUsername, etPasswprd;
    private Button btnLogin;
    private TextView tvMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        authService = ApiConfig.getClient().create(AuthService.class);

        init();
        listener();
        sharedPreferences = getSharedPreferences(Constans.SHARED_PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();


    }

    private void init(){

        etUsername = findViewById(R.id.etUsername);
        etPasswprd = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvMasuk = findViewById(R.id.tvMasuk);
    }

    private void listener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Tidak boleh kosong");
                }else if (etPasswprd.getText().toString().isEmpty()) {
                    etPasswprd.setError("Tidak boleh kosong");
                }else {
                    login();

                }
            }
        });

        tvMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLoginActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void login() {
        showProgressBar("Loading", "Memeriksa data...", true);
        authService.loginAdmin(etUsername.getText().toString(), etPasswprd.getText().toString())
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        showProgressBar("sd", "SD", false);
                        if (response.isSuccessful() && response.body().getCode() == 200) {
                            if (response.body().getRole() == 1) { // admin
                                editor.putBoolean("login", true);
                                editor.putString(Constans.USER_ID, response.body().getUserId());
                                editor.putInt(Constans.ROLE, response.body().getRole());
                                editor.apply();
                                startActivity(new Intent(AdminLoginActivity.this, AdminMainActivty.class));
                                finish();

                            }else {
                                showToast("err", "Terjadi kesalahan");

                            }

                        }else {
                            showToast("err", response.body().getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        showProgressBar("sd", "SD", false);
                        showToast("err", "Tidak ada koneksi internet");



                    }
                });



    }

    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdminLoginActivity.this);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setCancelable(false);
                progressDialog = builder.create();
            }
            progressDialog.show(); // Menampilkan progress dialog
        } else {
            // Menyembunyikan progress dialog jika ada
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
    private void showToast(String jenis, String text) {
        if (jenis.equals("success")) {
            Toasty.success(AdminLoginActivity.this, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(AdminLoginActivity.this, text, Toasty.LENGTH_SHORT).show();
        }
    }
}