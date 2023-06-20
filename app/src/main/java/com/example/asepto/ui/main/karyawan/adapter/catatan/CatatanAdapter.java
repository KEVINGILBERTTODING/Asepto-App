package com.example.asepto.ui.main.karyawan.adapter.catatan;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asepto.R;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.CatatanModel;

import java.util.List;


public class CatatanAdapter extends RecyclerView.Adapter<CatatanAdapter.ViewHolder> {
    Context context;
    List<CatatanModel> catatanModelList;
    private AlertDialog progressDialog;
    private KaryawanService karyawanService;

    public CatatanAdapter(Context context, List<CatatanModel> catatanModelList) {
        this.context = context;
        this.catatanModelList = catatanModelList;
    }

    @NonNull
    @Override
    public CatatanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_catatan, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CatatanAdapter.ViewHolder holder, int position) {

        holder.tvCatatan.setText(catatanModelList.get(holder.getAdapterPosition()).getCatatan());
        holder.tvDate.setText(catatanModelList.get(holder.getAdapterPosition()).getTanggalEvent());



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
        TextView tvDate, tvCatatan;
        LinearLayout lrDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAction = itemView.findViewById(R.id.ivIconAction);
            ivAction2 = itemView.findViewById(R.id.ivIconAction2);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCatatan = itemView.findViewById(R.id.tvCatatan);
            lrDetail = itemView.findViewById(R.id.layoutDetail);
            karyawanService = ApiConfig.getClient().create(KaryawanService.class);


        }
    }

}
