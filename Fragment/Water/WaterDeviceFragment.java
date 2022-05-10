package com.fit5046.wildsecured.Fragment.Water;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentMedicalIntroSectionBinding;
import com.fit5046.wildsecured.databinding.FragmentWaterDeviceBinding;


public class WaterDeviceFragment extends Fragment {

    private FragmentWaterDeviceBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWaterDeviceBinding.inflate(inflater, container, false);
        binding.waterDeviceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        System.out.println(getActivity().getSupportFragmentManager().getBackStackEntryCount());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}