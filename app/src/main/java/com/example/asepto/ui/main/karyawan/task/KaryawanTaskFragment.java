package com.example.asepto.ui.main.karyawan.task;

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
import com.example.asepto.databinding.FragmentKaryawanTaskBinding;
import com.example.asepto.ui.main.karyawan.adapter.task.TaskAdapter;
import com.example.asepto.util.Constans;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanTaskFragment extends Fragment implements TaskAdapter.OnButtonClickListener {

    private FragmentKaryawanTaskBinding binding;
    private List<TaskModel> taskModelList;
    private LinearLayoutManager linearLayoutManager;
    private KaryawanService karyawanService;
    private AdminService adminService;
    private SharedPreferences sharedPreferences;
    private String userId, namaProject, projectId, karyawanId;
    private Integer status;
    private TaskAdapter taskAdapter;
    private AlertDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentKaryawanTaskBinding.inflate(inflater, container, false);
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
        getTotalProgress();
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




    }

    private void getTask() {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getTaskByProjectId(projectId).enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                showProgressBar("d", "d", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    taskModelList = response.body();
                    taskAdapter = new TaskAdapter(getContext(), taskModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvTask.setLayoutManager(linearLayoutManager);
                    binding.rvTask.setAdapter(taskAdapter);
                    binding.rvTask.setHasFixedSize(true);
                    taskAdapter.setOnButtonClickListener(KaryawanTaskFragment.this);
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

    private void getTotalProgress() {
        showProgressBar("Loading", "Memuat data...", false);
        karyawanService.getTotalProgress(projectId).enqueue(new Callback<ProgressModel>() {
            @Override
            public void onResponse(Call<ProgressModel> call, Response<ProgressModel> response) {
                showProgressBar("s", "s", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    binding.tvProgress.setText(String.valueOf(response.body().getProgress() + "%"));
                    binding.progressBar.setProgress(Integer.parseInt(String.valueOf(response.body().getProgress())));
                }else {
                    binding.progressBar.setProgress(0);
                    binding.tvProgress.setText("0%");
                }
            }

            @Override
            public void onFailure(Call<ProgressModel> call, Throwable t) {
                showProgressBar("s", "s", false);
                showToast("err", "Tidak ada koneksi internet");

                binding.progressBar.setProgress(0);
                binding.tvProgress.setText("0%");

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

    @Override
    public void onButtonClicked() {
        getTotalProgress();

    }
}