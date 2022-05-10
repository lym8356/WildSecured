package com.fit5046.wildsecured.Fragment.Water;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentWaterResourceBinding;


public class WaterResourceFragment extends Fragment {
    private FragmentWaterResourceBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWaterResourceBinding.inflate(inflater, container, false);
        binding.waterResouceBack.setOnClickListener(new View.OnClickListener() {
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