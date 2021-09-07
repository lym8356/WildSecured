package com.fit5046.wildsecured.Viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit5046.wildsecured.Entity.Category;
import com.fit5046.wildsecured.Repository.CategoryRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository catRepo;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        catRepo = new CategoryRepository(application);
    }

    public LiveData<List<Category>> getAllCategory(final int listId){
        return catRepo.getAllCategory(listId);
    }

    public void insert(Category category){ catRepo.insert(category); }
    public void update(Category category){ catRepo.update(category); }
    public void delete(Category category){ catRepo.delete(category); }
}
