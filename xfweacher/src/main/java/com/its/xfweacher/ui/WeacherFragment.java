package com.its.xfweacher.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.its.xfweacher.R;
import com.its.xfweacher.helper.APIHelper;
import com.its.xfweacher.helper.GetTokenItf;
import com.its.xfweacher.helper.WeatherAsyncTask;
import com.its.xfweacher.json.entity.Result;
import com.its.xfweacher.json.entity.Weather;
import com.its.xfweacher.json.entity.Weather_data;
import com.its.xfweacher.utils.DateUtils;
import com.its.xfweacher.utils.SystemUtils;

import java.util.List;
import android.support.v4.widget.SwipeRefreshLayout;
public class WeacherFragment extends Fragment implements APIHelper.WeatherReflush,GetTokenItf {

	private static final String TAG = "WeacherFragment";
	private TextView txtLoaction,txtTime;
	private TextView txtWeacherName,txtTemperature;

	TextView txtContent;
	private ImageView ivpic11;
	private ImageView ivpic12;
	private TextView tvweek1;
	private TextView tvwea1;
	private TextView tvwind1;
	private TextView tvtemper1;

	private ImageView ivpic21;
	private ImageView ivpic22;
	private TextView tvweek2;
	private TextView tvwea2;
	private TextView tvwind2;
	private TextView tvtemper2;

	private ImageView ivpic31;
	private ImageView ivpic32;
	private TextView tvweek3;
	private TextView tvwea3;
	private TextView tvwind3;
	private TextView tvtemper3;
	SwipeRefreshLayout mSwipeRefreshLayout;
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

		txtLoaction = (TextView) view.findViewById(R.id.txtLoaction);
		txtTime = (TextView) view.findViewById(R.id.txtTime);

		txtWeacherName = (TextView) view.findViewById(R.id.txtWeacherName);
		txtTemperature = (TextView) view.findViewById(R.id.txtTemperature);

		txtContent = (TextView) view.findViewById(R.id.txtContent);


		// �ڶ�������
		ivpic21 = (ImageView) view.findViewById(R.id.ivpic21);
		ivpic22 = (ImageView) view.findViewById(R.id.ivpic22);

		tvweek2 = (TextView) view.findViewById(R.id.tvweek2);
		tvwea2 = (TextView) view.findViewById(R.id.tvwea2);
		tvwind2 = (TextView) view.findViewById(R.id.tvwind2);
		tvtemper2 = (TextView) view.findViewById(R.id.tvtemper2);

		// ���������
		ivpic31 = (ImageView) view.findViewById(R.id.ivpic31);
		ivpic32 = (ImageView) view.findViewById(R.id.ivpic32);

		tvweek3 = (TextView) view.findViewById(R.id.tvweek3);
		tvwea3 = (TextView) view.findViewById(R.id.tvwea3);
		tvwind3 = (TextView) view.findViewById(R.id.tvwind3);
		tvtemper3 = (TextView) view.findViewById(R.id.tvtemper3);

		// ��һ������
		ivpic11 = (ImageView) view.findViewById(R.id.ivpic11);
		ivpic12 = (ImageView) view.findViewById(R.id.ivpic12);

		tvweek1 = (TextView) view.findViewById(R.id.tvweek1);
		tvwea1 = (TextView) view.findViewById(R.id.tvwea1);
		tvwind1 = (TextView) view.findViewById(R.id.tvwind1);
		tvtemper1 = (TextView) view.findViewById(R.id.tvtemper1);

		mSwipeRefreshLayout.setRefreshing(true);
		mSwipeRefreshLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}, 3000);
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

		APIHelper.setWeatherReflush(this);//回调
		String token = SystemUtils.getToken();
		if(!TextUtils.isEmpty(token)) {
			APIHelper.getWeather(token);
		}
	}

	@Override
	public void onResume(){
		APIHelper.setWeatherReflush(this);
		super.onResume();
	}
	@Override
	public void onReflush(List<com.its.xfweacher.entity.Weather> pList) {
		for(com.its.xfweacher.entity.Weather w :pList)
			Log.e(TAG,w.getWeatherdate()+","+w.getPm25()+","+w.getTemperature()+","+w.getWeatherstr()+","+w.getWind()+"\n\r");

		com.its.xfweacher.entity.Weather w = pList.get(0);
		txtLoaction.setText("");
		String date = DateUtils.Timestamp2String(w.getAddtime());
		txtTime.setText(date);

		//
		txtWeacherName.setText(w.getWeatherstr());
		txtTemperature.setText(w.getTemperature());
		txtContent.setText("PM2.5:" + w.getPm25());


		//ivpic11.setImageBitmap(w.get);
		//ivpic12.setImageBitmap(wa.getNightPicture());
		tvweek1.setText(w.getWeatherdate());
		tvwea1.setText(w.getWeatherstr());
		tvwind1.setText(w.getWind());
		tvtemper1.setText(w.getTemperature());

		w = pList.get(1);
		tvtemper2.setText(w.getTemperature());
		//ivpic21.setImageBitmap(w.getDayPicture());
		//ivpic22.setImageBitmap(w.getNightPicture());
		tvweek2.setText(w.getWeatherdate());
		tvwea2.setText(w.getWeatherstr());
		tvwind2.setText(w.getWind());
		tvtemper2.setText(w.getTemperature());

		w = pList.get(2);
		tvtemper3.setText(w.getTemperature());
		//ivpic31.setImageBitmap(wa.getDayPicture());
		//ivpic32.setImageBitmap(wa.getNightPicture());
		tvweek3.setText(w.getWeatherdate());
		tvwea3.setText(w.getWeatherstr());
		tvwind3.setText(w.getWind());
		tvtemper3.setText(w.getTemperature());

	}

	@Override
	public void AfterGet() {
		String token = SystemUtils.getToken();
		if(!TextUtils.isEmpty(token)) {
			APIHelper.getWeather(token);
		}
	}
}
