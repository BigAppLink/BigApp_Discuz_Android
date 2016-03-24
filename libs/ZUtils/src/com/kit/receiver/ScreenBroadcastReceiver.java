package com.kit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenBroadcastReceiver extends BroadcastReceiver {
    private String action = null;


    @Override
    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            // 开屏
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            // 锁屏
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            // 解锁
        }
    }
}

//    private void startScreenBroadcastReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        filter.addAction(Intent.ACTION_USER_PRESENT);
//        context.registerReceiver(mScreenReceiver, filter);
//    }