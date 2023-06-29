package com.example.asepto.ui.main.karyawan.adapter.project;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asepto.R;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.ProgressModel;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.ui.main.karyawan.task.KaryawanTaskFragment;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    Context context;
    List<ProjectModel> projectModelList;
    private KaryawanService karyawanService;
    private AlertDialog progressDialog;

    public ProjectAdapter(Context context, List<ProjectModel> projectModelList) {
        this.context = context;
        this.projectModelList = projectModelList;
    }

    @NonNull
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_project, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ViewHolder holder, int position) {
        holder.tvnNamaProject.setText(projectModelList.get(holder.getAdapterPosition()).getNamaProject());
        holder.tvEmail.setText(projectModelList.get(holder.getAdapterPosition()).getEmailPerusahaan());
        holder.tvProjectScope.setText(projectModelList.get(holder.getAdapterPosition()).getKategori());
        holder.tvTgllMulai.setText(projectModelList.get(holder.getAdapterPosition()).getTglMulai());
        holder.tvTglSelesai.setText(projectModelList.get(holder.getAdapterPosition()).getTglSelesai());
        holder.tvDeskripsi.setText(projectModelList.get(holder.getAdapterPosition()).getDeskripsiProject());

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




        // menampilkan jumlah progress

        showProgressBar("Loading", "Memuat data...", false);
        karyawanService.getTotalProgress(projectModelList.get(holder.getAdapterPosition()).getProjectId())
                .enqueue(new Callback<ProgressModel>() {
            @Override
            public void onResponse(Call<ProgressModel> call, Response<ProgressModel> response) {
                showProgressBar("s", "s", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    holder.progressBar.setProgress(response.body().getProgress());
                    holder.tvProgress.setText(String.valueOf(response.body().getProgress() + "%"));
                }else {
                    holder.progressBar.setProgress(0);
                    holder.tvProgress.setText(0 + "%");
                }
            }

            @Override
            public void onFailure(Call<ProgressModel> call, Throwable t) {
                showProgressBar("s", "s", false);
                holder.progressBar.setProgress(0);
                holder.tvProgress.setText("Gagal memuat data");




            }
        });


    }

    @Override
    public int getItemCount() {
        return projectModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView ivAction, ivAction2;
        TextView tvnNamaProject, tvEmail, tvProjectScope, tvTgllMulai,
        tvTglSelesai, tvDeskripsi, tvProgress;
        LinearLayout lrDetail;
        ProgressBar progressBar;
        Button btnProgress;

        private RelativeLayout rlAction;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAction = itemView.findViewById(R.id.ivIconAction);
            ivAction2 = itemView.findViewById(R.id.ivIconAction2);
            tvnNamaProject = itemView.findViewById(R.id.tvNamaProject);
            tvEmail = itemView.findViewById(R.id.tvEmailPerusahaan);
            tvProjectScope = itemView.findViewById(R.id.tvProjectScope);
            tvTgllMulai = itemView.findViewById(R.id.tvTglMulai);
            tvTglSelesai = itemView.findViewById(R.id.tvTglSelesai);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            lrDetail = itemView.findViewById(R.id.layoutDetail);
            tvProgress = itemView.findViewById(R.id.tvProgress);
            progressBar = itemView.findViewById(R.id.progressBar);
            btnProgress = itemView.findViewById(R.id.btnProgress);
            rlAction = itemView.findViewById(R.id.rlAction);

            karyawanService = ApiConfig.getClient().create(KaryawanService.class);

            btnProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new KaryawanTaskFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("nama_project", projectModelList.get(getAdapterPosition()).getNamaProject());
                    bundle.putString("project_id", projectModelList.get(getAdapterPosition()).getProjectId());
                    bundle.putString("karyawan_id", projectModelList.get(getAdapterPosition()).getKaryawanId());
                    bundle.putInt("status", Integer.parseInt(projectModelList.get(getAdapterPosition()).getStatus()));
                    fragment.setArguments(bundle);
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameKaryawn, fragment).addToBackStack(null)
                            .commit();
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
