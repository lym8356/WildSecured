package com.fit5046.wildsecured.Viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.fit5046.wildsecured.Entity.Wildlife;
import com.fit5046.wildsecured.Repository.WildlifeRepository;

import java.util.List;

public class WildlifeViewModel extends AndroidViewModel {

    private WildlifeRepository wildlifeRepo;

    public WildlifeViewModel(@NonNull Application application) {
        super(application);
        wildlifeRepo = new WildlifeRepository(application);
    }

    public List<Wildlife> getWildlifeInGroup(String wildlifeGroup){
        return wildlifeRepo.getWildlifeInGroup(wildlifeGroup);
    }

    public List<Wildlife> getAllWildlifeList(){
        return wildlifeRepo.getAllWildlifeList();
    }
    public void insert(Wildlife wildlife){ wildlifeRepo.insert(wildlife); }
    public void insertAll(List<Wildlife> wildlifeList){ wildlifeRepo.insertAll(wildlifeList); }
    public void delete(Wildlife wildlife){ wildlifeRepo.delete(wildlife); }
    public void deleteAll(){ wildlifeRepo.deleteAll(); }
    public void update(Wildlife wildlife){ wildlifeRepo.update(wildlife); }
}
