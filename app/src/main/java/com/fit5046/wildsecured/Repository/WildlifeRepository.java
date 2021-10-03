package com.fit5046.wildsecured.Repository;

import android.app.Application;

import com.fit5046.wildsecured.DAO.WildlifeDAO;
import com.fit5046.wildsecured.Database.AppDatabase;
import com.fit5046.wildsecured.Entity.Wildlife;
import com.fit5046.wildsecured.R;

import java.util.List;

public class WildlifeRepository {

    private WildlifeDAO wildlifeDAO;

    public WildlifeRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        wildlifeDAO = db.wildlifeDAO();
    }

    public void insert(Wildlife wildlife){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() { wildlifeDAO.insert(wildlife); }
        });
    }

    public void insertAll(List<Wildlife> wildlifeList){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() { wildlifeDAO.insertAll(wildlifeList); }
        });
    }

    public void delete(Wildlife wildlife){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() { wildlifeDAO.delete(wildlife); }
        });
    }

    public void deleteAll(){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() { wildlifeDAO.deleteAll(); }
        });
    }

    public void update(Wildlife wildlife){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() { wildlifeDAO.update(wildlife); }
        });
    }

    public List<Wildlife> getAllWildlifeList(){
        return wildlifeDAO.getAllWildlifeList();
    }

    public List<Wildlife> getWildlifeInGroup(String wildlifeGroup){
        return wildlifeDAO.getWildlifeInGroup(wildlifeGroup);
    }
}
