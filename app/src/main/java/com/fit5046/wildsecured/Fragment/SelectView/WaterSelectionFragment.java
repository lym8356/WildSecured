package com.fit5046.wildsecured.Fragment.SelectView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.Fragment.MarkdownFragment;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentWaterSelectionBinding;

public class WaterSelectionFragment extends Fragment {
    FragmentWaterSelectionBinding binding;

    public WaterSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWaterSelectionBinding.inflate(inflater, container, false);
        Bundle bundle = new Bundle();
        binding.waterInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        binding.waterSources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","water_sources.md");
                bundle.putString("md_title","Water Sources");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.waterStillConstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","still_construction.md");
                bundle.putString("md_title","Still Construction");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.waterPurification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","water_purification.md");
                bundle.putString("md_title","Water Purification");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}