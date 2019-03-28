package com.jennyni.fallproject.activity.welcome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.jennyni.fallproject.activity.ModifyPswActivity;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.CountDownTimerUtils;
import com.jennyni.fallproject.utils.JsonParse;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "PasswordActivity";
    private RelativeLayout rl_title_bar;
    private TextView tv_main_title,tv_back;
    private EditText ed_phone,ed_code,ed_new_psw,et_new_psw,et_psw;
    private Button btn_getSMSCode,btn_psw;
    private ImageView iv_show_psw1,iv_show_psw2;
    private boolean isShowPsw=false;
    public static final int MSG_Pass_OK = 1;
    //from为security时是从设置密保界面跳转过来的，否则就是从登录界面跳转过来的
    private String from;
    private String currentPhone,currentCode,currentPsw,currentRePsw;
    private CountDownTimerUtils mCountDownTimerUtils;        //设置倒计时和变量

    public static final int MSG_Psw_OK = 1;    //获取改密数据
    public static final int MSG_Psw_NOUSER = 1;
    public static final int MSG_Psw_HAVEUSER = 2;
    public static final int MSG_Psw_PASSWORD_FAIL = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        //获取从登录界面和设置界面传递过来的数据
        from = getIntent().getStringExtra("from");
        initView();         //初始化界面
        initSMS();          //初始化短信模块
        //获取输入在相应控件中的字符串
        currentPhone = ed_phone.getText().toString().trim();
        currentCode = ed_code.getText().toString().trim();
        currentPsw = et_new_psw.getText().toString().trim();
        currentRePsw = et_psw.getText().toString().trim();
    }

    /**
     * 初始化短信模块
     */
    private void initSMS() {
        //创建倒计时对象
        mCountDownTimerUtils = new CountDownTimerUtils(btn_getSMSCode,60000,1000);

        //初始化短信模块
        EventHandler eventHandler = new EventHandler() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            public void afterEvent(int event, int result, Object data) {
                // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                new Handler(Looper.getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        int event = msg.arg1;
                        int result = msg.arg2;
                        Object data = msg.obj;
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            Log.e("handleMessage: ", data == null ? "" : data.toString());
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // 处理成功得到验证码的结果
                                // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达

                                Toast.makeText(PasswordActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                            } else {
                                //  处理错误的结果
                                ((Throwable) data).printStackTrace();

                                Toast.makeText(PasswordActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            Log.e("handleMessage: ", data == null ? "" : data.toString());
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // 处理验证码验证通过的结果
                                Toast.makeText(PasswordActivity.this, "验证码验证成功", Toast.LENGTH_SHORT).show();

                                sendRequest_psw(currentPhone,currentPsw);     //向服务器请求数据


                            } else {
                                // 处理错误的结果
                                ((Throwable) data).printStackTrace();
                                Toast.makeText(PasswordActivity.this, "验证码验证失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                        // 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                        return false;
                    }
                }).sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);

    }



    /**
     * 初始化界面，获取界面控件及处理相应控件的点击事件
     */
    private void initView() {
        //标题栏
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        ed_phone = findViewById(R.id.ed_phone);
        ed_code = findViewById(R.id.ed_code);
        et_new_psw = findViewById(R.id.et_new_psw);
        et_psw = findViewById(R.id.et_psw);
        iv_show_psw1 = findViewById(R.id.iv_show_psw1);
        iv_show_psw2 = findViewById(R.id.iv_show_psw2);
        btn_getSMSCode = findViewById(R.id.btn_getSMSCode);
        btn_psw = findViewById(R.id.btn_psw);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:          //返回键
                finish();
                break;
            case R.id.iv_show_psw1:      //展示密码
                currentPsw = et_new_psw.getText().toString();
                if (isShowPsw){
                    iv_show_psw1.setImageResource(R.drawable.hide_psw_icon);
                    et_new_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowPsw = false;
                    if (currentPsw != null){
                        et_new_psw.setSelection(currentPsw.length());
                    }
                }else{
                    iv_show_psw1.setImageResource(R.drawable.show_psw_icon);
                    et_new_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码
                    isShowPsw = true;
                    if (currentPsw != null){
                        et_new_psw.setSelection(currentPsw.length());
                    }
                }
                break;
            case R.id.iv_show_psw2:      //展示密码
                currentRePsw = et_psw.getText().toString();
                if (isShowPsw){
                    iv_show_psw2.setImageResource(R.drawable.hide_psw_icon);
                    et_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowPsw = false;
                    if (currentRePsw != null){
                        et_psw.setSelection(currentRePsw.length());
                    }
                }else{
                    iv_show_psw2.setImageResource(R.drawable.show_psw_icon);
                    et_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码
                    isShowPsw = true;
                    if (currentRePsw != null){
                        et_psw.setSelection(currentRePsw.length());
                    }
                }
                break;
            case R.id.btn_getSMSCode:   //获取验证码

                getSMSCode();
                break;
            case R.id.btn_psw:     //改密码

                if (TextUtils.isEmpty(currentPhone)){
                    Toast.makeText(PasswordActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(currentCode)){
                    Toast.makeText(PasswordActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(currentPsw) || TextUtils.isEmpty(currentRePsw)) {
                    Toast.makeText(PasswordActivity.this, "请输入新密码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!currentPsw.equals(currentRePsw)) {
                    Toast.makeText(PasswordActivity.this, "输入的密码与再次确认密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }


                Pattern p = Pattern.compile("1[0-9]{10,10}");
                Matcher m = p.matcher(currentPhone);
                if(m.matches() ){

                    compareSMSCode();           //验证验证码
                }else{
                    btn_psw.setClickable(false);
                    Toast.makeText(PasswordActivity.this, "请输入合法的手机号码", Toast.LENGTH_SHORT).show();
                }

                break;

        }


    }

    /**
     * 获取验证码
     */
    private void getSMSCode() {
        mCountDownTimerUtils.start();       //开始倒计时操作
        if (ed_phone.getText().toString().length() == 0 && ed_phone.getText().toString().length() != 11) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        SMSSDK.getVerificationCode("86", ed_phone.getText().toString());
    }

    /**
     * 验证验证码
     */
    private void compareSMSCode() {
        if (ed_phone.getText().toString().length() == 0 && ed_phone.getText().toString().length() != 11) {
            Toast.makeText(this, "请输入合法的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed_phone.getText().toString().length() == 0) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        SMSSDK.submitVerificationCode("86", ed_phone.getText().toString(), ed_code.getText().toString());
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
                case MSG_Pass_OK:
                    if (msg.obj != null) {
                        //获取数据
                        UserChangePassBean.ResultBean  bean = (UserChangePassBean.ResultBean ) msg.obj;
                        Log.e("TAG","handleMessage:"+ bean.getPhone_password());
                        showGsonPsw(bean); //保存sp
                        Toast.makeText(PasswordActivity.this, "密码设置成功",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(PasswordActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
        }
    };

    private void showGsonPsw(UserChangePassBean.ResultBean bean) {
        if (bean.getPhone_password().equals(currentPsw)) {
            SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();  //获取编辑器
            editor.putString("pass", currentPsw);             //保存新密码
            editor.commit();                                    //提交修改

        }
    }


    /**
     * 请求网络，忘记密码（更改密码接口）
     * @param currentPhone
     * @param currentPsw
     */
    private void sendRequest_psw(String currentPhone, String currentPsw) {
        //3.修改密码
        //String url3 = "http://www.phyth.cn/index/fall/userChangePass/account/"+ account +"/pass/"+cardid+"/newpass/"+cardpass;
        String url = Constant.BASE_WEBSITE+Constant.REQUEST_PSW_USER_URL
                +"?account="+ currentPhone +"&pass="+currentPsw+"&newpass="+currentRePsw;
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
                    Log.e("MSG_ChangePass_OK", "请求密码异常！");
                    Toast.makeText(PasswordActivity.this, "请求密码异常！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Message message = new Message();
                message.what = MSG_Pass_OK;
                message.obj = resultBean;
                handler.sendMessage(message);
            }
        });

    }

    @Override
    protected void onDestroy() {
        mCountDownTimerUtils.cancel();
        super.onDestroy();
    }
}
