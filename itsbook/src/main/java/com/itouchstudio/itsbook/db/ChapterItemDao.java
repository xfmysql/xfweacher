package com.itouchstudio.itsbook.db;

import android.content.Context;

import com.itouchstudio.itsbook.entity.ChapterItem;
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
public class ChapterItemDao {
    private Context context;
    private Dao<ChapterItem, Integer> dao;
    private DbHelper helper;
    public ChapterItemDao(Context context) {
        this.context = context;
        try {
            helper = DbHelper.getHelper(context);
            dao = helper.getDao(ChapterItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean create(ChapterItem notice){
        try {
            return dao.create(notice) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean createOrUpdate(ChapterItem albumInfo){
        try {
            return dao.createOrUpdate(albumInfo).getNumLinesChanged() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addAll(List<ChapterItem> list){
        if(list == null || list.size() <= 0) return false;
        for (ChapterItem albumInfo : list){
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
    public boolean delete(ChapterItem info){
        try {

            return dao.delete(info)>0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<ChapterItem> getList(){
        try {
            QueryBuilder builder = dao.queryBuilder();
            return dao.query(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }
}
