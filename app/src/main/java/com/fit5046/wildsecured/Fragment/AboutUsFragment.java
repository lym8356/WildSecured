package com.fit5046.wildsecured.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentAboutUsBinding;


public class AboutUsFragment extends Fragment {
    FragmentAboutUsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAboutUsBinding.inflate(inflater, container, false);

        binding.aboutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}