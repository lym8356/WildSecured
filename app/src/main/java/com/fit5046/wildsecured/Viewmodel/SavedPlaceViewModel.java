package com.fit5046.wildsecured.Viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit5046.wildsecured.Repository.SavedPlaceRepository;
import com.fit5046.wildsecured.SavedPlace;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SavedPlaceViewModel extends AndroidViewModel {
    private LiveData<List<SavedPlace>> userSavedPlacesList;
    private SavedPlaceRepository savedPlaceRepo;

    public SavedPlaceViewModel(@NonNull Application application) {
        super(application);
        savedPlaceRepo = new SavedPlaceRepository(application);
        userSavedPlacesList = savedPlaceRepo.getAllSavedPlacesLists();
    }

    public LiveData<List<SavedPlace>> getSavedPlaceList() {return userSavedPlacesList;}
    public void insert(SavedPlace savedPlace){ savedPlaceRepo.insert(savedPlace); }
    public void update(SavedPlace savedPlace){ savedPlaceRepo.update(savedPlace); }
    public void delete(SavedPlace savedPlace){ savedPlaceRepo.delete(savedPlace); }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<SavedPlace> findByPlaceIdFuture(final String placeId){
        return savedPlaceRepo.findByPlaceIdFuture(placeId);
    }
}
