package com.its.xfweacher.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class NewsFragment extends Fragment  implements AdapterView.OnItemClickListener,View.OnClickListener {

	private RssFeed feed;
	Handler showHandler;
	public final String TAG = "NewsFragment";

	ListView listView;

	articleAdapter adapter = null;
	String RSS_URL = Constants.Techn_URL;
	//LayoutInflater inflater = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		//this.inflater = inflater;
		View view = inflater.inflate(R.layout.fragment_news, container,false);
		Button btnTechn = (Button)view.findViewById(R.id.btnTechn);
		btnTechn.setOnClickListener(this);
		Button btnInternet = (Button)view.findViewById(R.id.btnInternet);
		btnInternet.setOnClickListener(this);
		Button btnEnter = (Button)view.findViewById(R.id.btnEnter);
		btnEnter.setOnClickListener(this);
		Button btnSocia = (Button)view.findViewById(R.id.btnSocia);
		btnSocia.setOnClickListener(this);

		Button btnInter = (Button)view.findViewById(R.id.btnInter);
		btnInter.setOnClickListener(this);
		Button btnMil = (Button)view.findViewById(R.id.btnMil);
		btnMil.setOnClickListener(this);
		Button btnAuto = (Button)view.findViewById(R.id.btnAuto);
		btnAuto.setOnClickListener(this);
		Button btnHealth = (Button)view.findViewById(R.id.btnHealth);
		btnHealth.setOnClickListener(this);
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

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.btnTechn:
			{
				RSS_URL = Constants.Techn_URL;
				break;
			}
			case R.id.btnInternet:
			{
				RSS_URL = Constants.Internet_URL;
				break;
			}
			case R.id.btnEnter:
			{
				RSS_URL = Constants.Enter_URL;
				break;
			}
			case R.id.btnSocia:
			{
				RSS_URL = Constants.Socia_URL;
				break;
			}
			case R.id.btnInter:
			{
				RSS_URL = Constants.Inter_URL;
				break;
			}
			case R.id.btnMil:
			{
				RSS_URL = Constants.Mil_URL;
				break;
			}
			case R.id.btnAuto:
			{
				RSS_URL = Constants.Auto_URL;
				break;
			}
			case R.id.btnHealth:
			{
				RSS_URL = Constants.Health_URL;
				break;
			}
		}
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
