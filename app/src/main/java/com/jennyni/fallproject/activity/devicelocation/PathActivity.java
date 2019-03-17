package com.jennyni.fallproject.activity.devicelocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.jennyni.fallproject.Bean.AskTrackBetweenBean;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.JsonParse;
import com.jennyni.fallproject.utils.UtilsHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ..................解析里的url的cardid,d1,d2
 * 显示设备轨迹
 */
public class PathActivity extends BaseMapActivity {
    private TextView tv_main_title, tv_back;
    private RelativeLayout rl_title_bar;

    private static final int MSG_ASKTRACK_OK = 1;
    private static final String STARTTIME_KEY = "d1";
    private static final String ENDTIME_KEY = "d2";
    private static final String CARDID_KEY = "cardid";
    public static final String SAFEMODE_KEY = "safemode_key";

    private String startTime, endTime, account, cardid;


    public static void startActivity(Context context, String startTime, String endTime, String cardid) {
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
        //cardid = ..............................;
        sendrequest_askTrace();
        // mode = (SafeLocationMode) getIntent().getSerializableExtra(SAFEMODE_KEY);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        //标题栏
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("设备轨迹");
        tv_back = (TextView) findViewById(R.id.tv_back);
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mode != null) {
//            LatLng latLng = new LatLng(Double.valueOf(mode.getCentreLocationX()),
//                    Double.valueOf(mode.getCentreLocationY()));
//            addcircle(latLng, Double.valueOf(Double.valueOf(mode.getSafetyRadius())), Color.parseColor("#7ffe841e"));
//            addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.ic_centrallocation));
//        }
//    }


    /**
     * 事件捕获
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
                                addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.trace_1));
                                return;
                            }

                            for (int i = 0; i < modelist.size(); i++) {
                                AskTrackBetweenBean.ResultBean pathMode = modelist.get(i);
                                LatLng latLng = new LatLng(Double.valueOf(pathMode.getLat()), Double.valueOf(pathMode.getLng()));
                                if (i == 0) {//起点
                                    moveToPoint(latLng);
                                    addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.trace_1));
                                } else if (i == modelist.size() - 1) {//终点
                                    addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.trace_2));
                                } else {
                                    addMarker(latLng, BitmapFactory.decodeResource(getResources(), R.drawable.location_marker));
                                }
                                list.add(latLng);
                            }
//                        //轨迹线
                            addLinePath(list, getResources().getColor(R.color.grassgreen));
                        }
                        break;
                    }
            }

        }
    };

    /**
     * 请求网络，查询设备定位
     */

    private void sendrequest_askTrace() {
        //12.请求查询历史设备运动轨迹： (d1>d2)
        //String url12 = "http://www.phyth.cn/index/fall/askTrackBetween/account/"+account+"/cardid/"+cardid+"/d1/"+ cardpass+"/d2/0";
        String url = Constant.BASE_WEBSITE + Constant.REQUEST_ASKTRACKBETWEEN_DEVICE_URL +
                "/account/" + account + "/cardid/" + cardid + "/d1/" + startTime + "/d2/" + endTime;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //开启异步访问网络
        call.enqueue(new Callback() {
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
        });

    }


}
