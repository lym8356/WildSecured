package com.fit5046.wildsecured.util;

import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.WildLifeDataModal.WildLifeDataResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Helper {
    /**
     * Helper method to provide the art resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getArtResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 781) {
            return R.drawable.ic_fog;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }

    // convert milliseconds date to day of week
    public static String formatDate(long dateInMilliseconds) {
        DateFormat dateFormat = new SimpleDateFormat("EEEE ");
        String dateString = dateFormat.format(new Date(dateInMilliseconds * 1000));
        return dateString;
    }

    // loop through wild animal info
    public static int getDangerousWildLifeCount(ArrayList<WildLifeDataResponse> wildLifeDataResponses){
        String[] dangerousAnimal = {"wolf", "Wolf", "bear", "Bear", "snake", "Snake",
                "shark", "Shark", "jellyfish", "Jellyfish", "crocodile", "Crocodile", "taipan", "Taipan"};
        int count = 0;
        for (int i=0; i<wildLifeDataResponses.size(); i++){
            for (int j=0; j<dangerousAnimal.length; j++){
                if (wildLifeDataResponses.get(i).getCommonNam() != null){
                    if (wildLifeDataResponses.get(i).getCommonNam().contains(dangerousAnimal[j])){
                        count ++;
                    }
                }
            }
        }
        return count;
    }

    public static int getDangerousInsectCount(ArrayList<WildLifeDataResponse> wildLifeDataResponses){
        String[] dangerousInsect = {"spider", "Spider", "wasp", "Wasp", "hornet", "Hornet",
                "scorpion", "Scorpion", "centipede", "Centipede"};
        int count = 0;
        for (int i=0; i<wildLifeDataResponses.size(); i++){
            for (int j=0; j<dangerousInsect.length; j++){
                if (wildLifeDataResponses.get(i).getCommonNam() != null){
                    if (wildLifeDataResponses.get(i).getCommonNam().contains(dangerousInsect[j])){
                        count ++;
                    }
                }
            }
        }
        return count;
    }
}
