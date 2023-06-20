package com.example.asepto.ui.main.karyawan.adapter.project;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asepto.R;
import com.example.asepto.data.model.ProjectModel;
import com.example.asepto.ui.main.karyawan.progress.KaryawanProgressFragment;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    Context context;
    List<ProjectModel> projectModelList;

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
        holder.tvnNamaPerusahaan.setText(projectModelList.get(holder.getAdapterPosition()).getNamaPerusahaan());
        holder.tvEmail.setText(projectModelList.get(holder.getAdapterPosition()).getEmailPerusahaan());
        holder.tvProjectScope.setText(projectModelList.get(holder.getAdapterPosition()).getNamaProject());
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

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView ivAction, ivAction2;
        TextView tvnNamaPerusahaan, tvEmail, tvProjectScope, tvTgllMulai,
        tvTglSelesai, tvDeskripsi;
        LinearLayout lrDetail;
        Button btnProgress;
        private RelativeLayout rlAction;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAction = itemView.findViewById(R.id.ivIconAction);
            ivAction2 = itemView.findViewById(R.id.ivIconAction2);
            tvnNamaPerusahaan = itemView.findViewById(R.id.tvNamaPerusahaan);
            tvEmail = itemView.findViewById(R.id.tvEmailPerusahaan);
            tvProjectScope = itemView.findViewById(R.id.tvProjectScope);
            tvTgllMulai = itemView.findViewById(R.id.tvTglMulai);
            tvTglSelesai = itemView.findViewById(R.id.tvTglSelesai);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            lrDetail = itemView.findViewById(R.id.layoutDetail);
            btnProgress = itemView.findViewById(R.id.btnProgress);
            rlAction = itemView.findViewById(R.id.rlAction);

            btnProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new KaryawanProgressFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("nama_project", projectModelList.get(getAdapterPosition()).getNamaProject());
                    bundle.putString("project_id", projectModelList.get(getAdapterPosition()).getProjectId());
                    fragment.setArguments(bundle);
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameKaryawn, fragment).addToBackStack(null)
                            .commit();
                }
            });
        }

    }
}
