package com.example.asepto.ui.main.admin.task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.ProgressModel;
import com.example.asepto.data.model.ResponseModel;
import com.example.asepto.data.model.TaskModel;
import com.example.asepto.databinding.FragmentAdminTaskBinding;
import com.example.asepto.ui.main.admin.adapter.progress.TaskAdapter;
import com.example.asepto.util.Constans;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminTaskFragment extends Fragment {

    private FragmentAdminTaskBinding binding;
    private List<TaskModel> taskModelList;
    private LinearLayoutManager linearLayoutManager;
    private KaryawanService karyawanService;
    private AdminService adminService;
    private SharedPreferences sharedPreferences;
    private String userId, namaProject, projectId, karyawanId;
    private Integer status;
    private TaskAdapter progressAdapter;
    private AlertDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminTaskBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        karyawanService = ApiConfig.getClient().create(KaryawanService.class);
        adminService = ApiConfig.getClient().create(AdminService.class);
        userId = sharedPreferences.getString(Constans.USER_ID, null);
        namaProject = getArguments().getString("nama_project");
        projectId = getArguments().getString("project_id");
        karyawanId = getArguments().getString("karyawan_id");
        status = getArguments().getInt("status");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvNamaProject.setText(namaProject);
        getTask();
        listener();
        checkDeadLineProject();

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
                insertTask();

            }
        });


    }
    private void insertTask() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_add_task);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnSimpan = dialog.findViewById(R.id.btnSimpan);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose);
        EditText etTask = dialog.findViewById(R.id.etTask);
        dialog.show();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTask.getText().toString().isEmpty()) {
                    etTask.setError("Tidak boleh kosong");
                    etTask.requestFocus();
                }else {
                    showProgressBar("Loading", "Menambahkan task baru...", true);
                    adminService.insertTask(
                            projectId, etTask.getText().toString(),
                            karyawanId
                    ).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            showProgressBar("s", "s", false);
                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                showToast("success", "Berhasil menambahkan task baru");
                                getTask();
                                dialog.dismiss();
                            }else {
                                showToast("err", "Gagal menambahkan task baru");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            showProgressBar("s", "s", false);
                            showToast("err", "Tidak ada koneksi internet");



                        }
                    });
                }
            }
        });

    }

    private void getTask() {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getTaskByProjectId(projectId).enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                showProgressBar("d", "d", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    taskModelList = response.body();
                    progressAdapter = new TaskAdapter(getContext(), taskModelList);
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
            public void onFailure(Call<List<TaskModel>> call, Throwable t) {
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

    private void checkDeadLineProject(){
        showProgressBar("Loading", "Memuat data...", true);
        karyawanService.getDeadLineProject(projectId).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("ds", "ds", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.layout_alert_deadline);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    Button btnOke = dialog.findViewById(R.id.btnOke);

                    dialog.show();

                    btnOke.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }else {

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showToast("err", "Tidak ada koneksi internet");

            }
        });
    }

}