package com.its.xfweacher.helper;

import android.content.Context;
import android.widget.Toast;

import com.its.xfweacher.R;
import com.its.xfweacher.utils.LocationUtils;
import com.its.xfweacher.utils.NetUtil;

/**
 * Created by xf on 2016/2/11.
 */
public class LocationHelper {
    protected static LocationUtils mLocationUtils;
    public static void startLocation(Context context,LocationUtils.LocationListener cityNameStatus) {
        if (NetUtil.getNetworkState(context) == NetUtil.NETWORN_NONE) {
            Toast.makeText(context, R.string.net_error, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mLocationUtils == null)
            mLocationUtils = new LocationUtils(context, cityNameStatus);
        if (!mLocationUtils.isStarted()) {
            mLocationUtils.startLocation();// 开始定位
        }
    }

    public static void stopLocation() {
        if (mLocationUtils != null && mLocationUtils.isStarted())
            mLocationUtils.stopLocation();
    }

}
