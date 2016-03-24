package com.kit.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * 获取设备IP和mac地址工具类
 * 
 * @author zhoud
 * @date 2014-8-2 下午13:25
 */
public class NetAddressUtil {

	/**
	 * 获取设备IP地址
	 * 
	 * @return 设备ip地址
	 */
	public static String getLocalIpAddress(){ 
        
        try{ 
             for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) { 
                 NetworkInterface intf = en.nextElement();   
                    for (Enumeration<InetAddress> enumIpAddr = intf   
                            .getInetAddresses(); enumIpAddr.hasMoreElements();) {   
                        InetAddress inetAddress = enumIpAddr.nextElement();   
                        if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {   
                             
                            return inetAddress.getHostAddress().toString();   
                        }   
                    }   
             } 
        }catch (SocketException e) { 
            // TODO: handle exception 
        	Log.v("Steel", "WifiPreference IpAddress---error-" + e.toString());
        } 
         
        return null;  
    } 

	/**
	 * 获取设备mac地址
	 * @param mContext
	 * @return 设备mac地址
	 */
	public static  String getLocalMacAddress(Context mContext) {
		WifiManager wifi = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
}
