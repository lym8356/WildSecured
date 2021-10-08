package com.fit5046.wildsecured.Fragment.FirePages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.Fragment.MedicalPages.IntroSectionFragment;
import com.fit5046.wildsecured.Fragment.MedicalPages.MedicalBitesFragment;
import com.fit5046.wildsecured.Fragment.MedicalPages.MedicalBoneFragment;
import com.fit5046.wildsecured.Fragment.MedicalPages.MedicalEmergencySectionFragment;
import com.fit5046.wildsecured.Fragment.MedicalPages.MedicalIntroSectionFragment;
import com.fit5046.wildsecured.Fragment.Plants.EdiblePlantsFragment;
import com.fit5046.wildsecured.Fragment.Plants.PlantMedicineFragment;
import com.fit5046.wildsecured.Fragment.Plants.PlantsIdetificationFragment;
import com.fit5046.wildsecured.Fragment.Plants.SpecificRemFragment;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentFireSelectionBinding;
import com.fit5046.wildsecured.databinding.FragmentMedicalSelectionBinding;
import com.fit5046.wildsecured.databinding.FragmentPlantsSelectionBinding;

public class FireSelectionFragment extends Fragment {

   private FragmentFireSelectionBinding binding;

    public FireSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFireSelectionBinding.inflate(inflater, container, false);
        int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
        System.out.println(count);
        binding.fireIntrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        binding.fireBasicFireIntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FireBasicFireFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });

        binding.howToBulidIntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HowToBulidFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });

        binding.primitveMethods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PrimitveMethodsFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

}
