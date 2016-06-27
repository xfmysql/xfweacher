package com.its.xfweacher.entity;

import com.j256.ormlite.field.DatabaseField;


/**
 * Created by Ender on 2016/1/17.
 */
public class OnedayWeacher {

        @DatabaseField(generatedId = true)
        public int ID;
        @DatabaseField
        public long CityCode;
        @DatabaseField
        public long AddTime;
        @DatabaseField
        public long WeatherDate;

    @DatabaseField
    public String Temperature;
    @DatabaseField
    public String TemperatuRef;

        @DatabaseField
        public String WeatherStr;
        @DatabaseField
        public String PM25;
        @DatabaseField
        public String Wind;
        @DatabaseField
        public String WindSpeed;
        @DatabaseField
        public String WeacherImg;

    }