package com.itouchstudio.itsbook.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ScrollView;

import com.itouchstudio.itsbook.R;
import com.itouchstudio.itsbook.view.SwanTextView;

/**
 * Created by Administrator on 2016-5-13.
 */
public class TxtReaderActivity extends Activity  implements
        SwanTextView.OnTextChangedListener {

    private static final String LOG_TAG = "TxtReader";
    private static final int SHOW_TXT = 1;

    private SwanTextView mTextShow;
    private ScrollView mScrollView;
    private String mStringShow = null;

    private boolean mContinueRead = true;
    private boolean mHaveNewText = false;

    private int mCurBottom = -1;
    private int mNum = -1;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TXT:
                    mTextShow.setText((CharBuffer) msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.txt_reader);

        String uri = Environment.getExternalStorageDirectory() + File.separator+"Download/1.txt";

        mScrollView = (ScrollView) findViewById(R.id.text_show_scroll);

        mTextShow = (SwanTextView) findViewById(R.id.text_show);
        mTextShow.setOnTextChangedListener(this);

        new TextShowTask().execute(uri);
    }

    private void showText(String path) throws IOException, InterruptedException {

        InputStreamReader is = new InputStreamReader(new FileInputStream(path), "GB2312");

        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024 * 2];
        while (true) {

            if (mCurBottom == mScrollView.getScrollY()) {
                Log.e(LOG_TAG, "getScrollY:" + mCurBottom + " scroll:" + mScrollView.getScrollY());
                mCurBottom = -1;
                mNum++;
                Log.e(LOG_TAG, "mNum="+mNum);
                if (mNum % 2 == 0) {
                    mContinueRead = true;
                    Log.e(LOG_TAG, "mNum/2=0="+mNum);
                }
            }

            if (mContinueRead && is.read(buf) > 0) {
                mContinueRead = false;

                if (sb.length() > 4096) {
                    sb.delete(0, 2048);

                    Message msg = mHandler.obtainMessage(SHOW_TXT);
                    msg.obj = CharBuffer.wrap(sb.toString());
                    mHandler.sendMessage(msg);

                    mStringShow = sb.append(buf).toString();
                    mHaveNewText = true;
                } else {
                    while (sb.length() < 4096) {
                        sb.append(buf);
                        is.read(buf);
                    }

                    sb.append(buf);
                    Message msg = mHandler.obtainMessage(SHOW_TXT);
                    msg.obj = CharBuffer.wrap(sb.toString());
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    private class TextShowTask extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPostExecute(Object param) {
            Log.d(LOG_TAG, "Send broadcast");
        }

        @Override
        protected Object doInBackground(Object... params) {
            String uri = params[0].toString();

            try {
                showText(uri);
            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception", e);
            }

            return null;
        }
    }

    /**
     * 显示文本之前
     * @param bottom
     */
    @Override
    public void onPreOnDraw(int bottom) {
        mCurBottom = bottom - mScrollView.getHeight();
        Log.e(LOG_TAG, "onPreOnDraw(bottom:" + bottom + " getHeight:" + mScrollView.getHeight()+" mCurBottom:"+mCurBottom);

        if (mHaveNewText && !TextUtils.isEmpty(mStringShow)) {
            mHaveNewText = false;

            Message msg = mHandler.obtainMessage(SHOW_TXT);
            msg.obj = CharBuffer.wrap(mStringShow);
            mHandler.sendMessage(msg);
        }
    }
}
