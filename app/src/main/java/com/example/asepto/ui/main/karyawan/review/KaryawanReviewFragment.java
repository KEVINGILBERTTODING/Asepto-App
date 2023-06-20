package com.example.asepto.ui.main.karyawan.review;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.asepto.R;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.FeddBackModel;
import com.example.asepto.data.model.KaryawanModel;
import com.example.asepto.data.model.ProgressModel;
import com.example.asepto.data.model.ResponseModel;
import com.example.asepto.databinding.FragmentKaryawanProgressBinding;
import com.example.asepto.databinding.FragmentKaryawanReviewBinding;
import com.example.asepto.ui.main.karyawan.adapter.progress.ProgressAdapter;
import com.example.asepto.ui.main.karyawan.adapter.review.ReviewAdapter;
import com.example.asepto.util.Constans;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanReviewFragment extends Fragment {

    private FragmentKaryawanReviewBinding binding;
    private List<FeddBackModel> feddBackModelList;
    private LinearLayoutManager linearLayoutManager;
    private KaryawanService karyawanService;
    private SharedPreferences sharedPreferences;
    private String userId;
    private ReviewAdapter reviewAdapter;
    private AlertDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentKaryawanReviewBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        karyawanService = ApiConfig.getClient().create(KaryawanService.class);
        userId = sharedPreferences.getString(Constans.USER_ID, null);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFeedBack();
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


    }
    private void getMyProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        karyawanService.getMyProfile(userId).enqueue(new Callback<KaryawanModel>() {
            @Override
            public void onResponse(Call<KaryawanModel> call, Response<KaryawanModel> response) {
                showProgressBar("d", "d", false);
                if (response.isSuccessful() && response.body() != null) {

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
    private void getFeedBack() {
        showProgressBar("Loading", "Memuat data...", true);
        karyawanService.getMyFeedBack(userId).enqueue(new Callback<List<FeddBackModel>>() {
            @Override
            public void onResponse(Call<List<FeddBackModel>> call, Response<List<FeddBackModel>> response) {
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
            public void onFailure(Call<List<FeddBackModel>> call, Throwable t) {
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