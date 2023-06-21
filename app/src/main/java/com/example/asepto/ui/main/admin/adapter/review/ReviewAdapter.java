package com.example.asepto.ui.main.admin.adapter.review;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.asepto.data.model.FeedBackModel;
import com.example.asepto.data.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    Context context;
    List<FeedBackModel> feedBackModelList;
    private AlertDialog progressDialog;
    private AdminService adminService;

    public ReviewAdapter(Context context, List<FeedBackModel> feedBackModelList) {
        this.context = context;
        this.feedBackModelList = feedBackModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_review_admin, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvProjectName.setText(feedBackModelList.get(holder.getAdapterPosition()).getNamaProject());
        holder.tvNamaPegawai.setText(feedBackModelList.get(holder.getAdapterPosition()).getNama());
        holder.etReview.setText(feedBackModelList.get(holder.getAdapterPosition()).getFeedback());



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
        return feedBackModelList.size();
    }

    public void filter (ArrayList<FeedBackModel> filteredList) {
        feedBackModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAction, ivAction2;
        TextView tvProjectName, tvNamaPegawai;
        EditText etReview;
        Button btnUbah, btnDelete, btnSimpan;
        LinearLayout lrDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAction = itemView.findViewById(R.id.ivIconAction);
            ivAction2 = itemView.findViewById(R.id.ivIconAction2);
            etReview = itemView.findViewById(R.id.etReview);
            tvNamaPegawai = itemView.findViewById(R.id.tvNamaPegawai);
            tvProjectName = itemView.findViewById(R.id.tvNamaProject);
            lrDetail = itemView.findViewById(R.id.layoutDetail);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnSimpan = itemView.findViewById(R.id.btnSimpan);
            btnUbah = itemView.findViewById(R.id.btnUbah);
            adminService = ApiConfig.getClient().create(AdminService.class);

            btnUbah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnSimpan.setVisibility(View.VISIBLE);
                    btnUbah.setVisibility(View.GONE);
                    etReview.setEnabled(true);
                    etReview.requestFocus();
                }
            });

            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar("Loading", "Mengubah data...", true);
                    adminService.updateFeedBack(
                            feedBackModelList.get(getAdapterPosition()).getFeedbackId(),
                            etReview.getText().toString()
                    ).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            showProgressBar("d", "d", false);
                            if (response.isSuccessful() && response.body().getCode() == 200){
                                showToast("success", "Berhasil mengubah review");
                                feedBackModelList.get(getAdapterPosition()).setFeedback(etReview.getText().toString());
                                notifyDataSetChanged();
                                btnSimpan.setVisibility(View.GONE);
                                btnUbah.setVisibility(View.VISIBLE);
                                etReview.setEnabled(false);
                            }else {
                                showToast("err", "Terjadi Kesalahan");
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
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.layout_alert_delete);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    Button btnOke, btnBatal;
                    btnOke = dialog.findViewById(R.id.btnOke);
                    btnBatal = dialog.findViewById(R.id.btnBatal);
                    dialog.show();
                    btnBatal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btnOke.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showProgressBar("Loading", "Menghapus data...", true);
                            adminService.deleteReview(feedBackModelList.get(getAdapterPosition()).getFeedbackId())
                                    .enqueue(new Callback<ResponseModel>() {
                                        @Override
                                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                            showProgressBar("s", "s", false);
                                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                                showToast("success", "Berhasil menghapus data");
                                                feedBackModelList.remove(getAdapterPosition());
                                                notifyDataSetChanged();
                                                dialog.dismiss();
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
