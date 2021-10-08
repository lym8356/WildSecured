package com.fit5046.wildsecured.Fragment.Plants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.Fragment.Water.WaterDeviceFragment;
import com.fit5046.wildsecured.Fragment.Water.WaterPureFragment;
import com.fit5046.wildsecured.Fragment.Water.WaterResourceFragment;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentPlantsSelectionBinding;
import com.fit5046.wildsecured.databinding.FragmentWaterSelectionBinding;


public class PlantsSelectionFragment extends Fragment {

   private FragmentPlantsSelectionBinding binding;

    public PlantsSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlantsSelectionBinding.inflate(inflater, container, false);
        int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
        System.out.println(count);
        binding.plantsIntrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        binding.plantMedicineIntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PlantMedicineFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });

        binding.plantsIdetificationIntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PlantsIdetificationFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });

        binding.specificRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SpecificRemFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.ediblePlantIntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EdiblePlantsFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
       // Inflate the layout for this fragment
        return binding.getRoot();
    }


}