package com.jennyni.fallproject.activity.devicelocation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jennyni.fallproject.Bean.AskFallInfoBean;
import com.jennyni.fallproject.Bean.UserUpdateBean;
import com.jennyni.fallproject.R;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * 选择时间功能，用于查看设备定位轨迹功能
 */
public class SelectTimeActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String CARDID_KEY = "cardid";
    private TextView tv_main_title, tv_back, tv_switch, tv_date, tv_start_time, tv_end_time;
    private RelativeLayout rl_title_bar;
    private LinearLayout ll_endtime, ll_starttime;
    private boolean isStartTime;
    Calendar instance;
    private DatePickerDialog datePickerDialog;
    private String cardid;


    public static void startActivity(Context context, String cardid) {
        Intent intent = new Intent(context, SelectTimeActivity.class);
        intent.putExtra(CARDID_KEY, cardid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
        cardid = getIntent().getStringExtra(CARDID_KEY);
        initView();         //初始化控件
        setListener();      //控件的点击事件
    }


    /**
     * 初始化控件
     */
    private void initView() {
        //标题栏
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("选择时间");
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_switch = (TextView) findViewById(R.id.tv_save);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_back.setVisibility(View.VISIBLE);
        tv_switch.setVisibility(View.VISIBLE);

        //日期，开始时间，结束时间
        ll_starttime = findViewById(R.id.ll_starttime);
        ll_endtime = findViewById(R.id.ll_endtime);
        instance = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, R.style.MyDatePickerDialogTheme,
                this, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH),
                instance.get(Calendar.DAY_OF_MONTH));
    }


    private void setListener() {
        ll_endtime.setOnClickListener(this);        //开始时间按钮
        ll_starttime.setOnClickListener(this);      //结束时间按钮
        tv_back.setOnClickListener(this);
        //返回按钮
        tv_switch.setOnClickListener(this);         //选择完成按钮（切换至设备轨迹界面）

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_back:      //返回按钮

                SelectTimeActivity.this.finish();
                break;
            case R.id.tv_save:      //保存按钮

                PathActivity.startActivity(this, tv_start_time.getText().toString(), tv_end_time.getText().toString(), cardid);
                break;
            case R.id.ll_starttime:   //选择开始时间按钮
                isStartTime = true;
                datePickerDialog.show();
                break;
            case R.id.ll_endtime:    //选择结束时间按钮
                isStartTime = false;
                datePickerDialog.show();
                break;

        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String str = String.format("%d-%s-%d", year, month < 9 ? "0".concat(String.valueOf(month + 1)) : String.valueOf(month + 1), dayOfMonth);
        if (isStartTime) {
            tv_start_time.setText(str);
        } else {
            tv_end_time.setText(str);
        }

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


    }


    public static void startActivity(Context context, AskFallInfoBean.ResultBean devicebean) {
        Intent intent = new Intent(context,SelectTimeActivity.class);
        intent.putExtra(PathActivity.SAFEMODE_KEY, (Serializable) devicebean);
        context.startActivity(intent);
    }
}
