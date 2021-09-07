package com.fit5046.wildsecured.Repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.fit5046.wildsecured.DAO.ItemDAO;
import com.fit5046.wildsecured.Database.AppDatabase;
import com.fit5046.wildsecured.Entity.Item;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ItemRepository {

    private ItemDAO itemDAO;

    public ItemRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        itemDAO = db.itemDAO();
    }

    public void insert(Item item){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                itemDAO.insert(item);
            }
        });
    }

    public void update(Item item){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                itemDAO.update(item);
            }
        });
    }

    public void delete(Item item){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                itemDAO.delete(item);
            }
        });
    }

    public LiveData<List<Item>> getAllItem(final int catId){
        return itemDAO.getAllItems(catId);
    }
    public LiveData<Integer> getAllCheckedItemCountByCategory(final int catId){ return itemDAO.getAllCheckedItemCountByCategory(catId); }
    public LiveData<Integer> getAllCheckedItemCountByList(final int listId){ return itemDAO.getAllCheckedItemCountByList(listId); }
    public LiveData<Integer> getAllItemCountByCategory(final int catId){ return itemDAO.getAllItemCountByCategory(catId); }
    public LiveData<Integer> getAllItemCountByList(final int listId){ return itemDAO.getAllItemCountByList(listId); }


}
