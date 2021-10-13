package com.fit5046.wildsecured.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.Fragment.SelectView.DirectionSelectionFragment;
import com.fit5046.wildsecured.Fragment.SelectView.FireSelectionFragment;
import com.fit5046.wildsecured.Fragment.SelectView.WaterSelectionFragment;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentInformationBinding;

public class InformationFragment extends Fragment {

    private FragmentInformationBinding binding;

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInformationBinding.inflate(inflater, container, false);
        binding.infoMedicalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.infoDirectionCard.setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View v) {
                Fragment fragment = new DirectionSelectionFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.infoFireCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FireSelectionFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.infoPlantCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.infoWaterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new WaterSelectionFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });

        return binding.getRoot();
    }
}