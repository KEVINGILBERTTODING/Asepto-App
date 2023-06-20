package com.example.asepto.ui.main.admin.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asepto.R;
import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.databinding.FragmentAdminHomeBinding;
import com.example.asepto.ui.main.auth.LoginActivity;
import com.example.asepto.ui.main.karyawan.project.KaryawanProjectFragment;
import com.example.asepto.util.Constans;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomeFragment extends Fragment {
    private FragmentAdminHomeBinding binding;
    private AdminService adminService;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String userId;
    private AlertDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constans.USER_ID, null);
        editor = sharedPreferences.edit();
        adminService = ApiConfig.getClient().create(AdminService.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTotalProject("all", binding.tvTotalProject);
        getTotalProject("1", binding.tvTotalProjectSelesai);
        getTotalProject("0", binding.tvTotalProjectSedangBerjalan);
        listener();
    }

    private void listener() {
        binding.cvProjectSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace("1");
            }
        });

        binding.cvTotalProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace("all");
            }
        });
        binding.cvTotalProjectSedangBerjalan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace("0");
            }
        });

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.layout_alert_log_out);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Button btnYa, btnTidak;
                btnYa = dialog.findViewById(R.id.btnOke);
                btnTidak = dialog.findViewById(R.id.btnBatal);
                dialog.show();

                btnYa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logOut();
                        dialog.dismiss();
                    }
                });
                btnTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void getTotalProject(String status, TextView tvTotal) {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllProject(status).enqueue(new Callback<List<ProjectModel>>() {
            @Override
            public void onResponse(Call<List<ProjectModel>> call, Response<List<ProjectModel>> response) {
                showProgressBar("d", "d", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    tvTotal.setText(String.valueOf(response.body().size()));
                }else {
                    tvTotal.setText("0");
                }
            }

            @Override
            public void onFailure(Call<List<ProjectModel>> call, Throwable t) {
                showProgressBar("d", "d", false);
                showToast("err", "Tidak ada koneksi internet");


            }
        });

    }


    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
            Toasty.success(getContext(), text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), text, Toasty.LENGTH_SHORT).show();
        }
    }

    private void replace(String status){
        Fragment fr = new KaryawanProjectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("status_id", status);
        fr.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameKaryawn, fr).addToBackStack(null)
                .commit();

    }

    private void logOut() {
        editor.clear().apply();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();

    }
}