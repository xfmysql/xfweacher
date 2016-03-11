package com.its.xfweacher.helper;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.its.xfweacher.AppContext;
import com.its.xfweacher.entity.Weather;
import com.its.xfweacher.json.entity.RssFeed;
import com.its.xfweacher.utils.Constants;
import com.its.xfweacher.utils.SystemUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by xf on 2016/3/1.
 */
public class APIHelper {

    private Context mContext;
    static final String TAG = "APIHelper";
    public static void getTokenCode()
    {
        Ion.with(AppContext.getInstance())
                .load(Constants.Url_Oauth_Authorize)
                        //.setHeader("Authorization", "Bearer " + t)
                .setBodyParameter("client_id", Constants.API_Username)
                //.setBodyParameter("password", "")
                .setBodyParameter("response_type", "code")
                .setBodyParameter("redirect_uri", "")
                .setBodyParameter("state", "c39af7d864dfac92478b56a5989f3102")
                .setBodyParameter("accept", "true")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            Log.e(TAG, result.toString());
                            getToken(result);
                        } catch (Exception er) {
                            er.printStackTrace();
                        }
                    }
                });
    }

    static void getToken(String code){
        Ion.with(AppContext.getInstance())
                .load(Constants.Url_Oauth_Token)
                        //.setHeader("Authorization", "Bearer " + t)
                .setBodyParameter("client_id", Constants.API_Username)
                .setBodyParameter("client_secret", Constants.API_Password)
                .setBodyParameter("code", code)
                .setBodyParameter("grant_type", "authorization_code")
                .setBodyParameter("redirect_uri", "1")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            Log.e(TAG, result.toString());
                            String access_token = result.get("access_token").toString().replace("\"", "");
                            SystemUtils.setToken(access_token);
                        } catch (Exception er) {
                            er.printStackTrace();
                        }
                    }
                });
    }//end function


    public interface WeatherReflush
    {
        void onReflush(List<Weather> list);
    }
    private static WeatherReflush weatherReflush;
    public static void setWeatherReflush(WeatherReflush pWeatherReflush){
        weatherReflush = pWeatherReflush;
    }
    public static void getWeather(String token){
        Ion.with(AppContext.getInstance())
                .load(Constants.Url_Oauth_Api)
                        //.setHeader("Authorization","Bearer "+token)
                .setBodyParameter("oauth_token", token)
                .setBodyParameter("api", "getweather")
                .setBodyParameter("city", "绍兴")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            Gson gson = new Gson();
                            Log.e(TAG,result.toString());
                            String s = gson.toJson(result.getAsJsonArray("data"));

                            List<Weather> wlist = gson.fromJson(s, new TypeToken<List<Weather>>() {
                            }.getType());

                            if(weatherReflush!=null)//回调
                                weatherReflush.onReflush(wlist);
//                            for(Weather w :wlist)
//                                Log.e(TAG,w.getWeatherdate()+","+w.getPm25()+","+w.getTemperature()+","+w.getWeatherstr()+","+w.getWind()+"\n\r");

                        } catch (Exception er) {
                            er.printStackTrace();
                        }
                    }
                });
    }//end function

    public static RssFeed getFeed(String urlStr) throws ParserConfigurationException, SAXException, IOException {
        URL url = new URL(urlStr);
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        RssHandler rssHandler = new RssHandler();
        xmlReader.setContentHandler(rssHandler);
        InputStream inputStream = url.openStream();
        Charset charset = Charset.forName("GBK");
        InputStreamReader r = new InputStreamReader(inputStream, charset);
        InputSource source = new InputSource(r);
        xmlReader.parse(source);
        inputStream.close();
        return rssHandler.getRssFeed();
    }

}
