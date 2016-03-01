package com.its.xfweacher.context;

import android.app.Application;
import android.content.Context;

/**
 * Created by xf on 2016/2/2.
 */
public class ITSApplication extends Application {
    private static ITSApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    };

    public static Application getInstance(){
        return instance;
    }
}
