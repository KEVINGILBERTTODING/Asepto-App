package com.example.asepto.ui.main.admin.catatan;


import android.app.AlertDialog;
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
import com.example.asepto.data.model.CatatanModel;
import com.example.asepto.data.model.CatatanModel;
import com.example.asepto.databinding.FragmentAdminCatatanBinding;
import com.example.asepto.databinding.FragmentAdminReviewBinding;
import com.example.asepto.ui.main.admin.Review.AdminProjectReviewFragment;
import com.example.asepto.ui.main.admin.adapter.catatan.CatatanAdapter;
import com.example.asepto.ui.main.admin.adapter.review.ReviewAdapter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCatatanFragment extends Fragment {

    private FragmentAdminCatatanBinding binding;
    private List<CatatanModel> catatanModelList;
    private LinearLayoutManager linearLayoutManager;
    private KaryawanService karyawanService;
    private CatatanAdapter catatanAdapter;
    private AlertDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminCatatanBinding.inflate(inflater, container, false);
        karyawanService = ApiConfig.getClient().create(KaryawanService.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCatatan();
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
                        .replace(R.id.frameAdmin, new AdminProjectReviewFragment())
                        .addToBackStack(null).commit();
            }
        });



    }

    private void getCatatan() {
        showProgressBar("Loading", "Memuat data...", true);
        karyawanService.getCatatan().enqueue(new Callback<List<CatatanModel>>() {
            @Override
            public void onResponse(Call<List<CatatanModel>> call, Response<List<CatatanModel>> response) {
                showProgressBar("d", "d", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    catatanModelList = response.body();
                    catatanAdapter = new CatatanAdapter(getContext(), catatanModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvCatatan.setLayoutManager(linearLayoutManager);
                    binding.rvCatatan.setAdapter(catatanAdapter);
                    binding.rvCatatan.setHasFixedSize(true);
                    binding.tvEmpty.setVisibility(View.GONE);

                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<CatatanModel>> call, Throwable t) {
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