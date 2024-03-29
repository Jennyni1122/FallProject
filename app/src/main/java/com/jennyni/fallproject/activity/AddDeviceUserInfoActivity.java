package com.jennyni.fallproject.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
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
import com.jennyni.fallproject.Bean.SetUpBean;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.net.NetWorkBuilder;
import com.jennyni.fallproject.utils.ActivityCollectorUtil;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.JsonParse;
import com.jennyni.fallproject.utils.StringUtil;
import com.jennyni.fallproject.utils.UtilsHelper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 添加设备用户信息，绑定设备
 */
public class AddDeviceUserInfoActivity extends ActivityCollectorUtil implements View.OnClickListener {
    public static final String TAG = "AddDevice";
    public static final int REQUEST_CODE = 0x12;
    private RelativeLayout rl_title_bar;
    private LinearLayout ll_addressfence;
    private TextView tv_main_title, tv_back, tv_switch, tv_guardian, tv_geocenter, tv_result;
    private EditText et_device_name, et_idcard, et_dev_cardid, et_dev_psw, et_georadius;
    private RadioGroup sex_event, set_event;
    private RadioButton male, female, open, close;
    private Button btn_sure_code;
    private ImageView iv_head_icon, iv_show_psw;
    private boolean isShowPsw = false;    //是否展示密码
    private String spUserPhone;         //定义 获取登录时的用户名
    private String currentDevCode, currentDevPsw;
    public static final int MSG_ADDUSER_OK = 1;
    public static final int MSG_BINDDEV_OK = 2;
    String isgeo, issex, dname, idcard, geocenter, georadius = "1000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_add_device_user_info);

        initView();         //初始化控件
        setListener();      //控件的点击事件
    }

    @Override
    protected void onDestroy() {
        removeActivity(this);
        super.onDestroy();

    }

    private void initView() {
        //标题栏
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("添加用户信息");
        tv_back = findViewById(R.id.tv_back);
        tv_switch = findViewById(R.id.tv_save);
        tv_back.setVisibility(View.VISIBLE);
        tv_switch.setVisibility(View.VISIBLE);
        tv_switch.setText("保存");
        //设备用户信息
        tv_guardian = findViewById(R.id.tv_guardian);       //监护电话（默认手机号）
        tv_result = findViewById(R.id.tv_result);           //验证绑定关系的结果显示
        tv_geocenter = findViewById(R.id.tv_geocenter);     //地理围栏中心点(围栏开启可见)
        et_device_name = findViewById(R.id.et_device_name); //输入设备用户名
        et_idcard = findViewById(R.id.et_idcard);           //输入身份证
        et_dev_cardid = findViewById(R.id.et_dev_cardid);   //输入设备号
        et_dev_psw = findViewById(R.id.et_dev_psw);         //输入设备密码
        et_georadius = findViewById(R.id.et_georadius);     //输入围栏半径（围栏开启可见）
        btn_sure_code = findViewById(R.id.btn_sure_code);   //验证绑定关系按钮
        iv_show_psw = findViewById(R.id.iv_show_psw);       //设备密码可见设置
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
        tv_guardian.setText(spUserPhone);           //紧急联系电话默认为手机登录号
    }


    /**
     * 设置控件的点击监听事件
     */
    private void setListener() {
        btn_sure_code.setOnClickListener(this);     //设备绑定关系验证按钮
        iv_show_psw.setOnClickListener(this);       //设备密码可见设置
        tv_back.setOnClickListener(this);           //返回键
        tv_switch.setOnClickListener(this);         //保存设备用户信息按钮
        tv_geocenter.setOnClickListener(this);      //设置地理围栏：选择地址按钮
    }

    /**
     * 控件的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:          //返回按钮
                AddDeviceUserInfoActivity.this.finish();
                break;
            case R.id.tv_save:          //保存设备用户信息

                //身份证格式
                Pattern p = Pattern.compile("[0-9]{17,17}[0-9X]");
                Matcher m = p.matcher(et_idcard.getText().toString());
                if (m.matches()) {
                    //Intent跳转
                    if (isNetworkAvaible(AddDeviceUserInfoActivity.this)) {

                    } else {
                        Toast.makeText(AddDeviceUserInfoActivity.this, "当前网络错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddDeviceUserInfoActivity.this, "请输入合法的身份证号", Toast.LENGTH_SHORT).show();
                }

                sendrequest_saveData();       //从服务器上拉数据

                break;
            case R.id.iv_show_psw:      //密码可见设置
                currentDevPsw = et_dev_psw.getText().toString();
                if (isShowPsw) {
                    iv_show_psw.setImageResource(R.drawable.hide_psw_icon);
                    et_dev_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());   //隐藏密码
                    isShowPsw = false;
                    if (currentDevPsw == null) {
                        et_dev_psw.setSelection(currentDevPsw.length());
                    }
                } else {
                    iv_show_psw.setImageResource(R.drawable.show_psw_icon); //显示密码
                    et_dev_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//隐藏回车
                    isShowPsw = true;
                    if (currentDevPsw != null) {
                        et_dev_psw.setSelection(currentDevPsw.length());
                    }
                }
                break;
            case R.id.tv_geocenter:       //围栏地址选择
                Intent intent = new Intent(AddDeviceUserInfoActivity.this, GetAddressByKeyword.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.btn_sure_code:    //验证绑定关系按钮

                sendrequest_recode();     //请求网络，验证绑定关系
                break;
        }

    }


    void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 保存设备用户信息，进行上传
     */
    private void sendrequest_saveData() {
        dname = et_device_name.getText().toString().trim();
        idcard = et_idcard.getText().toString().trim();
        currentDevCode = et_dev_cardid.getText().toString();
        currentDevPsw = et_dev_psw.getText().toString();
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
        if (StringUtil.isEmpty(currentDevCode)) {
            toast("设备号不能为空");
            return;
        }
        if (StringUtil.isEmpty(currentDevPsw)) {
            toast("设备密码不能为空");
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
            //    "&guardian=" + spUserPhone +
                "&isgeo=" + (close.isChecked() ? "0" : "1") +
                "&geocenter=" + URLEncoder.encode(geocenter) +
                "&georadius=" + georadius;
        Log.e(TAG, url);
        Callback callback=new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "MSG_ADDUSER_FAIL" + "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "MSG_ADDUSER_OK" + "请求成功：" + response);
                SetUpBean.ResultBean setupInfo = JsonParse.getInstance().getSetupInfo(response.body().string());
                //Log.e(TAG, "MSG_ADDUSER_OK" + str);
                Message message = new Message();
                message.what = MSG_ADDUSER_OK;
                message.obj = setupInfo;
                handler.sendMessage(message);
            }
        };
        NetWorkBuilder.getInstance().getOkHttp(url,callback);

