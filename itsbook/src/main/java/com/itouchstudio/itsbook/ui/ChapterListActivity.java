package com.itouchstudio.itsbook.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.itouchstudio.itsbook.R;
import com.itouchstudio.itsbook.db.ChapterItemDao;
import com.itouchstudio.itsbook.db.DbControl;
import com.itouchstudio.itsbook.db.DbHelper;
import com.itouchstudio.itsbook.entity.ChapterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 一次载入3章
 * 如果有一页，要加入
 * 如果有下一页，要加入
 */
public class ChapterListActivity extends Activity {
    final String TAG = ChapterListActivity.this.getClass().getName();
    ListView mListView;
    MyAdapter mListViewAdapter;
   Button btnInstall;
    int i = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chapterlist);
        mListView = (ListView) findViewById(R.id.listView);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/
        List<ChapterItem> _list =  DbControl.chapterItemDao.getList();
        mListViewAdapter = new MyAdapter(this,_list);
        mListView.setAdapter(mListViewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ChapterItem i =(ChapterItem)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(ChapterListActivity.this,ChapterActivity.class);
                Bundle bundle=new Bundle();
                //传递name参数为tinyphp
                bundle.putString(ChapterActivity.TXT_FileName,i.fileName);
                intent.putExtras(bundle);
                startActivity(intent);
                Log.i("my", "onItemClick clicked" + i.fileName);
            }
        });

        btnInstall=(Button)findViewById(R.id.btnInstall);
        btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<ChapterItem> list = new ArrayList<ChapterItem>();
                ChapterItem ci = new ChapterItem();
                ci.fileName = ""+i;
                ci.title = "第"+i+"章";
                list.add(ci);
                i++;
                DbControl.chapterItemDao.addAll(list);
                List<ChapterItem> _list = DbControl.chapterItemDao.getList();
                mListViewAdapter.setList(_list);
                mListViewAdapter.notifyDataSetChanged();
            }
        });
    }


    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局 /*构造函数*/
        List<ChapterItem> listChapter = null;
        public MyAdapter(Context context,List<ChapterItem> list) {
            this.mInflater = LayoutInflater.from(context);
            if(list!=null)
                listChapter = list;
        }
        public void setList(List<ChapterItem> segmentList) {
            listChapter = segmentList;
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
            ChapterItem chapter = (ChapterItem)listChapter.get(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.ui_chapterlist_item, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.textview);
                convertView.setTag(holder);//绑定ViewHolder对象
                holder.text.setText(chapter.title);//               }
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
                holder.text.setText(chapter.title);
            }
            return convertView;
        }
    }
}
