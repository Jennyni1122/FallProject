package com.jennyni.fallproject.activity;

import android.annotation.SuppressLint;
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

import com.jennyni.fallproject.Bean.AskDevInfoBean;
import com.jennyni.fallproject.Bean.SetUpBean;
import com.jennyni.fallproject.Bean.UserUpdateBean;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.JsonParse;
import com.jennyni.fallproject.utils.UtilsHelper;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 编辑设备用户信息
 */
public class EditDevUserActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "EditDevice";
    private RelativeLayout rl_title_bar;
    private LinearLayout ll_addressfence;
    private TextView tv_main_title,tv_back,tv_switch,tv_guardian,tv_geocenter,tv_dev_cardid;
    private TextView tv_weilandizhi,tv_weilanfanwei,tv_mi,tv_shuoming;
    private EditText et_device_name,et_idcard,et_georadius;
    private RadioGroup sex_event,set_event;
    private RadioButton male,female,open,close;
    private Button btn_sure_code;
    private ImageView iv_head_icon;
    private String spUserPhone,cardid;         //定义 获取登录时的用户名
    private String currentDevCode,currentDevPsw;
    public static final int MSG_EDIT_OK = 1;
    public static final int MSG_SAVE_OK = 2;
    String isgeo,issex,dname,idcard,geocenter,georadius;
    List<UserUpdateBean.ResultBean> devicelist;
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
        rl_title_bar= findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_main_title= findViewById(R.id.tv_main_title);
        tv_main_title.setText("编辑用户信息");
        tv_back =  findViewById(R.id.tv_back);
        tv_switch =  findViewById(R.id.tv_save);
        tv_back.setVisibility(View.VISIBLE);
        tv_switch.setVisibility(View.VISIBLE);
        tv_switch.setText("保存");
        //设备用户信息
        tv_guardian = findViewById(R.id.tv_guardian);       //监护电话（默认手机号）
        tv_geocenter = findViewById(R.id.tv_geocenter);     //地理围栏中心点(围栏开启可见)
        et_device_name = findViewById(R.id.et_device_name); //输入设备用户名
        et_idcard = findViewById(R.id.et_idcard);           //输入身份证
        et_georadius = findViewById(R.id.et_georadius);     //输入围栏半径（围栏开启可见）
        btn_sure_code = findViewById(R.id.btn_sure_code);   //验证绑定关系按钮
        iv_head_icon = findViewById(R.id.iv_head_icon);     //头像根据性别变化
        ll_addressfence = findViewById(R.id.ll_addressfence);//用于设置地理围栏开启之后操作
        //单击按钮：性别，围栏设置
        male = (RadioButton)findViewById(R.id.man);
        female = (RadioButton)findViewById(R.id.woman);
        close = (RadioButton) findViewById(R.id.close);
        open = (RadioButton)findViewById(R.id.open);
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

    /**
     * 请求网络，加载设备用户信息
     */
    private void sendrequest_askDevInfo() {

        cardid = devicelist.get(0).getCard_id();
        String url = Constant.BASE_WEBSITE+Constant.REQUEST_ASKDEVINFO_DEVICE_URL + "/account/" +spUserPhone +"/cardid/"+ cardid;
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
                String str = JsonParse.getInstance().getAskDevInfo(response.body().string());
                Log.e(TAG, "MSG_EDIT_OK" + str);
                Message message = new Message();
                message.what = MSG_EDIT_OK;
                message.obj = str;
                handler.sendMessage(message);

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
                case MSG_EDIT_OK:        //查看用户数据
                    if (msg.obj != null) {
                        //获取数据
                        //    String dev_result = (String) msg.obj;
                        final AskDevInfoBean.ResultBean devinfobean = (AskDevInfoBean.ResultBean) msg.obj;
                        Log.e("TAG", "handleMessage:" + devinfobean.getDname());
                        //显示用户信息数据
                        et_device_name.setText(devinfobean.getDname()); //设备用户名
                        et_idcard.setText(devinfobean.getIdcard());     //身份证
                        tv_guardian.setText(spUserPhone);               //监护人
                        //性别设置，男，则按钮为男，头像为老爷爷；  女，按钮为女，头像为老奶奶
                        sex_event.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                RadioButton sex = findViewById(checkedId);
                                if (devinfobean.getDsex().equals(sex.getText().toString())){
                                    iv_head_icon.setImageResource(R.drawable.icon_male);
                                }else {
                                    iv_head_icon.setImageResource(R.drawable.icon_female);
                                }
                            }
                        });

                        //围栏开启设备，围栏开启：设置围栏内容可见，显示中心点经纬度，显示半径；  围栏关闭：显示不可见
                        set_event.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                RadioButton set = findViewById(checkedId);
                                if (devinfobean.getIsgeo() == 1){
                                    set.getText().toString().equals("开启");
                                    ll_addressfence.setVisibility(View.VISIBLE);
                                    tv_geocenter.setText(devinfobean.getGeocenter());
                                    et_georadius.setText(devinfobean.getGeoradius());
                                }else {
                                    ll_addressfence.setVisibility(View.INVISIBLE);
                                }
                            }
                        });


                    }
                    break;
                case MSG_SAVE_OK:        //修改用户信息，进行上传
//                    if (msg.obj != null) {
//                        //获取数据
//                        String result = (String) msg.obj;
//                        Log.e("handleMessage", result);
//                        tv_result.setText(result);      //显示绑定结果
//                    }
                    break;
            }
        }
    };




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:      //返回按钮
                EditDevUserActivity.this.finish();
                break;
            case R.id.tv_save:      //保存按钮

                Toast.makeText(this, "修改成功~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_geocenter:     //围栏设置
                Intent intent = new Intent(EditDevUserActivity.this,GetAddressByKeyword.class);
                startActivity(intent);
                break;

        }









    }
}
