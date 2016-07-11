package com.itouchstudio.itsbook.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.itouchstudio.itsbook.R;
import com.itouchstudio.itsbook.entity.Segment;
import com.itouchstudio.itsbook.util.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 一次载入3章，分成很多段，用来定位到上次阅读的段
 * 如果是第一段，要提前读入上一章
 * 如果有最后一段，提前读入下一章
 */
public class ChapterActivity extends Activity implements Observer {
    final String TAG = ChapterActivity.this.getClass().getName();
    public static final String readNumberKey = "readnumber";
    public static final String readChaperKey = "readchapter";
    public static final String TXT_FileName = "filename";

    ListView mListView;
    MyAdapter mListViewAdapter;
    int nextChapter = 1;
    String threeChapterText = "";
    LoadChapterObservable loadChapterObservable = null;
    boolean needSelect = true;
    int maxChapter = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chapter);

        mListView = (ListView) findViewById(R.id.listView);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/

        mListViewAdapter = new MyAdapter(this,null);
        mListView.setAdapter(mListViewAdapter);
        //region setlistener
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastItemIndex;//当前ListView中最后一个Item的索引

            //当ListView不在滚动，并且ListView的最后一项的索引等于adapter的项数减一时则自动加载（因为索引是从0开始的）
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mListViewAdapter.getCount() - 1) {
                    Log.e(TAG, "onScrollStateChanged");
                    if(nextChapter<maxChapter-1) {
                        nextChapter += 1;
                        PreferencesHelper.putIntValue(readChaperKey, nextChapter);
                        loadChapterObservable.LoadData();
                    }

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
            private int mListViewFirstItem = 0;
            //listView中第一项的在屏幕中的位置
            private int mScreenY = 0;
            //是否向上滚动
            private boolean mIsScrollToUp = false;
            //这三个int类型的参数可以自行Log打印一下就知道是什么意思了
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //ListView 的FooterView也会算到visibleItemCount中去，所以要再减去一
                lastItemIndex = firstVisibleItem + visibleItemCount - 1;
                Log.e("onScroll", "firstVisibleItem= " + firstVisibleItem + "=" + visibleItemCount + "=" + totalItemCount);
                //region 判断方向
                if(mListView.getChildCount()>0)
                {

                    boolean isScrollToUp = false;
                    View childAt = mListView.getChildAt(firstVisibleItem);
                    if(childAt==null) return;
                    int[] location = new int[2];
                    childAt.getLocationOnScreen(location);
                    Log.d("onScroll", "firstVisibleItem= "+firstVisibleItem+" , y="+location[1]);

                    if(firstVisibleItem!=mListViewFirstItem)
                    {
                        if(firstVisibleItem>mListViewFirstItem)
                        {
                            Log.e("--->", "向上滑动");
                            isScrollToUp = true;
                        }else{
                            Log.e("--->", "向下滑动");
                            isScrollToUp = false;
                        }
                        mListViewFirstItem = firstVisibleItem;
                        mScreenY = location[1];
                    }else{
                        if(mScreenY>location[1])
                        {
                            Log.i("--->", "->向上滑动");
                            isScrollToUp = true;
                        }
                        else if(mScreenY<location[1])
                        {
                            Log.i("--->", "->向下滑动");
                            isScrollToUp = false;
                        }
                        mScreenY = location[1];
                    }

                    if(mIsScrollToUp!=isScrollToUp)
                    {
                        //onScrollDirectionChanged(mIsScrollToUp);
                    }
                }
                //end region
            }
        });
        //endregion

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {
            String filename = bundle.getString(TXT_FileName);
            if (TextUtils.isEmpty(filename)) {
                int hadChapter = PreferencesHelper.GetInt(readChaperKey);
                if (hadChapter > 0) {
                    nextChapter = hadChapter;
                }
            } else {
                nextChapter = Integer.parseInt(filename);
                Log.e(TAG, " you select file : " + nextChapter);
                PreferencesHelper.putIntValue(readChaperKey, nextChapter);
                PreferencesHelper.putIntValue(readNumberKey, 0);
                needSelect = false;
            }
        }else {
            int hadChapter = PreferencesHelper.GetInt(readChaperKey);
            if (hadChapter > 0) {
                nextChapter = hadChapter;
            }
        }
        loadChapterObservable = new LoadChapterObservable(this);
        loadChapterObservable.addObserver(this);//add server
        loadChapterObservable.LoadData();

        Button btnList=(Button)findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChapterActivity.this,ChapterListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void update(Observable observable, Object o) {
        Log.e(TAG,"Data has changed ");
        threeChapterText = ((LoadChapterObservable)observable).getChapter();
        handler.sendEmptyMessage(0);
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode==KeyEvent.KEYCODE_BACK){
//            new Intent(ChapterActivity.this,ChapterListActivity.class);
//            finish();
//        }
//        return false;
//    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //threeChapterText
            List<Segment> segmentList = new ArrayList<Segment>();
            int length = 500;
            int position = 0;

            while(length<threeChapterText.length()){
                Segment s = new Segment(position,position+"@"+threeChapterText.substring(0,length));
                segmentList.add(s);
                threeChapterText = threeChapterText.substring(length,threeChapterText.length());
                position++;
            }
            Segment s = new Segment(position,"最后 一段----------------\n"+threeChapterText);//加入最后一段
            segmentList.add(s);
            mListViewAdapter.addList(segmentList);

            mListViewAdapter.notifyDataSetChanged();

            if(needSelect) { //打开app，定位到上次的位置
                int readSegment = PreferencesHelper.GetInt(readNumberKey);
                mListView.setSelection(readSegment);
                needSelect = false;
            }
        }
    };
    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局 /*构造函数*/
        List<Segment> listChapter = null;
        public MyAdapter(Context context,List<Segment> list) {
            this.mInflater = LayoutInflater.from(context);
            listChapter = list;
        }
        public void setList(List<Segment> segmentList) {
            listChapter = segmentList;
        }
        public void addList(List<Segment> segmentList) {
            if(listChapter!=null)
                listChapter.addAll(segmentList);
            else{
                listChapter = segmentList;
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
