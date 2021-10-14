package com.fit5046.wildsecured.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fit5046.wildsecured.Entity.Wildlife;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.Utils.Helper;
import com.fit5046.wildsecured.databinding.ActivityWildlifeDetailBinding;
import com.google.gson.Gson;

public class WildlifeDetailActivity extends AppCompatActivity {

    private ActivityWildlifeDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWildlifeDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Gson gson = new Gson();
        Wildlife wildlife = gson.fromJson(getIntent().getStringExtra("wildlifeObject"), Wildlife.class);
        if (wildlife.getImageUrl() == null || wildlife.getImageUrl().equals("")){
            Glide.with(getApplicationContext())
                    .load(R.drawable.image_not_available)
                    .override(350, 200)
                    .into(binding.wildlifeDetailImage);
        }else{
            Glide.with(getApplicationContext())
                    .load(wildlife.getImageUrl())
                    .placeholder(R.drawable.load)
                    .override(350, 200)
                    .into(binding.wildlifeDetailImage);
        }
        binding.wildlifeInfoTitle.setText(wildlife.getCommonName());
        binding.wildlifeDetailCommonName.setText(wildlife.getCommonName());
        binding.wildlifeDetailSciName.setText(wildlife.getScientificName());
        binding.wildlifeDetailDesc.setText(wildlife.getBriefDescription());
        binding.wildlifeDetailThreatLevel.setText(Helper.getThreatLevelString(wildlife.getDangerLevel()));
        binding.wildlifeDetailThreatLevel.setTextColor(getResources().getColor(Helper.getThreatColor(wildlife.getDangerLevel())));
        binding.wildlifeDetailIdentification.setText(wildlife.getIdentification());
        binding.wildlifeDetailHabitat.setText(wildlife.getHabitat());
        binding.wildlifeDetailRisk.setText(wildlife.getRiskToHuman());

        binding.wildlifeInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}