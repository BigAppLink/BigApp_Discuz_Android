package com.kit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetConnectionChangeReceiver extends BroadcastReceiver {
	String packnameString = null;

		@Override
		public void onReceive(Context context, Intent intent) {
			packnameString = context.getPackageName();

			ConnectivityManager connectMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			//if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
			if (wifiNetInfo.isConnected()) {
				// connect network
				Toast.makeText(context, "connect Wi-Fi", Toast.LENGTH_LONG).show();
				//networkFlag=true;
			} 
			if(mobNetInfo.isConnected()){
				// connect network
				Toast.makeText(context, "connect 3G", Toast.LENGTH_LONG).show();
				//networkFlag=true;
			}
//			if(!networkFlag){
//				// unconnect network
//				Toast.makeText(context, "unconnect network", Toast.LENGTH_LONG).show();
//				//networkFlag=false;
//			}
		}
	}