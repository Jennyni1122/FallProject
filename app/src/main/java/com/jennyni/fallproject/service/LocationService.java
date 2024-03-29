package com.jennyni.fallproject.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import com.google.gson.Gson;
import com.jennyni.fallproject.Bean.AskAllFallInfoBean;
import com.jennyni.fallproject.Bean.AskFallInfoBean;
import com.jennyni.fallproject.Bean.UserUpdateBean;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.activity.devicelocation.DevUserDetailActivity;
import com.jennyni.fallproject.net.NetWorkBuilder;
import com.jennyni.fallproject.receiver.NotifyReciver;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.UtilsHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LocationService extends Service {

    private static final String STOP_KEY = "stopservice";
    private static final String TAG = "LocationService";
    private static final int NOTIFICATION_ID = 0x13;
    private static final long OVERTIME = 1000 * 60 * 30;
    private NotificationManager notificationManager;

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
            sendrequest_allFAllInfo("");     //查询网络，查询设备最新数据（无围栏坐标点及围栏范围的参数）
            sendEmptyMessageDelayed(0, 30 * 1000);

        }
    };

    /**
     * 查询网络，查询设备最新数据
     */
    private void sendrequest_allFAllInfo(String cardid) {

        //10.请求设备最新数据(跌倒报警与地理围栏报警)
        //String url10 = "http://www.phyth.cn/index/fall/askfallinfo/account/" + account + "/cardid/" + cardid;

        String account = UtilsHelper.readLoginUserName(this);      //登录的用户名

        String url = Constant.BASE_WEBSITE + Constant.REQUEST_ASKALLFALLINFO_DEVICE_URL + "?account=" + account;
        Callback callback=new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //联网失败
                Log.e("MSG_MAPPAGE_FAIL", "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("MSG_MAPPAGE_OK", "请求成功：" + response);
                Gson gson = new Gson();
                String string = response.body().string();
                AskAllFallInfoBean askFallInfoBean =new AskAllFallInfoBean();
                try {
                    askFallInfoBean = gson.fromJson(string, AskAllFallInfoBean.class);
                    DevUserDetailActivity.sendBroadcastReceiver(LocationService.this,
                            LocationService.this.getPackageName().concat(DevUserDetailActivity.BroadcastReceiver_ACTION),
                            askFallInfoBean);
                }catch (Exception e){return;}
                if (askFallInfoBean != null && askFallInfoBean.getStatus() == 200) {
                    List<AskAllFallInfoBean.ResultBean> list = askFallInfoBean.getResult();
                    if (list.size() == 0) return;
                    Log.e(TAG, "开始遍历，长度：" + list.size());
                    for (final AskAllFallInfoBean.ResultBean bean : list) {
                        if (isOutOfRadius(bean)) {
//                             handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Log.e(TAG, String.format("%s在范围外，请注意!", bean.getName() == null ? "未知用户" : bean.getName()));
//                                        sendNotifycation(String.format("%s在范围外，请注意!", bean.getName() == null ? "未知用户" : bean.getName()), bean.getCard_id());
//                                    }
//                                });

                            if (!isOverTime(bean.getTime())) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e(TAG, String.format("%s在范围外，请注意!", bean.getName() == null ? "未知用户" : bean.getName()));
                                        sendNotifycation(String.format("%s在范围外，请注意!", bean.getName() == null ? "未知用户" : bean.getName()), bean.getCard_id());
                                    }
                                });
                            } else {
                                cancleNotify(NOTIFICATION_ID);
                            }
                        }

                        if (isFalled(bean)) {
//                            handler.post(new Runnable() {
//                                     @Override
//                                    public void run() {
//                                         Log.e(TAG, String.format("%s发生跌倒，请注意!", bean.getName() == null ? "未知用户" : bean.getName()));
//                                         sendNotifycation(String.format("%s发生跌倒，请注意!", bean.getName() == null ? "未知用户" : bean.getName()), bean.getCard_id());
//                                     }
//                                 });

                            if (!isOverTime(bean.getTime())) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e(TAG, String.format("%s发生跌倒，请注意!", bean.getName() == null ? "未知用户" : bean.getName()));
                                        sendNotifycation(String.format("%s发生跌倒，请注意!", bean.getName() == null ? "未知用户" : bean.getName()), bean.getCard_id());
                                    }
                                });
                            } else {
                                cancleNotify(NOTIFICATION_ID);
                            }

                        }

                    }
                }
            }
        };        //开启异步访问网络

        NetWorkBuilder.getInstance().getOkHttp(url,callback);
    }

    /**
     * 是否超时
     *
     * @param time
     * @return
     */
    boolean isOverTime(Long time) {
      //  return time*1000 + OVERTIME < System.currentTimeMillis();
        //超时为true
        long currentTimeMillis = System.currentTimeMillis();
        long l = time * 1000 + OVERTIME;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String fallTime= sdf.format(new Date(currentTimeMillis));
//        String nowTime= sdf.format(new Date(l));
        if (l < currentTimeMillis) {
                return true;
            }else {
                return false;
            }

    }

    /**
     * 是否跌倒
     *
     * @param
     * @return
     */
    boolean isFalled(AskAllFallInfoBean.ResultBean bean) {
        if (bean.getFall() == 1 || bean.getFall() == 2) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 范围外
     *
     * @return
     */
    boolean isOutOfRadius(AskAllFallInfoBean.ResultBean bean) {
        if (bean.getFence() == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getBooleanExtra(STOP_KEY, false)) {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    void cancleNotify(int notificationId) {
        if (notificationManager != null) {
            notificationManager.cancel(notificationId);
        }
    }


    void sendNotifycation(String str, String cardid) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            /**
             *  创建通知栏管理工具
             */

            notificationManager = (NotificationManager) getSystemService
                    (NOTIFICATION_SERVICE);

            /**
             *  实例化通知栏构造器
             */

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            Intent intent = new Intent(this, NotifyReciver.class);
            intent.putExtra("cardid", cardid);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, new Random().nextInt(1000), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            /**
             *  设置Builder
             */
            //设置标题
            mBuilder.setContentTitle(getString(R.string.app_name))
                    //设置内容
                    .setContentText(str)
                    //设置大图标
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_fall))
                    //设置小图标
                    .setSmallIcon(R.drawable.icon_fall)
                    //设置通知时间
                    .setWhen(System.currentTimeMillis())
                    //首次进入时显示效果
                    .setContentIntent(pendingIntent)
                    .setTicker(str)
                    //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                    .setDefaults(Notification.DEFAULT_SOUND);
            //发送通知请求
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        } else {
            Intent intent = new Intent(this, NotifyReciver.class);
            intent.putExtra("cardid", cardid);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, new Random().nextInt(1000), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            int notificationId = new Random().nextInt(Integer.MAX_VALUE);
            Notification.Builder builder = new Notification.Builder(this, "1"); //与channelId对应
            //icon title text必须包含，不然影响桌面图标小红点的展示
            builder.setContentIntent(pendingIntent);
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(str)
                    .setNumber(3)
                    .setDefaults(Notification.DEFAULT_SOUND); //久按桌面图标时允许的此条通知的数量
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


            NotificationChannel channel = new NotificationChannel("1",
                    "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }


}
