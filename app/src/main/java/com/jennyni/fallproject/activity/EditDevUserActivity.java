package com.jennyni.fallproject.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jennyni.fallproject.R;
import com.jennyni.fallproject.utils.UtilsHelper;

/**
 * 编辑设备用户信息
 */
public class EditDevUserActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "EditDevice";
    private RelativeLayout rl_title_bar;
    private LinearLayout ll_addressfence;
    private TextView tv_main_title,tv_back,tv_switch,tv_guardian,tv_geocenter,tv_dev_cardid;
    private TextView tv_weilandizhi,tv_weilanfanwei,tv_mi,tv_shuoming;
    private EditText et_device_name,et_idcard,et_georadius;
    private RadioGroup sex_event,set_event;
    private RadioButton male,female,open,close;
    private Button btn_sure_code;
    private ImageView iv_head_icon;
    private String spUserPhone;         //定义 获取登录时的用户名
    private String currentDevCode,currentDevPsw;
    public static final int MSG_ADDUSER_OK = 1;
    public static final int MSG_BINDDEV_OK = 2;
    String isgeo,issex,dname,idcard,geocenter,georadius;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dev_user);

        initView();         //初始化控件
        setListener();      //控件的点击事件
    }


    private void initView() {
        //标题栏
        rl_title_bar= findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_main_title= findViewById(R.id.tv_main_title);
        tv_main_title.setText("编辑用户信息");
        tv_back =  findViewById(R.id.tv_back);
        tv_switch =  findViewById(R.id.tv_save);
        tv_back.setVisibility(View.VISIBLE);
        tv_switch.setVisibility(View.VISIBLE);
        tv_switch.setText("保存");
        //设备用户信息
        tv_guardian = findViewById(R.id.tv_guardian);       //监护电话（默认手机号）
        tv_geocenter = findViewById(R.id.tv_geocenter);     //地理围栏中心点(围栏开启可见)
        et_device_name = findViewById(R.id.et_device_name); //输入设备用户名
        et_idcard = findViewById(R.id.et_idcard);           //输入身份证
        tv_dev_cardid = findViewById(R.id.tv_dev_cardid);   //输入设备号
        et_georadius = findViewById(R.id.et_georadius);     //输入围栏半径（围栏开启可见）
        btn_sure_code = findViewById(R.id.btn_sure_code);   //验证绑定关系按钮
        iv_head_icon = findViewById(R.id.iv_head_icon);     //头像根据性别变化
        ll_addressfence = findViewById(R.id.ll_addressfence);//用于设置地理围栏开启之后操作
        //单击按钮：性别，围栏设置
        male = (RadioButton)findViewById(R.id.man);
        female = (RadioButton)findViewById(R.id.woman);
        close = (RadioButton) findViewById(R.id.close);
        open = (RadioButton)findViewById(R.id.open);
        sex_event = (RadioGroup) findViewById(R.id.sex_event);           //性别选择
        set_event = (RadioGroup) findViewById(R.id.sex_event);           //地理围栏选择
        sex_event.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton sex = findViewById(checkedId);
                Toast.makeText(EditDevUserActivity.this,"您选择了："+sex.getText().toString(),Toast.LENGTH_SHORT).show();
                if (sex.getText().toString().equals("男")){
                    issex = "男";
                }else {
                    issex = "女";
                }
            }
        });
        set_event.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton set = findViewById(checkedId);
                Toast.makeText(EditDevUserActivity.this,"您选择了："+set.getText().toString(),Toast.LENGTH_SHORT).show();
                if (set.getText().toString().equals("开始")){
                    isgeo = "1";
                    ll_addressfence.setVisibility(View.VISIBLE);
                }else {
                    isgeo = "0";
                }

            }
        });

        //从SharedPreferences中获取登录时的用户名
        spUserPhone = UtilsHelper.readLoginUserName(this);
        tv_guardian.setText(spUserPhone);           //紧急联系电话默认为手机登录号

    }

    private void setListener() {
        tv_back.setOnClickListener(this);           //返回键
        tv_switch.setOnClickListener(this);         //保存设备用户信息按钮
        tv_geocenter.setOnClickListener(this);      //设置地理围栏：选择地址按钮

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:      //返回按钮
                EditDevUserActivity.this.finish();
                break;
            case R.id.tv_save:      //保存按钮

                Toast.makeText(this, "修改成功~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_geocenter:     //围栏设置
                Intent intent = new Intent(EditDevUserActivity.this,GetAddressByKeyword.class);
                startActivity(intent);
                break;

        }

        //用askdevinfo接口开看以及编辑的设备用户信息？？？？







    }
}
