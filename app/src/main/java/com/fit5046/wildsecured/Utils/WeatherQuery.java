package com.fit5046.wildsecured.Utils;

import com.fit5046.wildsecured.WeatherModel.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherQuery {
    @GET("data/2.5/onecall?")
    Call<WeatherResponse> getWeatherData(@Query("lat") String lat,
                                         @Query("lon") String lon,
                                         @Query("exclude") String exclude,
                                         @Query("units") String units,
                                         @Query("APPID") String app_id);
}
