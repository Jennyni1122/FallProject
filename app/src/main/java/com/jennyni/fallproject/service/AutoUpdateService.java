package com.jennyni.fallproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 后台自动更新
 * Created by Jenny on 2019/2/26.
 */

public class AutoUpdateService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
