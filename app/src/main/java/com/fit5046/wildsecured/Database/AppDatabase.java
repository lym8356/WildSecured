package com.fit5046.wildsecured.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fit5046.wildsecured.DAO.CategoryDAO;
import com.fit5046.wildsecured.DAO.ItemDAO;
import com.fit5046.wildsecured.DAO.UserListDAO;
import com.fit5046.wildsecured.Entity.Category;
import com.fit5046.wildsecured.Entity.Item;
import com.fit5046.wildsecured.Entity.UserList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {UserList.class, Category.class, Item.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserListDAO userListDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract ItemDAO itemDAO();

    public static AppDatabase INSTANCE;

    private static final String dbName = "AppDatabase";

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
