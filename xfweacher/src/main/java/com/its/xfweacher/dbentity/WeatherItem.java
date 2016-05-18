package com.its.xfweacher.dbentity;

import com.j256.ormlite.field.DatabaseField;


/**
 * Created by Ender on 2016/1/17.
 */
public class WeatherItem {



        @DatabaseField(id = true)
        public int id;
        @DatabaseField
        public String citycode;
        @DatabaseField
        public String weatherdate;
        @DatabaseField
        public long addtime;
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
    }