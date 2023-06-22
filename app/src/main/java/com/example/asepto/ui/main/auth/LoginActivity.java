package com.example.asepto.ui.main.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AuthService authService;
    private TextView tvMasuk;
    private AlertDialog progressDialog;
    private EditText etUsername, etEmail, etUsernameAdmin, etPassword;
    private LinearLayout lrLoginAdmin, lrLoginKaryawan;
    private Button btnLogin, btnKaryawan, btnAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authService = ApiConfig.getClient().create(AuthService.class);
        sharedPreferences = getSharedPreferences(Constans.SHARED_PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        init();
        listener();

        if (sharedPreferences.getBoolean("login", false) == true){
            if (sharedPreferences.getInt(Constans.ROLE, 0) == 2) {
                startActivity(new Intent(LoginActivity.this, KaryawanMainActivty.class));
                finish();
            }else {
                startActivity(new Intent(LoginActivity.this, AdminMainActivty.class));
                finish();
            }
        }
    }

    private void init(){
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        btnLogin = findViewById(R.id.btnLogin);
        btnKaryawan = findViewById(R.id.btnKaryawan);
        btnAdmin = findViewById(R.id.btnAdmin);
        etUsernameAdmin = findViewById(R.id.etUsernameAdmin);
        etPassword = findViewById(R.id.etPassword);
        lrLoginAdmin = findViewById(R.id.lrLoginAdmin);
        lrLoginKaryawan = findViewById(R.id.lrLoginKaryawan);
    }


    private void listener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lrLoginAdmin.getVisibility() == View.VISIBLE) {
                    if (etUsernameAdmin.getText().toString().isEmpty()) {
                        etUsernameAdmin.setError("Username tidak boleh kosong");
                        etUsernameAdmin.requestFocus();
                    }else if (etPassword.getText().toString().isEmpty()) {
                        etPassword.setError("Password tidak boleh kosong");
                        etPassword.requestFocus();
                    }else {
                        loginAdmin();
                    }

                }else {

                    if (etUsername.getText().toString().isEmpty()) {
                        etUsername.setError("Username tidak boleh kosong");
                        etUsername.requestFocus();
                    }else if (etEmail.getText().toString().isEmpty()) {
                        etEmail.setError("Email tidak boleh kosong");
                        etEmail.requestFocus();
                    }else {
                        login();
                    }


                }
            }
        });



        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAdmin.setBackgroundColor(getColor(R.color.blue));
                btnKaryawan.setBackgroundColor(getColor(R.color.blue2));
                lrLoginKaryawan.setVisibility(View.GONE);
                lrLoginAdmin.setVisibility(View.VISIBLE);


            }
        });



        btnKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnKaryawan.setBackgroundColor(getColor(R.color.blue));
                btnAdmin.setBackgroundColor(getColor(R.color.blue2));
                lrLoginKaryawan.setVisibility(View.VISIBLE);
                lrLoginAdmin.setVisibility(View.GONE);

            }
        });

    }

    private void login() {
        showProgressBar("Loading", "Memeriksa data...", true);
        authService.loginKaryawan(etUsername.getText().toString(), etEmail.getText().toString())
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        showProgressBar("sd", "SD", false);
                        if (response.isSuccessful() && response.body().getCode() == 200) {
                            if (response.body().getRole() == 2) { // karyawan
                                editor.putBoolean("login", true);
                                editor.putString(Constans.USER_ID, response.body().getUserId());
                                editor.putInt(Constans.ROLE, response.body().getRole());
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, KaryawanMainActivty.class));
                                finish();

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

    private void loginAdmin() {
        showProgressBar("Loading", "Memeriksa data...", true);
        authService.loginAdmin(etUsernameAdmin.getText().toString(), etPassword.getText().toString())
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
                                startActivity(new Intent(LoginActivity.this, AdminMainActivty.class));
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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);
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
            Toasty.success(LoginActivity.this, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(LoginActivity.this, text, Toasty.LENGTH_SHORT).show();
        }
    }
}