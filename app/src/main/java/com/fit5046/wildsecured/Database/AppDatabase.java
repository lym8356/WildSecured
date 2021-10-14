package com.fit5046.wildsecured.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.fit5046.wildsecured.DAO.CategoryDAO;
import com.fit5046.wildsecured.DAO.ItemDAO;
import com.fit5046.wildsecured.DAO.SavedPlaceDAO;
import com.fit5046.wildsecured.DAO.UserListDAO;
import com.fit5046.wildsecured.DAO.WildlifeDAO;
import com.fit5046.wildsecured.Entity.Category;
import com.fit5046.wildsecured.Entity.Item;
import com.fit5046.wildsecured.Entity.UserList;
import com.fit5046.wildsecured.Entity.Wildlife;
import com.fit5046.wildsecured.SavedPlace;
import com.fit5046.wildsecured.Utils.Helper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {UserList.class, Category.class, Item.class, SavedPlace.class, Wildlife.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserListDAO userListDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract ItemDAO itemDAO();
    public abstract SavedPlaceDAO savedPlaceDAO();
    public abstract WildlifeDAO wildlifeDAO();

    public static AppDatabase INSTANCE;

    private static final String dbName = "AppDatabase";

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static synchronized AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, dbName)
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    AppDatabase database = AppDatabase.getInstance(context);
                                    try {
                                        List<Wildlife> wildlifeList = Helper.readData();
                                        database.wildlifeDAO().insertAll(wildlifeList);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
