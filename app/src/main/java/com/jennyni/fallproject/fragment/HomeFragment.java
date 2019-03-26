package com.jennyni.fallproject.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.PullToRefreshView;
import com.jennyni.fallproject.Bean.UserLoginBean;
import com.jennyni.fallproject.Bean.UserUpdateBean;
import com.jennyni.fallproject.FallProjectApplication;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.activity.AddDeviceUserInfoActivity;
import com.jennyni.fallproject.activity.devicelocation.DevUserDetailActivity;
import com.jennyni.fallproject.adapter.DeviceListAdapter;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.JsonParse;
import com.jennyni.fallproject.utils.UtilsHelper;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 *
 * HomeFragment 用户列表 显示添加设备功能，解绑设备，点击列表项进入设备定位
 */

public class HomeFragment extends Fragment {    //implements DeviceListAdapter.LongClickListener
    public static final String TAG = "HomeFragment";
    private RelativeLayout rl_title_bar;
    private TextView tv_main_title, tv_switch, tv_none;
    private PullToRefreshView mPullToRefreshView;
    private RecyclerView recycleView;
    public static final int REFRESH_DELAY = 1000;
    public static final int MSG_DevUser_OK = 1; //加载设备，获取数据
    public static final int MSG_DelDev_OK = 2; //解绑设备，获取数据
    private DeviceListAdapter adapter;
    List<UserUpdateBean.ResultBean> devicelist;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //加载设备用户信息列表数据
        View view = initView(inflater, container);
        sendrequest_initData();
        return view;
    }


    private View initView(final LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //标题栏
        rl_title_bar = (RelativeLayout) view.findViewById(R.id.title_bar);
        rl_title_bar.setVisibility(View.VISIBLE);
        tv_main_title = (TextView) view.findViewById(R.id.tv_main_title);
        tv_main_title.setText("用户列表");
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_switch = view.findViewById(R.id.tv_save);
        tv_switch.setVisibility(View.VISIBLE);
        tv_switch.setText("添加设备");
        tv_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到添加设备界面
                Intent intent = new Intent(getActivity(), AddDeviceUserInfoActivity.class);
                startActivity(intent);
            }
        });
        tv_none = (TextView) view.findViewById(R.id.tv_none);     //无设备时显示的文本
        //下拉刷新
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.
                pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.
                OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        sendrequest_initData();      //加载设备用户信息列表数据

                    }
                }, REFRESH_DELAY);
            }
        });

        //列表框
        recycleView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //这里用线性显示 类似于	ListView
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DeviceListAdapter(getActivity());
        recycleView.setAdapter(adapter);
        //列表项单击事件
        adapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {          //点击列表项进入设备定位界面
                UserUpdateBean.ResultBean resultBean = devicelist.get(position);
                DevUserDetailActivity.startActivity(getActivity(), resultBean.getCard_id());
            }


            @Override
            public void onItemLongClick(View view, int position) {      //长按解绑
                String card_id = devicelist.get(position).getCard_id();
                showDelDevDialog(card_id);         //显示删除对话框
            }
        });

        return view;
    }

    /**
     * 显示是否删除的提示框
     */
    private void showDelDevDialog(final String cardid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendrequest_delDevice(cardid);        //请求网络，删除设备
                dialog.dismiss();
            }
        }).setMessage("跌倒守护：确定解绑设备吗？").create();
        dialog.show();
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
                case MSG_DevUser_OK:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        Log.e(TAG, "handleMessage" + result);
                        //使用Gson解析数据
                        devicelist = JsonParse.getInstance().getuserUpdateInfo(result);
                        if (devicelist != null) {
                            if (devicelist.size() > 0) {
                                adapter.setData(devicelist);
                            } else {
                                //无数据显示"暂无设备信息~"
                                tv_none.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        //无数据显示"暂无设备信息~"
                        tv_none.setVisibility(View.VISIBLE);
                    }
                    break;
                case MSG_DelDev_OK:         //解绑设备
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        Log.e(TAG, "handleMessage" + result);
                        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        //重新请求刷新刷新数据= =按道理这里应该是请求刷新方法的，但是这样写直接一点，以后写多了就好了
                        sendrequest_initData();
                    }
                    break;
            }
        }
    };


    /**
     * 加载设备用户信息列表数据
     */
    private void sendrequest_initData() {
        String account = UtilsHelper.readLoginUserName(getActivity());
        String url = Constant.BASE_WEBSITE + Constant.REQUEST_UPDATE_USER_URL + "/account/" + account;
        Log.e(TAG, "initData: " + url);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "MSG_DevUser_FAIL" + "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.e(TAG, "MSG_DevUser_OK" + "请求成功：" + res);
                Message msg = new Message();
                msg.what = MSG_DevUser_OK;
                msg.obj = res;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 请求网络，解绑设备
     */
    private void sendrequest_delDevice(String cardid) {
        //String url6 = "http://www.phyth.cn/index/fall/delDevice/account/" + account + "/cardid/" + cardid;
        String account = UtilsHelper.readLoginUserName(getActivity());
        String url = Constant.BASE_WEBSITE + Constant.REQUEST_DEL_DEVICE_URL + "/account/" + account + "/cardid/" + cardid;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //开启异步访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //联网失败
                Log.e("MSG_FAIL", "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("MSG_OK", "请求成功：" + response);
                String str = JsonParse.getInstance().getDelDeviceInfo(response.body().string());
                Log.e("MSG_OK", str);
                Message message = new Message();
                message.what = MSG_DelDev_OK;
                message.obj = str;
                handler.sendMessage(message);
            }
        });

    }

}
