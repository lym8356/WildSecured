package com.fit5046.wildsecured;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fit5046.wildsecured.databinding.ActivityAnimalInfoBinding;
import com.fit5046.wildsecured.databinding.ActivityHomeBinding;

public class AnimalInfoActivity extends AppCompatActivity {

    private ActivityAnimalInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnimalInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.animalAdviceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}