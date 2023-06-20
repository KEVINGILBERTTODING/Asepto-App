package com.example.asepto.ui.main.karyawan.adapter.progress;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asepto.R;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.ProgressModel;
import com.example.asepto.data.model.ResponseModel;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder> {
    Context context;
    List<ProgressModel> progressModelList;
    private AlertDialog progressDialog;
    private KaryawanService karyawanService;

    public ProgressAdapter(Context context, List<ProgressModel> progressModelList) {
        this.context = context;
        this.progressModelList = progressModelList;
    }

    @NonNull
    @Override
    public ProgressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_progress, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProgressAdapter.ViewHolder holder, int position) {

        holder.tvProgress.setText(progressModelList.get(holder.getAdapterPosition()).getProgress());
        holder.tvDate.setText(progressModelList.get(holder.getAdapterPosition()).getTanggal());
        holder.etDetail.setText(progressModelList.get(holder.getAdapterPosition()).getKeterangan());

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
        return progressModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAction, ivAction2;
        TextView tvProgress, tvDate;
        EditText etDetail;
        LinearLayout lrDetail;
        Button btnUpdate, btnSimpan;
        private RelativeLayout rlAction;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAction = itemView.findViewById(R.id.ivIconAction);
            ivAction2 = itemView.findViewById(R.id.ivIconAction2);
            etDetail = itemView.findViewById(R.id.etDetail);
            tvProgress = itemView.findViewById(R.id.tvProgress);
            tvDate = itemView.findViewById(R.id.tvDate);
            lrDetail = itemView.findViewById(R.id.layoutDetail);
            btnUpdate = itemView.findViewById(R.id.btnUbah);
            btnSimpan = itemView.findViewById(R.id.btnSimpan);
            rlAction = itemView.findViewById(R.id.rlAction);
            karyawanService = ApiConfig.getClient().create(KaryawanService.class);

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etDetail.setEnabled(true);
                    etDetail.requestFocus();
                    btnSimpan.setVisibility(View.VISIBLE);
                    btnUpdate.setVisibility(View.GONE);
                }
            });

            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if (etDetail.getText().toString().isEmpty()) {
                       etDetail.setError("Tidak boleh kosong");
                   }else {
                       showProgressBar("Loading", "Mengubah data...", true);
                       karyawanService.updateProgress(
                               progressModelList.get(getAdapterPosition()).getProgressId(),
                               etDetail.getText().toString()

                       ).enqueue(new Callback<ResponseModel>() {
                           @Override
                           public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                               showProgressBar("s", "s", false);
                               if (response.isSuccessful() && response.body().getCode() == 200) {
                                   showToast("success", "Berhasil mengubah progress");
                                   progressModelList.get(getAdapterPosition()).setKeterangan(etDetail.getText().toString());
                                   notifyDataSetChanged();
                                   btnSimpan.setVisibility(View.GONE);
                                   btnUpdate.setVisibility(View.VISIBLE);
                                   etDetail.setEnabled(false);

                               }else {
                                   showToast("err", "Terjadi kesalahan");
                               }
                           }

                           @Override
                           public void onFailure(Call<ResponseModel> call, Throwable t) {
                               showProgressBar("s", "s", false);
                               showToast("err", "Tidak ada koneksi internet");



                           }
                       });
                   }
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
