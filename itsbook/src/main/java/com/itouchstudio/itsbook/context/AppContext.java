package com.itouchstudio.itsbook.context;

import android.app.Application;

import com.itouchstudio.itsbook.db.DbControl;
import com.itouchstudio.itsbook.db.DbHelper;

/**
 * Created by Administrator on 2016-5-12.
 */
public class AppContext extends Application {
    private static AppContext appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        //DbHelper.getHelper(this).init();//创建数据库
        DbControl.Initial(appContext);
    }

    public static AppContext getInstance() {
        return appContext;
    }

}
