package com.jennyni.fallproject.activity.devicelocation;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.jennyni.fallproject.BuildConfig;
import com.jennyni.fallproject.R;
import com.yingze.gouldmap.utils.LocationUtil;


import java.util.List;


/**
 * 地图功能基类，功能点基本在这里实现，
 * 附带请求需要的定位权限！
 *
 */

public abstract class BaseMapActivity extends Activity implements AMapLocationListener, PermissionsRequest.OnPermissionRequestListener {

    protected MapView mMapView;
    protected LocationUtil mLocation;
    protected PermissionsRequest permissionsRequest;
    protected boolean ishasLocationPermission;
    protected AMap aMap;
    protected GeocodeSearch geocodeSearch;
    protected double lat;
    protected double lng;
 //   private LoadingController mLoadingController = new LoadingController();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initMap(Bundle savedInstanceState) {

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mLocation = new LocationUtil();
        mLocation.initLocation(this, this);
        permissionsRequest = new PermissionsRequest(this, this, true,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
        permissionsRequest.setRequireCheck(true);


        aMap = mMapView.getMap();
        aMap.setMyLocationEnabled(true);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

    }


    protected void moveToPoint(LatLng latLng) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, aMap.getMaxZoomLevel() - 4));
    }


    @Override
    public void onResume() {
        super.onResume();
        requestPermissions();
        mMapView.onResume();
    }

    protected void requestPermissions() {
        permissionsRequest.requestPermissions();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mLocation = null;
        permissionsRequest = null;
        super.onDestroy();
    }

    protected void startLocation() {
        if (ishasLocationPermission) {
            mLocation.startLocation();
        } else {
            showToast("未请求到相关的定位权限");
        }
    }


    protected void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                locationSuccess(aMapLocation);
            } else {
                locationFail(aMapLocation);
            }
            if (!BuildConfig.BUILD_TYPE.equals("release")) {
                Log.e("BaseMapActivity: ", aMapLocation.getErrorInfo());
            }
        } else {
            showToast("定位异常！");
        }
    }

    protected void locationSuccess(AMapLocation aMapLocation) {

    }

    protected void locationFail(AMapLocation aMapLocation) {

    }

    @Override
    public void onPermissionRequestSuccess() {
        ishasLocationPermission = true;
    }


    @Override
    public void onPermissionRequestFail() {
        ishasLocationPermission = false;
        permissionsRequest.showMissingPermissionDialog("缺少相关定位权请手动开启！");
    }


    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionsRequest.PERMISSION_REQUEST_CODE && permissionsRequest.hasAllPermissionsGranted(grantResults)) {
            permissionsRequest.setRequireCheck(true);
            // 全部权限都已获取
            onPermissionRequestSuccess();
        } else {
            permissionsRequest.setRequireCheck(false);
            onPermissionRequestFail();
        }
    }

//    @Override
//    public void onCameraChange(CameraPosition cameraPosition) {
//
//    }
//
//    @Override
//    public void onCameraChangeFinish(CameraPosition cameraPosition) {
//        mLoadingController.showProgress(this, (ViewGroup) findViewById(R.id.rl_root));
//        LatLng target = cameraPosition.target;
//        getAddressByLatlng(target);
//        lat = cameraPosition.target.latitude;
//        lng = cameraPosition.target.longitude;//获取经度
//    }

    protected void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }

//    @Override
//    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
////        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
////        String formatAddress = regeocodeAddress.getFormatAddress().replace("广东省深圳市盐田区", "");
////        tv_location.setText(formatAddress);
////        loadingController.cancelProgress();
////        tv_moveaddress.setText(formatAddress.concat("\n").concat(String.format("(%s,%s)", String.valueOf(lat), String.valueOf(lng))));
//    }

//    @Override
//    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//
//    }

    /**
     * 添加圆形覆盖物
     */
    protected Circle addcircle(LatLng latLng, double radius, int fillcolor) {
        CircleOptions options = new CircleOptions();
        options.center(latLng);
        options.visible(true);
        options.fillColor(fillcolor);
        options.strokeColor(Color.parseColor("#00000000"));
        options.radius(radius);
        return aMap.addCircle(options);

    }

    /**
     * 添加marker点
     *
     * @param latLng
     * @param bitmap
     */
    protected Marker addMarker(LatLng latLng, Bitmap bitmap) {
        MarkerOptions options = new MarkerOptions();
        options.visible(true);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        options.icon(bitmapDescriptor);
        options.draggable(false);
        options.position(latLng);
        return aMap.addMarker(options);

    }

    /**
     * 返回两点间的距离
     *
     * @param latLng1
     * @param latLng2
     * @return
     */
    public static float getPoint2PointLength(LatLng latLng1, LatLng latLng2) {
        return AMapUtils.calculateLineDistance(latLng1, latLng2);
    }


    /**
     * 点对点连线
     *
     * @param latLngList
     * @param linecolor
     */
    protected void addLinePath(List<LatLng> latLngList, int linecolor) {

        PolylineOptions options = new PolylineOptions();
        options.color(linecolor);
        options.width(3);
        options.addAll(latLngList);
        aMap.addPolyline(options);
    }

    /**
     * 设置地图的拖拽监听和拖拽后坐标的网络请求
     * 和
     */
    protected void setMapDragListener(AMap.OnCameraChangeListener listener, GeocodeSearch.OnGeocodeSearchListener onGeocodeSearchListener) {
        if (listener != null) {
            aMap.setOnCameraChangeListener(listener);
        }
        geocodeSearch = new GeocodeSearch(this);
        if (onGeocodeSearchListener != null) {
            geocodeSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
        }
    }

}
