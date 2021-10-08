package com.fit5046.wildsecured.Fragment.MedicalPages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentMedicalBitesBinding;

public class MedicalBitesFragment extends Fragment {

    FragmentMedicalBitesBinding binding;
    public MedicalBitesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMedicalBitesBinding.inflate(inflater, container, false);

        binding.medicalBitesBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        binding.medicalBiteGoTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.medicalBiteGoTopBtn.setScrollY(0);
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}