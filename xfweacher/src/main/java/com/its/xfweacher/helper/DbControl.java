package com.its.xfweacher.helper;

import android.content.Context;

/**
 * Created by Ender on 2016/1/17.
 */
public class DbControl {
    public static WeatherDao weatherDao;

    public static void Initial(Context context){

        weatherDao = new WeatherDao(context);
    }
}
