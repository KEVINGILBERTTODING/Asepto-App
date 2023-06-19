package com.example.asepto.ui.main.admin.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asepto.R;
import com.example.asepto.databinding.FragmentAdminHomeBinding;

public class AdminHomeFragment extends Fragment {
    private FragmentAdminHomeBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding =FragmentAdminHomeBinding.inflate(inflater, container, false);

       return binding.getRoot();
    }
}