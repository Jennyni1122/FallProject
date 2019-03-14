package com.jennyni.fallproject.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jennyni.fallproject.R;

public class PasswordActivity extends AppCompatActivity {
    private EditText et_validate_name, et_user_name;
    private Button btn_validate;
    private TextView tv_main_title;
    private TextView tv_back;
    //from为security时是从设置密保界面跳转过来的，否则就是从登录界面跳转过来的
    private String from;
    private TextView tv_reset_psw, tv_user_name;
    private RelativeLayout rl_title_bar;

    public static final int MSG_Psw_OK = 1;    //获取注册数据
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


    }
}
