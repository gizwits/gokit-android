package com.xpg.gokit.utils;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 网络工具类
 * 
 *@author Sunny Ding
 * 
 * **/
public class NetUtils {
	/**
	  * 判断当前手机是否连上Wifi
	  * 
	  * @param context 上下文
	  * @return boolean 是否连上网络
	  * 
	  * **/
	 static public boolean isWifiConnected(Context context) {  
		      if (context != null) {  
		          ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
		                  .getSystemService(Context.CONNECTIVITY_SERVICE);  
		          NetworkInfo mWiFiNetworkInfo = mConnectivityManager  
		                  .getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
		          if (mWiFiNetworkInfo != null) {  
		              if( mWiFiNetworkInfo.isAvailable()){
		            	  return mWiFiNetworkInfo.isConnected();
		              }else{
		            	  return false;
		              }
		          }  
		     }  
		     return false;  
		 }  
	 /**
	  * 判断当前手机的网络是否可用
	  * 
	  * @param context 上下文
	  * @return boolean 是否连上网络
	  * 
	  * **/
	 static public boolean isMobileConnected(Context context) {  
		      if (context != null) {  
		          ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
		                  .getSystemService(Context.CONNECTIVITY_SERVICE);  
		          NetworkInfo mMobileNetworkInfo = mConnectivityManager  
		                 .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
		          if (mMobileNetworkInfo != null) {  
		             if( mMobileNetworkInfo.isAvailable()){
		            	 return mMobileNetworkInfo.isConnected();
		             }else{
		            	 return false;
		             }
		          }  
		     }  
		     return false;  
		 } 
	 /**
	  * 判断当前网络是手机网络还是WIFI
	  * 
	  * @param context 上下文
	  * @return ConnectedType 数据类型
	  * 
	  * **/
	 public static int getConnectedType(Context context) {  
		      if (context != null) {  
		          ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
		                  .getSystemService(Context.CONNECTIVITY_SERVICE);
		       // 获取代表联网状态的NetWorkInfo对象 
		          NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		          // 判断NetWorkInfo对象是否为空；判断当前的网络连接是否可用 
		          if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {  
		              return mNetworkInfo.getType();  
		          }  
		      }  
		     return -1;  
		 }
	 /**
	  * 获取当前WIFI的SSID
	  * 
	  * @param context 上下文
	  * @return ssid 
	  * 
	  * **/
	 public static String getCurentWifiSSID(Context context){
		 String ssid = "";
		 if(context!=null){
			 WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			 WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			 ssid = wifiInfo.getSSID();
			 if (ssid.substring(0, 1).equals("\"")
						&& ssid.substring(ssid.length() - 1).equals("\"")) {
					ssid = ssid.substring(1, ssid.length() - 1);
				}
		 }
		 return ssid;
	 }
	 
	 /**
		 * 用来获得手机扫描到的所有wifi的信息
		 * 
		 * @param c 上下文
		 * 
		 * */
		static public List<ScanResult> getCurrentWifiScanResult(Context c ){
			WifiManager wifiManager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
			wifiManager.startScan();
			return wifiManager.getScanResults();
		}

}
