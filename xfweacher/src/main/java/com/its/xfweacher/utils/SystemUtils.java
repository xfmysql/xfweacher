package com.its.xfweacher.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.its.xfweacher.AppContext;

/**
 * Created by xf on 2016/3/11.
 */
public class SystemUtils {

    public static String getToken(){
        return AppContext.getStringPreferences(Constants.Key_Token);
    }
    public static void setToken(String value){
        AppContext.setStringPreferences(Constants.Key_Token, value);
    }

    public static int getWeatherReflushTime(){
        return AppContext.getIntPreferences(Constants.Key_WeatherReflushTime);
    }
    public static void setWeatherReflushTime(int value){
        AppContext.setIntPreferences(Constants.Key_WeatherReflushTime, value);
    }

}
