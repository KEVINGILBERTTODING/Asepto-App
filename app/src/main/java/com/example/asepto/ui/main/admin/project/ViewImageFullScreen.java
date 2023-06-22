package com.example.asepto.ui.main.admin.project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.asepto.R;
import com.example.asepto.databinding.FragmentViewImageFullScreenBinding;

public class ViewImageFullScreen extends Fragment {
    private FragmentViewImageFullScreenBinding binding;
    private String image;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewImageFullScreenBinding.inflate(inflater, container, false);
        image = getArguments().getString("image");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Glide.with(getContext())
                .load(image)
                .into(binding.ivEvidence);

        listener();

    }

    private void listener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}