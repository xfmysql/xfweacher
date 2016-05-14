package com.itouchstudio.itsbook.ui;

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
import com.itouchstudio.itsbook.util.PreferencesHelper;
import com.itouchstudio.itsbook.view.FlipperLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ChapterActivity extends Activity implements OnClickListener, FlipperLayout.TouchListener {
	private static final String TAG = "MainActivity";
	private String text = "";
	private int textLenght = 0;

	private static final int COUNT = 400;

	//本页开始
	private int currentTopEndIndex = 0;
	//本页结束位置,也就是下一页开始
	private int nextStartIndex = 0;
	//下一页结束位置
	private int nextEndIndex = 0;

	private static final String readNumberKey = "readnumber";
	private static final String readChaperKey = "readchapter";
	int nextChapter = 1;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			//打开的时候默认读取
			FlipperLayout rootLayout = (FlipperLayout) findViewById(R.id.container);
			View recoverView = LayoutInflater.from(ChapterActivity.this).inflate(R.layout.view_new, null);
			View view1 = LayoutInflater.from(ChapterActivity.this).inflate(R.layout.view_new, null);
			View view2 = LayoutInflater.from(ChapterActivity.this).inflate(R.layout.view_new, null);
			rootLayout.initFlipperViews(ChapterActivity.this, view2, view1, recoverView);

			textLenght = text.length();
			System.out.println("----textLenght----->" + textLenght);

			TextView textView = (TextView) view1.findViewById(R.id.textview);

			int hadRead = PreferencesHelper.GetInt(readNumberKey);
			Log.e(TAG, hadRead + "--已经读了--" + textLenght);

			if(hadRead>0){//上次有阅读
				if (textLenght > hadRead) {
					showPage(view1, hadRead);

					//String page = text.subSequence(hadRead, hadRead+COUNT).toString();
					//textView.setText(page);
					if (textLenght > (hadRead << 1)) {//有第3页
						showPage(view2,hadRead+COUNT);
						//page = text.subSequence(hadRead+COUNT, hadRead + COUNT*2 ).toString();
						//textView.setText(page);
						//txtNumber.setText(((hadRead+ COUNT)/COUNT+1)+"/"+textLenght/COUNT);
						nextStartIndex = hadRead + COUNT;
						nextEndIndex = hadRead + COUNT*2 ;
						Log.e(TAG,"-开始->" + currentTopEndIndex + "-第二页开始->" + nextStartIndex + "--第二页结束-->" + nextEndIndex);
					} else {
						nextChapter++;
						new ReadingThread().start();
						currentTopEndIndex = 0;
						nextEndIndex = COUNT;
						showPage(view2,nextEndIndex);

					}
				} else {
					nextChapter++;
					new ReadingThread().start();
					currentTopEndIndex = 0;
					nextEndIndex = COUNT;
					showPage(view1,nextEndIndex);

				}

			}else {//第一次读
				if (textLenght > COUNT) { //有第二页
					showPage(view1,0);

					if (textLenght > (COUNT << 1)) {//有第3页 <<1等于 *2
						//textView.setText(text.subSequence(COUNT, COUNT * 2));//第二页
						//txtNumber.setText((COUNT/COUNT+1)+"/"+textLenght/COUNT);
						showPage(view2, COUNT);
						nextStartIndex = COUNT;
						nextEndIndex = COUNT << 1;
					} else { //没有第3页,取到结尾
						showPage(view2, COUNT);
						nextStartIndex = textLenght;
						nextEndIndex = textLenght;
						//new ReadingThread().start();
					}
				} else { //小于一页
					showPage(view1, 0);
					nextStartIndex = textLenght;
					nextEndIndex = textLenght;
					//new ReadingThread().start();
				}
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		int _chapter = PreferencesHelper.GetInt(readChaperKey);
		if(_chapter>0)
			nextChapter = _chapter;
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
		if (direction == FlipperLayout.TouchListener.MOVE_TO_LEFT) {
			currentTopEndIndex = nextStartIndex;//开始
			final int nextIndex = nextEndIndex + COUNT;//结束
			nextStartIndex = nextEndIndex;//下次开始
			if (textLenght > nextIndex) { //第一页
				showPage(view,nextEndIndex);
				nextEndIndex = nextIndex;
			} else {
				//nextEndIndex = textLenght;
				//重新读取
				nextChapter++;
				new ReadingThread().start();
				currentTopEndIndex = 0;
				nextEndIndex = COUNT;
				showPage(view,nextEndIndex);

			}

		} else {
			nextEndIndex = nextStartIndex;
			nextStartIndex = currentTopEndIndex;
			currentTopEndIndex = currentTopEndIndex - COUNT;
			showPage(view,currentTopEndIndex - COUNT);
		}

		PreferencesHelper.putIntValue(readChaperKey, nextChapter);
		PreferencesHelper.putIntValue(readNumberKey, currentTopEndIndex);
		Log.e(TAG,"-top->" + currentTopEndIndex + "-show->" + nextStartIndex + "--bottom-->" + nextEndIndex);
		return view;
	}


	void showPage(View view,int hadRead){
		TextView textView = (TextView) view.findViewById(R.id.textview);
		String page = "";
		if((hadRead+COUNT)<textLenght){
			page = text.subSequence(hadRead, hadRead+COUNT).toString();
		}
		else
			page = text.subSequence(hadRead,textLenght).toString();
		textView.setText(page);

		TextView txtNumber = (TextView) view.findViewById(R.id.txtNumber);
		txtNumber.setText((hadRead / COUNT + 1) + "/" + textLenght / COUNT);

	}

	@Override
	public boolean whetherHasPreviousPage() {
		return nextStartIndex > COUNT;
	}

	@Override
	public boolean whetherHasNextPage() {
		return nextStartIndex < textLenght;
	}

	@Override
	public boolean currentIsFirstPage() {
		boolean should = currentTopEndIndex > COUNT;
		if (!should) {
			nextEndIndex = nextStartIndex;
			nextStartIndex = currentTopEndIndex;
			currentTopEndIndex = currentTopEndIndex - COUNT;
		}
		return should;
	}

	@Override
	public boolean currentIsLastPage() {
		boolean should = nextEndIndex < textLenght;
		if (!should) {
			currentTopEndIndex = nextStartIndex;
			final int nextIndex = nextEndIndex + COUNT;
			nextStartIndex = nextEndIndex;
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
				Log.e(TAG,nextChapter+"第几章");
				response = am.open(nextChapter+".txt");
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
