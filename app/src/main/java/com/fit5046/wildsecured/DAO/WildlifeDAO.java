package com.fit5046.wildsecured.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fit5046.wildsecured.Entity.Wildlife;

import java.util.List;

@Dao
public interface WildlifeDAO {

    @Query("Select * from Wildlife")
    List<Wildlife> getAllWildlifeList();

    @Query("Select * from Wildlife where wildlifeGroup = :wildlifeGroup")
    List<Wildlife> getWildlifeInGroup(String wildlifeGroup);

    @Insert
    void insert(Wildlife wildlife);

    @Insert
    void insertAll(List<Wildlife> wildlifeList);

    @Delete
    void delete(Wildlife wildlife);

    @Query("Delete from wildlife")
    void deleteAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Wildlife wildlife);


}
