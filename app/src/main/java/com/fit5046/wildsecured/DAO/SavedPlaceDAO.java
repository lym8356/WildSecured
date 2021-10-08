package com.fit5046.wildsecured.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fit5046.wildsecured.SavedPlace;

import java.util.List;

@Dao
public interface SavedPlaceDAO {

    @Insert
    void insert(SavedPlace savedPlace);

    @Update
    void update(SavedPlace savedPlace);

    @Delete
    void delete(SavedPlace savedPlace);

    @Query("Select * from SavedPlace")
    LiveData<List<SavedPlace>> getAllSavedPlace();

    @Query("Select * from SavedPlace where placeId = :pId limit 1")
    SavedPlace findByPlaceId(String pId);

    @Query("Select * from SavedPlace where isSelected = 1 limit 1")
    SavedPlace findCurrentDefaultPlace();

}
