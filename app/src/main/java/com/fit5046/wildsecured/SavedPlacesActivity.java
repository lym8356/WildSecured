package com.fit5046.wildsecured;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fit5046.wildsecured.Adapter.SavedPlaceAdapter;
import com.fit5046.wildsecured.Fragment.MapFragment;
import com.fit5046.wildsecured.Viewmodel.SavedPlaceViewModel;
import com.fit5046.wildsecured.databinding.ActivitySavedPlacesBinding;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
                    Gson gson = new Gson();
                    Log.d("TAG", gson.toJson(savedPlaceList));
                }
            }
        });
    }

    @Override
    public void showOnMap(MarkerOptions markerOptions) {
//        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
//        if (mapFragment != null){
//            mapFragment.showClickedLocation(markerOptions);
//        }else{
//            MapFragment mapFragment1 = new MapFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, mapFragment1).commit();
//        }
    }

    @Override
    public void getDirection(String lat, String lon) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + lat + "," + lon + "&mode=l"));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    @Override
    public void deleteSavedPlace(SavedPlace savedPlace) {
        savedPlaceViewModel.delete(savedPlace);
    }

    @Override
    public void setDefaultLocation(SavedPlace savedPlace) {
        savedPlace.setSelected(!savedPlace.isSelected);
        savedPlaceViewModel.update(savedPlace);

        CompletableFuture<SavedPlace> currentDefaultPlaceFuture = savedPlaceViewModel.findCurrentDefaultPlace();
        currentDefaultPlaceFuture.thenApply(currentDefaultPlace -> {
            if (currentDefaultPlace != null){
                currentDefaultPlace.setSelected(!currentDefaultPlace.isSelected);
                savedPlaceViewModel.update(currentDefaultPlace);
            }
            return true;
        });
    }
}