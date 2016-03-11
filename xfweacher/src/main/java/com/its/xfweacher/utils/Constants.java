package com.its.xfweacher.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by xf on 2016/3/11.
 */
public class Constants {
    public static final String Url_Oauth_Host = "http://oauth.itouchstudio.com/";//http://192.168.1.143:9080/oauth/oauth2-php/server/
    public static final String Url_Oauth_Authorize = Url_Oauth_Host + "authorize.php";
    public static final String Url_Oauth_Token = Url_Oauth_Host + "token.php";
    public static final String Url_Oauth_Api = Url_Oauth_Host + "resource.php";

    public static final String API_Username = "xfweather";
    public static final String API_Password = "wangba314";

    //region preferences key
    public static final String Key_Token = "preferences_token";
    public static final String Key_WeatherReflushTime = "preferences_weatherreflushtime";

    //endregion
    public final static String DEFAULT_SAVE_PHOTO_PATH = Environment.getExternalStorageDirectory()+ File.separator;

}
