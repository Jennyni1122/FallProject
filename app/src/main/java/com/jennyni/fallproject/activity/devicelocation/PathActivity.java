package com.jennyni.fallproject.activity.devicelocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.jennyni.fallproject.Bean.AskTodayTrackBean;
import com.jennyni.fallproject.Bean.AskTrackBetweenBean;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.net.NetWorkBuilder;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.JsonParse;
import com.jennyni.fallproject.utils.UtilsHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *
 * 显示设备轨迹
 */
public class PathActivity extends BaseMapActivity {
    private TextView tv_main_title, tv_back,tv_select_time;
    private RelativeLayout rl_title_bar;
    private static  final int MSG_TODAY_OK = 1;
    private static final int MSG_ASKTRACK_OK = 2;
    private static final String STARTTIME_KEY = "d1";
    private static final String ENDTIME_KEY = "d2";
    private static final String CARDID_KEY = "cardid";
    public static final String SAFEMODE_KEY = "safemode_key";
    private static final String DEVICEBEAN_KEY = "devicebean";
    private String startTime, endTime, account, cardid;


    /**
     * 设备定位界面到轨迹界面的参数参数传递
     * @param context
     * @param cardid
     */
    public static void startActivity(Context context, String cardid) {
        Intent intent = new Intent(context, PathActivity.class);
        intent.putExtra(CARDID_KEY, cardid);
        context.startActivity(intent);
    }

