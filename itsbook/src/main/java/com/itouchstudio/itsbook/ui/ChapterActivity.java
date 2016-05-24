package com.itouchstudio.itsbook.ui;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;

import com.itouchstudio.itsbook.R;
import com.itouchstudio.itsbook.util.PreferencesHelper;
import com.itouchstudio.itsbook.view.FlipperLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ChapterActivity  extends Activity implements OnClickListener, FlipperLayout.TouchListener {
	private static final String TAG = ChapterActivity.class.getName();
	private String text = "";
	private int textLenght = 0;

	private static final int COUNT = 400;

	private int currentTopEndIndex = 0;

	private int currentShowEndIndex = 0;

	private int currentBottomEndIndex = 0;
	private static final String readNumberKey = "readnumber";
	private static final String readChaperKey = "readchapter";
	int nextChapter = 1,readProcess = 0;

	SeekBar seekBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		int _chapter = PreferencesHelper.GetInt(readChaperKey);
		if(_chapter>0)
			nextChapter = _chapter;
		new ReadingThread().start();
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());

	}

	private class OnSeekBarChangeListenerImp implements
			SeekBar.OnSeekBarChangeListener {

		// 触发操作，拖动
		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
			readProcess = progress;
		}

		// 表示进度条刚开始拖动，开始拖动时候触发的操作
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		// 停止拖动时候
		public void onStopTrackingTouch(SeekBar seekBar) {
			PreferencesHelper.putIntValue(readNumberKey, readProcess*COUNT);
			handler.sendEmptyMessage(0);
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public View createView(final int direction) {
		String txt = "";
		int page = 0;
		if (direction == FlipperLayout.TouchListener.MOVE_TO_LEFT) {
			currentTopEndIndex = currentShowEndIndex;//本页开始
			final int nextIndex = currentBottomEndIndex + COUNT;//下一页结束
			currentShowEndIndex = currentBottomEndIndex;//下一页开始
			if (textLenght > nextIndex) {
				txt = text.substring(currentBottomEndIndex, nextIndex);
				currentBottomEndIndex = nextIndex;
			} else {
				txt = text.substring(currentBottomEndIndex, textLenght);
				currentBottomEndIndex = textLenght;
			}
			page = (currentShowEndIndex / COUNT+1);
		} else {
			currentBottomEndIndex = currentShowEndIndex;
			currentShowEndIndex = currentTopEndIndex;
			currentTopEndIndex = currentTopEndIndex - COUNT;
			txt = text.substring(currentTopEndIndex - COUNT, currentTopEndIndex);
			page = (currentShowEndIndex / COUNT -1 );
		}

		View view = LayoutInflater.from(this).inflate(R.layout.view_new, null);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		TextView txtNumber = (TextView) findViewById(R.id.txtNumber);
		textView.setText(txt);
		txtNumber.setText(page + "/" + (textLenght / COUNT+1));
		seekBar.setProgress(page);

		PreferencesHelper.putIntValue(readChaperKey, nextChapter);
		PreferencesHelper.putIntValue(readNumberKey, currentTopEndIndex);//第一页和最后一页这里没调用

		System.out.println("-top->" + currentTopEndIndex + "-show->" + currentShowEndIndex + "--bottom-->" + currentBottomEndIndex);
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
		if (!should) { //前一页
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

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			FlipperLayout rootLayout = (FlipperLayout) findViewById(R.id.container);
			View recoverView = LayoutInflater.from(ChapterActivity.this).inflate(R.layout.view_new, null);
			View view1 = LayoutInflater.from(ChapterActivity.this).inflate(R.layout.view_new, null);
			View view2 = LayoutInflater.from(ChapterActivity.this).inflate(R.layout.view_new, null);
			rootLayout.initFlipperViews(ChapterActivity.this, view2, view1, recoverView);

			textLenght = text.length();

			System.out.println("----textLenght----->" + textLenght);

			TextView textView = (TextView) view1.findViewById(R.id.textview);
			TextView txtNumber = (TextView) findViewById(R.id.txtNumber);

			int hadRead = PreferencesHelper.GetInt(readNumberKey);

			seekBar.setEnabled(true);
			seekBar.setMax(textLenght/COUNT+1);
			seekBar.setProgress(hadRead / COUNT+1);

			Log.e(TAG, hadRead + "--已经读了--" + textLenght);
			if(hadRead>0) {//上次有阅读
				if (textLenght > hadRead) {//大于已读的
					int endIndex = hadRead+COUNT;
					if(textLenght<endIndex) endIndex = textLenght;//最后一页
					textView.setText(text.subSequence(hadRead,endIndex));
					txtNumber.setText((hadRead / COUNT+1) + "/" + (textLenght / COUNT+1));

					textView = (TextView) view2.findViewById(R.id.textview);
					int wantRead = hadRead+COUNT*2;
					if (textLenght > wantRead) {//有第三页，完整的取第二页
						int startIndex = hadRead+COUNT;
						textView.setText(text.subSequence(startIndex, hadRead+COUNT*2));
						currentShowEndIndex = startIndex;
						currentBottomEndIndex = startIndex+COUNT;
					} else if(textLenght>(hadRead+COUNT)){//最后第二页 = 没有第三页，有第二页
						int startIndex = hadRead+COUNT;
						textView.setText(text.subSequence(startIndex, textLenght));
						currentShowEndIndex = startIndex;
						currentBottomEndIndex = startIndex+COUNT;

					}else {//最后一页=没有第二页
						currentShowEndIndex = textLenght;
						currentBottomEndIndex = textLenght;
					}
				} else {
					Log.e(TAG,"应该不会发生");
				}
			}else {
				//region 未读过
				if (textLenght > COUNT) {
					textView.setText(text.subSequence(0, COUNT));
					txtNumber.setText((COUNT / COUNT) + "/" + (textLenght / COUNT+1));

					textView = (TextView) view2.findViewById(R.id.textview);

					if (textLenght > (COUNT*2)) {
						textView.setText(text.subSequence(COUNT, COUNT*2));
						currentShowEndIndex = COUNT;
						currentBottomEndIndex = COUNT*2;
					} else {
						textView.setText(text.subSequence(COUNT, textLenght));
						currentShowEndIndex = textLenght;
						currentBottomEndIndex = textLenght;
					}
				} else { //小于一页
					textView.setText(text.subSequence(0, textLenght));
					currentShowEndIndex = textLenght;
					currentBottomEndIndex = textLenght;
				}
				//endregion
			}
		};
	};

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
					text = new String(baos.toByteArray(), "GBK");//UTF-8
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
