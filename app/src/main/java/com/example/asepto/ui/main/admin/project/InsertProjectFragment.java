package com.example.asepto.ui.main.admin.project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.asepto.R;
import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.model.KaryawanModel;
import com.example.asepto.data.model.ResponseModel;
import com.example.asepto.databinding.FragmentInsertProjectBinding;
import com.example.asepto.ui.main.admin.adapter.spinner.SpinnerListKaryawan;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class InsertProjectFragment extends Fragment {
    private AlertDialog progressDialog;
    private FragmentInsertProjectBinding binding;
    private String karyawanId, kategori, namaKaryawan;
    private SpinnerListKaryawan spinnerListKaryawan;
    private List<KaryawanModel> karyawanModelList;
    private AdminService adminService;
    private String [] kategoriProject = {
            "Website", "Landing Page", "Logo", "Branding", "Mobile Design",
            "Dashboard", "3D Design", "Animation Design"
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInsertProjectBinding.inflate(inflater, container, false);
        init();


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener();
        getAllKaraywan();
    }

    private void init() {
        ArrayAdapter adapterKategori = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, kategoriProject);
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spKategori.setAdapter(adapterKategori);
        adminService = ApiConfig.getClient().create(AdminService.class);
    }

    private void listener() {
        binding.spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kategori = kategoriProject[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spKaryawan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                karyawanId = String.valueOf(spinnerListKaryawan.getKaryawanId(position));
                namaKaryawan = String.valueOf(spinnerListKaryawan.getNamaKaryawan(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.etTglMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateTimePicker(binding.etTglMulai);
            }
        });

        binding.etTglBerakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateTimePicker(binding.etTglBerakhir);
            }
        });

        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etNamaProject.getText().toString().isEmpty()) {
                    binding.etNamaProject.setError("Tidak boleh kosong");
                    binding.etNamaProject.requestFocus();
                }else  if (binding.etDeskripsi.getText().toString().isEmpty()) {
                    binding.etDeskripsi.setError("Tidak boleh kosong");
                    binding.etDeskripsi.requestFocus();
                }else  if (binding.etTglMulai.getText().toString().isEmpty()) {
                    binding.etTglMulai.setError("Tidak boleh kosong");
                    binding.etTglMulai.requestFocus();
                }else  if (binding.etTglBerakhir.getText().toString().isEmpty()) {
                    binding.etTglBerakhir.setError("Tidak boleh kosong");
                    binding.etTglBerakhir.requestFocus();
                }else  if (binding.etNamaPerusahaan.getText().toString().isEmpty()) {
                    binding.etNamaPerusahaan.setError("Tidak boleh kosong");
                    binding.etNamaPerusahaan.requestFocus();
                }else  if (binding.etEmailPerusahaan.getText().toString().isEmpty()) {
                    binding.etEmailPerusahaan.setError("Tidak boleh kosong");
                    binding.etEmailPerusahaan.requestFocus();
                }else  if (binding.etNominal.getText().toString().isEmpty()) {
                    binding.etNominal.setError("Tidak boleh kosong");
                    binding.etNominal.requestFocus();
                }else {
                    insertData();
                }
            }
        });
    }

    private void getAllKaraywan() {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllKaryawan().enqueue(new Callback<List<KaryawanModel>>() {
            @Override
            public void onResponse(Call<List<KaryawanModel>> call, Response<List<KaryawanModel>> response) {
                showProgressBar("s", "s", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    karyawanModelList = response.body();
                    spinnerListKaryawan = new SpinnerListKaryawan(getContext(), karyawanModelList);
                    binding.spKaryawan.setAdapter(spinnerListKaryawan);
                }else {
                    binding.btnSimpan.setEnabled(false);
                    showToast("err", "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<List<KaryawanModel>> call, Throwable t) {
                showProgressBar("s", "s", false);
                showToast("err", "Tidak ada koneksi internet");



            }
        });

    }

    private void insertData() {

        showProgressBar("Loading", "Menyimpan data...", true);
        adminService.insertProject(
                binding.etNamaProject.getText().toString(),
                binding.etDeskripsi.getText().toString(),
                kategori,
                binding.etTglMulai.getText().toString(),
                binding.etTglBerakhir.getText().toString(),
                binding.etNamaPerusahaan.getText().toString(),
                binding.etEmailPerusahaan.getText().toString(),
                binding.etNominal.getText().toString(),
                karyawanId,
                namaKaryawan
        ).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("s", "S", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    showToast("success", "Berhasil menambahkan project baru");
                    getActivity().onBackPressed();
                }else {
                    showToast("err", "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("s", "S", false);
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
    private void getDateTimePicker(TextView tvDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormatted, monthFormatted;
                if (month < 10) {
                    monthFormatted = String.format("%02d", month + 1);
                }else {
                    monthFormatted = String.valueOf(month + 1);
                }

                if (dayOfMonth < 10) {
                    dateFormatted = String.format("%02D", dayOfMonth);

                }else {
                    dateFormatted = String.valueOf(dayOfMonth);
                }

                tvDate.setText(year + "-" + monthFormatted + "-" + dateFormatted);
            }
        });
        datePickerDialog.show();
    }

}