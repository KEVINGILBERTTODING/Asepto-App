package com.example.asepto.ui.main.admin.adapter.review;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asepto.R;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.FeedBackModel;

import java.util.ArrayList;
import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    Context context;
    List<FeedBackModel> feedBackModelList;
    private AlertDialog progressDialog;
    private KaryawanService karyawanService;

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
        LinearLayout lrDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAction = itemView.findViewById(R.id.ivIconAction);
            ivAction2 = itemView.findViewById(R.id.ivIconAction2);
            etReview = itemView.findViewById(R.id.etReview);
            tvNamaPegawai = itemView.findViewById(R.id.tvNamaPegawai);
            tvProjectName = itemView.findViewById(R.id.tvNamaProject);
            lrDetail = itemView.findViewById(R.id.layoutDetail);
            karyawanService = ApiConfig.getClient().create(KaryawanService.class);


        }
    }

}
