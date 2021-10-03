package com.fit5046.wildsecured.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.wildsecured.Fragment.MedicalPages.MedicalSelectionFragment;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.databinding.FragmentInformationBinding;

public class InformationFragment extends Fragment {

    private FragmentInformationBinding binding;

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInformationBinding.inflate(inflater, container, false);
        setUi();
        binding.infoMedicalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MedicalSelectionFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
            }
        });

        return binding.getRoot();
    }


    private void setUi(){
        String text = "Your <b> survival </b> kit need not be elaborate. You need only functional items that will meet your needs and a case to hold the items. For example, you might want to use a bandage box, soap dish, tobacco tin, first-aid case, or another suitable container. This container should be:<br> &#8226 Water-repellent or waterproof <br> &#8226 Easy to carry or attach to your body <br> &#8226 Suitable to accept various-sized components <br> &#8226 Durable";
//        binding.test.setText(Helper.fromHtml(text));
//
//        int testImg = getResources().getIdentifier("hiker_v2", "drawable", "com.fit5046.wildsecured");
//        binding.testImage.setImageResource(testImg);
    }
}