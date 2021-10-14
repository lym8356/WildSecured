package com.fit5046.wildsecured.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fit5046.wildsecured.Adapter.WildlifeInfoAdapter;
import com.fit5046.wildsecured.Entity.Wildlife;
import com.fit5046.wildsecured.Viewmodel.WildlifeViewModel;
import com.fit5046.wildsecured.databinding.ActivitySnakeRecognizerBinding;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SnakeRecognizerActivity extends AppCompatActivity {

    private ActivitySnakeRecognizerBinding binding;
    private WildlifeInfoAdapter wildlifeInfoAdapter;
    private WildlifeViewModel wildlifeViewModel;
    private List<Wildlife> snakeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySnakeRecognizerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.snakeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SnakeRecognizerActivity.this, ClassifierActivity.class);
                startActivity(intent);
            }
        });
        binding.snakeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initViewModel();
        initRecyclerView();
        getSnakeFromDb();
    }

    private void initRecyclerView() {
        binding.snakeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.snakeRecyclerView.setHasFixedSize(true);
        binding.snakeRecyclerView.setItemViewCacheSize(20);
        binding.snakeRecyclerView.setDrawingCacheEnabled(true);
        wildlifeInfoAdapter = new WildlifeInfoAdapter(this);
    }

    private void initViewModel() {
        wildlifeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(WildlifeViewModel.class);
    }

    private void getSnakeFromDb(){
        CompletableFuture future = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                snakeList = wildlifeViewModel.getWildlifeInGroup("Reptiles");
                System.out.println( "Data from database: " + snakeList.size());
            }
        }).thenRun(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wildlifeInfoAdapter.setWildLifeList(snakeList);
                        binding.snakeRecyclerView.setAdapter(wildlifeInfoAdapter);
                    }
                });
            }
        });
    }
}