//        OkHttpClient okHttpClient = new OkHttpClient();
//        final Request request = new Request.Builder().url(url).build();
//        Call call = okHttpClient.newCall(request);
//        //开启异步访问网络
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e(TAG, "MSG_ADDUSER_FAIL" + "请求失败：" + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.e(TAG, "MSG_ADDUSER_OK" + "请求成功：" + response);
//                SetUpBean.ResultBean setupInfo = JsonParse.getInstance().getSetupInfo(response.body().string());
////                Log.e(TAG, "MSG_ADDUSER_OK" + str);
//                Message message = new Message();
//                message.what = MSG_ADDUSER_OK;
//                message.obj = setupInfo;
//                handler.sendMessage(message);
//            }
//        });

    }

    /**
     * 请求网络，验证设备绑定关系（注：设备号和密码是给定不变的）
     */
    private void sendrequest_recode() {
        currentDevCode = et_dev_cardid.getText().toString();
        currentDevPsw = et_dev_psw.getText().toString();
        String url = Constant.BASE_WEBSITE + Constant.REQUEST_ADD_DEVICE_URL
                + "?account=" + spUserPhone + "&cardid=" + currentDevCode + "&cardpass=" + currentDevPsw;
        Callback callback=new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //联网失败
                Log.e(TAG, "MSG_BIND_FAIL" + "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "MSG_BINDDEV_OK" + "请求成功：" + response);
                String str = JsonParse.getInstance().getAddDeviceInfo(response.body().string());
                Log.e(TAG, "MSG_BINDDEV_OK" + str);
                Message message = new Message();
                message.what = MSG_BINDDEV_OK;
                message.obj = str;
                handler.sendMessage(message);
            }
        };
        NetWorkBuilder.getInstance().getOkHttp(url,callback);
    }
        //开启异步访问网络
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //联网失败
//                Log.e(TAG, "MSG_BIND_FAIL" + "请求失败：" + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.e(TAG, "MSG_BINDDEV_OK" + "请求成功：" + response);
//                String str = JsonParse.getInstance().getAddDeviceInfo(response.body().string());
//                Log.e(TAG, "MSG_BINDDEV_OK" + str);
//                Message message = new Message();
//                message.what = MSG_BINDDEV_OK;
//                message.obj = str;
//                handler.sendMessage(message);
//            }
//        });
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
                case MSG_ADDUSER_OK:        //用户数据保存
                        //获取数据
                        if (msg.obj != null) {
                            SetUpBean.ResultBean setupbean = (SetUpBean.ResultBean) msg.obj;
                            Log.e("TAG", "handleMessage:" + setupbean.getDev_name());
                            Toast.makeText(AddDeviceUserInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

                            finish();
                        }else {
                            Toast.makeText(AddDeviceUserInfoActivity.this, "保存遇到问题了，log看下原因= =", Toast.LENGTH_SHORT).show();
                        }
                    break;
                case MSG_BINDDEV_OK:        //验证设备绑定关系
                    if (msg.obj != null) {
                        //获取数据
                        String result = (String) msg.obj;
                        Log.e("handleMessage", result);
                        tv_result.setText(result);      //显示绑定结果
                    }
                    break;
            }
        }
    };

    /**
     * 从选择地址界面回传经纬度
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE) {
                LatLonPoint geocenter = data.getParcelableExtra("geocenter");
                if (geocenter != null) {
                    tv_geocenter.setText(geocenter.getLongitude() + "," + geocenter.getLatitude());    //获取经纬度
                }
            }
    }


    /**
     * 判断网络是否可用
     *
     * @param activity
     * @return
     */
    private boolean isNetworkAvaible(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {// 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++)
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
            }
        }
        return false;
    }

}


