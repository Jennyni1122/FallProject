package com.jennyni.fallproject;

import android.app.Application;
import android.content.Context;

import com.mob.MobApplication;
import com.mob.MobSDK;

/**
 * 获取上下文,让每个类都能随时调用上下文
 * Created by Jenny on 2019/2/21.
 */

public class FallProjectApplication  extends MobApplication {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
        mContext = getApplicationContext();

    }
}
