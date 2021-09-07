package com.fit5046.wildsecured.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fit5046.wildsecured.DAO.CategoryDAO;
import com.fit5046.wildsecured.DAO.ItemDAO;
import com.fit5046.wildsecured.DAO.UserListDAO;
import com.fit5046.wildsecured.Database.AppDatabase;
import com.fit5046.wildsecured.Entity.UserList;

import java.util.List;

public class UserListRepository {

    private UserListDAO userListDAO;

    private LiveData<List<UserList>> allUserLists;

    public UserListRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        userListDAO = db.userListDAO();
        allUserLists = userListDAO.getAllList();
    }

    public LiveData<List<UserList>> getAllUserLists() { return allUserLists; }

    public void insert(final UserList userList){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userListDAO.insert(userList);
            }
        });
    }

    public void delete(final UserList userList){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userListDAO.delete(userList);
            }
        });
    }

    public void update(final UserList userList){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                userListDAO.update(userList);
            }
        });
    }
}
