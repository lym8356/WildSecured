package com.fit5046.wildsecured.Fragment.SelectView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.Fragment.MarkdownFragment;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentFireSelectionBinding;

public class FireSelectionFragment extends Fragment {
    FragmentFireSelectionBinding binding;
    public FireSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFireSelectionBinding.inflate(inflater, container, false);
        Bundle bundle = new Bundle();

        binding.fireInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        binding.fireSiteSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","fire_site.md");
                bundle.putString("md_title","Site Selection and Preparation");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.fireMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","fire_material.md");
                bundle.putString("md_title","Fire Material Selection");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.fireBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","fire_build.md");
                bundle.putString("md_title","How To Build A Fire");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.fireLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("md_file_name","fire_light.md");
                bundle.putString("md_title","How To Light A Fire");
                Fragment fragment = new MarkdownFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}