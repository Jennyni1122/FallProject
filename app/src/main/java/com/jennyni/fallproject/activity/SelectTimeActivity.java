package com.jennyni.fallproject.activity;

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

import com.jennyni.fallproject.R;

import java.sql.Time;
import java.util.Calendar;

/** 。。。。。。。。。。。。。。。。。。。。。//传值跳转还没写，还有保存按钮，解析
 * 选择时间功能，用于查看设备定位轨迹功能
 */
public class SelectTimeActivity extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{
    private TextView tv_main_title, tv_back,tv_switch,tv_date,tv_start_time, tv_end_time;
    private RelativeLayout rl_title_bar;
    private LinearLayout ll_date,ll_endtime,ll_starttime;
    private boolean isStartTime;
    Calendar instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

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
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_back.setVisibility(View.VISIBLE);
        tv_switch.setVisibility(View.VISIBLE);

        //日期，开始时间，结束时间
        ll_date = findViewById(R.id.ll_date);
        ll_starttime = findViewById(R.id.ll_starttime);
        ll_endtime = findViewById(R.id.ll_endtime);

    }


    private void setListener() {
        ll_date.setOnClickListener(this);           //日期按钮
        ll_endtime.setOnClickListener(this);        //开始时间按钮
        ll_starttime.setOnClickListener(this);      //结束时间按钮
        tv_back.setOnClickListener(this);           //返回按钮
        tv_switch.setOnClickListener(this);         //选择完成按钮（切换至设备轨迹界面）

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_back:      //返回按钮

                SelectTimeActivity.this.finish();
                break;
            case R.id.tv_save:      //保存按钮

               // PathActivity.startActivity(this,tv_start_time.getText().toString(),tv_end_time.getText().toString(),mode);
                break;
            case R.id.ll_date:      //选择日期按钮

                instance = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MyDatePickerDialogTheme,
                        this, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH),
                        instance.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case R.id.ll_starttime:   //选择开始时间按钮

                isStartTime = true;
                break;
            case R.id.ll_endtime:    //选择结束时间按钮
                instance = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.MyDatePickerDialogTheme,
                        this, instance.get(Calendar.HOUR_OF_DAY),instance.get(Calendar.MINUTE),true);
                timePickerDialog.show();
                break;

        }
    }


//    public static void startActivity(Context context, SafeLocationMode mode) {
//        Intent intent = new Intent(context, SelectTimeActivity.class);
//        intent.putExtra(PathActivity.SAFEMODE_KEY, mode);
//        context.startActivity(intent);
//    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String str = String.format("%d-%s-%d", year, month < 9 ? "0".concat(String.valueOf(month + 1)) : String.valueOf(month + 1), dayOfMonth);
        tv_date.setText(str);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (isStartTime) {
            tv_start_time.setText(String.format("%s %s:%d", tv_date.getText().toString(), hourOfDay < 10 ? "0".concat(String.valueOf(hourOfDay)) : String.valueOf(hourOfDay), minute));
        } else {
            tv_end_time.setText(String.format("%s %s:%d", tv_date.getText().toString(), hourOfDay < 10 ? "0".concat(String.valueOf(hourOfDay)) : String.valueOf(hourOfDay), minute));
        }
        isStartTime = false;
    }
}
