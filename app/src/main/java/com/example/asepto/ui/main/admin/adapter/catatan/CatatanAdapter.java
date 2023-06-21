package com.example.asepto.ui.main.admin.adapter.catatan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asepto.R;
import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.CatatanModel;
import com.example.asepto.data.model.ResponseModel;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CatatanAdapter extends RecyclerView.Adapter<CatatanAdapter.ViewHolder> {
    Context context;
    List<CatatanModel> catatanModelList;
    private AlertDialog progressDialog;
    private KaryawanService karyawanService;
    private AdminService adminService;

    public CatatanAdapter(Context context, List<CatatanModel> catatanModelList) {
        this.context = context;
        this.catatanModelList = catatanModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_catatan_admin, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvTglEvent.setText(catatanModelList.get(holder.getAdapterPosition()).getTanggalEvent());
        holder.tvDate.setText(catatanModelList.get(holder.getAdapterPosition()).getTanggalEvent());
        holder.etCatatan.setText(catatanModelList.get(holder.getAdapterPosition()).getCatatan());



        holder.ivAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.lrDetail.setVisibility(View.VISIBLE);
                holder.ivAction.setVisibility(View.GONE);
                holder.ivAction2.setVisibility(View.VISIBLE);
            }
        });

        holder.ivAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.lrDetail.setVisibility(View.GONE);
                holder.ivAction.setVisibility(View.VISIBLE);
                holder.ivAction2.setVisibility(View.GONE);

            }
        });


    }

    @Override
    public int getItemCount() {
        return catatanModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAction, ivAction2;
        TextView tvDate, tvTglEvent;
        EditText etCatatan;
        LinearLayout lrDetail, lrTglEvent;
        Button btnUbah, btnSimpan, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAction = itemView.findViewById(R.id.ivIconAction);
            ivAction2 = itemView.findViewById(R.id.ivIconAction2);
            tvDate = itemView.findViewById(R.id.tvDate);
            lrDetail = itemView.findViewById(R.id.layoutDetail);
            tvTglEvent = itemView.findViewById(R.id.tvTglEvent);
            lrTglEvent = itemView.findViewById(R.id.lrTglEvent);
            etCatatan = itemView.findViewById(R.id.etCatatan);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnSimpan = itemView.findViewById(R.id.btnSimpan);
            btnUbah = itemView.findViewById(R.id.btnUbah);
            adminService = ApiConfig.getClient().create(AdminService.class);

            btnUbah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etCatatan.setEnabled(true);
                    btnUbah.setVisibility(View.GONE);
                    btnSimpan.setVisibility(View.VISIBLE);
                    lrTglEvent.setVisibility(View.VISIBLE);
                }
            });

            tvTglEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context);


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
                                dateFormatted = String.format("%02d", dayOfMonth);
                            }else {
                                dateFormatted = String.valueOf(dayOfMonth);
                            }
                            tvTglEvent.setText(year + "-" + monthFormatted + "-" + dateFormatted);
                        }


                    });
                    datePickerDialog.show();
                }
            });


            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar("Loading", "Menyimpan data...", true);
                    adminService.updateCatatan(
                            catatanModelList.get(getAdapterPosition()).getCatatanId(),
                            etCatatan.getText().toString(),
                            tvTglEvent.getText().toString()
                    ).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            showProgressBar("d", "s", false);
                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                showToast("success", "Berhasil mengubah data");
                                catatanModelList.get(getAdapterPosition()).setCatatan(etCatatan.getText().toString());
                                catatanModelList.get(getAdapterPosition()).setTanggalEvent(tvTglEvent.getText().toString());
                                notifyDataSetChanged();
                                etCatatan.setEnabled(false);
                                btnSimpan.setVisibility(View.GONE);
                                btnUbah.setVisibility(View.VISIBLE);
                                lrTglEvent.setVisibility(View.GONE);
                            }else {
                                showToast("err", "Terjadi kesalahan");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            showProgressBar("d", "s", false);
                            showToast("err", "Tidak ada koneksi internet");



                        }
                    });


                }
            });
        }
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
