package com.jennyni.fallproject.activity.devicelocation;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jennyni.fallproject.Bean.AskFallInfoBean;
import com.jennyni.fallproject.Bean.UserUpdateBean;
import com.jennyni.fallproject.FallProjectApplication;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.activity.EditDevUserActivity;
import com.jennyni.fallproject.fragment.HomeFragment;
import com.jennyni.fallproject.service.LocationService;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.DBUtils;
import com.jennyni.fallproject.utils.JsonParse;
import com.jennyni.fallproject.utils.LocationServiceUtils;
import com.jennyni.fallproject.utils.UtilsHelper;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 显示设备定位，用户跌倒报警和地理围栏功能以及报警，报警提示
 */
public class DevUserDetailActivity extends BaseMapActivity implements GeocodeSearch.OnGeocodeSearchListener, AMap.OnCameraChangeListener {
    //标题栏
    public static final String TAG = "DevUserDetailActivity";
    private static final String CARDID_KEY = "cardid";
    private static final String DEVICEBEAN_KEY = "devicebean";
    private TextView tv_main_title, tv_back, tv_edit_device;
    private RelativeLayout rl_title_bar;
    //内容控件
    private TextView tv_dev_user, tv_address, tv_dev_num, tv_idcard, tv_alert, tv_state;
    private ImageView iv_path,iv_rssi, iv_power;
    public static final int MSG_DevUser_OK = 1; //加载设备，获取数据
    public static final int MSG_FALLINFO_OK = 2;    //获取跌倒设备数据
    private Circle circle;
    private Marker marker;
    private Marker marker1;
    private LocationBroadcastReceiver broadcastReceiver;
    private AskFallInfoBean.ResultBean fallbean;
    private UserUpdateBean.ResultBean devicebean;
    private String account, cardid, dname;


