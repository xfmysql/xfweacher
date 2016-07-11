package com.itouchstudio.itsbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.itouchstudio.itsbook.entity.ChapterItem;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ender on 2015/11/26.
 */
public class DbHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "itsbook.db";
    private static final int DBVersion = 1;
    private Map<String, Dao> daos = new HashMap<String, Dao>();

    private DbHelper(Context context)
    {
        super(context, TABLE_NAME, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try
        {
            TableUtils.createTable(connectionSource, ChapterItem.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if(oldVersion == 4 && newVersion == 5){
            //从版本1到版本2时，增加了一个字段 desc
            String sql = "alter table schoolinfo add [schoolname] varchar";
            database.execSQL(sql);
        }
    }

    /**
     * 重置
     */
    public void init() {
        try {
            TableUtils.dropTable(connectionSource, ChapterItem.class, true);
            onCreate(this.getWritableDatabase(), connectionSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DbHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DbHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null)
        {
            synchronized (DbHelper.class)
            {
                if (instance == null)
                    instance = new DbHelper(context);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className))
        {
            dao = daos.get(className);
        }
        if (dao == null)
        {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet())
        {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
