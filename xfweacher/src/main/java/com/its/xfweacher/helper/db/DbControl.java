package com.its.xfweacher.helper.db;

import android.content.Context;

/**
 * Created by Ender on 2016/1/17.
 */
public class DbControl {
    public static OneWeacherDao oneWeacherDao;
    public static TodayWeacherDao todayWeacherDao;
    public static void Initial(Context context){
        todayWeacherDao = new TodayWeacherDao(context);
        oneWeacherDao = new OneWeacherDao(context);
    }
}
