package com.jennyni.fallproject.activity.welcome;

import android.content.Intent;
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

import com.jennyni.fallproject.Bean.UserRegisterBean;
import com.jennyni.fallproject.R;
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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_main_title, tv_back;
    private RelativeLayout rl_title_bar;
    private ImageView iv_show_psw;
    private EditText et_register_userphone, et_register_psw, et_code;
    private Button btn_register, btn_getSMSCode;
    private String currentUserphone, currentPsw;
    private boolean isShowPsw = false;
    private MHandler mHandler;
    public static final int MSG_REGISTER_OK = 1;    //获取注册数据
    private CountDownTimerUtils mCountDownTimerUtils;        //设置倒计时和变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mHandler = new MHandler();
        initView();     //初始化界面
        initSMS();      //初始化短信模块
    }

    /**
     * 初始化界面
     */
    private void initView() {
        //标题栏
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_back = findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        //注册控件
        btn_register = findViewById(R.id.btn_register);
        et_code = findViewById(R.id.et_code);
        btn_getSMSCode = findViewById(R.id.btn_getSMSCode);
        et_register_userphone = findViewById(R.id.et_register_userphone);
        et_register_psw = findViewById(R.id.et_register_psw);
        iv_show_psw = findViewById(R.id.iv_show_psw);
        tv_back.setOnClickListener(this);
        iv_show_psw.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_getSMSCode.setOnClickListener(this);
    }

    /**
     * 初始化短信模块
     */
    private void initSMS() {
        //创建倒计时对象
        mCountDownTimerUtils = new CountDownTimerUtils(btn_getSMSCode, 60000, 1000);

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

                                Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                            } else {
                                //  处理错误的结果
                                ((Throwable) data).printStackTrace();

                                Toast.makeText(RegisterActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            Log.e("handleMessage: ", data == null ? "" : data.toString());
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // 处理验证码验证通过的结果
//                                Toast.makeText(RegisterActivity.this, "验证码验证成功", Toast.LENGTH_SHORT).show();

                                sendRequest_register(currentUserphone, currentPsw);     //向服务器请求数据


                            } else {
                                // 处理错误的结果
                                ((Throwable) data).printStackTrace();
                                Toast.makeText(RegisterActivity.this, "验证码验证失败", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:          //返回键
                RegisterActivity.this.finish();
                break;
            case R.id.iv_show_psw:      //展示密码
                currentPsw = et_register_psw.getText().toString();
                if (isShowPsw) {
                    iv_show_psw.setImageResource(R.drawable.hide_psw_icon);
                    et_register_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowPsw = false;
                    if (currentPsw != null) {
                        et_register_psw.setSelection(currentPsw.length());
                    }
                } else {
                    iv_show_psw.setImageResource(R.drawable.show_psw_icon);
                    et_register_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码
                    isShowPsw = true;
                    if (currentPsw != null) {
                        et_register_psw.setSelection(currentPsw.length());
                    }
                }
                break;
            case R.id.btn_getSMSCode:   //获取验证码
                getSMSCode();
                break;
            case R.id.btn_register:     //注册

                //获取输入在相应控件中的字符串
                currentUserphone = et_register_userphone.getText().toString().trim();
                currentPsw = et_register_psw.getText().toString().trim();


                if (TextUtils.isEmpty(currentUserphone)) {
                    Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(currentPsw)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                Pattern p = Pattern.compile("1[0-9]{10,10}");
                Matcher m = p.matcher(currentUserphone);
                if (m.matches()) {
                    compareSMSCode();           //验证验证码
                } else {
                    btn_register.setClickable(false);
                    Toast.makeText(RegisterActivity.this, "请输入合法的手机号码", Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }


    /**
     * 事件捕获
     */
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_REGISTER_OK:
                    if (msg.obj != null) {
                        UserRegisterBean.ResultBean bean = (UserRegisterBean.ResultBean) msg.obj;
                        Log.e("TAG", "handleMessage:" + bean.getAccount());
                        Toast.makeText(RegisterActivity.this, "注册成功",
                                Toast.LENGTH_SHORT).show();
                        mCountDownTimerUtils.onFinish();
                        //把用户名和密码保存到SharedPreferences中
                        //saveRegisterInfo(currentUserphone, currentPsw);
                        //注册成功后把用户名传递到LoginActivity.java中
                        Intent data = new Intent();
                        data.putExtra("account", bean.getAccount());
                        setResult(RESULT_OK, data);
                        RegisterActivity.this.finish();
                        //判断用户是否已存在
//                        if (isExistUserName(bean)) {
//                            Toast.makeText(RegisterActivity.this, "此账户名已经存在",
//                                    Toast.LENGTH_SHORT).show();
//                            return;
//                        }else {
//                            Toast.makeText(RegisterActivity.this, "注册成功",
//                                    Toast.LENGTH_SHORT).show();
//                            mCountDownTimerUtils.onFinish();
//                            //把用户名和密码保存到SharedPreferences中
//                            //saveRegisterInfo(currentUserphone, currentPsw);
//                            //注册成功后把用户名传递到LoginActivity.java中
//                            Intent data = new Intent();
//                            data.putExtra("account", bean.getAccount());
//                            setResult(RESULT_OK, data);
//                            RegisterActivity.this.finish();
//                        }
                    }
                    break;
            }
        }
    }

    /**
     * 请求网络，进行注册
     */
    private void sendRequest_register(String account, String pass) {
        // http://www.phyth.cn/index/fall/userRegister/account/18860000306/pass/123456
        String url = Constant.BASE_WEBSITE + Constant.REQUEST_REGISTER_USER_URL + "?account=" + account + "&pass=" + pass;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("MSG_RESGISTER_OK", "请求成功：" + response);
                UserRegisterBean.ResultBean resultBean = JsonParse.getInstance().getuserRegisterInfo(response.body().string());
                Message message = new Message();
                message.what = MSG_REGISTER_OK;
                message.obj = resultBean;  // 将服务器返回的结果存放到Message中 message.obj = response;
                mHandler.sendMessage(message);
            }
        });

    }


    /**
     * 从服务器中读取输入的用户名，判断是否有此用户名
     */
    private boolean isExistUserName(UserRegisterBean.ResultBean bean) {
        boolean has_userName = false;
        if (!TextUtils.isEmpty(bean.getAccount())) {
            has_userName = true;
        }
        return has_userName;
    }

