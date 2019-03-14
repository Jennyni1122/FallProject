package com.jennyni.fallproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jennyni.fallproject.service.AutoUpdateService;

/**
 * Created by Jenny on 2019/2/26.
 */

public class AutoUpdateReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, AutoUpdateService.class);
        context.startActivity(intent1);
    }
}
