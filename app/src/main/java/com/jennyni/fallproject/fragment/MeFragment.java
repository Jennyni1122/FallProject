package com.jennyni.fallproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jennyni.fallproject.R;

import com.jennyni.fallproject.activity.SettingActivity;
import com.jennyni.fallproject.activity.UserInfoActivity;
import com.jennyni.fallproject.receiver.UpdateUserInfoReceiver;
import com.jennyni.fallproject.utils.UtilsHelper;
import com.jennyni.fallproject.activity.welcome.LoginActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class MeFragment extends Fragment implements View.OnClickListener{

    private RelativeLayout rl_userinfo,rl_collection,rl_setting;
    private CircleImageView iv_avatar;
    private View view;
    private UpdateUserInfoReceiver updateUserInfoReceiver;  //暂不用
    private IntentFilter filter;
    private boolean isLogin = false;


    public MeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me,container,false);
        initView(view);         //初始化控件
        return view;
    }

    /**
     * 2.初始化界面控件
     * @param view
     */
    private void initView(View view) {
        rl_userinfo = view.findViewById(R.id.rl_userinfo);
        rl_collection = view.findViewById(R.id.rl_collection);
        rl_setting = view.findViewById(R.id.rl_setting);

        iv_avatar = view.findViewById(R.id.iv_avatar);

        isLogin = UtilsHelper.readLoginStatus(getActivity());

        setLoginParams(isLogin);    //设置“我”界面的登录成功状态
        setListener();              //单击监听事件
        receiver();                 //接收广播

    }

    /**
     * 3. 接收广播 (暂不用)
     * 个人资料界面在修改头像成功后需要及时更新“我”界面的头像
     * receiver()方法接收传递过来的头像信息并更新界面头像
     */
    private void receiver() {
        updateUserInfoReceiver = new UpdateUserInfoReceiver(new UpdateUserInfoReceiver.BaseOnReceiveMsgListener() {
            @Override
            public void onReceiveMsg(Context context, Intent intent) {
                String action = intent.getAction();
                if (UpdateUserInfoReceiver.ACTION.UPDATE_USERINFO.equals(action)){
                    String type = intent.getStringExtra(UpdateUserInfoReceiver.INTENT_TYPE.TYPE_NAME);
                    if (UpdateUserInfoReceiver.INTENT_TYPE.UPDATE_HEAD.equals(type)){
                        String head = intent.getStringExtra("head");
                        Bitmap bt = BitmapFactory.decodeFile(head); //加载手机系统中的图片
                        if (bt != null){
                            Drawable drawable = new BitmapDrawable(bt);
                            iv_avatar.setImageDrawable(drawable);
                        }
                    }
                }
            }
        });
        filter = new IntentFilter(UpdateUserInfoReceiver.ACTION.UPDATE_USERINFO);
        getActivity().registerReceiver(updateUserInfoReceiver,filter);
    }

    private void setListener() {
        rl_userinfo.setOnClickListener(this);    //个人资料按钮
        rl_collection.setOnClickListener(this); //使用帮助按钮
        rl_setting.setOnClickListener(this);    //设置按钮
        iv_avatar.setOnClickListener(this);     //点击头像按钮

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateUserInfoReceiver != null) {
            getActivity().unregisterReceiver(updateUserInfoReceiver);
        }
    }
    /**
     * 1. 各功能的单击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_userinfo:    //点击个人资料
                if (isLogin){
                    //跳转到个人资料界面
                    Intent userinfoIntent = new Intent(getActivity(),UserInfoActivity.class);
                    startActivityForResult(userinfoIntent,1);   //回传数据
                }
                break;
            case R.id.rl_collection:
                //跳到使用帮助界面
                Toast.makeText(getActivity(), "暂未开放", Toast.LENGTH_SHORT).show();
//                Intent collection = new Intent(getActivity(),SettingActivity.class);
//                startActivity(collection);
                break;
            case R.id.rl_setting:
                    Intent settingIntent = new Intent(getActivity(),SettingActivity.class);
                    startActivityForResult(settingIntent,1);
                break;

        }

    }


    /**
     * 4. 登录成功后设置我的界面
     * setLoginParams()方法，用于设置登陆成功后“我”界面中的头像与用户名的显示状态。
     */
    public void setLoginParams(boolean isLogin) {

        if (isLogin){
            String userName = UtilsHelper.readLoginUserName(getActivity());
            iv_avatar.setImageResource(R.drawable.default_head);
        }
    }

    /**
     * 5.回传数据
     * MeFragment类中重写onActivityResult()方法，
     * 在该方法中接收从“登录”界面或者“设置”界面回传过来的登录状态，从而设置“我”界面。
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //参数1：这个整数requestCode用于与startActivityForResult中的requestCode中值进行比较判断，是以便确认返回的数据是从哪个Activity返回的。
        //参数2：由子Activity通过其setResult()方法返回。适用于多个activity都返回数据时，来标识到底是哪一个activity返回的值。
        //参数3：一个Intent对象，带有返回的数据。可以通过data.getXxxExtra( );方法来获取指定数据类型的数据。
        if (data != null){
            boolean isLogin = data.getBooleanExtra("isLogin",false);
            setLoginParams(isLogin);    //登录成功后设置我的界面
            this.isLogin = isLogin;
        }
    }

}
