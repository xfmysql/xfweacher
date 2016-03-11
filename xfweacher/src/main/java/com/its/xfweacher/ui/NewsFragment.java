package com.its.xfweacher.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.its.xfweacher.R;
import com.its.xfweacher.helper.APIHelper;
import com.its.xfweacher.json.entity.RssFeed;
import com.its.xfweacher.json.entity.RssItem;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class NewsFragment extends Fragment  implements AdapterView.OnItemClickListener {

	public final String RSS_URL = "http://news.baidu.com/n?cmd=1&class=mil&tn=rss";
	private RssFeed feed;
	Handler showHandler;
	public final String TAG = "NewsFragment";

	ListView listView;

	articleAdapter adapter = null;
	//LayoutInflater inflater = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		//this.inflater = inflater;
		View view = inflater.inflate(R.layout.fragment_news, container,false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		listView = (ListView) getActivity().findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		adapter = new articleAdapter(NewsFragment.this.getActivity(), new ArrayList<RssItem>());
		listView.setAdapter(adapter);

		showHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch(msg.what) {
					case 1:
					{
						List<RssItem> list = feed.getAllItems();
						adapter.setData(list);
						break;
					}
				}
				super.handleMessage(msg);
			}
		};
		new Thread(downloadImage).start();
	}

	Runnable downloadImage = new Runnable() {
		public void run() {
			try {
				feed = APIHelper.getFeed(RSS_URL);
				showHandler.sendEmptyMessage(0x1);
				return;
			} catch(ParserConfigurationException e) {
				e.printStackTrace();
				return;
			} catch(SAXException e) {
				e.printStackTrace();
				return;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	};
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Intent intent = new Intent();
		intent.setClass(this.getActivity(), NewsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("title", feed.getItem(position).getTitle());
		bundle.putString("description", feed.getItem(position).getDescription());
		bundle.putString("link", feed.getItem(position).getLink());
		bundle.putString("pubdate", feed.getItem(position).getPubdate());
		intent.putExtra("android.intent.extra.rssItem", bundle);
		intent.putExtra("url",feed.getItem(position).getLink());
		startActivityForResult(intent, 0x0);
	}

	public void onReflush(View v){
		new Thread(downloadImage).start();
	}

	class articleAdapter extends BaseAdapter {
		private List<RssItem> list = null;
		private Context mContext;

		public articleAdapter(Context mContext, List<RssItem> list) {
			this.mContext = mContext;
			this.list = list;
		}
		public void setData(List<RssItem> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public int getCount() {
			return this.list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View view, ViewGroup arg2) {

			RssItem rp = list.get(position);
			view = LayoutInflater.from(mContext).inflate(R.layout.uc_listitem, null);

			ImageView imgNew = (ImageView) view.findViewById(R.id.imgNew);
			TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
			TextView txtDescript = (TextView) view.findViewById(R.id.txtDescript);

			txtTitle.setText(rp.getTitle());
			txtDescript.setText(rp.getDescription());

			return view;
		}
	}
}
