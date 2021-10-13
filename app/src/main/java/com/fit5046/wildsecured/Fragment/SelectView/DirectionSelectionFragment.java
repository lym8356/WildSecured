package com.fit5046.wildsecured.Fragment.SelectView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.Fragment.MarkdownFragment;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentDirectionSelectionBinding;

public class DirectionSelectionFragment extends Fragment {
    FragmentDirectionSelectionBinding binding;

    public DirectionSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDirectionSelectionBinding.inflate(inflater, container, false);
        Bundle bundle = new Bundle();

        binding.directionInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        binding.directionSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","direction_sun.md");
                bundle.putString("md_title","Using the Sun and Shadows");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.directionMoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","direction_moon.md");
                bundle.putString("md_title","Using the Moon and the Stars");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.directionOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","direction_other.md");
                bundle.putString("md_title","Other Means of Determining Direction");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}