package com.fit5046.wildsecured.Utils;

import com.fit5046.wildsecured.GoogleModel.GoogleResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceQuery {

    @GET("maps/api/place/nearbysearch/json?")
    Call<GoogleResponseModel> getNearByPlaces(@Query("location") String location,
                                              @Query("radius") String radius,
                                              @Query("type") String type,
                                              @Query("key") String key);

//    @GET
//    Call<DirectionResponseModel> getDirection(@Url String url);
}
