package com.example.asepto.ui.main.admin.adapter.project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asepto.R;
import com.example.asepto.data.api.AdminService;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.data.model.ResponseModel;
import com.example.asepto.ui.main.admin.project.UpdateProjectFragment;
import com.example.asepto.ui.main.karyawan.progress.KaryawanProgressFragment;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    Context context;
    List<ProjectModel> projectModelList;
    private AlertDialog progressDialog;
    private AdminService adminService;

    public ProjectAdapter(Context context, List<ProjectModel> projectModelList) {
        this.context = context;
        this.projectModelList = projectModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_project_admin, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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


    }

    @Override
    public int getItemCount() {
        return projectModelList.size();
    }

    public void filter(ArrayList<ProjectModel> filteredList) {
        projectModelList = filteredList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView ivAction, ivAction2;
        TextView tvnNamaProject, tvEmail, tvProjectScope, tvTgllMulai,
        tvTglSelesai, tvDeskripsi;
        LinearLayout lrDetail;
        Button btnProgress, btnSelesai;
        ImageButton btnEdit, btnDelete;
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
            btnSelesai = itemView.findViewById(R.id.btnFinish);
            btnProgress = itemView.findViewById(R.id.btnProgress);
            rlAction = itemView.findViewById(R.id.rlAction);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);

            adminService = ApiConfig.getClient().create(AdminService.class);

            btnProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new KaryawanProgressFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("nama_project", projectModelList.get(getAdapterPosition()).getNamaProject());
                    bundle.putString("project_id", projectModelList.get(getAdapterPosition()).getProjectId());
                    bundle.putInt("status", Integer.parseInt(projectModelList.get(getAdapterPosition()).getStatus()));
                    fragment.setArguments(bundle);
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameKaryawn, fragment).addToBackStack(null)
                            .commit();
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
                            adminService.deleteProject(projectModelList.get(getAdapterPosition()).getId())
                                    .enqueue(new Callback<ResponseModel>() {
                                        @Override
                                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                                showProgressBar("s", "S", false);
                                                showToast("success", "Berhasil menghapus data");
                                                projectModelList.remove(getAdapterPosition());
                                                notifyDataSetChanged();
                                                dialog.dismiss();
                                            }else{
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
                    });



                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new UpdateProjectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", projectModelList.get(getAdapterPosition()).getId());
                    bundle.putString("nama_project", projectModelList.get(getAdapterPosition()).getNamaProject());
                    bundle.putString("project_id", projectModelList.get(getAdapterPosition()).getProjectId());
                    bundle.putString("deskripsi", projectModelList.get(getAdapterPosition()).getDeskripsiProject());
                    bundle.putString("tgl_mulai", projectModelList.get(getAdapterPosition()).getTglMulai());
                    bundle.putString("tgl_selesai", projectModelList.get(getAdapterPosition()).getTglSelesai());
                    bundle.putString("nama_perusahaan", projectModelList.get(getAdapterPosition()).getNamaPerusahaan());
                    bundle.putString("email_perusahaan", projectModelList.get(getAdapterPosition()).getEmailPerusahaan());
                    bundle.putString("nominal", projectModelList.get(getAdapterPosition()).getBudget());
                    fragment.setArguments(bundle);
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameAdmin, fragment).addToBackStack(null)
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
