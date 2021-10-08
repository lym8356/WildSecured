package com.fit5046.wildsecured.Fragment.FirePages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentFireBasicFireBinding;


public class FireBasicFireFragment extends Fragment {

    private FragmentFireBasicFireBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFireBasicFireBinding.inflate(inflater, container, false);
        binding.fireIntroBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        binding.fireIntroGoTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.fireIntroScrollView.setScrollY(0);
            }
        });
        System.out.println(getActivity().getSupportFragmentManager().getBackStackEntryCount());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

}