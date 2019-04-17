package com.jennyni.fallproject.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.mob.MobApplication;
import com.mob.MobSDK;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 获取上下文,让每个类都能随时调用上下文
 * Created by Jenny on 2019/2/21.
 */

public class FallProjectApplication  extends MobApplication {

    private static Context mContext;
    private static Map<String, Activity> destroyMap = new HashMap<>();


    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
        mContext = getApplicationContext();

    }


    /**
     * 添加到销毁的列队
     * <p/>
     * 要销毁的activity
     */
    public static void addDestroyActiivty(Activity activity, String activityName) {
        destroyMap.put(activityName, activity);
    }

    /**
     * 销毁指定的activity
     */
    public static void destroyActivity(String activityName) {
        Set<String> keySet = destroyMap.keySet();
        for (String key : keySet) {
            destroyMap.get(key).finish();
        }
    }
}
