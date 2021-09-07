package com.fit5046.wildsecured.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fit5046.wildsecured.Entity.UserList;

import java.util.List;

@Dao
public interface UserListDAO {

    @Insert
    void insert(UserList userList);

    @Update
    void update(UserList userList);

    @Delete
    void delete(UserList userList);

    @Query("Select * from UserList")
    LiveData<List<UserList>> getAllList();
}
