package com.fit5046.wildsecured.WildLifeDataModal;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WildLifeQuery {

    @GET("ws/distributions/radius?")
    Call<ArrayList<WildLifeDataResponse>> getWildLifeData(@Query("latitude") String lat,
                                                          @Query("longitude") String lon,
                                                          @Query("radius") String radius);

}
