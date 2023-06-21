package com.example.asepto.ui.main.admin.Review;

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

import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.FeedBackModel;
import com.example.asepto.data.model.KaryawanModel;
import com.example.asepto.databinding.FragmentAdminReviewBinding;
import com.example.asepto.ui.main.admin.adapter.review.ReviewAdapter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminReviewFragment extends Fragment {

    private FragmentAdminReviewBinding binding;
    private List<FeedBackModel> feddBackModelList;
    private LinearLayoutManager linearLayoutManager;
    private AdminService adminService;
    private ReviewAdapter reviewAdapter;
    private AlertDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminReviewBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFeedBack();
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


    }

    private void getFeedBack() {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllReview().enqueue(new Callback<List<FeedBackModel>>() {
            @Override
            public void onResponse(Call<List<FeedBackModel>> call, Response<List<FeedBackModel>> response) {
                showProgressBar("d", "d", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    feddBackModelList = response.body();
                    reviewAdapter = new ReviewAdapter(getContext(), feddBackModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvReview.setLayoutManager(linearLayoutManager);
                    binding.rvReview.setAdapter(reviewAdapter);
                    binding.rvReview.setHasFixedSize(true);
                    binding.tvEmpty.setVisibility(View.GONE);

                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<FeedBackModel>> call, Throwable t) {
                showProgressBar("d", "d", false);
                binding.tvEmpty.setVisibility(View.VISIBLE);
                showToast("err", "Tidak ada koneksi internet");

            }
        });

    }

    private void filter(String text){
        ArrayList<FeedBackModel> filteredList = new ArrayList<>();
        for (FeedBackModel item : feddBackModelList) {
            if (item.getNamaProject().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
            reviewAdapter.filter(filteredList);
            if (filteredList.isEmpty()) {

            }else {
                reviewAdapter.filter(filteredList);
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