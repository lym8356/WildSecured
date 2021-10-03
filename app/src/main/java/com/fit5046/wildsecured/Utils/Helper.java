package com.fit5046.wildsecured.Utils;

import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;

import com.fit5046.wildsecured.Entity.Wildlife;
import com.fit5046.wildsecured.R;
import com.fit5046.wildsecured.WildlifeModel.WildLifeDataModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    public static List<Wildlife> findCommonElements(List<WildLifeDataModel> fromApi, List<Wildlife> fromDb){
        List<Wildlife> resultList = new ArrayList<>();
        for (WildLifeDataModel wildLifeDataModel: fromApi){
            Wildlife wildlife = find(fromDb, wildLifeDataModel);
            if (wildlife != null){
                resultList.add(wildlife);
            }
        }
        return resultList;
    }

    private static Wildlife find(List<Wildlife> fromDb, WildLifeDataModel wildLifeDataModel) {
        for (Wildlife wildlife: fromDb){
            if (matches(wildlife, wildLifeDataModel)){
                return wildlife;
            }
        }
        return null;
    }

    private static boolean matches(Wildlife wildlife, WildLifeDataModel wildLifeDataModel) {
        if (wildlife.getCommonName().equals(wildLifeDataModel.getCommonName())){
            return true;
        }else{
            return false;
        }
    }


    public static String getThreatLevelString(String dangerLevel){
        if (dangerLevel.equals("1")){
            return "Harmless";
        }else if (dangerLevel.equals("2")){
            return "Pose a threat";
        }else if (dangerLevel.equals("3")){
            return "Dangerous";
        }else if (dangerLevel.equals("4")){
            return "Life Threatening";
        }else{
            return null;
        }
    }

    public static int getThreatColor(String dangerLevel){
        if (dangerLevel.equals("1")){
            return R.color.harmless;
        }else if (dangerLevel.equals("2")){
            return R.color.harmful;
        }else if (dangerLevel.equals("3")){
            return R.color.dangerous;
        }else if (dangerLevel.equals("4")){
            return R.color.fatal;
        }else{
            return R.color.black;
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (html == null){
            return new SpannableString("");
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }else{
            return Html.fromHtml(html);
        }
    }

}
