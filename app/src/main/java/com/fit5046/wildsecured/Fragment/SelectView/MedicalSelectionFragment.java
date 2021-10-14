package com.fit5046.wildsecured.Fragment.SelectView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.Fragment.MarkdownFragment;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentMedicalSelectionBinding;

public class MedicalSelectionFragment extends Fragment {

    FragmentMedicalSelectionBinding binding;
    public MedicalSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMedicalSelectionBinding.inflate(inflater, container, false);
        Bundle bundle = new Bundle();

        binding.medicalInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        binding.medicalMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","medical_maintenance.md");
                bundle.putString("md_title","Maintenance of Health");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.medicalEmergencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","medical_emergencies.md");
                bundle.putString("md_title","Medical Emergencies");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.medicalBone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","medical_bone.md");
                bundle.putString("md_title","Bone and Joint Injury");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.medicalBites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","medical_bites.md");
                bundle.putString("md_title","Bites and Stings");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.medicalWounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","medical_wounds.md");
                bundle.putString("md_title","Wounds");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.medicalEnvironmental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","medical_environmental.md");
                bundle.putString("md_title","Environmental Injuries");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}