package com.youzu.clan.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ProfileChangedReceiver extends BroadcastReceiver {
      
    private static final String TAG = "ProfileChangedReceiver";
      
    @Override  
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");  
        Log.i(TAG, msg);
    }  
  
}  
