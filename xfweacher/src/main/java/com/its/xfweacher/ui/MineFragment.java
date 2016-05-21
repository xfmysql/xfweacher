package com.its.xfweacher.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.its.xfweacher.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class MineFragment extends Fragment {

	TextView txtDeviceID;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine, container,false);

		txtDeviceID = (TextView)view.findViewById(R.id.txtDeviceID);
		txtDeviceID.setText(getCPUSerial());
		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	/**
	 * 获取CPU序列号
	 *
	 * @return CPU序列号(16位)
	 * 读取失败为"0000000000000000"
	 */
	public static String getCPUSerial() {
		String str = "", strCPU = "", cpuAddress = "0000000000000000";
		try {
			//读取CPU信息
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			//查找CPU序列号
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				Log.e("",str);
				if (str != null) {
					//查找到序列号所在行
					if (str.indexOf("Serial") > -1) {
						//提取序列号
						strCPU = str.substring(str.indexOf(":") + 1,str.length());
						//去空格
						cpuAddress = strCPU.trim();
						break;
					}
				}else{
					//文件结尾
					break;
				}
			}
		} catch (IOException ex) {
			//赋予默认值
			ex.printStackTrace();
		}
		return cpuAddress;
	}
}
