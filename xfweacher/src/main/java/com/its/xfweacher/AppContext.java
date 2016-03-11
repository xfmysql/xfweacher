package com.its.xfweacher;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.its.xfweacher.helper.APIHelper;
import com.its.xfweacher.utils.SystemUtils;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * Created by Ender on 2015/11/22.
 */
public class AppContext extends Application {
    private static AppContext appContext;
    private static final String TAG = "AppContext";
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

        //region 初始化逻辑
        try {
            //region 初始化语音
            int volumn = 8,speed = 8, speadtype=0;
//            String strVolumn = appContext.getProperty(AppConfig.SetVolumn);
//            if(!StringUtils.isEmpty(strVolumn))
//                volumn = Integer.parseInt(strVolumn);
//            String strSpeed = appContext.getProperty(AppConfig.SetSpeed);
//            if(!StringUtils.isEmpty(strSpeed))
//                speed = Integer.parseInt(strSpeed);
//
//            String speaktype = appContext.getProperty(AppConfig.SetSpeakType);
//            if(!StringUtils.isEmpty(speaktype)) {
//                if (Integer.parseInt(speaktype) == 0)
//                    speadtype = 0;
//                else
//                    speadtype = 1;
//            }
            //TtsService.getTtsService().initial(appContext,volumn,speed,speadtype);
            //TtsService.getTtsService().speak("欢迎使用顺治校安系统");
            //endregion
        } catch (Exception e){
            //ViewUtils.showToast("系统初始化失败");
        }

        String token = SystemUtils.getToken();
        if(TextUtils.isEmpty(token))
        {
            APIHelper.getTokenCode();
        }

        //endregion

    }


    public static AppContext getInstance() {
        return appContext;
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
    //endregion
}