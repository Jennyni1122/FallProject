package com.jennyni.fallproject.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 减少项目中的重复代码量与便于后续调用
 *  UtilsHelper类中创建readLoginStatus()方法，从SharedPreferences中读取登录状态。
 * “我”界面需要根据登录状态设置相应的图标和控件的显示
 * Created by Jenny on 2019/1/22.
 */

public class UtilsHelper {

    /**
     * 获得屏幕宽度
     */
    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 从SharedPreferences中读取登录状态
     * @param context
     * @return
     */
    public static boolean readLoginStatus(Context context) {

        SharedPreferences sp = context.getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin",false);
        return isLogin;
    }

    /**
     * 从SharedPreferences中读取登录用户名
     */
    public static String readLoginUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo",
                Context.MODE_PRIVATE);
        String account = sp.getString("account", "");//读取登录时的用户名
        return account;
    }

    /**
     * 清除SharedPreferences中的登录状态和登录时的用户名
     */
    public static void clearLoginStatus(Context context){
        SharedPreferences sp=context.getSharedPreferences("loginInfo", Context.
                MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit(); //获取编辑器
        editor.putBoolean("isLogin", false);        //清除登录状态
        editor.putString("loginUserName", "");     //清除用户名
        editor.commit();                               //提交修改
    }

}
