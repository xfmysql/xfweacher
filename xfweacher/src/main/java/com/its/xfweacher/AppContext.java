package com.its.xfweacher;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.its.xfweacher.helper.APIHelper;
import com.its.xfweacher.helper.DbControl;
import com.its.xfweacher.helper.DbHelper;
import com.its.xfweacher.helper.GetTokenItf;
import com.its.xfweacher.ui.MainActivity;
import com.its.xfweacher.utils.Constants;
import com.its.xfweacher.utils.SystemUtils;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * Created by Ender on 2015/11/22.
 */
public class AppContext extends Application implements GetTokenItf {
    private static AppContext appContext;
    private static final String TAG = AppContext.class.getName();
    static SharedPreferences sharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();

        //region 初始化变量
        // 注册App异常崩溃处理器
        //Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler(this));
        //注册Bugly
        //CrashReport.initCrashReport(this, "900012812", false);
        appContext = this;
        sharedPreferences = AppContext.getInstance().getSharedPreferences("xfweather", AppContext.getInstance().MODE_PRIVATE);
        initSDDirectory();
        //endregion
        //region 获取token
        String token = SystemUtils.getToken();
        if(TextUtils.isEmpty(token))
        {
            APIHelper.getTokenCode(this);
        }
        else {  //已结获取过token ,超过token过期时间重新获取
            String reflushTime = SystemUtils.getTokenReflushTime();
            if ((System.currentTimeMillis() / 1000 - Long.parseLong(reflushTime)) > 3600) {
                APIHelper.getTokenCode(this);
            }
        }
        //endregion
        //db
        if(!getBooleanPreferences(Constants.Key_AppInit))
            DbHelper.getHelper(this).init();
        DbControl.Initial(this);

    }


    public static AppContext getInstance() {
        return appContext;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {

            Intent intent = new Intent(context,MainActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
            //1秒钟后重启应用
            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }
    //初始化
    private void initSDDirectory() {
//        File imagePath = new File(AppConfig.DEFAULT_SAVE_IMAGE_PATH );
//        if (!imagePath.exists()){
//            if (imagePath.mkdirs()) {
//                Log.e(TAG, "创建目录："+imagePath.getAbsolutePath() + "成功！");
//            } else {
//                Log.e(TAG, "创建目录："+imagePath.getAbsolutePath() + "失败！");
//            }
//        }

    }



    //region 属性获取保存

    public static boolean getBooleanPreferences(String key){
        return sharedPreferences.getBoolean(key, true);
    }

    public static void setBooleanPreferences(String key,boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static String getStringPreferences(String key){
        return sharedPreferences.getString(key, "");
    }

    public static void setStringPreferences(String key,String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static int getIntPreferences(String key){
        return sharedPreferences.getInt(key, 0);
    }

    public static void setIntPreferences(String key,int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }

    @Override
    public void AfterGet() {
        String token = SystemUtils.getToken();
        if(!TextUtils.isEmpty(token)) {
            APIHelper.getWeather(token);
        }
    }

    //endregion
}