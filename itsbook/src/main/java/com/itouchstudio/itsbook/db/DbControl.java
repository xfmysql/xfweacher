package com.itouchstudio.itsbook.db;

import android.content.Context;

/**
 * Created by Ender on 2016/1/17.
 */
public class DbControl {
    public static ChapterItemDao chapterItemDao;

    public static void Initial(Context context){

        chapterItemDao = new ChapterItemDao(context);
    }
}
