package com.example.asepto.ui.main.admin.karyawan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.asepto.R;
import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.KaryawanModel;
import com.example.asepto.data.model.ProgressModel;
import com.example.asepto.data.model.ResponseModel;
import com.example.asepto.databinding.FragmentAdminKaryawanBinding;
import com.example.asepto.databinding.FragmentKaryawanProgressBinding;
import com.example.asepto.ui.main.admin.adapter.karyawan.KaryawanAdapter;
import com.example.asepto.util.Constans;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminKaryawanFragment extends Fragment {

    private FragmentAdminKaryawanBinding binding;
    private List<KaryawanModel> karyawanModelList;
    private LinearLayoutManager linearLayoutManager;
    private AdminService adminService;
    private KaryawanAdapter karyawanAdapter;
    private AlertDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminKaryawanBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllKaryawan();
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
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameAdmin, new InsertKaryawanFragment())
                        .addToBackStack(null).commit();
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

    private void getAllKaryawan() {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllKaryawan().enqueue(new Callback<List<KaryawanModel>>() {
            @Override
            public void onResponse(Call<List<KaryawanModel>> call, Response<List<KaryawanModel>> response) {
                showProgressBar("s", "S", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    karyawanModelList = response.body();
                    karyawanAdapter = new KaryawanAdapter(getContext(), karyawanModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvKaryawan.setLayoutManager(linearLayoutManager);
                    binding.rvKaryawan.setAdapter(karyawanAdapter);
                    binding.rvKaryawan.setHasFixedSize(true);
                    binding.tvEmpty.setVisibility(View.GONE);
                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<KaryawanModel>> call, Throwable t) {
                showProgressBar("s", "S", false);
                binding.tvEmpty.setVisibility(View.VISIBLE);
                showToast("err", "Tidak ada koneksi internet");


            }
        });
    }

    private void filter(String text) {
        ArrayList<KaryawanModel> filteredList = new ArrayList<>();
        for (KaryawanModel item : karyawanModelList) {
            if (item.getNama().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

            karyawanAdapter.filter(filteredList);
            if (filteredList.isEmpty()) {

            }else {
                karyawanAdapter.filter(filteredList);
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