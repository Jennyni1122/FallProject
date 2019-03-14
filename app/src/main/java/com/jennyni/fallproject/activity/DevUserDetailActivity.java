package com.jennyni.fallproject.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Polyline;
import com.jennyni.fallproject.Bean.AskFallInfoBean;
import com.jennyni.fallproject.FallProjectApplication;
import com.jennyni.fallproject.PermissionsActivity;
import com.jennyni.fallproject.R;

import java.util.List;

/**
 * 显示设备定位，用户跌倒报警和地理围栏功能以及报警，报警提示
 */
public class DevUserDetailActivity extends PermissionsActivity implements AMapLocationListener {
    //标题栏
    private TextView tv_main_title,tv_back, tv_switch;
    private RelativeLayout rl_title_bar;
    //内容控件
    private TextView tv_devicename,tv_address,tv_phone,tv_rssi,tv_power,tv_idcard,tv_alert,tv_state;
    private ImageView iv_select_time;
    private com.itheima.PullToRefreshView mPullToRefreshView;
    public static final int REFRESH_DELAY = 1000; //刷新延迟
    public static final int MSG_MAPPAGE_OK = 1;    //获取设备数据

    //通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.0001;
    MapView mMapView = null;
    AMap aMap;//初始化地图控制器对象
    private Polyline mVirtureRoad;
    private Marker mMoveMarker;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    String cardid,dname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // FallProjectApplication application = (FallProjectApplication) getApplication();
        setContentView(R.layout.activity_dev_user_detail);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);      // 此方法必须重写

        //从FindFragment的消息列表点击事件进来，显示跌倒该用户的跌倒信息
        AskFallInfoBean.ResultBean  askfallinfo = (AskFallInfoBean.ResultBean) getIntent().getSerializableExtra("askfallinfo");
        String position = getIntent().getStringExtra("position");
        if (askfallinfo != null) {
            cardid =askfallinfo.getCard_id();
            dname = askfallinfo.getDname();
        }

        init();
        initView();     //初始化控件

    }

    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }
    }

    //设置一些amap属性
    private void setUpMap() {

        //自定义系统小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //设置小蓝点的图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));


    }

    private void initView() {
        //标题栏，左退出至列表项，右边进入编辑设备的界面
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("设备定位");//显示设备用户名字
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DevUserDetailActivity.this.finish();
            }
        });
        tv_switch = (TextView) findViewById(R.id.tv_save);
        tv_switch.setVisibility(View.VISIBLE);
        tv_switch.setText("编辑");
        tv_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(  DevUserDetailActivity.this, EditDevUserActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //内容控件
        tv_devicename = findViewById(R.id.tv_devicename);
        tv_address = findViewById(R.id.tv_address);
        tv_phone = findViewById(R.id.tv_phone);
        tv_rssi = findViewById(R.id.tv_rssi);
        tv_power = findViewById(R.id.tv_power);
        tv_idcard = findViewById(R.id.tv_idcard);
        tv_alert = findViewById(R.id.tv_alert);
        tv_state = findViewById(R.id.tv_state);
        iv_select_time = findViewById(R.id.iv_select_time);
        iv_select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevUserDetailActivity.this,SelectTimeActivity.class);
                startActivity(intent);
            }
        });

    }






    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null&&amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                double latitude = amapLocation.getLatitude();
                double longitude = amapLocation.getLongitude();
                Log.d("AddressFenceActivity", "当前经纬度: " + latitude + "," + longitude);
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();//启动定位
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public String[] getPermission() {
        return new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    }

    @Override
    public void onPermissionRequestSuccess() {

    }

    @Override
    public void onPermissionRequestFail() {
        showMissingPermissionDialog("缺少权限");
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
        deactivate();       //停止定位
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
