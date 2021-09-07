package com.fit5046.wildsecured.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fit5046.wildsecured.Entity.Category;
import com.fit5046.wildsecured.Entity.Item;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("Select * from Category where listId = :lId")
    LiveData<List<Category>> getAllCategory(int lId);
}
