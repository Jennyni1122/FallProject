package com.jennyni.fallproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jennyni.fallproject.R;
import com.jennyni.fallproject.activity.welcome.LoginActivity;
import com.jennyni.fallproject.utils.ActivityCollectorUtil;
import com.jennyni.fallproject.utils.UtilsHelper;

/**
 * MeFragment的设置功能：改密/退出登录
 */
public class SettingActivity extends ActivityCollectorUtil {
    private TextView tv_main_title, tv_back;
    private RelativeLayout rl_title_bar;
    private RelativeLayout rl_modify_psw, rl_security_setting, rl_exit_login;
    public static SettingActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_setting);

        instance = this;
        initView();
    }

    @Override
    protected void onDestroy() {
        removeActivity(this);
        super.onDestroy();
    }

    private void initView() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("设置");
        tv_back = (TextView) findViewById(R.id.tv_back);
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        rl_modify_psw = (RelativeLayout) findViewById(R.id.rl_modify_psw);
        rl_security_setting = (RelativeLayout) findViewById(R.id.rl_security_setting);
        rl_exit_login = (RelativeLayout) findViewById(R.id.rl_exit_login);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
        //修改密码的点击事件
        rl_modify_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改密码的界面
                Intent intent = new Intent(SettingActivity.this, ModifyPswActivity.class);
                startActivity(intent);
            }
        });
        //版本更新的点击事件
        rl_security_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "暂未开放", Toast.LENGTH_SHORT).show();
                //跳转到检查更新界面
                Intent intent = new Intent(SettingActivity.this, CheckUpdateActivity.class);
                startActivity(intent);
            }
        });
        //退出登录的点击事件
        rl_exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "退出登录成功",
                        Toast.LENGTH_SHORT).show();
                //清除登录状态和登录时的用户名
                UtilsHelper.clearLoginStatus(SettingActivity.this);
                //退出登录成功后把退出成功的状态传递到MainActivity中
//                Intent data = new Intent();
//                data.putExtra("isLogin", false);
//                setResult(RESULT_OK, data);
//                FallProjectApplication.destroyActivity("mainActivity");
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent);
                finishAllActivity();
            }
        });

    }
}
