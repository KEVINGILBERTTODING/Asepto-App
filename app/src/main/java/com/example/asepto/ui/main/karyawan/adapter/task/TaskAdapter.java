package com.example.asepto.ui.main.karyawan.adapter.task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private KaryawanService karyawanService;

    private OnButtonClickListener onButtonClickListener;


    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }


    public interface OnButtonClickListener {
        void onButtonClicked();
    }

    public TaskAdapter(Context context, List<TaskModel> taskModel) {
        this.context = context;
        this.taskModel = taskModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_task_karyawan, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTaskName.setText(taskModel.get(holder.getAdapterPosition()).getTaskName());
        holder.tvTaskName.setText(taskModel.get(holder.getAdapterPosition()).getTaskName());

        if (taskModel.get(holder.getAdapterPosition()).getStatus() == 1) {
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }

        // cek apakah checkbox di klik atau tidak
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {

                        Dialog dialogInsertKeterangan = new Dialog(context);
                        dialogInsertKeterangan.setContentView(R.layout.layout_add_keterangan_task);
                        dialogInsertKeterangan.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        EditText etKeterangan = dialogInsertKeterangan.findViewById(R.id.etKeterangan);
                        Button btnSimpan = dialogInsertKeterangan.findViewById(R.id.btnSimpan);
                        ImageButton btnClose = dialogInsertKeterangan.findViewById(R.id.btnClose);
                        dialogInsertKeterangan.show();

                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogInsertKeterangan.dismiss();
                                holder.checkBox.setChecked(false);
                            }
                        });

                        btnSimpan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (etKeterangan.getText().toString().isEmpty()) {
                                    etKeterangan.setError("Tidak boleh kosong");
                                    etKeterangan.requestFocus();
                                } else {
                                    showProgressBar("Loading", "Checked task...", true);
                                    karyawanService.taskCompleted(
                                            taskModel.get(holder.getAdapterPosition()).getTaskId(),
                                            etKeterangan.getText().toString()
                                    ).enqueue(new Callback<ResponseModel>() {
                                        @Override
                                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                            showProgressBar("s", "s", false);
                                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                                showToast("success", "Task completed");
                                                onButtonClickListener.onButtonClicked();
                                                taskModel.get(holder.getAdapterPosition()).setStatus(1);
                                                dialogInsertKeterangan.dismiss();
                                                holder.checkBox.setChecked(true);
                                                notifyDataSetChanged();
                                            } else {
                                                showToast("err", "Terjadi kesalahan");
                                                holder.checkBox.setChecked(false);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                                            showProgressBar("s", "s", false);
                                            showToast("err", "Tidak ada koneksi internet");
                                            holder.checkBox.setChecked(false);
                                        }
                                    });
                                }
                            }
                        });


                } else if (isChecked == false) {
                    showProgressBar("Loading", "Unchecked task", true);
                    karyawanService.taskUnCompleted(taskModel.get(holder.getAdapterPosition()).getTaskId())
                            .enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    showProgressBar("s", "s", false);
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        holder.checkBox.setChecked(false);
                                        taskModel.get(holder.getAdapterPosition()).setStatus(0);
                                        notifyDataSetChanged();
                                        Toasty.normal(context, "Task unchecked", Toasty.LENGTH_SHORT).show();
                                        onButtonClickListener.onButtonClicked();

                                    } else {
                                        showToast("err", "Terjadi Kesalahan");
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

    @Override
    public int getItemCount() {
        return taskModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvTaskName;
        private CheckBox checkBox;

        private RelativeLayout rlAction;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            rlAction = itemView.findViewById(R.id.rlAction);
            checkBox = itemView.findViewById(R.id.checkBox);
            karyawanService = ApiConfig.getClient().create(KaryawanService.class);




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
