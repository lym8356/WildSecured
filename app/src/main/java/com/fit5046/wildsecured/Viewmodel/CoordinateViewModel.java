package com.fit5046.wildsecured.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fit5046.wildsecured.GoogleModel.SharedPlaceModel;

public class CoordinateViewModel extends ViewModel {

    public MutableLiveData<SharedPlaceModel> sharedPlace = new MutableLiveData<>();

    public SharedPlaceModel getPlace(){ return sharedPlace.getValue(); }

    public void setSharedPlace(SharedPlaceModel sharedPlace){ this.sharedPlace.setValue(sharedPlace); }
}
