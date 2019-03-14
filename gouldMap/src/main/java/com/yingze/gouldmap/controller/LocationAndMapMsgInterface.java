package com.yingze.gouldmap.controller;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.model.CameraPosition;

public interface LocationAndMapMsgInterface {

	public void onCameraChange(CameraPosition camera);
	public void onCameraChangeFinish(CameraPosition camera);
	public void onLocationChangedSucceed(AMapLocation amapLocation);
	public void onLocationChangedFail(int errorCode,String errorMsg);
	
	
}
