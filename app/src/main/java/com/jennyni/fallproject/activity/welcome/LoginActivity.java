package com.jennyni.fallproject.activity.welcome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.jennyni.fallproject.Bean.UserLoginBean;
import com.jennyni.fallproject.R;
import com.jennyni.fallproject.activity.MainActivity;
import com.jennyni.fallproject.utils.Constant;
import com.jennyni.fallproject.utils.JsonParse;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    private EditText et_login_psw,et_login_userphone;
    private TextView tv_quick_register,tv_forget_psw;
    private ImageView iv_show_psw;
    private Button btn_login;
    private boolean isShowPsw=false;    //是否展示密码
    private boolean hasUserName;
    private boolean hasPwd;
    private SharedPreferences sharedPreferences;
    public static final int MSG_LOGIN_OK = 1;//获取登录数据
    public static final int MSG_LOGIN_FAIL = 2;//获取登录数据

    String currentUserphone,currentPsw;        //输入的手机号码和密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();     //初始化控件

    }

    /**
     * 初始化控件
     */
    private void initView() {
        //手机号码，密码，密码可见，快速注册，忘记密码，登录按钮
        et_login_userphone =  findViewById(R.id.et_login_userphone);
        et_login_psw= findViewById(R.id.et_login_psw);
        iv_show_psw = findViewById(R.id.iv_show_psw);
        tv_quick_register= findViewById(R.id.tv_quick_register);
        tv_forget_psw = findViewById(R.id.tv_forget_psw);
        btn_login = findViewById(R.id.btn_login);
      //监听事件：密码可见，快速注册，忘记密码，登录按钮
        iv_show_psw.setOnClickListener(this);
        tv_quick_register.setOnClickListener(this);
        tv_forget_psw.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 登录时先从数据库里面读取用户的用户名和密码
         *
         * 如果不为空则直接登录
         * */
        sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String name = sharedPreferences.getString("account", "");
        String code = sharedPreferences.getString("pass", "");
        if (name.length() > 1 && code.length() > 1) {
            et_login_userphone.setText(name);
            et_login_psw.setText(code);
            //login(); //执行登录的方法
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_show_psw:      //密码可见设置
                currentPsw = et_login_psw.getText().toString();
                if (isShowPsw){
                    iv_show_psw.setImageResource(R.drawable.hide_psw_icon);
                    et_login_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());   //隐藏密码
                    isShowPsw = false;
                    if (currentPsw == null){
                        et_login_psw.setSelection(currentPsw.length());
                    }
                }else {
                    iv_show_psw.setImageResource(R.drawable.show_psw_icon); //显示密码
                    et_login_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//隐藏回车
                    isShowPsw = true;
                    if (currentPsw != null){
                        et_login_psw.setSelection(currentPsw.length());
                    }
                }
                break;
            case R.id.btn_login:    //登录按钮
                login();        //执行登录的方法

                break;
            case R.id.tv_quick_register:        //快速注册
                Intent registerintent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerintent);
                //startActivityForResult(registerintent,1);
                break;
            case R.id.tv_forget_psw:            //忘记密码
                Intent forgetIntent = new Intent(LoginActivity.this,PasswordActivity.class);
                startActivity(forgetIntent);
                break;
        }
    }



    /**
     * 执行登录的方法
     */
    private void login() {
        //把输入框的用户号码和密码字段提取出来
        currentUserphone = et_login_userphone.getText().toString().trim();
        currentPsw = et_login_psw.getText().toString().trim();

        if (TextUtils.isEmpty(currentUserphone)){
            Toast.makeText(LoginActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPsw)){
            Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
            //号码格式匹配
            Pattern p = Pattern.compile("1[0-9]{10,10}");
            Matcher m = p.matcher(currentUserphone);
            if (m.matches()) {
                //Intent跳转
                if (isNetworkAvaible(LoginActivity.this)) {

                    sendRequest_login(currentUserphone,currentPsw);    //如果均不为空 则执行向服务器请求数据
                } else {
                    Toast.makeText(LoginActivity.this,"当前网络错误",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this,"请输入合法的手机号码",Toast.LENGTH_SHORT).show();
            }

    }


    /**
     * 事件捕获
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_LOGIN_OK:
                    //解析JSON, 登录时服务器返回来的数据
                    if (msg.obj!= null){
                        //获取数据
                        UserLoginBean.ResultBean bean = (UserLoginBean.ResultBean) msg.obj;
                        Log.e("TAG","handleMessage:"+ bean.getAccount());
                        // tv_result.setText(result);
                        setGsonData(bean);
                    }
                    break;

            }
        }
    };

    /**
     * 向服务器请求数据，进行登录操作
     */
    private void sendRequest_login(String account,String pass) {

       //2.登录
       // String url2 = "http://www.phyth.cn/index/fall/userlogin/account/"+account+"/pass/"+cardid;
        String url = Constant.BASE_WEBSITE + Constant.REQUEST_LOGIN_USER_URL+"?account="+account+"&pass="+pass;
        Log.e(TAG, url);
        // 1. 获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        // 2. 创建Request对象
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
                Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Log.e("MSG_LOGIN_FAIL", "请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功
                Log.e("MSG_LOGIN_OK", "请求成功：" + response);
                UserLoginBean.ResultBean resultBean = JsonParse.getInstance().getuserLoginInfo(response.body().string());
                if (resultBean==null){
                    Log.e("MSG_LOGIN_OK", "请求登录异常！");
           //         Toast.makeText(LoginActivity.this, "请求登录异常！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Message message = new Message();
                message.what = MSG_LOGIN_OK;
                message.obj = resultBean;
                handler.sendMessage(message);
            }
        });

    }


    /**
     * 把登录的数据保存
     */
    private void setGsonData(UserLoginBean.ResultBean bean) {
        if (bean.getAccount() == null) {
            Toast.makeText(this, bean.getReason(), Toast.LENGTH_SHORT).show();
            return;
        } else {
        /**
         * 当返回来的数据和输入的用户名一样时则表示登录成功
         *把用户名和密码添加到数据库里面  以便下一次自动登录
         * */

        if (bean.getAccount().equals(currentUserphone)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("account", bean.getAccount());
            editor.putString("pass", currentPsw);
            editor.commit();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);      //跳转到主页面

            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            //保存登录状态和登录的用户名
            saveLoginStatus(true,currentUserphone);
            //把登录成功的状态传递到MainActivity中
            Intent data=new Intent();
            data.putExtra("isLogin", true);
            setResult(RESULT_OK, data);
            finish();

        } else {
            Toast.makeText(LoginActivity.this, "登录账户和请求账户产生差异~", Toast.LENGTH_SHORT);
        }
    }
}


    /**
     *保存登录状态和登录用户名到SharedPreferences中
     */
    private void saveLoginStatus(boolean status,String account){
        //loginInfo表示文件名
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();     //获取编辑器
        editor.putBoolean("isLogin", status);          //存入boolean类型的登录状态
        editor.putString("account", account);          //存入登录时的用户名
        editor.commit();                                   //提交修改
    }

    /**
     * 判断网络是否可用
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //从注册界面传递过来的用户名
            String account =data.getStringExtra("account");
            if(!TextUtils.isEmpty(account)){
                et_login_userphone.setText(account);
                //设置光标的位置
                et_login_userphone.setSelection(account.length());
            }
        }
    }

}
