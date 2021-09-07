package com.fit5046.wildsecured.Viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit5046.wildsecured.Entity.UserList;
import com.fit5046.wildsecured.Repository.UserListRepository;

import java.util.List;

public class UserListViewModel extends AndroidViewModel {

    private LiveData<List<UserList>> userLists;
    private UserListRepository listRepo;

    public UserListViewModel(Application application){
        super(application);
        listRepo = new UserListRepository(application);
        userLists = listRepo.getAllUserLists();
    }

    public LiveData<List<UserList>> getUserLists() {return userLists;}
    public void insert(UserList userList){ listRepo.insert(userList); }
    public void update(UserList userList){ listRepo.update(userList); }
    public void delete(UserList userList){ listRepo.delete(userList); }
}
