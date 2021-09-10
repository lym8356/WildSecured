package com.fit5046.wildsecured;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fit5046.wildsecured.Adapter.SavedPlaceAdapter;
import com.fit5046.wildsecured.Adapter.UserListAdapter;
import com.fit5046.wildsecured.Viewmodel.SavedPlaceViewModel;
import com.fit5046.wildsecured.databinding.ActivitySavedPlacesBinding;

import java.util.List;

public class SavedPlacesActivity extends AppCompatActivity implements SavedPlaceAdapter.HandleCardClick {

    private RecyclerView savedPlaceRecyclerView;
    private ActivitySavedPlacesBinding binding;
    private SavedPlaceViewModel savedPlaceViewModel;
    private TextView noResultTextView;
    private SavedPlaceAdapter savedPlaceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedPlacesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        savedPlaceRecyclerView = binding.savedPlacesRecyclerView;
        noResultTextView = binding.noResultText;
        binding.savedPlaceBack.setOnClickListener(v->{finish();});

        initViewModel();
        initRecyclerView();
    }

    private void initRecyclerView() {
        savedPlaceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        savedPlaceRecyclerView.setHasFixedSize(true);
        savedPlaceAdapter = new SavedPlaceAdapter(this, this, getApplication(), this);
        savedPlaceRecyclerView.setAdapter(savedPlaceAdapter);
    }

    private void initViewModel() {
        savedPlaceViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(SavedPlaceViewModel.class);
        savedPlaceViewModel.getSavedPlaceList().observe(this, new Observer<List<SavedPlace>>() {
            @Override
            public void onChanged(List<SavedPlace> savedPlaceList) {
                if (savedPlaceList == null || savedPlaceList.size() == 0){
                    noResultTextView.setVisibility(View.VISIBLE);
                    savedPlaceRecyclerView.setVisibility(View.GONE);
                }else{
                    savedPlaceAdapter.setUserSavedPlacesList(savedPlaceList);
                    savedPlaceRecyclerView.setVisibility(View.VISIBLE);
                    noResultTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void getDirection() {

    }

    @Override
    public void deleteSavedPlace(SavedPlace savedPlace) {
        savedPlaceViewModel.delete(savedPlace);
    }
}