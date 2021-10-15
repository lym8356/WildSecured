package com.fit5046.wildsecured.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.databinding.FragmentStatisticsBinding;

import br.tiagohm.markdownview.css.ExternalStyleSheet;

public class StatisticsFragment extends Fragment {


    FragmentStatisticsBinding binding;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = FragmentStatisticsBinding.inflate(inflater, container, false);

        binding.statisticsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        binding.statisticsMd.addStyleSheet(ExternalStyleSheet.fromAsset("markdown_css.css", null));
        binding.statisticsMd.loadMarkdownFromAsset("markdowns/Statistics.md");

        binding.statisticsGoTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.statisticsScrollView.setScrollY(0);
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}