    /**
     * 选择时间界面到轨迹界面的参数传递
     * @param context
     * @param startTime
     * @param endTime
     * @param cardid
     */
    public static void startActivity( Context context, String startTime, String endTime, String cardid) {
        Intent intent = new Intent(context, PathActivity.class);
        intent.putExtra(STARTTIME_KEY, startTime);
        intent.putExtra(ENDTIME_KEY, endTime);
        intent.putExtra(CARDID_KEY, cardid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        initMap(savedInstanceState);
        initView();     //初始化控件
        startTime = getIntent().getStringExtra(STARTTIME_KEY);
        endTime = getIntent().getStringExtra(ENDTIME_KEY);
        cardid = getIntent().getStringExtra(CARDID_KEY);
        account = UtilsHelper.readLoginUserName(this);
        sendrequest_askTodayTrace();    //查询今日运动轨迹
        sendrequest_askTrace(); //查询选择日期之间的运动轨迹

    }



    /**
     * 初始化控件
     */
    private void initView() {
        //标题栏
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("设备轨迹");
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_select_time = findViewById(R.id.tv_save);
        tv_select_time.setText("选择时间");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_back.setVisibility(View.VISIBLE);
        tv_select_time.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new View.OnClickListener() {     //返回设备定位界面
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_select_time.setOnClickListener(new View.OnClickListener() {      //点击进入选择时间日期
            @Override
            public void onClick(View v) {

            //跳转到选择时间界面，将解析数据传值
             SelectTimeActivity.startActivity(PathActivity.this,cardid);
            }
        });
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
                case MSG_TODAY_OK:
                if (msg.obj != null){
                    //获取数据
                    String result = (String) msg.obj;
                    Log.e("handleMessage", result);
                    List<AskTodayTrackBean.ResultBean> todaylist = JsonParse.getInstance().getAskTodayTraceInfo(result);
                    if (todaylist == null) {
                        showToast("无轨迹或无绑定关系");
                    } else {
                        List<LatLng> list = new ArrayList<>();
                        if (todaylist.size() == 1) {
                            AskTodayTrackBean.ResultBean pathMode = todaylist.get(0);
                            LatLng latLng = new LatLng(Double.valueOf(pathMode.getLat()), Double.valueOf(pathMode.getLng()));
                            addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.trace1));
                            return;
                        }

                        for (int i = 0; i < todaylist.size(); i++) {
                            AskTodayTrackBean.ResultBean pathMode =todaylist.get(i);
                            LatLng latLng = new LatLng(Double.valueOf(pathMode.getLat()), Double.valueOf(pathMode.getLng()));
                            if (i == 0) {//起点
                                moveToPoint(latLng);
                                addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.trace1));
                            } else if (i == todaylist.size() - 1) {//终点
                                addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.trace2));
                            } else {
                                addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.location_marker1));
                            }
                            list.add(latLng);
                        }
                        //轨迹线
                        addLinePath(list, getResources().getColor(R.color.grassgreen));
                    }

                }
                break;
                case MSG_ASKTRACK_OK:
                    if (msg.obj != null) {
                        //获取数据
                        String result = (String) msg.obj;
                        Log.e("handleMessage", result);
                        List<AskTrackBetweenBean.ResultBean> modelist = JsonParse.getInstance().getAskTraceBetweenInfo(result);
                        if (modelist == null) {
                            showToast("无轨迹或无绑定关系");
                        } else {
                            List<LatLng> list = new ArrayList<>();
                            if (modelist.size() == 1) {
                                AskTrackBetweenBean.ResultBean pathMode = modelist.get(0);
                                LatLng latLng = new LatLng(Double.valueOf(pathMode.getLat()), Double.valueOf(pathMode.getLng()));
                                addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.trace1));
                                return;
                            }

                            for (int i = 0; i < modelist.size(); i++) {
                                AskTrackBetweenBean.ResultBean pathMode = modelist.get(i);
                                LatLng latLng = new LatLng(Double.valueOf(pathMode.getLat()), Double.valueOf(pathMode.getLng()));
                                if (i == 0) {//起点
                                    moveToPoint(latLng);
                                    addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.trace1));
                                } else if (i == modelist.size() - 1) {//终点
                                    addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.trace2));
                                } else {
                                    addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.location_marker1));
                                }
                                list.add(latLng);
                            }
                            //轨迹线
                            addLinePath(list, getResources().getColor(R.color.rdTextColorPress));
                        }
                    }
                    break;
            }

        }
    };


    /**
     * 请求网络，查询今日运动轨迹
     */
    private void sendrequest_askTodayTrace() {
         //11.今日运动轨迹
        String url = Constant.BASE_WEBSITE+Constant.REQUEST_TODAY_TRACE_URL +"?account=" +account+"&cardid="+cardid;
        Callback callback=new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //联网失败
                Log.e("MSG_TODAY_FAIL", "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("MSG_TODAY_OK", "请求成功：" + response);
                String res = response.body().string();
                Log.e("MSG_OK", res);
                Message message = new Message();
                message.what = MSG_ASKTRACK_OK;
                message.obj = res;
                handler.sendMessage(message);
            }
        };
        //开启异步访问网络
        NetWorkBuilder.getInstance().getOkHttp(url,callback);


    }


    /**
     * 请求网络，查询设备定位（两个日期之间）
     */

    private void sendrequest_askTrace() {
        //12.请求查询历史设备运动轨迹： (d1>d2)
        //String url12 = "http://www.phyth.cn/index/fall/askTrackBetween/account/"+account+"/cardid/"+cardid+"/d1/"+ cardpass+"/d2/0";
        String url = Constant.BASE_WEBSITE + Constant.REQUEST_ASKTRACKBETWEEN_DEVICE_URL +
                "?account=" + account + "&cardid=" + cardid + "&d1=" + startTime + "&d2=" + endTime;
        Callback callback=new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //联网失败
                Log.e("MSG_TRACE_FAIL", "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("MSG_TRACK_OK", "请求成功：" + response);
                String res = response.body().string();
                Log.e("MSG_OK", res);
                Message message = new Message();
                message.what = MSG_ASKTRACK_OK;
                message.obj = res;
                handler.sendMessage(message);
            }
        };
        //开启异步访问网络
        NetWorkBuilder.getInstance().getOkHttp(url,callback);

    }


}
