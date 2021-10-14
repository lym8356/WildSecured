package com.fit5046.wildsecured.Fragment.SelectView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.Fragment.MarkdownFragment;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentPlantSelectionBinding;

public class PlantSelectionFragment extends Fragment {
    FragmentPlantSelectionBinding binding;

    public PlantSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPlantSelectionBinding.inflate(inflater, container, false);
        Bundle bundle = new Bundle();

        binding.plantInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        binding.plantIdentification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","plant_identification.md");
                bundle.putString("md_title","Plant Identification");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.plantEdibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","plant_edibility.md");
                bundle.putString("md_title","Edibility of Plants");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.plantMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","plant_medicine.md");
                bundle.putString("md_title","Plants For Medicine");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.plantMiscellaneous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","plant_miscellaneous.md");
                bundle.putString("md_title","Miscellaneous Use");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}