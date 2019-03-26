package com.jennyni.fallproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.jennyni.fallproject.activity.devicelocation.DevUserDetailActivity;

/**
 * Created by Jenny on 2019/3/21.
 */

public class NotifyReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "通知发送成功", Toast.LENGTH_SHORT).show();
        String cardid = intent.getStringExtra("cardid");
        DevUserDetailActivity.startActivity(context, cardid);
    }
}
