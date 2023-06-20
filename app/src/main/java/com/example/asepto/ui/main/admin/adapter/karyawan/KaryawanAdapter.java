package com.example.asepto.ui.main.admin.adapter.karyawan;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asepto.R;
import com.example.asepto.data.api.ApiConfig;
import com.example.asepto.data.api.KaryawanService;
import com.example.asepto.data.model.KaryawanModel;
import com.example.asepto.ui.main.admin.karyawan.UpdateKaryawanFragment;

import java.util.ArrayList;
import java.util.List;


public class KaryawanAdapter extends RecyclerView.Adapter<KaryawanAdapter.ViewHolder> {
    Context context;
    List<KaryawanModel> karyawanModelList;

    public KaryawanAdapter(Context context, List<KaryawanModel> karyawanModelList) {
        this.context = context;
        this.karyawanModelList = karyawanModelList;
    }

    @NonNull
    @Override
    public KaryawanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_karyawan, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull KaryawanAdapter.ViewHolder holder, int position) {

        holder.tvNamaKaryawan.setText(karyawanModelList.get(holder.getAdapterPosition()).getNama());
        holder.tvEmailKaryawan.setText(karyawanModelList.get(holder.getAdapterPosition()).getEmail());
    }

    @Override
    public int getItemCount() {
        return karyawanModelList.size();
    }

    public void filter(ArrayList<KaryawanModel> filteredList) {
        karyawanModelList = filteredList;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNamaKaryawan, tvEmailKaryawan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaKaryawan = itemView.findViewById(R.id.tvNamaKaryawan);
            tvEmailKaryawan = itemView.findViewById(R.id.tvEmail);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Fragment fragment = new UpdateKaryawanFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", karyawanModelList.get(getAdapterPosition()).getKaryawanId());
            bundle.putString("nama", karyawanModelList.get(getAdapterPosition()).getNama());
            bundle.putString("email", karyawanModelList.get(getAdapterPosition()).getEmail());
            bundle.putString("telepon", karyawanModelList.get(getAdapterPosition()).getTelp());
            bundle.putString("no_rek", karyawanModelList.get(getAdapterPosition()).getNorekening());
            bundle.putString("nama_bank", karyawanModelList.get(getAdapterPosition()).getBank());
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameAdmin, fragment).addToBackStack(null)
                    .commit();

        }
    }

}
