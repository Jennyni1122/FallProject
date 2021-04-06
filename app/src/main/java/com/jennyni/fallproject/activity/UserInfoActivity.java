package com.jennyni.fallproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jennyni.fallproject.R;
import com.jennyni.fallproject.utils.ActivityCollectorUtil;
import com.jennyni.fallproject.utils.UtilsHelper;

/**
 * 点击用户头像进入修改资料界面
 */
public class UserInfoActivity extends ActivityCollectorUtil {
    private TextView tv_main_title,tv_back,tv_user_name;
    private RelativeLayout rl_title_bar;
    private String account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_user_info);
        initView();
        account = UtilsHelper.readLoginUserName(this);
        tv_user_name.setText(account);
    }

    @Override
    protected void onDestroy() {
        removeActivity(this);
        super.onDestroy();
    }

    private void initView() {
        tv_main_title= (TextView)findViewById(R.id.tv_main_title);
        tv_main_title.setText("个人资料");
        rl_title_bar= (RelativeLayout)findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        tv_back= (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        tv_user_name = findViewById(R.id.tv_user_name);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
