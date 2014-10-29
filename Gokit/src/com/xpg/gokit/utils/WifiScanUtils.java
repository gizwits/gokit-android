package com.xpg.gokit.utils;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
/**
 * Wifi扫描工具
 * 
 * @author Sunny Ding
 * */
public class WifiScanUtils {
	/**
	 * 用来获得手机扫描到的所有wifi的信息
	 * 
	 * @param c 上下文
	 * 
	 * */
	static public List<ScanResult> getCurentWifiScanResult(Context c ){
		WifiManager wifiManager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
		wifiManager.startScan();
		return wifiManager.getScanResults();
	}
}
