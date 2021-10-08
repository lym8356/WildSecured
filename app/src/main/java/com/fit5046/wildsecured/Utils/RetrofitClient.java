package com.fit5046.wildsecured.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit weatherRetrofit = null;
    private static Retrofit alaRetrofit = null;
    private static Retrofit googleRetrofit = null;
    public static final String GOOGLE_PLACE_BASE_URL = "https://maps.googleapis.com/";
    public static final String OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org/";
    public static final String ALA_BASE_URL = "https://biocache-ws.ala.org.au/";

    public static Retrofit openWeatherRetrofitClient(){
        if (weatherRetrofit == null){
            weatherRetrofit = new Retrofit.Builder()
                    .baseUrl(OPEN_WEATHER_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return weatherRetrofit;
    }

    public static Retrofit alaRetrofitClient(){
        if (alaRetrofit == null){
            alaRetrofit = new Retrofit.Builder()
                    .baseUrl(ALA_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return alaRetrofit;
    }

    public static Retrofit placeRetrofitClient(){
        if (googleRetrofit == null){
            googleRetrofit = new Retrofit.Builder()
                    .baseUrl(GOOGLE_PLACE_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return googleRetrofit;
    }
}
