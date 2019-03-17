package com.jennyni.fallproject.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jennyni.fallproject.R;

/**
 * 检测更新界面（目前放图标即可）
 */
public class CheckUpdateActivity extends AppCompatActivity {
    private TextView tv_main_title,tv_back;
    private RelativeLayout rl_title_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_user_info);

        initView();
    }

    private void initView() {
        tv_main_title= (TextView)findViewById(R.id.tv_main_title);
        tv_main_title.setText("检测更新");
        rl_title_bar= (RelativeLayout)findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        tv_back= (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
