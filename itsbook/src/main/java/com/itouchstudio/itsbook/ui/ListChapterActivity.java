package com.itouchstudio.itsbook.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.itouchstudio.itsbook.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ListChapterActivity extends Activity  {
    final String TAG = ListChapterActivity.this.getClass().getName();
    ListView mListView;
    MyAdapter mListViewAdapter;
    int nextChapter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_chapter);

        mListView = (ListView) findViewById(R.id.listView);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/
        List<String> list = new ArrayList<>();
        list.add("第一张");
        list.add("第二张");
        list.add("第三张");

        mListViewAdapter = new MyAdapter(this,list);
        mListView.setAdapter(mListViewAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastItemIndex;//当前ListView中最后一个Item的索引
            //当ListView不在滚动，并且ListView的最后一项的索引等于adapter的项数减一时则自动加载（因为索引是从0开始的）
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.i(TAG, "onScrollStateChanged");
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mListViewAdapter.getCount() - 1) {
                    Log.e(TAG, "onScrollStateChanged");
                    //加载数据代码，此处省略了
                    nextChapter++;
                    new ReadingThread().start();

                }

            }
            //这三个int类型的参数可以自行Log打印一下就知道是什么意思了
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
                //ListView 的FooterView也会算到visibleItemCount中去，所以要再减去一
                lastItemIndex = firstVisibleItem + visibleItemCount - 1;
            }
        });
    }


    private class ReadingThread extends Thread {
        public void run() {
            AssetManager am = getAssets();
            InputStream response;
            try {
                Log.e(TAG,nextChapter+"第几章");
                response = am.open(nextChapter+".jpg");
                if (response != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i = -1;
                    while ((i = response.read()) != -1) {
                        baos.write(i);
                    }
                    String text = new String(baos.toByteArray(), "GBK");//UTF-8
                    mListViewAdapter.AddData(text);
                    handler.sendEmptyMessage(0);
                    baos.close();
                    response.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mListViewAdapter.notifyDataSetChanged();
        }
    };
    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局 /*构造函数*/
        List<String> listChapter = null;
        public MyAdapter(Context context,List<String> list) {
            this.mInflater = LayoutInflater.from(context);
            listChapter = list;
        }

        public void AddData(String chapter) {

            listChapter.add(chapter);
        }

        public final class ViewHolder {
            public TextView text;
        }

        @Override
        public int getCount() {
            return listChapter.size();
        }

        @Override
        public Object getItem(int position) {
            return listChapter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            String chapter = (String)listChapter.get(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.ui_list_chapter, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.textview);
                convertView.setTag(holder);//绑定ViewHolder对象
                holder.text.setText(chapter);//               }
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
                holder.text.setText(chapter);
            }
            return convertView;
        }
    }
}
