package com.fit5046.wildsecured.WeatherModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ForecastWeatherResponse {
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("sunrise")
    @Expose
    private Integer sunrise;
    @SerializedName("sunset")
    @Expose
    private Integer sunset;
    @SerializedName("moonrise")
    @Expose
    private Integer moonrise;
    @SerializedName("moonset")
    @Expose
    private Integer moonset;
    @SerializedName("moon_phase")
    @Expose
    private Double moonPhase;
    @SerializedName("temp")
    @Expose
    private Temp temp;
    @SerializedName("feels_like")
    @Expose
    private FeelsLike feelsLike;
    @SerializedName("pressure")
    @Expose
    private Integer pressure;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("dew_point")
    @Expose
    private Double dewPoint;
    @SerializedName("wind_speed")
    @Expose
    private Double windSpeed;
    @SerializedName("wind_deg")
    @Expose
    private Integer windDeg;
    @SerializedName("wind_gust")
    @Expose
    private Double windGust;
    @SerializedName("weather")
    @Expose
    private ArrayList<Weather> weather = new ArrayList<Weather>();
    @SerializedName("clouds")
    @Expose
    private Integer clouds;
    @SerializedName("pop")
    @Expose
    private Double pop;
    @SerializedName("uvi")
    @Expose
    private Double uvi;

    public Integer getDt() {
        return dt;
    }

    public Integer getSunrise() {
        return sunrise;
    }

    public Integer getSunset() {
        return sunset;
    }

    public Integer getMoonrise() {
        return moonrise;
    }

    public Integer getMoonset() {
        return moonset;
    }

    public Double getMoonPhase() {
        return moonPhase;
    }

    public Temp getTemp() {
        return temp;
    }

    public FeelsLike getFeelsLike() {
        return feelsLike;
    }

    public Integer getPressure() {
        return pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Double getDewPoint() {
        return dewPoint;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public Integer getWindDeg() {
        return windDeg;
    }

    public Double getWindGust() {
        return windGust;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public Integer getClouds() {
        return clouds;
    }

    public Double getPop() {
        return pop;
    }

    public Double getUvi() {
        return uvi;
    }
}


