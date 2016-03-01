package com.its.xfweacher.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.its.xfweacher.R;

public class NewsActivity extends Activity {
	private static final String TAG = "NewsActivity";
	private WebView webview = null;
	TextView txtTitle = null;
	private Intent mSourceIntent;
	String url = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsactivity);

		webview = (WebView)this.findViewById(R.id.webView);
		txtTitle =  (TextView)this.findViewById(R.id.txtTitle);
		url = getIntent().getStringExtra("url");
		if(TextUtils.isEmpty(url)){
			Toast.makeText(NewsActivity.this, "网址异常", Toast.LENGTH_LONG).show();
		}

		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setSupportZoom(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);

		webview.requestFocusFromTouch();



		webview.setWebChromeClient(new ReWebChomeClient());
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (url.indexOf("gettoken") > -1) {
					//runJS();
					view.loadUrl("javascript:onTokenSetup();");
				}
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
				}
				view.loadUrl(url);

				return true;
			}
		});
		//webview.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
		//webview.loadUrl("file:///android_asset/a.html");


		//synCookies(this,"http://hk.on168.net/");//设置cookies,注:这里一定要注意一点，在调用设置Cookie之后不能再设置
		webview.loadUrl(url);
		Log.e("WapActivity", "url====" +url);
	}

	public boolean onKeyDown(int keyCoder,KeyEvent event){
		if(keyCoder == KeyEvent.KEYCODE_BACK){
			if(webview.canGoBack()){
				webview.goBack();   //goBack()表示返回webView的上一页面
			}
			else
				NewsActivity.this.finish();
			return true;
		}
		return false;
	}

	public void goBack(View v) {
		if(webview.canGoBack()){
			webview.goBack();   //goBack()表示返回webView的上一页面
		}
		else
			NewsActivity.this.finish();
	}
	public void closePage(View v) {
		NewsActivity.this.finish();  //刷新
	}


	class ReWebChomeClient extends WebChromeClient {
		/**
		 * 会执行alert();
		 * @param view
		 * @param url
		 * @param message
		 * @param result
		 * @return
		 */
		@Override
		public boolean onJsAlert(WebView view, String url, String message,JsResult result)
		{
			return super.onJsAlert(view, url, message, result);
		}
		//public ReWebChomeClient(OpenFileChooserCallBack openFileChooserCallBack) {
		//    mOpenFileChooserCallBack = openFileChooserCallBack;
		//}
		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			txtTitle.setText(title);
		}
	}//end of class


}
