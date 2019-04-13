package com.jennyni.fallproject.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.google.gson.Gson;
import com.jennyni.fallproject.Bean.AskDevInfoBean;
import com.jennyni.fallproject.Bean.SetUpBean;
import com.jennyni.fallproject.Bean.UserUpdateBean;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.activity.devicelocation.DevUserDetailActivity;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.JsonParse;
import com.jennyni.fallproject.utils.StringUtil;
import com.jennyni.fallproject.utils.UtilsHelper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.jennyni.fallproject.activity.AddDeviceUserInfoActivity.REQUEST_CODE;

/**
 * 编辑设备用户信息
 */
public class EditDevUserActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "EditDevice";
    public static final int REQUEST_CODE1 = 0x13;
    private static final int ISGEO_OPEN = 1; //围栏开启
    private RelativeLayout rl_title_bar;
    private LinearLayout ll_addressfence;
    private TextView tv_main_title, tv_back, tv_switch, tv_guardian, tv_geocenter;
    private EditText et_device_name, et_idcard, et_georadius;
    private RadioGroup sex_event, set_event;
    private RadioButton male, female, open, close;
    private ImageView iv_head_icon;
    private String spUserPhone, cardid;         //定义 获取登录时的用户名
    private String currentDevCode, currentDevPsw;
    public static final int MSG_SHOW_OK = 1;
    public static final int MSG_EDIT_OK = 2;
    String isgeo, issex, dname, idcard, geocenter, georadius;
    List<UserUpdateBean.ResultBean> devicelist;
    private static final String CARDID_KEY = "cardid";

    public static void startActivity(Context context, String cardid) {

        Intent intent = new Intent(context, EditDevUserActivity.class);
        intent.putExtra(CARDID_KEY, cardid);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dev_user);

        initView();         //初始化控件
        setListener();      //控件的点击事件

        sendrequest_askDevInfo();       //请求网络，加载设备用户信息
    }


    private void initView() {
        //标题栏
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("编辑用户信息");
        tv_back = findViewById(R.id.tv_back);
        tv_switch = findViewById(R.id.tv_save);
        tv_back.setVisibility(View.VISIBLE);
        tv_switch.setVisibility(View.VISIBLE);
        tv_switch.setText("保存");
        //设备用户信息
        tv_guardian = findViewById(R.id.tv_guardian);       //监护电话（默认手机号）
        tv_geocenter = findViewById(R.id.tv_geocenter);     //地理围栏中心点(围栏开启可见)
        et_device_name = findViewById(R.id.et_device_name); //输入设备用户名
        et_idcard = findViewById(R.id.et_idcard);           //输入身份证
        et_georadius = findViewById(R.id.et_georadius);     //输入围栏半径（围栏开启可见）
        iv_head_icon = findViewById(R.id.iv_head_icon);     //头像根据性别变化
        ll_addressfence = findViewById(R.id.ll_addressfence);//用于设置地理围栏开启之后操作
        //单击按钮：性别，围栏设置
        male = (RadioButton) findViewById(R.id.man);
        female = (RadioButton) findViewById(R.id.woman);
        close = (RadioButton) findViewById(R.id.close);
        open = (RadioButton) findViewById(R.id.open);
        sex_event = (RadioGroup) findViewById(R.id.sex_event);           //性别选择
        set_event = (RadioGroup) findViewById(R.id.set_event);           //地理围栏选择
        sex_event.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton sex = findViewById(checkedId);
                //        Toast.makeText(AddDeviceUserInfoActivity.this, "您选择了：" + sex.getText().toString(), Toast.LENGTH_SHORT).show();
                if (checkedId == R.id.man) {
                    issex = "男";
                    //头像根据性别变化
                    iv_head_icon.setImageResource(R.drawable.icon_male);
                } else {
                    issex = "女";
                    iv_head_icon.setImageResource(R.drawable.icon_female);
                }
            }
        });
        set_event.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton set = findViewById(checkedId);
                //    Toast.makeText(AddDeviceUserInfoActivity.this, "您选择了：" + set.getText().toString(), Toast.LENGTH_SHORT).show();
                if (set.getText().toString().equals("开启")) {
                    isgeo = "1";
                    ll_addressfence.setVisibility(View.VISIBLE);
                } else {
                    ll_addressfence.setVisibility(View.GONE);
                    isgeo = "0";
                    tv_geocenter.setText("");           //围栏关闭时内容为空
                }

            }
        });

        //从SharedPreferences中获取登录时的用户名
        spUserPhone = UtilsHelper.readLoginUserName(this);
        tv_guardian.setText(spUserPhone);

    }

    private void setListener() {
        tv_back.setOnClickListener(this);           //返回键
        tv_switch.setOnClickListener(this);         //保存设备用户信息按钮
        tv_geocenter.setOnClickListener(this);      //设置地理围栏：选择地址按钮
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:      //返回按钮
                EditDevUserActivity.this.finish();
                break;
            case R.id.tv_save:      //保存按钮

                sendrequest_saveData();             //请求网络，修改保存用户信息
                break;
            case R.id.tv_geocenter:     //围栏设置
                Intent intent = new Intent(EditDevUserActivity.this, GetAddressByKeyword.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;

        }
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
                case MSG_SHOW_OK:        //查看用户数据
                    if (msg.obj != null) {
                        //获取数据
                        String response = (String) msg.obj;
                        Gson gson = new Gson();
                        final AskDevInfoBean askDevInfoBean = gson.fromJson(response, AskDevInfoBean.class);

                        if (askDevInfoBean.getStatus() == 200) {
                            final AskDevInfoBean.ResultBean devinfobean = askDevInfoBean.getResult();
                            Log.e("TAG", "handleMessage:" + devinfobean.getDname());
                            //显示用户信息数据
                            et_device_name.setText(devinfobean.getDname()); //设备用户名
                            et_idcard.setText(devinfobean.getIdcard());     //身份证
                            tv_guardian.setText(spUserPhone);               //监护人
//                            //性别设置，男，则按钮为男，头像为老爷爷；  女，按钮为女，头像为老奶奶
                            if ("男".equals(devinfobean.getDsex())) {
                                iv_head_icon.setImageResource(R.drawable.icon_male);
                                male.setChecked(true);
                            } else {
                                iv_head_icon.setImageResource(R.drawable.icon_female);
                                female.setChecked(true);
                            }


                            //围栏开启设备，围栏开启：设置围栏内容可见，显示中心点经纬度，显示半径；  围栏关闭：显示不可见
                            ll_addressfence.setVisibility(ISGEO_OPEN == devinfobean.getIsgeo() ? View.VISIBLE : View.GONE);
                            tv_geocenter.setText(devinfobean.getGeocenter());
                            et_georadius.setText(String.valueOf(devinfobean.getGeoradius()));
                            open.setChecked(ISGEO_OPEN == devinfobean.getIsgeo());
                            close.setChecked(ISGEO_OPEN != devinfobean.getIsgeo());
                            currentDevCode=cardid;

//                            set_event.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                                @Override
//                                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                                    RadioButton set = findViewById(checkedId);
//                                    if (devinfobean.getIsgeo() == 1) {
//
//                                        set.getText().toString().equals("开启");
//                                        ll_addressfence.setVisibility(View.VISIBLE);
//                                        tv_geocenter.setText(devinfobean.getGeocenter());
//                                        et_georadius.setText(String.valueOf(devinfobean.getGeoradius()));
//                                    } else {
//
//                                        ll_addressfence.setVisibility(View.INVISIBLE);
//                                    }
//                                }
//                            });
                        }

                    }
                    break;
                case MSG_EDIT_OK:        //修改用户信息，进行上传
                    if (msg.obj != null) {
                        //获取数据
                        if (msg.obj!=null){
                            SetUpBean.ResultBean setupbean = (SetUpBean.ResultBean) msg.obj;
                            Log.e("TAG", "handleMessage:" + setupbean.getDev_name());
                            Toast.makeText(EditDevUserActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(EditDevUserActivity.this, "保存遇到问题~", Toast.LENGTH_SHORT).show();
                        }

                    }
                    break;
            }
        }
    };


    /**
     * 请求网络，显示设备用户信息
     */
    private void sendrequest_askDevInfo() {

        cardid = getIntent().getStringExtra(CARDID_KEY);
        String url = Constant.BASE_WEBSITE + Constant.REQUEST_ASKDEVINFO_DEVICE_URL
                + "?account=" + spUserPhone + "&cardid=" + cardid;
        Log.e(TAG, url);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //开启异步访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "MSG_SHOW_FAIL" + "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "MSG_SHOW_OK" + "请求成功：" + response);
//                String str = JsonParse.getInstance().getAskDevInfo();
//                Log.e(TAG, "MSG_SHOW_OK" + str);
                Message message = new Message();
                message.what = MSG_SHOW_OK;
                message.obj = response.body().string();
                handler.sendMessage(message);

            }
        });

    }

    void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 修改设备用户信息，进行保存上传
     */
    private void sendrequest_saveData() {
        dname = et_device_name.getText().toString().trim();
        idcard = et_idcard.getText().toString().trim();
        geocenter = tv_geocenter.getText().toString();
        georadius = et_georadius.getText().toString();

        if (StringUtil.isEmpty(dname)) {
            toast("设备用户不能为空");
            return;
        }

        if (StringUtil.isEmpty(idcard)) {
            toast("身份证不能为空");
            return;
        }


        /**
         * http://www.phyth.cn/index/fall/setup/account/18860900316/cardid/18255180000/
         * guardian/13330000888/pilltime1/081080/isgeo/true/geocenter/115.672126,38.817129/georadius/1000
         */

        String url = Constant.BASE_WEBSITE + Constant.REQUEST_SETUP_DEVICE_URL +
                "?account=" + spUserPhone +
                "&cardid=" + currentDevCode +
                "&dname=" + URLEncoder.encode(dname) +
                "&sex=" + URLEncoder.encode(male.isChecked() ? "男" : "女") +
                "&idcard=" + idcard +
              //  "&guardian=" + spUserPhone +
                "&isgeo=" + (close.isChecked() ? "0" : "1") +
                "&geocenter=" + URLEncoder.encode(geocenter) +
                "&georadius=" + georadius;
        Log.e(TAG, url);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //开启异步访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "MSG_EDIT_FAIL" + "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "MSG_EDIT_OK" + "请求成功：" + response);
                SetUpBean.ResultBean setupInfo = JsonParse.getInstance().getSetupInfo(response.body().string());
                Message message = new Message();
                message.what = MSG_EDIT_OK;
                message.obj = setupInfo;
                handler.sendMessage(message);
            }
        });

    }

    /**
     * 从选择地址界面回传经纬度到编辑界面
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == REQUEST_CODE1) {
                LatLonPoint geocenter = data.getParcelableExtra("geocenter");
                if (geocenter != null) {
                    tv_geocenter.setText(geocenter.getLongitude() + "," + geocenter.getLatitude());    //获取经纬度
                }
            }
        }
    }


}
