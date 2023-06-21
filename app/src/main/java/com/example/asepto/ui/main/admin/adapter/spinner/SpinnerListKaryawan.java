package com.example.asepto.ui.main.admin.adapter.spinner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.asepto.data.model.KaryawanModel;

import java.util.List;

public class SpinnerListKaryawan extends ArrayAdapter<KaryawanModel> {

   public SpinnerListKaryawan(@NonNull Context context, List<KaryawanModel> karyawanModelList){
            super(context, android.R.layout.simple_spinner_item, karyawanModelList);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setText(getItem(position).getNama());
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setText(getItem(position).getNama());
            return view;
        }


    public String getKaryawanId(int position) {
        return getItem(position).getKaryawanId();
    }


    public String getNamaKaryawan(int position) {
       return getItem(position).getNama();
    }






    }
