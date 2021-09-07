package com.fit5046.wildsecured.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fit5046.wildsecured.Entity.Item;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    void insert(Item item);

    @Update
    void update(Item iTem);

    @Delete
    void delete(Item item);

    @Query("Select * from Item where categoryId = :catId ")
    LiveData<List<Item>> getAllItems(int catId);

    @Query("Select count(*) from Item where categoryId = :catId and isChecked = 1")
    LiveData<Integer> getAllCheckedItemCountByCategory(int catId);

    @Query("Select count(*) from Item where categoryId in (select id from Category where listId = :lId) and isChecked = 1")
    LiveData<Integer> getAllCheckedItemCountByList(int lId);

    @Query("Select count(*) from Item where categoryId = :catId")
    LiveData<Integer> getAllItemCountByCategory(int catId);

    @Query("Select count(*) from Item where categoryId in (select id from Category where listId = :lId)")
    LiveData<Integer> getAllItemCountByList(int lId);
}
