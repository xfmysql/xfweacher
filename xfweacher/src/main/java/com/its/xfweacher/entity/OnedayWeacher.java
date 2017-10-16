package com.its.xfweacher.entity;

import com.j256.ormlite.field.DatabaseField;


/**
 * Created by Ender on 2016/1/17.
 */
public class OnedayWeacher {

        @DatabaseField(generatedId = true)
        public int id;
        @DatabaseField
        public String citycode;
    @DatabaseField
    public String weatherdate;

        @DatabaseField
        public long addtime;
    @DatabaseField
    public String weathertxt;
    @DatabaseField
    public String pm25;
    @DatabaseField
    public String winddir;
    @DatabaseField
    public String windspd;
    @DatabaseField
    public String tmpheight;
    @DatabaseField
    public String tmplow;
    @DatabaseField
    public String ttimg;
        @DatabaseField
        public long updatetime;



    }