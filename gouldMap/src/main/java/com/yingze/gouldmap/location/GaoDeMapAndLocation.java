package com.yingze.gouldmap.location;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.CameraPosition;
import com.yingze.gouldmap.controller.LocationAndMapMsgInterface;

public class GaoDeMapAndLocation implements AMapLocationListener,LocationSource,OnCameraChangeListener{

	public AMapLocationClientOption mLocationOption = null;
	private AMapLocationClient mlocationClient;
	private OnLocationChangedListener mOnLocationChangedListener;
	private AMap mMap;
	private Context context;
	private LocationAndMapMsgInterface mInterface;
	
	
	private int whichMode;
	private boolean onceLocation;
	private long howLongBetweenLocation;
	
	public GaoDeMapAndLocation(Context context,LocationAndMapMsgInterface mInterface,AMap mMap) {
		this.context = context;
		this.mMap = mMap;
		this.mInterface = mInterface;
		
	}
	
	
	
	public void setInstance(int whichMode,
			boolean onceLocation,long howLongBetweenLocation){
		this.whichMode = whichMode;
		this.onceLocation = onceLocation;
		this.howLongBetweenLocation = howLongBetweenLocation;
		mMap.setLocationSource(this);//位置监听
		mMap.setOnCameraChangeListener(this);// 设置地图位置监听
	}



	@Override
	public void onCameraChange(CameraPosition camera) {
		mInterface.onCameraChange(camera);
		
	}

	@Override
	public void onCameraChangeFinish(CameraPosition camera) {
		mInterface.onCameraChangeFinish(camera);
		
	}

	@Override
	public void activate(OnLocationChangedListener listner) {
		mOnLocationChangedListener = listner;
		if (mlocationClient == null) {
			// 初始化定位
			mlocationClient = new AMapLocationClient(context);
			// 初始化定位参数
			mLocationOption = new AMapLocationClientOption();
			// 设置定位回调监听
			mlocationClient.setLocationListener(this);
			// 只定位一次
			if(onceLocation){
				mLocationOption.setOnceLocation(true);
			}else{
				// 设置定位间隔,单位毫秒,默认为2000ms--多次定位
				 mLocationOption.setInterval(howLongBetweenLocation);
			}
			// mLocationOption.setOnceLocation(true);
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
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();// 启动定位
		}
		
	}

	@Override
	public void deactivate() {
		// 停止定位的相关调用。
		mOnLocationChangedListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}

		mlocationClient = null;
		
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mOnLocationChangedListener != null && amapLocation != null) {
			if (amapLocation.getErrorCode() == 0) {
				}else{
					mInterface.onLocationChangedFail(amapLocation.getErrorCode(), amapLocation.getErrorInfo());
				}
			}else{
				mInterface.onLocationChangedFail(-1, "定位发生未知错误");
			}
		
	}
	
	public void onStop(){
		if(mlocationClient!=null){
			mlocationClient.stopLocation();
		}
	}

	
	public void onDestory(){
		if(mlocationClient!=null){
			mlocationClient.onDestroy();
		}
	}
}
