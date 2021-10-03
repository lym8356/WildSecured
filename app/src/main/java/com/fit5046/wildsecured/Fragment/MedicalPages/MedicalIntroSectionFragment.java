package com.fit5046.wildsecured.Fragment.MedicalPages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.databinding.FragmentMedicalIntroSectionBinding;

public class MedicalIntroSectionFragment extends Fragment {

    private FragmentMedicalIntroSectionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMedicalIntroSectionBinding.inflate(inflater, container, false);
        binding.medicalIntroBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        binding.medicalIntroGoTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.medicalIntroScrollView.setScrollY(0);
            }
        });
        System.out.println(getActivity().getSupportFragmentManager().getBackStackEntryCount());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}