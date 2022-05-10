package com.fit5046.wildsecured.Fragment.Plants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentEdiblePlantsBinding;
import com.fit5046.wildsecured.databinding.FragmentMedicalBitesBinding;


public class EdiblePlantsFragment extends Fragment {

    FragmentEdiblePlantsBinding binding;

    public EdiblePlantsFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEdiblePlantsBinding.inflate(inflater, container, false);

        binding.ediblePlantBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });


        return binding.getRoot();
    }

}