package com.jennyni.fallproject.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;
import com.jennyni.fallproject.Bean.AskFallInfoBean;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.activity.devicelocation.BaseMapActivity;
import com.jennyni.fallproject.activity.devicelocation.DevUserDetailActivity;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.JsonParse;
import com.jennyni.fallproject.utils.UtilsHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.jennyni.fallproject.activity.devicelocation.DevUserDetailActivity.MSG_MAPPAGE_OK;

/**
 * Created by YellowHuang on 2018/10/22.
 */

public class LocationService extends Service {


    private static final String STOP_KEY = "stopservice";

    public static void startService(Context context) {
        context.startService(new Intent(context, LocationService.class));
    }

    public static void stopService(Context context) {

        Intent service = new Intent(context, LocationService.class);
        service.putExtra("isStop", true);
        context.startService(service);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler.sendEmptyMessageDelayed(0, 30 * 1000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            sendrequest_initData();     //查询网络，查询设备最新数据（无围栏坐标点及围栏范围的参数）
            sendEmptyMessageDelayed(0, 30 * 1000);

        }
    };

    /**
     * 查询网络，查询设备最新数据
     */
    private void sendrequest_initData() {

//        //10.请求设备最新数据(跌倒报警与地理围栏报警)
//        //String url10 = "http://www.phyth.cn/index/fall/askfallinfo/account/" + account + "/cardid/" + cardid;
//
//        String account = UtilsHelper.readLoginUserName(this);      //登录的用户名
//
//        String url = Constant.BASE_WEBSITE+Constant.REQUEST_ASKFALLINFO_DEVICE_URL +
//                "/account/" + account + "/cardid/" + cardid;
//        OkHttpClient okHttpClient = new OkHttpClient();
//        final Request request = new Request.Builder().url(url).build();
//        Call call = okHttpClient.newCall(request);
//        //开启异步访问网络
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //联网失败
//                Log.e("MSG_MAPPAGE_FAIL", "请求失败：" + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("MSG_MAPPAGE_OK", "请求成功：" + response);
//                AskFallInfoBean.ResultBean bean = JsonParse.getInstance().getAskFallInfo(response.body().string());
//                if (bean==null){
//                    Log.e("MSG_MAPPAGE_OK", "请求设备定位异常！");
//                    Toast.makeText(LocationService.this, "请求设备定位异常！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Message message = new Message();
//                message.what = MSG_MAPPAGE_OK;
//                message.obj = bean;
//                handler.sendMessage(message);
//            }
//        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra(STOP_KEY, false)) {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }


//    /**
//     * 事件捕获
//     */
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case MSG_MAPPAGE_OK:
//                    if (msg.obj != null) {
//                        //获取数据
//                        AskFallInfoBean.ResultBean bean = (AskFallInfoBean.ResultBean) msg.obj;
//                        Log.e("TAG","handleMessage:"+"id:"+ bean.getId() + "设备编号："+ bean.getCard_id());
//                      //  parseLocation(bean);
//                    }
//                    break;
//            }
//        }
//    };

    //。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。复制原来项目,
//    @Override
//    public void onStateTure(String responseBody, String type) {
//        Intent intent = new Intent(getPackageName().concat(".safelocaiton"));
//        intent.putExtra("responseBody", responseBody);
//        sendBroadcast(intent);
//        try {
//
//            SafeLocationMode safeLocationMode = SafeLocationMode.getSafeLocationMode(new JSONObject(responseBody));
//            LatLng latLng = new LatLng(Double.valueOf(safeLocationMode.getCentreLocationX()),
//                    Double.valueOf(safeLocationMode.getCentreLocationY()));
//            float length = BaseMapActivity.getPoint2PointLength(latLng, new LatLng(Double.valueOf(safeLocationMode.getX()),
//                    Double.valueOf(safeLocationMode.getY())));
//            if (length > Double.valueOf(safeLocationMode.getSafetyRadius())) {
//                sendNotifycation();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    void sendNotifycation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            /**
             *  创建通知栏管理工具
             */

            NotificationManager notificationManager = (NotificationManager) getSystemService
                    (NOTIFICATION_SERVICE);

            /**
             *  实例化通知栏构造器
             */

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            /**
             *  设置Builder
             */
            //设置标题
            mBuilder.setContentTitle("幸福e区")
                    //设置内容
                    .setContentText("超出设定的安全范围，请注意！")
                    //设置大图标
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_fall))
                    //设置小图标
                    .setSmallIcon(R.drawable.icon_fall)
                    //设置通知时间
                    .setWhen(System.currentTimeMillis())
                    //首次进入时显示效果
                    .setTicker("超出设定的安全范围，请注意！")
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
                    .setContentText("超出设定的安全范围，请注意！")
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


}
