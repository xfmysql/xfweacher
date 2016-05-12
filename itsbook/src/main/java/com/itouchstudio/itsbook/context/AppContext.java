package com.itouchstudio.itsbook.context;

import android.app.Application;

/**
 * Created by Administrator on 2016-5-12.
 */
public class AppContext extends Application {
    private static AppContext appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    public static AppContext getInstance() {
        return appContext;
    }

}
