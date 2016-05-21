package com.its.xfweacher.dbentity;

import com.j256.ormlite.field.DatabaseField;


/**
 * Created by Ender on 2016/1/17.
 */
public class WeatherItem {

        @DatabaseField(id  = true)
        public int id;

        @DatabaseField
        public String citycode;

        @DatabaseField
        public long addtime;

    /**
         * 星期
         */
        @DatabaseField
        public String weatherdate;

        @DatabaseField
        public String tempnow;
        @DatabaseField
        public String weatherstr;
        @DatabaseField
        public String pm25;
        @DatabaseField
        public String wind;
        @DatabaseField
        public String windspeed;
        @DatabaseField
        public String temperature;
        @DatabaseField
        public String temperaturef;
        @DatabaseField
        public String wtimg;

        public com.its.xfweacher.entity.Weather tWeacher()
        {
                com.its.xfweacher.entity.Weather w = new com.its.xfweacher.entity.Weather();
             w.setAddtime(this.addtime);
                w.setCitycode(this.citycode);
                w.setPm25(this.pm25);
                w.setTemperature(this.temperature);
                w.setTemperaturef(this.temperaturef);
                w.setTempnow(this.tempnow);
                w.setWeatherdate(this.weatherdate);
                w.setWeatherstr(this.weatherstr);
                w.setWind(this.wind);
                w.setWtimg(this.wtimg);
                w.setWindspeed(this.windspeed);
             return w;
        }
    }