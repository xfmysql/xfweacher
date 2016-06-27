package com.its.xfweacher.helper.db;

import android.content.Context;

import com.its.xfweacher.entity.OnedayWeacher;
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
public class OneWeacherDao {
    private Context context;
    private Dao<OnedayWeacher, Integer> dao;
    private DbHelper helper;
    public OneWeacherDao(Context context) {
        this.context = context;
        try {
            helper = DbHelper.getHelper(context);
            dao = helper.getDao(OnedayWeacher.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean create(OnedayWeacher entity){
        try {
            return dao.create(entity) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean createOrUpdate(OnedayWeacher entity){
        try {
            return dao.createOrUpdate(entity).getNumLinesChanged() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addAll(List<OnedayWeacher> list){
        if(list == null || list.size() <= 0) return false;
        for (OnedayWeacher entity : list){
            try {
                dao.createOrUpdate(entity);
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
    public boolean delete(OnedayWeacher entity){
        try {

            return dao.delete(entity)>0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<OnedayWeacher> get7TodayWeacher(){
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().ge("AddTime", Integer.parseInt(DateUtils.Timestamp2String(new Date().getTime()/1000, "yyyyMMdd")));
            builder.limit(7);
            return dao.query(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public OnedayWeacher getToday() {
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
