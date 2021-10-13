package com.fit5046.wildsecured.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.fit5046.wildsecured.databinding.FragmentMarkdownBinding;

import br.tiagohm.markdownview.css.ExternalStyleSheet;

public class MarkdownFragment extends Fragment {

    private FragmentMarkdownBinding binding;
    private String fileName, pageTitle;
    public MarkdownFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMarkdownBinding.inflate(inflater, container, false);
        if (getArguments() != null){
            fileName = getArguments().getString("md_file_name");
            pageTitle = getArguments().getString("md_title");
        }else{
            fileName = "";
            pageTitle = "";
        }

        binding.mdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        binding.mdGoToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.mdScrollView.setScrollY(0);
            }
        });
        binding.mdTitle.setText(pageTitle);

        binding.md.addStyleSheet(ExternalStyleSheet.fromAsset("markdown_css.css", null));

        binding.md.loadMarkdownFromAsset("markdowns/" + fileName);



//        System.out.println(getActivity().getSupportFragmentManager().getBackStackEntryCount());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}