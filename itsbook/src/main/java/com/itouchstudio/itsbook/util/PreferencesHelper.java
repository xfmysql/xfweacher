package com.itouchstudio.itsbook.util;

import android.app.Application;
import android.content.SharedPreferences;

import com.itouchstudio.itsbook.context.AppContext;

/**
 * Created by Administrator on 2016-5-12.
 */
public class PreferencesHelper {
    final static String name = "itsbook_references";
    final static int mode = 0;
    public static int GetInt(String key)
    {
        SharedPreferences mSharedPreferences = AppContext.getInstance().getSharedPreferences(name, mode);
        return mSharedPreferences.getInt(key, 0);
    }
    public static String GetString(String key)
    {
        SharedPreferences mSharedPreferences = AppContext.getInstance().getSharedPreferences(name, mode);
        return mSharedPreferences.getString(key, "");
    }
    public static boolean GetBoolean(String key)
    {
        SharedPreferences mSharedPreferences = AppContext.getInstance().getSharedPreferences(name,mode);
        return mSharedPreferences.getBoolean(key, false);
    }
    public static long GetLong(String key)
    {
        SharedPreferences mSharedPreferences = AppContext.getInstance().getSharedPreferences(name, mode);
        return mSharedPreferences.getLong(key, 0x0);
    }

    public static boolean putIntValue(String key,int value){
        SharedPreferences mSharedPreferences = AppContext.getInstance().getSharedPreferences(name, mode);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(key, value);
        return mEditor.commit();
    }

    public static boolean putStringValue(String key,String value){
        SharedPreferences mSharedPreferences = AppContext.getInstance().getSharedPreferences(name, mode);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        return mEditor.commit();
    }

    public static boolean putBooleanValue(String key,boolean value){
        SharedPreferences mSharedPreferences = AppContext.getInstance().getSharedPreferences(name, mode);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key, value);
        return mEditor.commit();
    }
}
