package com.fit5046.wildsecured.Fragment.Plants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentEdiblePlantsBinding;
import com.fit5046.wildsecured.databinding.FragmentPlantMedicineBinding;


public class PlantMedicineFragment extends Fragment {

    FragmentPlantMedicineBinding binding;

    public PlantMedicineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlantMedicineBinding.inflate(inflater, container, false);

        binding.plantMedicineBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });


        return binding.getRoot();
    }

}