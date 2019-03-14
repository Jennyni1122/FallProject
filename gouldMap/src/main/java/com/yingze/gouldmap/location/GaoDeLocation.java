package com.yingze.gouldmap.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.yingze.gouldmap.controller.LocationMsgInterface;

public class GaoDeLocation implements AMapLocationListener {

	// 声明mLocationOption对象
	public AMapLocationClientOption mLocationOption = null;
	private AMapLocationClient mlocationClient;

	private Context context;
	private LocationMsgInterface mLocationMsgInterface;

	/**
	 * 
	 * @param context
	 * @param whichMode 0:高精度 1:仅wifi与基站 2仅GPS定位
	 * @param onceLocation 是否只定位一次
	 * @param howLongBetweenLocation 若onceLocation为false，此参数用来设置定位时间间隔，默认为2秒
	 */
	public void getDefaultInstance(Context context,LocationMsgInterface mLocationMsgInterface, int whichMode,
			boolean onceLocation,long howLongBetweenLocation) {
		this.context = context;
		this.mLocationMsgInterface = mLocationMsgInterface;
		// 参数初始化
		mlocationClient = new AMapLocationClient(context);
		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		// 设置定位监听
		mlocationClient.setLocationListener(this);
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式(仅wifi与基站定位)，Device_Sensors是仅设备模式(仅pgs)
		switch (whichMode) {
		case 0: {
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			break;
		}

		case 1: {
			mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
			break;
		}

		case 2: {
			mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);
			break;
		}

		default: {
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			break;
		}
		}

		// mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);

		// 只定位一次
		if(onceLocation){
			mLocationOption.setOnceLocation(true);
		}else{
			// 设置定位间隔,单位毫秒,默认为2000ms--多次定位
			 mLocationOption.setInterval(howLongBetweenLocation);
		}
		// 设置定位参数
		mlocationClient.setLocationOption(mLocationOption);
	}
	
	/**
	 * 开始定位
	 */
	public void startLocation(){
		if(mlocationClient!=null){
			mlocationClient.startLocation();
		}
	}
	
	/**
	 * 停止定位
	 */
	public void stopLocation(){
		if(mlocationClient!=null){
			mlocationClient.stopLocation();
		}
	}
	
	
	/**
	 * 若是单次定位，不许调用次方法，否则，在onDestory中调用
	 */
	public void onDestory(){
		if(mlocationClient!=null){
			mlocationClient.onDestroy();
		}
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null) {
			if (amapLocation.getErrorCode() == 0) {
				// 定位成功回调信息，设置相关消息
				mLocationMsgInterface.getLocationSucceed(amapLocation);
			} else {
				// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
				mLocationMsgInterface.getLocationFail(amapLocation.getErrorCode(),amapLocation.getErrorInfo());
				Log.d("msg",
						"location Error, ErrCode:"
								+ amapLocation.getErrorCode() + ", errInfo:"
								+ amapLocation.getErrorInfo());
			}
		}else{
			mLocationMsgInterface.getLocationFail(-1,"定位参数为空");
		}
	}
}
