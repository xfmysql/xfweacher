package com.its.xfweacher.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.its.xfweacher.R;

public class Fragment2 extends Fragment {
	private static final String TAG = "Fragment2";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg2, container,false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getTokenCode();
	}

	void getTokenCode()
	{
		Ion.with(this.getActivity())
			.load("http://192.168.1.143:9080/oauth/oauth2-php/server/authorize.php")
					//.setHeader("Authorization", "Bearer " + t)
				.setBodyParameter("client_id", "lianx315")
				.setBodyParameter("password", "")
				.setBodyParameter("response_type", "code")
				.setBodyParameter("redirect_uri", "")
				.setBodyParameter("state", "c39af7d864dfac92478b56a5989f3102")
				.setBodyParameter("accept", "true")
				.asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String result) {
						try {
							Log.e(TAG, result.toString());
							getToken(result);
						} catch (Exception er) {
							er.printStackTrace();
						}
					}
				});
	}

	void getToken(String code){
		Ion.with(this.getActivity())
				.load("http://192.168.1.143:9080/oauth/oauth2-php/server/token.php")
						//.setHeader("Authorization", "Bearer " + t)
				.setBodyParameter("client_id", "lianx315")
				.setBodyParameter("client_secret", "123456")
				.setBodyParameter("code", code)
				.setBodyParameter("grant_type", "authorization_code")
				.setBodyParameter("redirect_uri", "1")
				.asJsonObject()
				.setCallback(new FutureCallback<JsonObject>() {
					@Override
					public void onCompleted(Exception e, JsonObject result) {
						try {
							Log.e(TAG, result.toString());
							getWeather(result.get("access_token").toString().replace("\"",""));
						} catch (Exception er) {
							er.printStackTrace();
						}
					}
				});
	}//end function


	void getWeather(String token){
		Ion.with(this.getActivity())
				.load("http://192.168.1.143:9080/oauth/oauth2-php/server/resource.php")
				//.setHeader("Authorization","Bearer "+token)
				.setBodyParameter("a", "a")
				.setBodyParameter("oauth_token",token)
				.asString()
				.setCallback(new FutureCallback<String>() {
					@Override
					public void onCompleted(Exception e, String result) {
						try {
							Log.e(TAG, result.toString());

						} catch (Exception er) {
							er.printStackTrace();
						}
					}
				});
	}//end function

}
