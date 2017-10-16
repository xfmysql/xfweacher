package com.its.xfweacher.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by xf on 2016/3/11.
 */
public class Constants {
    //http://192.168.1.143/api/ http://oauth.itouchstudio.com/
    public static final String Url_Oauth_Host = "http://oauth.findide.com/";
    public static final String Url_Oauth_Authorize = Url_Oauth_Host + "authorize.php";
    public static final String Url_Oauth_Token = Url_Oauth_Host + "token.php";
    public static final String Url_Oauth_Api = Url_Oauth_Host + "resource.php";

    public static final String API_Username = "xfweather";
    public static final String API_Password = "wangba314";

    //region preferences key
    public static final String Key_Token = "preferences_token";
    public static final String Key_WeatherReflushTime = "preferences_weatherreflushtime";
    public static final String Key_AppInit = "preferences_appinit";
    //endregion


    public final static String Techn_URL = "http://news.baidu.com/n?cmd=4&class=technnews&tn=rss";
    public final static String Internet_URL = "http://news.baidu.com/n?cmd=4&class=internet&tn=rss";
    public final static String Enter_URL = "http://news.baidu.com/n?cmd=4&class=enternews&tn=rss";
    public final static String Socia_URL = "http://news.baidu.com/n?cmd=4&class=socianews&tn=rss";
    public final static String Inter_URL = "http://news.baidu.com/n?cmd=4&class=internews&tn=rss";
    public final static String Mil_URL = "http://news.baidu.com/n?cmd=1&class=mil&tn=rss";
    public final static String Auto_URL = "http://news.baidu.com/n?cmd=4&class=autonews&tn=rss";
    public final static String Health_URL = "http://news.baidu.com/n?cmd=4&class=healthnews&tn=rss";



    public final static String DEFAULT_SAVE_PHOTO_PATH = Environment.getExternalStorageDirectory()+ File.separator;

}
