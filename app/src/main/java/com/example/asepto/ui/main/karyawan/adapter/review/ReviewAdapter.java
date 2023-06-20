package com.example.asepto.ui.main.karyawan.adapter.review;

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
import com.example.asepto.data.model.FeddBackModel;
import com.example.asepto.data.model.ResponseModel;

import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    Context context;
    List<FeddBackModel> feedBackModelList;
    private AlertDialog progressDialog;
    private KaryawanService karyawanService;

    public ReviewAdapter(Context context, List<FeddBackModel> feedBackModelList) {
        this.context = context;
        this.feedBackModelList = feedBackModelList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_review, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {

        holder.tvProjectName.setText(feedBackModelList.get(holder.getAdapterPosition()).getNamaProject());
        holder.tvReview.setText(feedBackModelList.get(holder.getAdapterPosition()).getFeedback());



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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAction, ivAction2;
        TextView tvReview, tvProjectName;
        LinearLayout lrDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAction = itemView.findViewById(R.id.ivIconAction);
            ivAction2 = itemView.findViewById(R.id.ivIconAction2);
            tvReview = itemView.findViewById(R.id.tvReview);
            tvProjectName = itemView.findViewById(R.id.tvNamaProject);
            lrDetail = itemView.findViewById(R.id.layoutDetail);
            karyawanService = ApiConfig.getClient().create(KaryawanService.class);


        }
    }

}
