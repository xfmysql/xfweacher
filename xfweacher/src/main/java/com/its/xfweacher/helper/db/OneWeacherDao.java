package com.its.xfweacher.helper.db;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import com.its.xfweacher.entity.OnedayWeacher;
import com.its.xfweacher.utils.DateUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ender on 2016/1/17.
 */
public class OneWeacherDao {
    private Context context;
    private Dao<OnedayWeacher, Integer> dao;
    private DbHelper helper;
    private static final String TAG = "OneWeacherDao";
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


    public String get7TodayTimes(){
        try {
            String sql = "select max(addtime) from OnedayWeacher  group by weatherdate order by addtime desc limit 7" ;
            GenericRawResults<OnedayWeacher> results = dao.queryRaw(sql, new RawRowMapper<OnedayWeacher>() {
                @Override
                public OnedayWeacher mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    OnedayWeacher entity = new OnedayWeacher();
                    entity.addtime = Long.parseLong(resultColumns[0]);
                    return entity;
                }
            });
            List<OnedayWeacher> dictionaryList = new ArrayList<OnedayWeacher>();
            Iterator<OnedayWeacher> iterator = results.iterator();
            while (iterator.hasNext()) {
                OnedayWeacher dictionary = iterator.next();
                dictionaryList.add(dictionary);
            }
            String sqlStr = "";
            for(OnedayWeacher one : dictionaryList)
            {
                sqlStr+=one.addtime+",";
            }


            return sqlStr;
            //return dao.query(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<OnedayWeacher> get7TodayWeacher(){
        try {
            String sql = "select id,weatherdate,weathertxt,tmpheight,tmplow,winddir,windspd,pm25" +
                    " from onedayweacher where addtime in (select max(addtime) from OnedayWeacher " +
                    //" addtime<"+ //最近一周
                    "  group by weatherdate order by addtime desc limit 7)" ;
            String times = get7TodayTimes();
            sql = "select distinct 0,weatherdate,weathertxt,tmpheight,tmplow,winddir,windspd,pm25,00000000 " +
                    " from onedayweacher where addtime in ("+times+"1) order by addtime asc  limit 7";
            GenericRawResults<OnedayWeacher> results = dao.queryRaw(sql, new RawRowMapper<OnedayWeacher>() {
                @Override
                public OnedayWeacher mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    OnedayWeacher entity = new OnedayWeacher();
                    entity.id = Integer.parseInt(resultColumns[0]);
                    entity.weatherdate = resultColumns[1];
                    entity.weathertxt = resultColumns[2];
                    entity.tmpheight = resultColumns[3];
                    entity.tmplow = resultColumns[4];
                    entity.winddir = resultColumns[5];
                    entity.windspd = resultColumns[6];
                    entity.pm25 = resultColumns[7];
                    entity.addtime = Long.parseLong(resultColumns[8]);
                    return entity;
                }
            });
            List<OnedayWeacher> dictionaryList = new ArrayList<OnedayWeacher>();
            Iterator<OnedayWeacher> iterator = results.iterator();
            while (iterator.hasNext()) {
                OnedayWeacher dictionary = iterator.next();
                dictionaryList.add(dictionary);
            }
            Log.e(TAG,"weather size:"+dictionaryList.size());
            return dictionaryList;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public OnedayWeacher getToday() {
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq("weatherdate", DateUtils.Timestamp2String(new Date(), "yyyy-MM-dd"));
            return dao.queryForFirst(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
