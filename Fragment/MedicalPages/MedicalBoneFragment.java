package com.fit5046.wildsecured.Fragment.MedicalPages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentMedicalBoneBinding;

public class MedicalBoneFragment extends Fragment {
    FragmentMedicalBoneBinding binding;

    public MedicalBoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = FragmentMedicalBoneBinding.inflate(inflater, container, false);

        binding.medicalBoneBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        binding.medicalBoneGoTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.medicalEmergencyScrollView.setScrollY(0);
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}