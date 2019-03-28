package com.jennyni.fallproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jennyni.fallproject.Bean.UserChangePassBean;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.activity.welcome.LoginActivity;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.JsonParse;
import com.jennyni.fallproject.utils.UtilsHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 修改密码
 * Created by Jenny on 2019/3/6.
 */

public class ModifyPswActivity extends AppCompatActivity{
    public static final String TAG = "ModifyPswActivity";
    private TextView tv_main_title, tv_back;
    private Button btn_save;
    private RelativeLayout rl_title_bar;
    private EditText et_original_psw, et_new_psw;
    private String originalPsw, newPsw;
    private String spuserName;
    private ImageView iv_show_psw,iv_show_psw1;
    private boolean isShowPsw = false;
    public static final int MSG_ChangePass_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psw);

        initView();
        spuserName = UtilsHelper.readLoginUserName(this);     //读取登录账户

    }

    private void initView() {
        //标题栏
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("修改密码");
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        //初始密码，新密码
        et_original_psw = (EditText) findViewById(R.id.et_original_psw);
        et_new_psw = (EditText) findViewById(R.id.et_new_psw);
        iv_show_psw = (ImageView) findViewById(R.id.iv_show_psw);
//        iv_show_psw1 = (ImageView) findViewById(R.id.iv_show_psw1);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyPswActivity.this.finish();
            }
        });
        iv_show_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPsw = et_new_psw.getText().toString();
                if (isShowPsw) {
                    iv_show_psw.setImageResource(R.drawable.hide_psw_icon);
                    //隐藏密码
                    et_new_psw.setTransformationMethod(PasswordTransformationMethod.
                            getInstance());
                    isShowPsw = false;
                    if (newPsw != null) {
                        et_new_psw.setSelection(newPsw.length());
                    }
                } else {
                    iv_show_psw.setImageResource(R.drawable.show_psw_icon);
                    //显示密码
                    et_new_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPsw = true;
                    if (newPsw != null) {
                        et_new_psw.setSelection(newPsw.length());
                    }
                }
            }
        });
        //保存按钮的点击事件
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(originalPsw)) {
                    Toast.makeText(ModifyPswActivity.this, "请输入原始密码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!et_original_psw.getText().toString().equals(readPsw())) {
                    Toast.makeText(ModifyPswActivity.this, "输入的密码与原始密码不一致",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPsw)) {
                    Toast.makeText(ModifyPswActivity.this, "请输入新密码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                sendRequest_changepass();       //请求网络，更改密码
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
                case MSG_ChangePass_OK:
                    if (msg.obj != null) {
                        //获取数据
                        UserChangePassBean.ResultBean  bean = (UserChangePassBean.ResultBean ) msg.obj;
                        Log.e("TAG","handleMessage:"+ bean.getPhone_password());
                        modifyPsw(bean);

                    }
                    break;
            }
        }
    };

    /**
     * 请求网络，更改密码
     */
    private void sendRequest_changepass() {
        originalPsw = et_original_psw.getText().toString().trim();
        newPsw = et_new_psw.getText().toString().trim();
        //3.修改密码
        //String url3 = "http://www.phyth.cn/index/fall/userChangePass/account/"+ account +"/pass/"+cardid+"/newpass/"+cardpass;
        String url = Constant.BASE_WEBSITE+Constant.REQUEST_PSW_USER_URL
                +"?account="+ spuserName +"&pass="+originalPsw+"&newpass="+newPsw;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //开启异步访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //联网失败
                Log.e(TAG,"MSG_OK"+"请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG,"MSG_ChangePass_OK"+"请求成功：" + response);
                UserChangePassBean.ResultBean resultBean = JsonParse.getInstance().getuserChangePassInfo(response.body().string());
                if (resultBean==null){
                    Log.e("MSG_ChangePass_OK", "修改密码异常！");
                    Toast.makeText(ModifyPswActivity.this, "请求修改密码异常！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Message message = new Message();
                message.what = MSG_ChangePass_OK;
                message.obj = resultBean;
                handler.sendMessage(message);
            }
        });

    }

    /**
     * 把改密的新密码数据保存sp
     */
    private void modifyPsw(UserChangePassBean.ResultBean bean) {
        if (bean.getPhone_password().equals(newPsw)) {
            SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();  //获取编辑器
            editor.putString("pass", newPsw);             //保存新密码
            editor.commit();                                    //提交修改

            Toast.makeText(ModifyPswActivity.this, "新密码设置成功",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ModifyPswActivity.this,
                    LoginActivity.class);
            startActivity(intent);
//            SettingActivity.instance.finish(); //关闭设置界面
//            ModifyPswActivity.this.finish();   //关闭本界面
        }else {
            Toast.makeText(ModifyPswActivity.this, "修改密码发生异常~", Toast.LENGTH_SHORT);
        }

    }


    /**
     * 获取控件上的字符串
     */
    private void getEditString() {
        originalPsw = et_original_psw.getText().toString().trim();
        newPsw = et_new_psw.getText().toString().trim();
    }


    /**
     * 从SharedPreferences中读取原始密码...............从服务器？？？？？？？？？？
     */
    private String readPsw() {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String spPsw = sp.getString("pass", "");
        return spPsw;
    }

}
