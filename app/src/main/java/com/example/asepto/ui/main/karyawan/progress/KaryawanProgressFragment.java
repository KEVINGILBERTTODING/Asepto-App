package com.example.asepto.ui.main.karyawan.progress;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.asepto.R;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.KaryawanModel;
import com.example.asepto.data.model.ProgressModel;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.data.model.ResponseModel;
import com.example.asepto.databinding.FragmentKaryawanProgressBinding;
import com.example.asepto.databinding.FragmentKaryawanProjectBinding;
import com.example.asepto.ui.main.karyawan.adapter.progress.ProgressAdapter;
import com.example.asepto.ui.main.karyawan.adapter.project.ProjectAdapter;
import com.example.asepto.util.Constans;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanProgressFragment extends Fragment {

    private FragmentKaryawanProgressBinding binding;
    private List<ProgressModel> progressModelList;
    private LinearLayoutManager linearLayoutManager;
    private KaryawanService karyawanService;
    private SharedPreferences sharedPreferences;
    private String userId, namaProject, projectId, jabatan;
    private ProgressAdapter progressAdapter;
    private AlertDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentKaryawanProgressBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        karyawanService = ApiConfig.getClient().create(KaryawanService.class);
        userId = sharedPreferences.getString(Constans.USER_ID, null);
        namaProject = getArguments().getString("nama_project");
        projectId = getArguments().getString("project_id");


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvNamaProject.setText(namaProject);
        getProgress();
        getMyProfile();
        listener();
    }

    private void listener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProgress();
            }
        });
    }

    private void addProgress() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_add_progress);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        EditText etProgress, etDetail;
        etDetail = dialog.findViewById(R.id.etDetail);
        etProgress = dialog.findViewById(R.id.etprogress);
        Button btnSimpan = dialog.findViewById(R.id.btnSimpan);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose);

        dialog.show();

        etProgress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etProgress.getText().toString().isEmpty()) {
                    etProgress.setError("Tidak boleh kosong");
                }else {
                    if (Integer.parseInt(etProgress.getText().toString()) > 100) {
                        etProgress.setError("Tidak boleh lebih dari 100");
                    }else if (Integer.parseInt(etProgress.getText().toString()) < 1) {
                        etProgress.setError("Tidak boleh lebih kecil dari 1");
                    }else if (Integer.parseInt(etProgress.getText().toString()) == 0) {
                        etProgress.setError("Tidak valid");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etProgress.getText().toString().isEmpty()) {
                    etProgress.setError("Tidak boleh kosong");

                }else if (etDetail.getText().toString().isEmpty()) {
                    etDetail.setError("Tidak boleh kosong");
                }else{
                    showProgressBar("Loading", "Menyimpan data...", true);
                    karyawanService.insertProgress(
                            projectId, userId, jabatan, namaProject,
                            etProgress.getText().toString()+"%",
                            etDetail.getText().toString()
                    ).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            showProgressBar("d", "d", false);
                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                showToast("success", "Berhasil menambahkan progress");
                                getProgress();
                                dialog.dismiss();
                            }else {
                                showToast("err", "Terjadi kesalahan");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            showProgressBar("d", "d", false);
                                showToast("err", "Tidak ada koneksi internet");



                        }
                    });

                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void getMyProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        karyawanService.getMyProfile(userId).enqueue(new Callback<KaryawanModel>() {
            @Override
            public void onResponse(Call<KaryawanModel> call, Response<KaryawanModel> response) {
                showProgressBar("d", "d", false);
                if (response.isSuccessful() && response.body() != null) {
                    jabatan = response.body().getJabatan();
                }else {
                    showToast("err", "Tidak dapat memuat data");
                }
            }

            @Override
            public void onFailure(Call<KaryawanModel> call, Throwable t) {
                showProgressBar("d", "d", false);
                showToast("err", "Tidak ada koneksi internet");




            }
        });
    }

    private void getProgress() {
        showProgressBar("Loading", "Memuat data...", true);
        karyawanService.getProgressById(userId, projectId).enqueue(new Callback<List<ProgressModel>>() {
            @Override
            public void onResponse(Call<List<ProgressModel>> call, Response<List<ProgressModel>> response) {
                showProgressBar("d", "d", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    progressModelList = response.body();
                    progressAdapter = new ProgressAdapter(getContext(), progressModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvProgress.setLayoutManager(linearLayoutManager);
                    binding.rvProgress.setAdapter(progressAdapter);
                    binding.rvProgress.setHasFixedSize(true);
                    binding.tvEmpty.setVisibility(View.GONE);

                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ProgressModel>> call, Throwable t) {
                showProgressBar("d", "d", false);
                binding.tvEmpty.setVisibility(View.VISIBLE);
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

}