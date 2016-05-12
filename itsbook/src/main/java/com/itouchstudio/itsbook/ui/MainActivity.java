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
import com.itouchstudio.itsbook.util.PreferencesHelper;
import com.itouchstudio.itsbook.view.FlipperLayout;

public class MainActivity extends Activity implements OnClickListener, FlipperLayout.TouchListener {
	private static final String TAG = "MainActivity";
	private String text = "";
	private int textLenght = 0;

	private static final int COUNT = 400;

	//本页开始
	private int currentTopEndIndex = 0;
	//本页结束位置
	private int currentShowEndIndex = 0;
	//下一页结束位置
	private int currentBottomEndIndex = 0;

	private static final String readNumberKey = "readnumber";

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

			TextView textView = (TextView) view1.findViewById(R.id.textview);
			int hadRead = PreferencesHelper.GetInt(readNumberKey);
			Log.e(TAG,hadRead+"--已经读了--"+textLenght);
			TextView txtNumber = (TextView) view1.findViewById(R.id.txtNumber);
			txtNumber.setText((hadRead/COUNT+1)+"/"+textLenght/COUNT);
			if(hadRead>0){//上次有阅读
				if (textLenght > hadRead) {
					String page = text.subSequence(hadRead, hadRead+COUNT).toString();
					textView.setText(page);

					textView = (TextView) view2.findViewById(R.id.textview);//第二页
					txtNumber = (TextView) view2.findViewById(R.id.txtNumber);
					if (textLenght > (hadRead << 1)) {//有第二页
						page = text.subSequence(hadRead+COUNT, hadRead + COUNT*2 ).toString();
						textView.setText(page);
						txtNumber.setText(((hadRead+ COUNT)/COUNT+1)+"/"+textLenght/COUNT);
						currentShowEndIndex = hadRead + COUNT;
						currentBottomEndIndex = hadRead + COUNT*2 ;
						Log.e(TAG,"-开始->" + currentTopEndIndex + "-第二页开始->" + currentShowEndIndex + "--第二页结束-->" + currentBottomEndIndex);
					} else {
						textView.setText(text.subSequence(COUNT, textLenght));
						currentShowEndIndex = textLenght;
						currentBottomEndIndex = textLenght;
					}
				} else {
					textView.setText(text.subSequence(0, textLenght));
					currentShowEndIndex = textLenght;
					currentBottomEndIndex = textLenght;
				}

			}else {
				if (textLenght > COUNT) {
					textView.setText(text.subSequence(0, COUNT));//第一页
					textView = (TextView) view2.findViewById(R.id.textview);
					txtNumber = (TextView) view2.findViewById(R.id.txtNumber);
					if (textLenght > (COUNT << 1)) {//有第二页 <<1等于 *2
						textView.setText(text.subSequence(COUNT, COUNT * 2));//第二页
						txtNumber.setText((COUNT/COUNT+1)+"/"+textLenght/COUNT);
						currentShowEndIndex = COUNT;
						currentBottomEndIndex = COUNT << 1;
					} else {
						textView.setText(text.subSequence(COUNT, textLenght));
						currentShowEndIndex = textLenght;
						currentBottomEndIndex = textLenght;
					}
				} else {
					textView.setText(text.subSequence(0, textLenght));
					currentShowEndIndex = textLenght;
					currentBottomEndIndex = textLenght;
				}
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new ReadingThread().start();
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
		int currentPage = 0;
		if (direction == FlipperLayout.TouchListener.MOVE_TO_LEFT) {
			currentTopEndIndex = currentShowEndIndex;
			final int nextIndex = currentBottomEndIndex + COUNT;
			currentShowEndIndex = currentBottomEndIndex;
			if (textLenght > nextIndex) { //第一页
				txt = text.substring(currentBottomEndIndex, nextIndex);
				currentBottomEndIndex = nextIndex;
			} else {
				txt = text.substring(currentBottomEndIndex, textLenght);
				currentBottomEndIndex = textLenght;
			}
			currentPage = currentShowEndIndex/COUNT+1;
		} else {
			currentBottomEndIndex = currentShowEndIndex;
			currentShowEndIndex = currentTopEndIndex;
			currentTopEndIndex = currentTopEndIndex - COUNT;
			txt = text.substring(currentTopEndIndex - COUNT, currentTopEndIndex);
			currentPage = currentShowEndIndex/COUNT;
		}


		View view = LayoutInflater.from(this).inflate(R.layout.view_new, null);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(txt + "\n" +currentPage);
		PreferencesHelper.putIntValue(readNumberKey, currentTopEndIndex);
		TextView txtNumber = (TextView) view.findViewById(R.id.txtNumber);
		txtNumber.setText(currentPage+"/"+textLenght/COUNT);

		System.out.println(currentPage+"-top->" + currentTopEndIndex + "-show->" + currentShowEndIndex + "--bottom-->" + currentBottomEndIndex);
		return view;
	}

	@Override
	public boolean whetherHasPreviousPage() {
		return currentShowEndIndex > COUNT;
	}

	@Override
	public boolean whetherHasNextPage() {
		return currentShowEndIndex < textLenght;
	}

	@Override
	public boolean currentIsFirstPage() {
		boolean should = currentTopEndIndex > COUNT;
		if (!should) {
			currentBottomEndIndex = currentShowEndIndex;
			currentShowEndIndex = currentTopEndIndex;
			currentTopEndIndex = currentTopEndIndex - COUNT;
		}
		return should;
	}

	@Override
	public boolean currentIsLastPage() {
		boolean should = currentBottomEndIndex < textLenght;
		if (!should) {
			currentTopEndIndex = currentShowEndIndex;
			final int nextIndex = currentBottomEndIndex + COUNT;
			currentShowEndIndex = currentBottomEndIndex;
			if (textLenght > nextIndex) {
				currentBottomEndIndex = nextIndex;
			} else {
				currentBottomEndIndex = textLenght;
			}
		}
		return should;
	}

	private class ReadingThread extends Thread {
		public void run() {
			AssetManager am = getAssets();
			InputStream response;
			try {
				response = am.open("text.txt");
				if (response != null) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int i = -1;
					while ((i = response.read()) != -1) {
						baos.write(i);
					}
					text = new String(baos.toByteArray(), "UTF-8");//UTF-8
					baos.close();
					response.close();
					handler.sendEmptyMessage(0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
