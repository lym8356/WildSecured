package com.fit5046.wildsecured.WeatherModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse {
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("timezone_offset")
    @Expose
    private int timezone_offset;
    @SerializedName("current")
    @Expose
    private CurrentWeatherResponse currentWeatherResponse;
    @SerializedName("daily")
    @Expose
    private ArrayList<ForecastWeatherResponse> forecastWeatherResponses = new ArrayList<ForecastWeatherResponse>();

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public int getTimezone_offset() {
        return timezone_offset;
    }

    public CurrentWeatherResponse getCurrentWeatherResponse() {
        return currentWeatherResponse;
    }

    public ArrayList<ForecastWeatherResponse> getForecastWeatherResponses() {
        return forecastWeatherResponses;
    }
}
