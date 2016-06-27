package com.itouchstudio.itsbook.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.itouchstudio.itsbook.util.PreferencesHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;

/**
 * Created by its on 2016-06-21.
 */
public class LoadChapterObservable extends Observable {
    private int data = 0;
    private Context mContext;
    String chapterPre = "",chapter="",chapterNext="";
    public String getChapter(){
        return chapterPre+"\n"+chapter+"\n"+chapterNext;
    }

    public void LoadData(){
        int hadChapter = PreferencesHelper.GetInt(ChapterActivity.readChaperKey);
        new ReadingThread(hadChapter).start();
    }

    public LoadChapterObservable(Context context){
        mContext = context;
    }
    private class ReadingThread extends Thread {
        private int hadChapter = 1;
        public ReadingThread(int chapter){
            hadChapter = chapter>1?chapter:1;
        }
        public void run() {
            if(hadChapter>1)
                chapterPre = read(hadChapter-1);
            chapter = read(hadChapter);
            chapterNext = read(hadChapter+1);

            setChanged();
            //只有在setChange()被调用后，notifyObservers()才会去调用update()，否则什么都不干。
            notifyObservers();
        }

        String read(int chp)
        {
            String _chapter="";
            AssetManager am = mContext.getAssets();
            InputStream response;
            try {
                response = am.open(chp+".txt");
                if (response != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i = -1;
                    while ((i = response.read()) != -1) {
                        baos.write(i);
                    }
                    _chapter = new String(baos.toByteArray(), "UTF-8");//UTF-8
                    Log.e("LoadChapterObservable", "第 " + chp + " 章： " + _chapter.length());
                    baos.close();
                    response.close();
                    return _chapter;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
