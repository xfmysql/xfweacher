package com.itouchstudio.itsbook.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.itouchstudio.itsbook.R;
import com.itouchstudio.itsbook.entity.NextPage;
import com.itouchstudio.itsbook.util.PreferencesHelper;
import com.itouchstudio.itsbook.view.FlipperLayout;

public class MainActivity extends Activity implements OnClickListener, FlipperLayout.TouchListener {
	private static final String TAG = "MainActivity";
	private String text = "";
	private int textLenght = 0;



	//本页开始
	private int currentTopEndIndex = 0;

	//下一页结束位置
	private int nextEndIndex = 0;

	private static final String readNumberKey = "readnumber";
	private static final String readChaperKey = "readchapter";

	NextPage nextpage = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			//打开的时候默认读取
			FlipperLayout rootLayout = (FlipperLayout) findViewById(R.id.container);
			View recoverView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_new, null);
			View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_new, null);
			View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_new, null);
			rootLayout.initFlipperViews(MainActivity.this, view2, view1, recoverView);

			textLenght = text.length();
			System.out.println("----textLenght----->" + textLenght);
			int hadReadStart = PreferencesHelper.GetInt(readNumberKey);
			Log.e(TAG, hadReadStart + "--已经读了--" + textLenght);

			if(hadReadStart>0){//上次有阅读
				if (textLenght > hadReadStart) {
					showPage(view1, hadReadStart);

					if (textLenght > ( hadReadStart + NextPage.COUNT << 1)) {//有第2页
						showPage(view2,hadReadStart + NextPage.COUNT);
						nextpage.startIndex = hadReadStart + NextPage.COUNT;
						nextpage.endIndex = hadReadStart + NextPage.COUNT*2 ;
					} else { //没有下一页，下一章
						nextpage.nextChapter++;
						new ReadingThread().start();
						showPage(view2,0);
						nextpage.startIndex = 0;
						nextpage.endIndex  = NextPage.COUNT;
					}
				} else {
					Log.e(TAG,"这里怎么进来的");
					nextpage.nextChapter++;
					new ReadingThread().start();
					nextEndIndex = NextPage.COUNT;
					showPage(view1,nextEndIndex);
				}

			}else {//第一次读
				if (textLenght > NextPage.COUNT) { //有第二页
					showPage(view1,0);
					if (textLenght > (NextPage.COUNT << 1)) {//有第2页 <<1等于 *2
						showPage(view2, NextPage.COUNT);
						nextpage.startIndex = NextPage.COUNT;
						nextpage.endIndex = NextPage.COUNT << 1;
					} else { //没有第3页,取到结尾
						showPage(view2, NextPage.COUNT);
						nextpage.startIndex = NextPage.COUNT;
						nextpage.endIndex = textLenght;
						//new ReadingThread().start();
					}
				} else { //小于一页
					showPage(view1, 0);
				}
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nextpage = new NextPage();

		int _chapter = PreferencesHelper.GetInt(readChaperKey);
		if(_chapter>0)
			nextpage.nextChapter = _chapter;
		new ReadingThread().start();
		handler.sendEmptyMessage(0);
	}

	@Override
	public void onClick(View v) {

	}


	/**
	 * 新建第三页
	 * @param direction
	 *            {@link MOVE_TO_LEFT,MOVE_TO_RIGHT}
	 * @return
	 */
	@Override
	public View createView(final int direction) {

		String txt = "";
		View view = LayoutInflater.from(this).inflate(R.layout.view_new, null);
//		if (direction == FlipperLayout.TouchListener.MOVE_TO_LEFT) {
//			currenStartIndex = nextpage.startIndex;//开始
//			final int nextIndex = nextEndIndex + nextpage.COUNT;//结束
//			nextStartIndex = nextEndIndex;//下次开始
//			if (textLenght > nextIndex) { //第一页
//				showPage(view,nextEndIndex);
//				nextEndIndex = nextIndex;
//			} else {
//				//nextEndIndex = textLenght;
//				//重新读取
//				nextpage.nextChapter++;
//				new ReadingThread().start();
//				currentTopEndIndex = 0;
//				nextEndIndex = COUNT;
//				showPage(view,nextEndIndex);
//
//			}
//
//		} else {
//			nextEndIndex = nextStartIndex;
//			nextStartIndex = currentTopEndIndex;
//			currentTopEndIndex = currentTopEndIndex - nextpage.COUNT;
//			showPage(view,currentTopEndIndex - COUNT);
//		}
//
//		PreferencesHelper.putIntValue(readChaperKey, nextChapter);
//		PreferencesHelper.putIntValue(readNumberKey, currentTopEndIndex);
//		Log.e(TAG,"-top->" + currentTopEndIndex + "-show->" + nextStartIndex + "--bottom-->" + nextEndIndex);
		return view;
	}


	void showPage(View view,int start){
		TextView textView = (TextView) view.findViewById(R.id.textview);
		String page = "";
		if((start + NextPage.COUNT)<textLenght){
			page = text.subSequence(start, start + NextPage.COUNT).toString();
		}
		else
			page = text.subSequence(start,textLenght).toString();
		textView.setText(page);

		TextView txtNumber = (TextView) view.findViewById(R.id.txtNumber);
		txtNumber.setText((start / NextPage.COUNT + 1) + "/" + textLenght / NextPage.COUNT);

	}

	@Override
	public boolean whetherHasPreviousPage() {
		return nextpage.startIndex > nextpage.COUNT;
	}

	@Override
	public boolean whetherHasNextPage() {
		return nextpage.startIndex < textLenght;
	}

	@Override
	public boolean currentIsFirstPage() {
		boolean should = currentTopEndIndex > nextpage.COUNT;
		if (!should) {
			nextEndIndex = nextpage.startIndex;
			nextpage.startIndex = nextpage.endIndex;
			currentTopEndIndex = currentTopEndIndex - nextpage.COUNT;
		}
		return should;
	}

	@Override
	public boolean currentIsLastPage() {
		boolean should = nextEndIndex < textLenght;
		if (!should) {
			currentTopEndIndex = nextpage.startIndex;
			final int nextIndex = nextEndIndex + nextpage.COUNT;
			nextpage.startIndex = nextEndIndex;
			if (textLenght > nextIndex) {
				nextEndIndex = nextIndex;
			} else {
				nextEndIndex = textLenght;
			}
		}
		return should;
	}

	private class ReadingThread extends Thread {
		public void run() {
			AssetManager am = getAssets();
			InputStream response;
			try {
				Log.e(TAG,nextpage.nextChapter+"第几章");
				response = am.open(nextpage.nextChapter+".txt");
				if (response != null) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int i = -1;
					while ((i = response.read()) != -1) {
						baos.write(i);
					}
					text = new String(baos.toByteArray(), "UTF-8");//UTF-8
					baos.close();
					response.close();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
