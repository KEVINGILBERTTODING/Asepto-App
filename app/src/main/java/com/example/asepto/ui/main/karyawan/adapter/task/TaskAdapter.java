package com.example.asepto.ui.main.karyawan.adapter.task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
                if (holder.checkBox.isChecked()) {
                    showToast("success", "Button is checked");
                }else {
                    showToast("successs", "Button  unchecked");
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
            adminService = ApiConfig.getClient().create(AdminService.class);




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