//    /**
//     * 保存用户名和密码到SharedPreferences中
//     */
//    private void saveRegisterInfo(String pid, String psw) {
//       // String md5Psw = MD5Utils.md5(psw);           //把密码用MD5加密
//        //loginInfo表示文件名
//        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();//获取编辑器
//        //以用户名为key,密码为value保存到SharedPreferences中
//        editor.putString("account",pid);
//        editor.putString("pass", psw);
//        editor.commit();//提交修改
//
//    }

    /**
     * 获取验证码
     */
    private void getSMSCode() {

        mCountDownTimerUtils.start();       //开始倒计时操作
        if (et_register_userphone.getText().toString().length() == 0 && et_register_userphone.getText().toString().length() != 11) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        SMSSDK.getVerificationCode("86", et_register_userphone.getText().toString());
    }


    /**
     * 验证验证码
     */
    private void compareSMSCode() {
        if (et_register_userphone.getText().toString().length() == 0 && et_register_userphone.getText().toString().length() != 11) {
            Toast.makeText(this, "请输入合法的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_register_userphone.getText().toString().length() == 0) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        SMSSDK.submitVerificationCode("86", et_register_userphone.getText().toString(), et_code.getText().toString());
    }


    @Override
    protected void onDestroy() {
        mCountDownTimerUtils.cancel();
        super.onDestroy();
    }
}
