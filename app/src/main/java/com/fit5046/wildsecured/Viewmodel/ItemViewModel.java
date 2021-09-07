package com.fit5046.wildsecured.Viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit5046.wildsecured.Entity.Item;
import com.fit5046.wildsecured.Repository.ItemRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ItemViewModel extends AndroidViewModel {

    private ItemRepository itemRepo;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepo = new ItemRepository(application);
    }

    public LiveData<List<Item>> getAllItem(final int catId){
        return itemRepo.getAllItem(catId);
    }

    public LiveData<Integer> getAllCheckedItemCountByCategory(final int catId) { return itemRepo.getAllCheckedItemCountByCategory(catId); }
    public LiveData<Integer> getAllCheckedItemCountByList(final int listId) { return itemRepo.getAllCheckedItemCountByList(listId); }
    public LiveData<Integer> getAllItemCountByCategory(final int catId) { return itemRepo.getAllItemCountByCategory(catId); }
    public LiveData<Integer> getAllItemCountByList(final int listId) { return itemRepo.getAllItemCountByList(listId); }



    public void insert(Item item){ itemRepo.insert(item); }
    public void update(Item item){ itemRepo.update(item); }
    public void delete(Item item){ itemRepo.delete(item); }
}
