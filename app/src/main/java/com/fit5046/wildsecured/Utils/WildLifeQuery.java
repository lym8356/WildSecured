package com.fit5046.wildsecured.Utils;

import com.fit5046.wildsecured.WildlifeModel.WildLifeDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WildLifeQuery {

//    @GET("ws/explore/group/")
//    Call<ArrayList<WildLifeDataModel>> getWildLifeData(@Url String url);
    @GET
    Call<ArrayList<WildLifeDataModel>> getWildLifeData(@Url String url);
}
