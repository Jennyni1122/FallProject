package com.yingze.gouldmap.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by YellowHuang on 2018/6/21.
 */


/**
 * 使用该类必须手动置空该类对象，否则会导致内存泄漏
 */
public class LocationUtil {

    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient;

    /**
     * 初始化高德定位
     */
    public void initLocation(Context context, AMapLocationListener listener) {
        // 参数初始化
        mlocationClient = new AMapLocationClient(context);
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位监听
        mlocationClient.setLocationListener(listener);
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 只定位一次
        mLocationOption.setOnceLocation(true);
        // 设置定位间隔,单位毫秒,默认为2000ms--多次定位
        // mLocationOption.setInterval(2000);
        // 设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
    }


    public void startLocation() {
        if (mlocationClient != null ) {
            mlocationClient.startLocation();
        }
    }


}
