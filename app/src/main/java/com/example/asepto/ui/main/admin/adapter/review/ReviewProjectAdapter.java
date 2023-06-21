package com.example.asepto.ui.main.admin.adapter.review;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asepto.R;
import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.model.KaryawanModel;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.data.model.ResponseModel;
import com.example.asepto.ui.main.admin.adapter.spinner.SpinnerListKaryawan;
import com.example.asepto.ui.main.admin.karyawan.UpdateKaryawanFragment;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;


public class ReviewProjectAdapter extends RecyclerView.Adapter<ReviewProjectAdapter.ViewHolder> {
    Context context;
    List<ProjectModel> projectModelList;
    private AlertDialog progressDialog;
    private AdminService adminService;
    private Spinner spKaryawan;
    private List<KaryawanModel> karyawanModelList;
    private SpinnerListKaryawan spinnerListKaryawan;
    private String karyawanId;

    public ReviewProjectAdapter(Context context, List<ProjectModel> projectModelList) {
        this.context = context;
        this.projectModelList = projectModelList;
    }

    @NonNull
    @Override
    public ReviewProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_project_reviiew, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewProjectAdapter.ViewHolder holder, int position) {

        holder.tvNamaProject.setText(projectModelList.get(holder.getAdapterPosition()).getNamaProject());
        holder.tvEmailProject.setText(projectModelList.get(holder.getAdapterPosition()).getEmailPerusahaan());
    }

    @Override
    public int getItemCount() {
        return projectModelList.size();
    }

    public void filter(ArrayList<ProjectModel> filteredList) {
        projectModelList = filteredList;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaProject, tvEmailProject;
        Button btnPilih;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaProject = itemView.findViewById(R.id.tvProjectName);
            tvEmailProject = itemView.findViewById(R.id.tvEmail);
            btnPilih = itemView.findViewById(R.id.btnPilih);
            adminService = ApiConfig.getClient().create(AdminService.class);

            btnPilih.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.layout_add_review);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    Button btnSimpan = dialog.findViewById(R.id.btnSimpan);
                    ImageButton btnClose = dialog.findViewById(R.id.btnClose);
                    EditText etReview = dialog.findViewById(R.id.etReview);
                    spKaryawan = dialog.findViewById(R.id.spKaryawan);


                    dialog.show();
                    spKaryawan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            karyawanId = String.valueOf(spinnerListKaryawan.getKaryawanId(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    getAllKaryawan();

                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btnSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showProgressBar("Loading", "Menyimpan data...", true);
                            adminService.insertReview(
                                    projectModelList.get(getAdapterPosition()).getProjectId(),
                                    karyawanId,
                                    projectModelList.get(getAdapterPosition()).getNamaProject(),
                                    etReview.getText().toString()
                            ).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    showProgressBar("d", "d", false);
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        dialog.dismiss();
                                        showToast("success", "Berhasil menambahkan review baru");
                                        ((FragmentActivity) context).onBackPressed();
                                    }else {
                                        showToast("err", "Terjadi kesalahan");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    showProgressBar("d", "d", false);
                                    showToast("err", "Tidak ada koneksi internet");



                                }
                            });


                        }
                    });



                }
            });

        }

    }
    private void getAllKaryawan() {
        showProgressBar("Loading", "Memuat data karyawan", true);
        adminService.getAllKaryawan().enqueue(new Callback<List<KaryawanModel>>() {
            @Override
            public void onResponse(Call<List<KaryawanModel>> call, Response<List<KaryawanModel>> response) {
                showProgressBar("s", "s", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    karyawanModelList = response.body();
                    spinnerListKaryawan = new SpinnerListKaryawan(context, karyawanModelList);
                    spKaryawan.setAdapter(spinnerListKaryawan);

                }else {
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

    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            Toasty.success(context, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(context, text, Toasty.LENGTH_SHORT).show();
        }
    }


}
