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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.itouchstudio.itsbook.R;
import com.itouchstudio.itsbook.entity.Segment;
import com.itouchstudio.itsbook.util.PreferencesHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 一次载入3章
 * 如果有一页，要加入
 * 如果有下一页，要加入
 */
public class ListChapterActivity extends Activity implements Observer {
    final String TAG = ListChapterActivity.this.getClass().getName();
    ListView mListView;
    MyAdapter mListViewAdapter;
    int nextChapter = 1;

    public static final String readNumberKey = "readnumber";
    public static final String readChaperKey = "readchapter";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_chapter);

        mListView = (ListView) findViewById(R.id.listView);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/

        mListViewAdapter = new MyAdapter(this,null);
        mListView.setAdapter(mListViewAdapter);
        //region setlistener
        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Segment s = (Segment) mListViewAdapter.getItem(position);
                Log.e(TAG, position + "- - - - -- " + s.Position + "- - - - -- " + s.SegmentText.substring(0, 5));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Segment s = (Segment) mListViewAdapter.getItem(position);
                Log.e(TAG, position + "======" + s.Position + "=====" + s.SegmentText.substring(0, 5));
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastItemIndex;//当前ListView中最后一个Item的索引

            //当ListView不在滚动，并且ListView的最后一项的索引等于adapter的项数减一时则自动加载（因为索引是从0开始的）
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mListViewAdapter.getCount() - 1) {
                    Log.e(TAG, "onScrollStateChanged");
                    //加载数据代码，此处省略了
                    nextChapter++;
                    //new ReadingThread().start();

                }
                // 不滚动时保存当前滚动到的位置
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    int mPosition = mListView.getFirstVisiblePosition();

                    Segment s = (Segment) mListViewAdapter.getItem(mPosition);
                    Log.e(TAG, mPosition + "======" + s.Position + "=====" + s.SegmentText.substring(0, 5));

                    PreferencesHelper.putIntValue(readNumberKey, s.Position);
                    PreferencesHelper.putIntValue(readChaperKey, nextChapter);

                }
            }

            //这三个int类型的参数可以自行Log打印一下就知道是什么意思了
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //ListView 的FooterView也会算到visibleItemCount中去，所以要再减去一
                lastItemIndex = firstVisibleItem + visibleItemCount - 1;
            }
        });
        //endregion
        int hadChapter = PreferencesHelper.GetInt(readChaperKey);
        if(hadChapter>0){
            nextChapter = hadChapter;
        }
        LoadChapterObservable loadChapterObservable = new LoadChapterObservable(this);
        loadChapterObservable.addObserver(this);//add server
        loadChapterObservable.LoadData();
    }

    @Override
    public void update(Observable observable, Object o) {
        Log.e(TAG,"Data has changed to" + ((LoadChapterObservable)observable).getChapter().length());
        handler.sendEmptyMessage(0);
    }




    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mListViewAdapter.notifyDataSetChanged();
        }
    };
    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局 /*构造函数*/
        List<Segment> listChapter = null;
        public MyAdapter(Context context,List<Segment> list) {
            this.mInflater = LayoutInflater.from(context);
            listChapter = list;
        }

        public void AddData(Segment chapter) {
            if(listChapter!=null)
                listChapter.add(chapter);
            else {
                listChapter = new ArrayList<>();
                listChapter.add(chapter);
            }
        }
        public void AddList(List<Segment> segmentList) {
            if(listChapter!=null)
                listChapter.addAll(segmentList);
            else {
                listChapter = new ArrayList<>();
                listChapter.addAll(segmentList);
            }
        }
        public void AddListPre(List<Segment> segmentList) {
            if(listChapter!=null)
                listChapter.addAll(0,segmentList);
            else {
                listChapter = new ArrayList<>();
                listChapter.addAll(0,segmentList);
            }
        }
        public final class ViewHolder {
            public TextView text;
        }

        @Override
        public int getCount() {
            if(listChapter!=null)
                return listChapter.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(listChapter!=null)
                return listChapter.get(position);
            return null;
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
            Segment chapter = (Segment)listChapter.get(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.ui_list_chapter, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.textview);
                convertView.setTag(holder);//绑定ViewHolder对象
                holder.text.setText(chapter.SegmentText);//               }
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
                holder.text.setText(chapter.SegmentText);
            }
            return convertView;
        }
    }
}
