package com.its.xfweacher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.its.xfweacher.R;
import com.its.xfweacher.api.APIHelper;
import com.its.xfweacher.entity.OnedayWeacher;
import com.its.xfweacher.entity.TodayWeacher;
import com.its.xfweacher.entity.Weather;
import com.its.xfweacher.helper.db.DbControl;
import com.its.xfweacher.helper.db.GetTokenItf;
import com.its.xfweacher.ui.uc.HorizontalListView;
import com.its.xfweacher.utils.DateUtils;
import com.its.xfweacher.utils.SystemUtils;

import java.util.List;
import android.support.v4.widget.SwipeRefreshLayout;
public class WeacherFragment extends Fragment implements APIHelper.WeatherReflush,GetTokenItf {

	private static final String TAG = "WeacherFragment";
	//listview
	HorizontalListView  mListView;
	MyAdapter mListViewAdapter;
	//oneday
	private TextView txtLoaction,txtTime,txtWeacherName,txtTemperature,txtContent;

	SwipeRefreshLayout mSwipeRefreshLayout;

	/**
	 * 先调用onCreate，在调用这里
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_weacher, container,false);

		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {

				String reflushTime = SystemUtils.getTokenReflushTime();
				if ((System.currentTimeMillis() / 1000 - Long.parseLong(reflushTime)) > 3600) {
					APIHelper.getTokenCode(WeacherFragment.this);
				}else {
					String token = SystemUtils.getToken();
					if(!TextUtils.isEmpty(token)) {
						APIHelper.getWeather(token);
					}
				}

				mSwipeRefreshLayout.setRefreshing(true);
				mSwipeRefreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						mSwipeRefreshLayout.setRefreshing(false);
					}
				}, 3000);
			}
		});

		mListView = (HorizontalListView) view.findViewById(R.id.listView);
		mListViewAdapter = new MyAdapter(this.getContext(),null);
		mListView.setAdapter(mListViewAdapter);

		txtLoaction = (TextView) view.findViewById(R.id.txtLoaction);
		txtTime = (TextView) view.findViewById(R.id.txtTime);
		txtWeacherName = (TextView) view.findViewById(R.id.txtWeacherName);
		txtTemperature = (TextView) view.findViewById(R.id.txtTemperature);
		txtContent = (TextView) view.findViewById(R.id.txtContent);
		TodayWeacher _entity = DbControl.todayWeacherDao.getToday();
		if(_entity!=null) {
			String date = DateUtils.Timestamp2String(_entity.AddTime, "yyyy-MM-dd");
			txtTime.setText(date);
			txtWeacherName.setText(_entity.WeatherStr);
			txtTemperature.setText(_entity.Temperature);
			txtContent.setText("PM2.5:" + _entity.PM25);
		}

		mSwipeRefreshLayout.setRefreshing(true);
		mSwipeRefreshLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}, 3000);

		APIHelper.setWeatherReflush(this);//回调,要在创建ui之后，
		onReflush();
		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {


			//new WeatherAsyncTask(this.getActivity()).execute("绍兴");
			//WeatherAsyncTask.setWeacherCallback(this);
		}catch (Exception e){
			e.printStackTrace();
		}


	}

	@Override
	public void onResume(){
		APIHelper.setWeatherReflush(this);
		super.onResume();
	}
	@Override
	public void onReflush() {
		List<OnedayWeacher> _list = DbControl.oneWeacherDao.get7TodayWeacher();
		if(_list!=null) {
			mListViewAdapter.setList(_list);
			mListViewAdapter.notifyDataSetChanged();
			//debug
			for(OnedayWeacher w : _list) {
				Log.e(TAG, w.WeatherStr + "," + w.PM25 + "," + w.Temperature + "," + w.Wind+ "," + w.WindSpeed + "\n\r");
			}
		}

	}

	@Override
	public void AfterGet() {
		String token = SystemUtils.getToken();
		if(!TextUtils.isEmpty(token)) {
			APIHelper.getWeather(token);
		}
	}


	private class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局 /*构造函数*/
		List<OnedayWeacher> listWeacher = null;
		public MyAdapter(Context context,List<OnedayWeacher> list) {
			this.mInflater = LayoutInflater.from(context);
			listWeacher = list;
		}
		public void setList(List<OnedayWeacher> list) {
			listWeacher = list;
		}

		public final class ViewHolder {
			public TextView txtWeek;
		}

		@Override
		public int getCount() {
			if(listWeacher!=null)
				return listWeacher.size();
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if(listWeacher!=null)
				return listWeacher.get(position);
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
			OnedayWeacher onedayWeacher = (OnedayWeacher)listWeacher.get(position);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.uc_li_oneday, null);
				holder = new ViewHolder();
				holder.txtWeek = (TextView) convertView.findViewById(R.id.txtWeek);
				convertView.setTag(holder);//绑定ViewHolder对象           }
			} else {
				holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
			}

			holder.txtWeek.setText(onedayWeacher.WeatherStr);
			//ivpic11.setImageBitmap(w.get);
			//ivpic12.setImageBitmap(wa.getNightPicture());
//			tvweek1.setText(w.getWeatherdate());
//			tvwea1.setText(w.getWeatherstr());
//			tvwind1.setText(w.getWind());
//			tvtemper1.setText(w.getTemperature());


			return convertView;
		}
	}
}
