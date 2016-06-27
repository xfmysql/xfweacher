package com.its.xfweacher.helper.db;

import android.content.Context;

import com.its.xfweacher.entity.TodayWeacher;
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
public class TodayWeacherDao {
    private Context context;
    private Dao<TodayWeacher, Integer> dao;
    private DbHelper helper;
    public TodayWeacherDao(Context context) {
        this.context = context;
        try {
            helper = DbHelper.getHelper(context);
            dao = helper.getDao(TodayWeacher.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean create(TodayWeacher entity){
        try {
            return dao.create(entity) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean createOrUpdate(TodayWeacher entity){
        try {
            return dao.createOrUpdate(entity).getNumLinesChanged() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addAll(List<TodayWeacher> list){
        if(list == null || list.size() <= 0) return false;
        for (TodayWeacher albumInfo : list){
            try {
                dao.createOrUpdate(albumInfo);
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
    public boolean delete(TodayWeacher entity){
        try {

            return dao.delete(entity)>0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<TodayWeacher> get7TodayWeacher(){
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().ge("addtime", Integer.parseInt(DateUtils.Timestamp2String(new Date().getTime()/1000, "yyyyMMdd")));
            builder.limit(7);
            return dao.query(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public TodayWeacher getToday() {
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
