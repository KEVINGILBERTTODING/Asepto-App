package com.example.asepto.ui.main.admin.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.asepto.R;
import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.FeedBackModel;
import com.example.asepto.data.model.KaryawanModel;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.databinding.FragmentAdminProjectBinding;
import com.example.asepto.databinding.FragmentKaryawanProjectBinding;
import com.example.asepto.ui.main.admin.adapter.project.ProjectAdapter;
import com.example.asepto.util.Constans;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProjectFragment extends Fragment {

    private FragmentAdminProjectBinding binding;
    private List<ProjectModel> projectModelList;
    private LinearLayoutManager linearLayoutManager;
    private AdminService adminService;
    private SharedPreferences sharedPreferences;
    private String userId, status;
    private ProjectAdapter projectAdapter;
    private AlertDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminProjectBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        adminService = ApiConfig.getClient().create(AdminService.class);
        userId = sharedPreferences.getString(Constans.USER_ID, null);
        status = getArguments().getString("status_id");

        if (status.equals("1")) {
            binding.btnAdd.setVisibility(View.GONE);
        }




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProject(status);

        listener();
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
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameAdmin, new InsertProjectFragment())
                        .addToBackStack(null).commit();
            }
        });

    }

    private void getProject(String status) {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllProject(status).enqueue(new Callback<List<ProjectModel>>() {
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

    private void filter(String text) {
        ArrayList<ProjectModel> filteredList = new ArrayList<>();
        for (ProjectModel item : projectModelList) {
            if (item.getNamaProject().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
            projectAdapter.filter(filteredList);
            if (filteredList.isEmpty()) {

            }else {
                projectAdapter.filter(filteredList);
            }
        }
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