package com.fit5046.wildsecured.Repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.fit5046.wildsecured.DAO.SavedPlaceDAO;
import com.fit5046.wildsecured.Database.AppDatabase;
import com.fit5046.wildsecured.SavedPlace;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SavedPlaceRepository {

    private SavedPlaceDAO savedPlaceDAO;

    private LiveData<List<SavedPlace>> allSavedPlaces;

    public SavedPlaceRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        savedPlaceDAO = db.savedPlaceDAO();
        allSavedPlaces = savedPlaceDAO.getAllSavedPlace();
    }

    public LiveData<List<SavedPlace>> getAllSavedPlacesLists() { return allSavedPlaces; }

    public void insert(final SavedPlace savedPlace){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                savedPlaceDAO.insert(savedPlace);
            }
        });
    }

    public void delete(final SavedPlace savedPlace){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                savedPlaceDAO.delete(savedPlace);
            }
        });
    }

    public void update(final SavedPlace savedPlace){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                savedPlaceDAO.update(savedPlace);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<SavedPlace> findByPlaceIdFuture(final String placeId) {
        return CompletableFuture.supplyAsync(new Supplier<SavedPlace>() {
            @Override
            public SavedPlace get() {
                return savedPlaceDAO.findByPlaceId(placeId);
            }
        }, AppDatabase.databaseWriteExecutor);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<SavedPlace> findCurrentDefaultPlace() {
        return CompletableFuture.supplyAsync(new Supplier<SavedPlace>() {
            @Override
            public SavedPlace get() {
                return savedPlaceDAO.findCurrentDefaultPlace();
            }
        }, AppDatabase.databaseWriteExecutor);
    }

}
