package com.fit5046.wildsecured.Repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.fit5046.wildsecured.DAO.CategoryDAO;
import com.fit5046.wildsecured.Database.AppDatabase;
import com.fit5046.wildsecured.Entity.Category;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CategoryRepository {

    private CategoryDAO categoryDAO;

    public CategoryRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        categoryDAO = db.categoryDAO();
    }

    public void insert(Category category){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDAO.insert(category);
            }
        });
    }

    public void update(Category category){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDAO.update(category);
            }
        });
    }

    public void delete(Category category){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDAO.delete(category);
            }
        });
    }

    public LiveData<List<Category>> getAllCategory(final int listId){
       return categoryDAO.getAllCategory(listId);
    }
}
