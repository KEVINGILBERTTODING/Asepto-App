package com.example.asepto.ui.main.admin.karyawan;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.asepto.R;
import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.model.ResponseModel;
import com.example.asepto.databinding.FragmentUpdateKaryawanBinding;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class UpdateKaryawanFragment extends Fragment {
    private FragmentUpdateKaryawanBinding binding;
    private AdminService adminService;
    private AlertDialog progressDialog;
    private String pegawaiId, jenisKelamin, jabatan;
    private String [] opsiJk = {"Laki-laki", "Perempuan"};
    private String [] opsiJabatan =  {"UI/UX Designer", "Branding Designer", "3D Ilustrator"};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpdateKaryawanBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);
        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
    }

    private void init(){
        pegawaiId = getArguments().getString("id");
        binding.etNamaKaryawan.setText(getArguments().getString("nama"));
        binding.etEmail.setText(getArguments().getString("email"));
        binding.etTelepon.setText(getArguments().getString("telepon"));
        binding.etNoRek.setText(getArguments().getString("no_rek"));
        binding.etNamaBank.setText(getArguments().getString("nama_bank"));

        ArrayAdapter adapterJenisKelamin = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiJk);
        adapterJenisKelamin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spJk.setAdapter(adapterJenisKelamin);

        ArrayAdapter adapterJabatan = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiJabatan);
        adapterJabatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spJabatan.setAdapter(adapterJabatan);
    }

    private void listener() {
        binding.spJabatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jabatan = opsiJabatan[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spJk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jenisKelamin = opsiJk[position];
                if (jenisKelamin.equals("Laki-laki")) {
                    jenisKelamin = "L";
                }else {
                    jenisKelamin  = "P";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etNamaKaryawan.getText().toString().isEmpty()) {
                    binding.etNamaKaryawan.setError("Tidak boleh kosong");
                }else if (binding.etNamaBank.getText().toString().isEmpty()) {
                    binding.etNamaBank.setError("Tidak boleh kosong");
                }else if (binding.etNoRek.getText().toString().isEmpty()) {
                    binding.etNoRek.setError("Tidak boleh kosong");
                }else if (binding.etEmail.getText().toString().isEmpty()) {
                    binding.etEmail.setError("Tidak boleh kosong");
                }else if (binding.etTelepon.getText().toString().isEmpty()) {
                    binding.etTelepon.setError("Tidak boleh kosong");
                }else {
                    update();
                }
            }
        });
        binding.btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void update() {
        showProgressBar("Loading", "Menyimpan data...", true);
        adminService.updateKarywan(
                pegawaiId,
                binding.etNamaKaryawan.getText().toString(),
                binding.etEmail.getText().toString(),
                jabatan,
                binding.etTelepon.getText().toString(),
                binding.etNoRek.getText().toString(),
                binding.etNamaBank.getText().toString(),
                jenisKelamin
        ).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("ds", "d", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    showToast("success", "Berhasil mengubah data");
                    getActivity().onBackPressed();
                }else {
                    showToast("err", "Terjadi kesalahan");
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("ds", "d", false);
                showToast("err", "Tidak ada koneksi internet");



            }
        });
    }

    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
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