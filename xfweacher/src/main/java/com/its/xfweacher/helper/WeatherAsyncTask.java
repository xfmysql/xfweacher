package com.its.xfweacher.helper.db;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.its.xfweacher.json.entity.Result;
import com.its.xfweacher.json.entity.Weather;
import com.its.xfweacher.json.entity.Weather_data;
import com.its.xfweacher.utils.HttpUtils;


/**
 * Created by xf on 2016/2/2.
 */
public class WeatherAsyncTask extends AsyncTask<String, Void, Weather> {

    public interface ShowWeacherCallback{
        public void onShowWeacher(Weather result);
    }
    private static ShowWeacherCallback callback;
    public static void setWeacherCallback(ShowWeacherCallback _callback){
        callback = _callback;
    }
    Context mContext;
    public WeatherAsyncTask(Context context){
        mContext = context;
    }

    @Override
    protected Weather doInBackground(String... params) {
        String ak = "";
        Weather weather = null;
        try {
            ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            ak = appInfo.metaData.getString("ak");
            String url = HttpUtils.getURl(params[0], ak);
            String jsonStr = HttpUtils.getJsonStr(url);
            weather = HttpUtils.fromJson(jsonStr);
            Result r = weather.getResults().get(0);
			/*List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			list = HttpUtils.toListMap(r);*/
            for(int i = 0;i<3;i++){
                Weather_data w = r.getWeather_data().get(i);
                w.setDayPicture(HttpUtils.getImage(w.getDayPictureUrl()));
                w.setNightPicture(HttpUtils.getImage(w.getNightPictureUrl()));
            }
        }
        catch (Exception e){
        e.printStackTrace();
        }


        return weather;
        }

    @Override
    protected void onPostExecute(Weather result ) {
        callback.onShowWeacher(result);
        /*
        txtLoaction.setText(res.getCurrentCity());
        txtTime.setText(result.getDate());

        Weather_data wa = res.getWeather_data().get(0);
        Log.e("Result", "" + res.toString());
        Log.e("Weather_data", "" + wa.toString());
        //
        txtWeacherName.setText(wa.getWeather());
        String str = wa.getDate();
        txtTemperature.setText(str.substring(14, str.length()-1));
        String pm2_5 = "".equals(res.getPm25()) ? "75" : res.getPm25();
        txtContent.setText("PM2.5:" + pm2_5);


        // Ӧ��Ϊ�������ϻ�ȡ����
//			ivpic11.setImageResource(R.drawable.d00);
//			ivpic12.setImageResource(R.drawable.d01);
        ivpic11.setImageBitmap(wa.getDayPicture());
        ivpic12.setImageBitmap(wa.getNightPicture());

        tvweek1.setText(str.substring(0, 2));
        tvwea1.setText(wa.getWeather());
        tvwind1.setText(wa.getWind());
        tvtemper1.setText(wa.getTemperature());

        wa = res.getWeather_data().get(1);
        // System.out.println(wa2);

        tvtemper2.setText(wa.getTemperature());
        ivpic21.setImageBitmap(wa.getDayPicture());
        ivpic22.setImageBitmap(wa.getNightPicture());
        tvweek2.setText(wa.getDate());
        tvwea2.setText(wa.getWeather());
        tvwind2.setText(wa.getWind());
        tvtemper2.setText(wa.getTemperature());

        wa = res.getWeather_data().get(2);

        // System.out.println(wa4);
        ivpic31.setImageBitmap(wa.getDayPicture());
        ivpic32.setImageBitmap(wa.getNightPicture());
        tvweek3.setText(wa.getDate());
        tvwea3.setText(wa.getWeather());
        tvwind3.setText(wa.getWind());
        tvtemper3.setText(wa.getTemperature());
        */

     }
}
