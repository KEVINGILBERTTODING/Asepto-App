package com.example.asepto.ui.main.karyawan.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.asepto.R;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.databinding.FragmentKaryawanProjectBinding;
import com.example.asepto.ui.main.karyawan.adapter.project.ProjectAdapter;
import com.example.asepto.util.Constans;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanProjectNavbarFragment extends Fragment {

    private FragmentKaryawanProjectBinding binding;
    private List<ProjectModel> projectModelList;
    private LinearLayoutManager linearLayoutManager;
    private KaryawanService karyawanService;
    private SharedPreferences sharedPreferences;
    private String userId;
    private ProjectAdapter projectAdapter;
    private AlertDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentKaryawanProjectBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        karyawanService = ApiConfig.getClient().create(KaryawanService.class);
        userId = sharedPreferences.getString(Constans.USER_ID, null);





        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProject("all");

        listener();
    }

    private void listener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getProject(String status) {
        showProgressBar("Loading", "Memuat data...", true);
        karyawanService.getMyProject(userId, status).enqueue(new Callback<List<ProjectModel>>() {
            @Override
            public void onResponse(Call<List<ProjectModel>> call, Response<List<ProjectModel>> response) {
                showProgressBar("d", "d", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    projectModelList = response.body();
                    projectAdapter = new ProjectAdapter(getContext(), projectModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvProject.setLayoutManager(linearLayoutManager);
                    binding.rvProject.setAdapter(projectAdapter);
                    binding.rvProject.setHasFixedSize(true);
                    binding.tvEmpty.setVisibility(View.GONE);

                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ProjectModel>> call, Throwable t) {
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