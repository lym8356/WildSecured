package com.fit5046.wildsecured.Fragment.FirePages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentMedicalBoneBinding;
import com.fit5046.wildsecured.databinding.FragmentPrimitveMethodsBinding;


public class PrimitveMethodsFragment extends Fragment {

   FragmentPrimitveMethodsBinding binding;


    public PrimitveMethodsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = FragmentPrimitveMethodsBinding.inflate(inflater, container, false);

        binding.fireGoBack.setOnClickListener(new View.OnClickListener() {
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