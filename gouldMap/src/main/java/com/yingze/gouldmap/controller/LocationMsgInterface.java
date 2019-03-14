package com.yingze.gouldmap.controller;

import com.amap.api.location.AMapLocation;

public interface LocationMsgInterface {

	public void getLocationSucceed(AMapLocation amapLocation);
	public void getLocationFail(int errorCode,String errorMsg);
}
