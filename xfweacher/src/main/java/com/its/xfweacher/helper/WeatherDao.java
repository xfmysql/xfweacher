package com.its.xfweacher.helper;

import android.content.Context;

import com.its.xfweacher.dbentity.WeatherItem;
import com.its.xfweacher.utils.DateUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ender on 2016/1/17.
 */
public class WeatherDao {
    private Context context;
    private Dao<WeatherItem, Integer> dao;
    private DbHelper helper;
    public WeatherDao(Context context) {
        this.context = context;
        try {
            helper = DbHelper.getHelper(context);
            dao = helper.getDao(WeatherItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean create(WeatherItem notice){
        try {
            return dao.create(notice) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean createOrUpdate(WeatherItem albumInfo){
        try {
            return dao.createOrUpdate(albumInfo).getNumLinesChanged() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addAll(List<WeatherItem> list){
        if(list == null || list.size() <= 0) return false;
        for (WeatherItem albumInfo : list){
            try {
                dao.create(albumInfo);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public boolean deleteAll(){
        try {
            DeleteBuilder builder = dao.deleteBuilder();
            return dao.delete(builder.prepare())>0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean delete(WeatherItem info){
        try {

            return dao.delete(info)>0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<WeatherItem> getAll(){
        try {
            QueryBuilder builder = dao.queryBuilder();
            return dao.query(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public WeatherItem getToday() {
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq("WeatherDate", Integer.parseInt(DateUtils.Timestamp2String(new Date().getTime()/1000, "yyyyMMdd")));
            return dao.queryForFirst(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
