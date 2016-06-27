package com.its.xfweacher.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.its.xfweacher.R;
import com.its.xfweacher.api.LocationHelper;
import com.its.xfweacher.utils.LocationUtils;

/**
 * Created by xf on 2016/2/2.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //定义底部导航栏的三个布局
    private WeacherFragment fg1;
    private RelativeLayout weacherLayout;
    private ImageView weacherImg;
    private TextView weacherTxt;

    private NewsFragment fg2;
    private RelativeLayout lookLayout;
    private ImageView lookImg;
    private TextView lookTxt;

    private Fragment3 fg3;
    private RelativeLayout foundLayout;
    private ImageView foundImg;
    private TextView foundTxt;

    private MineFragment fg4;
    private RelativeLayout mineLayout;
    private ImageView mineImg;
    private TextView mineTxt;
    //定义要用的颜色值
    private int white = 0xFFFFFFFF;
    private int gray = 0xFF7597B3;
    private int blue =0xFF0AB2FB;
    //定义FragmentManager对象
    FragmentManager fManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fManager = getSupportFragmentManager();
        initViews();
        initDatas();
        setChioceItem(0);
    }


    @Override
    protected void onStop() {
        super.onStop();
        LocationHelper.stopLocation();
    }

    //完成组件的初始化
    protected void initViews()
    {
        weacherImg = (ImageView) findViewById(R.id.weacherImg);
        weacherTxt = (TextView) findViewById(R.id.weacherTxt);
        weacherLayout = (RelativeLayout) findViewById(R.id.weacherLayout);
        weacherLayout.setOnClickListener(this);


        lookImg = (ImageView) findViewById(R.id.lookImg);
        lookTxt = (TextView) findViewById(R.id.lookTxt);
        lookLayout = (RelativeLayout) findViewById(R.id.lookLayout);
        lookLayout.setOnClickListener(this);

        foundTxt = (TextView) findViewById(R.id.foundTxt);
        foundImg = (ImageView) findViewById(R.id.foundImg);
        foundLayout = (RelativeLayout) findViewById(R.id.foundLayout);
        foundLayout.setOnClickListener(this);

        mineLayout = (RelativeLayout) findViewById(R.id.mineLayout);
        mineImg = (ImageView) findViewById(R.id.mineImg);
        mineTxt = (TextView) findViewById(R.id.mineTxt);
        mineLayout.setOnClickListener(this);





    }
    protected void initDatas(){
        LocationHelper.startLocation(this,locationListener);
    }


    LocationUtils.LocationListener locationListener = new LocationUtils.LocationListener() {
        @Override
        public void detecting() {
            Log.i("liweiping", "detecting...");
        }

        @Override
        public void succeed(String name) {
            Log.i("liweiping", name);
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(MainActivity.this,"没有这个城市",Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(MainActivity.this,"定位:"+name,Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void failed() {
            Toast.makeText(MainActivity.this, "定位失败",Toast.LENGTH_SHORT).show();
        }

    };
    //重写onClick事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.weacherLayout:
                setChioceItem(0);
                break;
            case R.id.lookLayout:
                setChioceItem(1);
                break;
            case R.id.foundLayout:
                setChioceItem(2);
                break;
            case R.id.mineLayout:
                setChioceItem(3);
                break;
            default:
                break;
        }

    }


    //定义一个选中一个item后的处理
    public void setChioceItem(int index)
    {
        //重置选项+隐藏所有Fragment
        FragmentTransaction transaction = fManager.beginTransaction();
        clearChioce();
        hideFragments(transaction);
        switch (index) {
            case 0:
                weacherImg.setImageResource(R.drawable.ic_tabbar_course_pressed);
                weacherTxt.setTextColor(blue);
                //course_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
                if (fg1 == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    fg1 = new WeacherFragment();
                    transaction.add(R.id.content, fg1);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(fg1);
                }
                break;

            case 1:
                lookImg.setImageResource(R.drawable.ic_tabbar_found_pressed);
                lookTxt.setTextColor(blue);
                //found_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
                if (fg2 == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    fg2 = new NewsFragment();
                    transaction.add(R.id.content, fg2);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(fg2);
                }
                break;

            case 2:
                foundImg.setImageResource(R.drawable.ic_tabbar_settings_pressed);
                foundTxt.setTextColor(blue);
                //settings_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
                if (fg3 == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    fg3 = new Fragment3();
                    transaction.add(R.id.content, fg3);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(fg3);
                }
                break;
            case 3:
                mineImg.setImageResource(R.drawable.ic_tabbar_settings_pressed);
                mineTxt.setTextColor(blue);
                //mineLayout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
                if (fg4 == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    fg4 = new MineFragment();
                    transaction.add(R.id.content, fg4);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(fg4);
                }
                break;
        }
        transaction.commit();
    }

    //隐藏所有的Fragment,避免fragment混乱
    private void hideFragments(FragmentTransaction transaction) {
        if (fg1 != null) {
            transaction.hide(fg1);
        }
        if (fg2 != null) {
            transaction.hide(fg2);
        }
        if (fg3 != null) {
            transaction.hide(fg3);
        }
        if (fg4 != null) {
            transaction.hide(fg4);
        }
    }


    //定义一个重置所有选项的方法
    public void clearChioce()
    {
        weacherImg.setImageResource(R.drawable.ic_tabbar_course_normal);
        weacherTxt.setTextColor(gray);
        weacherLayout.setBackgroundColor(white);

        lookImg.setImageResource(R.drawable.ic_tabbar_found_normal);
        lookTxt.setTextColor(gray);
        lookLayout.setBackgroundColor(white);

        foundImg.setImageResource(R.drawable.ic_tabbar_settings_normal);
        foundTxt.setTextColor(gray);
        foundLayout.setBackgroundColor(white);

        mineImg.setImageResource(R.drawable.ic_tabbar_settings_normal);
        mineLayout.setBackgroundColor(white);
        mineTxt.setTextColor(gray);

    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK)&& event.getAction() == KeyEvent.ACTION_UP) {
            Log.e(""," ACTION_UP");
        }
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK) && event.getAction() == KeyEvent.ACTION_DOWN) {
            moveTaskToBack(true);
        }
        try {
            return super.dispatchKeyEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
