package com.example.asepto.ui.main.admin.adapter.progress;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asepto.R;
import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.ResponseModel;
import com.example.asepto.data.model.TaskModel;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    Context context;
    List<TaskModel> taskModel;
    private AlertDialog progressDialog;
    private AdminService adminService;

    public TaskAdapter(Context context, List<TaskModel> taskModel) {
        this.context = context;
        this.taskModel = taskModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_task_admin, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTaskName.setText(taskModel.get(holder.getAdapterPosition()).getTaskName());
        holder.tvDatePost.setText(taskModel.get(holder.getAdapterPosition()).getDatePost());
        holder.tvNamaKaryawan.setText(taskModel.get(holder.getAdapterPosition()).getNamaKaryawan());
        holder.tvTaskName.setText(taskModel.get(holder.getAdapterPosition()).getTaskName());

        if (taskModel.get(holder.getAdapterPosition()).getStatus() == 1) {
            holder.tvStatus.setText("Selesai");
            holder.tvStatus.setTextColor(context.getColor(R.color.blue));
        }else {
            holder.tvStatus.setText("Belum selesai");
            holder.tvStatus.setTextColor(context.getColor(R.color.red));
        }



        if (taskModel.get(holder.getAdapterPosition()).getDatePost() == null) {
            holder.tvDatePost.setVisibility(View.GONE);
        }else {
            holder.tvDatePost.setVisibility(View.VISIBLE);
        }

        if (taskModel.get(holder.getAdapterPosition()).getKeterangan() == null){
            holder.tvKeterangan.setText("Belum ada keterangan");
        }else {
            holder.tvKeterangan.setText(taskModel.get(holder.getAdapterPosition()).getKeterangan());
        }




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
        return taskModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAction, ivAction2;
        ImageButton btnEdit, btnDelete;
        TextView tvTaskName, tvDatePost, tvStatus, tvNamaKaryawan, tvKeterangan;

        LinearLayout lrDetail;
        private RelativeLayout rlAction;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAction = itemView.findViewById(R.id.ivIconAction);
            ivAction2 = itemView.findViewById(R.id.ivIconAction2);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            tvDatePost = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNamaKaryawan = itemView.findViewById(R.id.tvNamaKaryawan);
            tvKeterangan = itemView.findViewById(R.id.tvKeteranganSelesai);
            lrDetail = itemView.findViewById(R.id.layoutDetail);
            rlAction = itemView.findViewById(R.id.rlAction);
            adminService = ApiConfig.getClient().create(AdminService.class);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar("Loading", "Menghapus task...", true);
                    adminService.deleteTask(taskModel.get(getAdapterPosition()).getTaskId()).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            showProgressBar("ss", "s", false);
                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                showToast("success", "Berhasil menghapus task");
                                taskModel.remove(getAdapterPosition());
                                notifyDataSetChanged();
                            }else {
                                showToast("err", "Terjadi kesalahan");
                            }
                         }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            showProgressBar("ss", "s", false);
                            showToast("err", "Tidak ada koneksi internet");
                        }
                    });
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialogUpdateTask = new Dialog(itemView.getContext());
                    dialogUpdateTask.setContentView(R.layout.layout_update_task);
                    dialogUpdateTask.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    final EditText etTask = dialogUpdateTask.findViewById(R.id.etTask);
                    final Button btnSimpan = dialogUpdateTask.findViewById(R.id.btnSimpan);
                    final ImageButton btnClose = dialogUpdateTask.findViewById(R.id.btnClose);
                    etTask.setText(taskModel.get(getAdapterPosition()).getTaskName());
                    dialogUpdateTask.show();

                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogUpdateTask.dismiss();
                        }
                    });

                    btnSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etTask.getText().toString().isEmpty()) {
                                etTask.setError("Tidak boleh kosong");
                                etTask.requestFocus();
                            }else {
                                showProgressBar("Loading", "Memperbaharui data...", true);
                                adminService.updateTask(
                                        taskModel.get(getAdapterPosition()).getTaskId(),
                                        etTask.getText().toString()
                                ).enqueue(new Callback<ResponseModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        showProgressBar("s", "s", false);
                                        if (response.isSuccessful() && response.body().getCode() == 200) {
                                            taskModel.get(getAdapterPosition()).setTaskName(etTask.getText().toString());
                                            notifyDataSetChanged();
                                            showToast("success", "Berhasil mengubah data");
                                            dialogUpdateTask.dismiss();
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
