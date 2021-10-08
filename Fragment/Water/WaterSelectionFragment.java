package com.fit5046.wildsecured.Fragment.Water;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.Fragment.MedicalPages.MedicalBitesFragment;
import com.fit5046.wildsecured.Fragment.MedicalPages.MedicalBoneFragment;
import com.fit5046.wildsecured.Fragment.MedicalPages.MedicalEmergencySectionFragment;
import com.fit5046.wildsecured.Fragment.MedicalPages.MedicalIntroSectionFragment;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentMedicalSelectionBinding;
import com.fit5046.wildsecured.databinding.FragmentWaterSelectionBinding;


public class WaterSelectionFragment extends Fragment {

  private FragmentWaterSelectionBinding binding;
  public WaterSelectionFragment(){

  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentWaterSelectionBinding.inflate(inflater, container, false);
    int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
    System.out.println(count);
    binding.waterInfoBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
      }
    });

    binding.waterResourceIntr.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = new WaterResourceFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
      }
    });

    binding.waterDeviceIntr.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = new WaterDeviceFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
      }
    });

    binding.waterPureIntr.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = new WaterPureFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
      }
    });

    // Inflate the layout for this fragment
    return binding.getRoot();
  }


}