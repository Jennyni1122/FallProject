package com.jennyni.fallproject.activity.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.jennyni.fallproject.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 欢迎界面
 */
public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGHT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        //利用Timer让此界面延迟3秒后再跳转,timer中有一个线程,这个线程不断执行task
        Timer timer = new Timer();
        //timertask实现runnable接口,TimerTask类表示一个在指定时间内执行的task
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        };
        timer.schedule(task,SPLASH_DISPLAY_LENGHT);  //设置这个task在延迟三秒之后自动执行
    }
}
