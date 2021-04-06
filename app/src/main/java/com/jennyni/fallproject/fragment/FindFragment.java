package com.jennyni.fallproject.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.WrapperListAdapter;

import com.itheima.PullToRefreshView;
import com.jennyni.fallproject.Bean.AskAllFallInfoBean;
import com.jennyni.fallproject.Bean.UserUpdateBean;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.activity.devicelocation.DevUserDetailActivity;
import com.jennyni.fallproject.adapter.WarningAdapter;
import com.jennyni.fallproject.service.LocationService;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.DBUtils;
import com.jennyni.fallproject.utils.JsonParse;
import com.jennyni.fallproject.utils.UtilsHelper;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 针对所有用户的跌倒报警信息：
 * 注意：这里使用的url：手机端刷新设备信息
 * 侧滑每条信息会出现红色删除按钮，实现下拉刷新或实时自动刷新，发出报警提示声音
 */
public class FindFragment extends Fragment implements WarningAdapter.IonSlidingViewClickListener {
    public static final String TAG = "FindFragment";
    private TextView tv_main_title, tv_none;
    private RelativeLayout rl_title_bar;
    private Context context;
    private RecyclerView recycleView;
    private WarningAdapter adapter;
    private PullToRefreshView mPullToRefreshView;
    public static final int REFRESH_DELAY = 1000;
    public static final int MSG_ALLFALL_OK = 1;                //获取数据
    public static final int MSG_ALLFALL_FAIL = 2;
    List<AskAllFallInfoBean.ResultBean> list;
    private String account;     //手机登录账户

    public FindFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater, container);
        sendrequest_allfallData();
        return view;
    }
    private void startNotifyService() {
        LocationService.startService(getActivity());
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);

        account = UtilsHelper.readLoginUserName(getActivity());
        //标题栏
        tv_main_title = (TextView) view.findViewById(R.id.tv_main_title);
        tv_main_title.setText("消息列表");
        rl_title_bar = (RelativeLayout) view.findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        tv_none = (TextView) view.findViewById(R.id.tv_none);
        //列表
        recycleView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WarningAdapter(getActivity(), this);
        recycleView.setAdapter(adapter);
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
                            sendrequest_allfallData();      //加载设备用户信息列表数据,获取报警信息

                    }
                }, REFRESH_DELAY);
            }
        });

        return view;
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
                case MSG_ALLFALL_OK:
                    if (msg.obj != null) {
                        //获取数据
                        String result = (String) msg.obj;
                        Log.e(TAG, "handleMessage:" + result);
                        list = JsonParse.getInstance().getAskAllFallInfo(result);
                        if (list != null) {
                            if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    AskAllFallInfoBean.ResultBean bean = list.get(i);
                                    if (bean.getFall() != 2 && bean.getFall() != 1 && bean.getFence() != 1) { //过滤
                                        list.remove(bean);
                                        i--;
                                    }
                                }
                                adapter.setData(list);
                            } else {
                                //无数据显示"暂无报警信息~"
                                tv_none.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        //无数据显示"暂无报警信息~"
                        tv_none.setVisibility(View.VISIBLE);
                    }
                    break;
                case MSG_ALLFALL_FAIL:
                    Toast.makeText(getActivity(), "加载报警信息失败~", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 请求网络，显示报警信息
     */
    private void sendrequest_allfallData() {

        String account = UtilsHelper.readLoginUserName(getActivity());
        String url = Constant.BASE_WEBSITE + Constant.REQUEST_ASKALLFALLINFO_DEVICE_URL + "?account=" + account;
        Callback callback=new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //联网失败
                Log.e(TAG, "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "请求成功：" + response);
                String res = response.body().string();
                Message message = new Message();
                message.what = MSG_ALLFALL_OK;
                message.obj = res;
                handler.sendMessage(message);
            }
        };
        //开启异步访问网络



    }



    //点击事件
    @Override
    public void onItemClick(View view, int position) {
        //(空指针)
//        Intent intent = new Intent(context, DevUserDetailActivity.class);
//        intent.putExtra("askfallinfo", (Serializable) list.get(position));
//        intent.putExtra("position", position + "");
//        startActivityForResult(intent, 1);
    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        adapter.removeData(position, tv_none, account);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String position = data.getStringExtra("position");
            adapter.removeData(Integer.parseInt(position), tv_none, account);
        }

    }
}
