package com.fit5046.wildsecured.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.CheckListActivity;
import com.fit5046.wildsecured.ClassifierActivity;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentMoreBinding;

public class MoreFragment extends Fragment {
    FragmentMoreBinding binding;
    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoreBinding.inflate(inflater, container, false);


        binding.moreSnakeRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassifierActivity.class);
                startActivity(intent);
            }
        });
        binding.moreAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AboutUsFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.moreTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", "tutorial");
                Fragment fragment = new HomeFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.moreStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new StatisticsFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });
        binding.moreBackpack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckListActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
}