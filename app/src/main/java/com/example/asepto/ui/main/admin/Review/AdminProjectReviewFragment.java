package com.example.asepto.ui.main.admin.Review;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.asepto.R;
import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.model.FeedBackModel;
import com.example.asepto.data.model.KaryawanModel;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.databinding.FragmentAdminProjectReviewBinding;
import com.example.asepto.ui.main.admin.adapter.review.ReviewProjectAdapter;
import com.example.asepto.ui.main.admin.adapter.spinner.SpinnerListKaryawan;
import com.example.asepto.ui.main.karyawan.adapter.project.ProjectAdapter;
import com.example.asepto.util.Constans;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProjectReviewFragment extends Fragment {

    private FragmentAdminProjectReviewBinding binding;
    private List<ProjectModel> projectModelList;
    private LinearLayoutManager linearLayoutManager;
    private AdminService adminService;
    private List<KaryawanModel> karyawanModelList;
    private ReviewProjectAdapter reviewProjectAdapter;
    private AlertDialog progressDialog;
    private SpinnerListKaryawan spinnerListKaryawan;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminProjectReviewBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProject();

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

    @Override
    public void onResume() {
        super.onResume();
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void getProject() {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllProject("all").enqueue(new Callback<List<ProjectModel>>() {
            @Override
            public void onResponse(Call<List<ProjectModel>> call, Response<List<ProjectModel>> response) {
                showProgressBar("d", "d", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    projectModelList = response.body();
                    reviewProjectAdapter= new ReviewProjectAdapter(getContext(), projectModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvProject.setLayoutManager(linearLayoutManager);
                    binding.rvProject.setAdapter(reviewProjectAdapter);
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






    private void filter(String text){
        ArrayList<ProjectModel> filteredList = new ArrayList<>();
        for (ProjectModel item : projectModelList) {
            if (item.getNamaProject().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
            reviewProjectAdapter.filter(filteredList);
            if (filteredList.isEmpty()) {

            }else {
                reviewProjectAdapter.filter(filteredList);
            }
        }
    }
}