package com.its.xfweacher.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.its.xfweacher.R;
import com.its.xfweacher.helper.WeatherAsyncTask;
import com.its.xfweacher.json.entity.Result;
import com.its.xfweacher.json.entity.Weather;
import com.its.xfweacher.json.entity.Weather_data;

public class WeacherFragment extends Fragment implements WeatherAsyncTask.ShowWeacherCallback {


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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_weacher, container,false);

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


		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WeatherAsyncTask.setWeacherCallback(this);


		try {
			new WeatherAsyncTask(this.getActivity()).execute("绍兴");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onShowWeacher(Weather result) {
		Result res = result.getResults().get(0);
        txtLoaction.setText(res.getCurrentCity());
        txtTime.setText(result.getDate());

        Weather_data wa = res.getWeather_data().get(0);
        Log.e("Result", "" + res.toString());
        Log.e("Weather_data", "" + wa.toString());
        //
        txtWeacherName.setText(wa.getWeather());
        String str = wa.getDate();
        txtTemperature.setText(str.substring(14, str.length()-1));
        String pm2_5 = "".equals(res.getPm25()) ? "75" : res.getPm25();
        txtContent.setText("PM2.5:" + pm2_5);


        // Ӧ��Ϊ�������ϻ�ȡ����
//			ivpic11.setImageResource(R.drawable.d00);
//			ivpic12.setImageResource(R.drawable.d01);
        ivpic11.setImageBitmap(wa.getDayPicture());
        ivpic12.setImageBitmap(wa.getNightPicture());

        tvweek1.setText(str.substring(0, 2));
        tvwea1.setText(wa.getWeather());
        tvwind1.setText(wa.getWind());
        tvtemper1.setText(wa.getTemperature());

        wa = res.getWeather_data().get(1);
        // System.out.println(wa2);

        tvtemper2.setText(wa.getTemperature());
        ivpic21.setImageBitmap(wa.getDayPicture());
        ivpic22.setImageBitmap(wa.getNightPicture());
        tvweek2.setText(wa.getDate());
        tvwea2.setText(wa.getWeather());
        tvwind2.setText(wa.getWind());
        tvtemper2.setText(wa.getTemperature());

        wa = res.getWeather_data().get(2);

        // System.out.println(wa4);
        ivpic31.setImageBitmap(wa.getDayPicture());
        ivpic32.setImageBitmap(wa.getNightPicture());
        tvweek3.setText(wa.getDate());
        tvwea3.setText(wa.getWeather());
        tvwind3.setText(wa.getWind());
        tvtemper3.setText(wa.getTemperature());
	}
}