    /**
     * 跳转界面，将解析数据传值
     *
     * @param context
     */
    public static void startActivity(Context context, UserUpdateBean.ResultBean devicebean) {
        Intent intent = new Intent(context, DevUserDetailActivity.class);
        intent.putExtra(DEVICEBEAN_KEY, devicebean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_user_detail);
        initMap(savedInstanceState);

        account = UtilsHelper.readLoginUserName(this);      //登录的用户名
        initView();     //初始化控件

        broadcastReceiver = new LocationBroadcastReceiver();
        IntentFilter filter = new IntentFilter(getPackageName().concat(".safelocaiton"));
        registerReceiver(broadcastReceiver, filter);
        setMapDragListener(null, this);

        Intent intent = getIntent();

        devicebean = (UserUpdateBean.ResultBean) intent.getSerializableExtra(DEVICEBEAN_KEY);
        cardid = devicebean.getCard_id();
//        sendrequest_userUpdateData();
        sendrequest_fallData();     //请求网络，查询跌倒最新数据

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
            public void onClick(View view) {            //返回按钮
                finish();
            }
        });
        tv_edit_device = (TextView) findViewById(R.id.tv_save);
        tv_edit_device.setVisibility(View.VISIBLE);
        tv_edit_device.setText("编辑设备");
        tv_edit_device.setOnClickListener(new View.OnClickListener() {       //跳转到编辑设备界面
            @Override
            public void onClick(View view) {            //进入编辑设备界面
//                Intent intent = new Intent(DevUserDetailActivity.this, EditDevUserActivity.class);
//                // startActivityForResult(intent, 1);
//                startActivity(intent);
//                finish();
                EditDevUserActivity.startActivity(DevUserDetailActivity.this,cardid);
            }
        });

        //内容控件
        tv_dev_user = findViewById(R.id.tv_dev_user);       //设备用户名
        tv_address = findViewById(R.id.tv_address);         //设备定位地址
        tv_dev_num = findViewById(R.id.tv_dev_num);         //设备编号
        iv_rssi = findViewById(R.id.iv_rssi);               //设备信号
        iv_power = findViewById(R.id.iv_power);             //设备电量
        tv_idcard = findViewById(R.id.tv_idcard);           //设备用户身份证
        tv_alert = findViewById(R.id.tv_alert);             //设备围栏范围（安全范围）
        tv_state = findViewById(R.id.tv_state);             //设备状态（跌倒报警，围栏）
        iv_path = findViewById(R.id.iv_path);        //查看轨迹按钮
        iv_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //跳转到设备轨迹界面
          PathActivity.startActivity(DevUserDetailActivity.this,devicebean.getCard_id());
            }
        });
    }

    @Override
    public void onPermissionRequestSuccess() {
        super.onPermissionRequestSuccess();

    }

    @Override
    protected void locationSuccess(AMapLocation aMapLocation) {
        super.locationSuccess(aMapLocation);

    }


    /**
     * 事件捕获
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FALLINFO_OK:           //设备跌倒报警信息
                    if (msg.obj != null) {
                        //获取数据
                        fallbean = (AskFallInfoBean.ResultBean) msg.obj;
                        Log.e("TAG", "handleMessage:" + "id:" + fallbean.getId() + "设备编号：" + fallbean.getCard_id());
                        setData();
                    }
                    break;
            }
        }
    };


    /**
     *
     * 将设备参数及定位的数据显示出来()
     */
    private void setData() {
        tv_dev_user.setText(fallbean.getDname());   //显示设备用户名
        tv_dev_num.setText(fallbean.getCard_id());  //显示设备编号
        tv_idcard.setText(devicebean.getDev_idcard());   //显示身份证

        //显示信号
        if (String.valueOf(fallbean.getRssi()).equals("2")){
            iv_rssi.setImageResource(R.drawable.rssi2);
        }else if (String.valueOf(fallbean.getRssi()).equals("3")){
            iv_rssi.setImageResource(R.drawable.rssi3);
        }else if (String.valueOf(fallbean.getRssi()).equals("4")){
            iv_rssi.setImageResource(R.drawable.rssi4);
        }else {
            iv_rssi.setImageResource(R.drawable.rssi0);
        }
        //显示电量
        if (String.valueOf(fallbean.getPower()).equals("1")){
            iv_power.setImageResource(R.drawable.power1);
        }else if (String.valueOf(fallbean.getPower()).equals("2")){
            iv_power.setImageResource(R.drawable.power2);
        }else if (String.valueOf(fallbean.getPower()).equals("3")){
            iv_power.setImageResource(R.drawable.power3);
        }else if (String.valueOf(fallbean.getPower()).equals("4")){
            iv_power.setImageResource(R.drawable.power4);
        }else {
            iv_power.setImageResource(R.drawable.power0);
        }

//        //显示安全范围
//        if (fallbean.getFence() == 1){
//           tv_alert.setText("超出范围！");
//           tv_alert.setTextColor(Color.RED);
//
//        }else {
//            tv_alert.setText("<"+devicebean.getGeoradius()+"米（半径）");
//            tv_alert.setTextColor(Color.GREEN);
//        }

        //显示跌倒 (地图显示报警跳动)
       if (fallbean.getFall() == 1){
          tv_state.setText("发生跌倒！");
          tv_state.setTextColor(Color.RED);
          sendFallNotifycation();       //发生通知
       }else if(fallbean.getFence() == 2){
           tv_state.setText("手动报警！");
           tv_state.setTextColor(Color.RED);
           sendFallNotifycation();       //发生通知
       }else {
           tv_state.setText("正常");
           tv_state.setTextColor(Color.GREEN);
       }


        //显示设备的定位，geopoints为设备定位，latlng(geocenter)为围栏中心点
        LatLng geopoints = new LatLng(Double.valueOf(fallbean.getLat()), Double.valueOf(fallbean.getLng()));
        if (devicebean.getIsgeo() == 1) {
            String geocenter = devicebean.getGeocenter();
            if (geocenter != null) {
                String array[] = geocenter.split(",");
                if (array != null && array.length >= 2) {
                    lat = Double.valueOf(array[0]);
                    lng = Double.valueOf(array[1]);
                }
            }
            LatLng lnglat = new LatLng(lng, lat);
            moveToPoint(lnglat);        //围栏中心点
            if (geopoints!=null){
                moveToPoint(geopoints);//设备定位点
            }
            //围栏部分,marker为围栏中心点，marker1为设备定位
            if (circle == null && marker == null) {
                circle = addcircle(lnglat, Double.valueOf(Double.valueOf(devicebean.getGeoradius())), Color.parseColor("#7f89EAF1"));
                marker = addMarker(lnglat, BitmapFactory.decodeResource(getResources(), R.drawable.ic_centrallocation));
            } else {
                circle.setCenter(lnglat);
                marker.setPosition(lnglat);
            }
            float length = getPoint2PointLength(lnglat, new LatLng(Double.valueOf(fallbean.getLng()),Double.valueOf(fallbean.getLat())));
            getAddressByLatlng(geopoints);
            //若设备定位marker1为空，则添加设备定位
            if (marker1 == null) {
                //设置定位信息来源： 1GPS, 0基站
                marker1 = addMarker(geopoints, BitmapFactory.decodeResource(getResources(), R.drawable.location_marker));
            }
            //如果设备定位距离大于围栏半径长度

            if (length > Double.valueOf(devicebean.getGeoradius())) {
                sendFenceNotifycation();     //发送通知
                marker1.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker)));
                marker1.setPosition(geopoints);

                tv_alert.setText("超出范围！");
                tv_alert.setTextColor(Color.RED);
            } else {
                marker1.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker)));
                marker1.setPosition(geopoints);

                tv_alert.setText("<"+devicebean.getGeoradius()+"米（半径）");
                tv_alert.setTextColor(Color.GREEN);
            }

        } else {
            showToast("该设备未启用电子围栏");
        }

    }

    /**通话1
     * 发出通知，跌倒通知
     */
    private void sendFallNotifycation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            /**
             *  创建通知栏管理工具
             */
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            /**
             *  实例化通知栏构造器
             */
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            /**
             *  设置Builder
             */
            //设置标题
            mBuilder.setContentTitle("跌倒守护")
                    //设置内容
                    .setContentText(fallbean.getDname()+"发生跌倒，请注意！")
                    //设置大图标
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_fall))
                    //设置小图标
                    .setSmallIcon(R.drawable.icon_fall)
                    //设置通知时间
                    .setWhen(System.currentTimeMillis())
                    //首次进入时显示效果
                    .setTicker(fallbean.getDname()+"发生跌倒，请注意！")
                    //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                    .setDefaults(Notification.DEFAULT_SOUND);
            //发送通知请求
            notificationManager.notify(new Random().nextInt(Integer.MAX_VALUE), mBuilder.build());
        } else {

            int notificationId = new Random().nextInt(Integer.MAX_VALUE);
            Notification.Builder builder = new Notification.Builder(this, "1"); //与channelId对应
            //icon title text必须包含，不然影响桌面图标小红点的展示
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("跌倒守护")
                    .setContentText(fallbean.getDname()+"发生跌倒，请注意！")
                    .setNumber(3); //久按桌面图标时允许的此条通知的数量
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


            NotificationChannel channel = new NotificationChannel("1",
                    "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(notificationId, builder.build());

        }
    }


    /**通知2
     * 发出通知，围栏通知
     */
    private void sendFenceNotifycation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            /**
             *  创建通知栏管理工具
             */
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            /**
             *  实例化通知栏构造器
             */
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            /**
             *  设置Builder
             */
            //设置标题
            mBuilder.setContentTitle("跌倒守护")
                    //设置内容
                    .setContentText(fallbean.getDname()+"超出设定的安全范围，请注意！")
                    //设置大图标
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_fall))
                    //设置小图标
                    .setSmallIcon(R.drawable.icon_fall)
                    //设置通知时间
                    .setWhen(System.currentTimeMillis())
                    //首次进入时显示效果
                    .setTicker(fallbean.getDname()+"超出设定的围栏范围，请注意！")
                    //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                    .setDefaults(Notification.DEFAULT_SOUND);
            //发送通知请求
            notificationManager.notify(new Random().nextInt(Integer.MAX_VALUE), mBuilder.build());
        } else {

            int notificationId = new Random().nextInt(Integer.MAX_VALUE);
            Notification.Builder builder = new Notification.Builder(this, "1"); //与channelId对应
            //icon title text必须包含，不然影响桌面图标小红点的展示
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("跌倒守护")
                    .setContentText(fallbean.getDname()+"超出设定的围栏范围，请注意！")
                    .setNumber(3); //久按桌面图标时允许的此条通知的数量
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


            NotificationChannel channel = new NotificationChannel("1",
                    "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(notificationId, builder.build());

        }

    }



    /**
     * （AskFallInfo）
     * 请求网络，查询设备跌倒报警最新数据
     */
    private void sendrequest_fallData() {
        //10.请求设备最新数据(跌倒报警与地理围栏报警)
        //String url10 = "http://www.phyth.cn/index/fall/askfallinfo/account/" + account + "/cardid/" + cardid;
        String url = Constant.BASE_WEBSITE + Constant.REQUEST_ASKFALLINFO_DEVICE_URL + "/account/" + account + "/cardid/" + cardid;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //开启异步访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //联网失败
                Log.e("MSG_MAPPAGE_FAIL", "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("MSG_MAPPAGE_OK", "请求成功：" + response);
                AskFallInfoBean.ResultBean bean = JsonParse.getInstance().getAskFallInfo(response.body().string());
                if (bean == null) {
                    Log.e("MSG_MAPPAGE_OK", "设备异常或者该设备无最新位置信息！");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showToast("设备异常或者该设备无最新位置信息！");
                        }
                    });
//                    Toast.makeText(DevUserDetailActivity.this, "请求设备定位异常！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Message message = new Message();
                message.what = MSG_FALLINFO_OK;
                message.obj = bean;
                handler.sendMessage(message);
            }
        });
    }


    @Override
    protected void onDestroy() {

        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }

    //地理编码回调函数
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        tv_address.setText(regeocodeAddress.getFormatAddress());
    }

    //地理逆编码回调函数
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }


    private class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


//            String responseBody = intent.getStringExtra("responseBody");
//            parseLocation(responseBody);
        }
    }

    /**
     * 从HomeFragment设备用户列表回传数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            int id = data.getIntExtra("id", 0);
            UserUpdateBean.ResultBean devicebean = DBUtils.getInstance(DevUserDetailActivity.this).getUpdateDevInfo(id);
            setData();
        }
    }

}
