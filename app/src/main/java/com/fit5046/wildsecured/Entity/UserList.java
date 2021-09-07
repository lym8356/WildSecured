package com.fit5046.wildsecured.Entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserList {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "listName")
    public String listName;

    public UserList(String listName) {
        this.listName = listName;
    }
}
