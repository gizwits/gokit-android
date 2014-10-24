package com.xpg.gokit.utils;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiScanUtils {
	static public List<ScanResult> getCurentWifiScanResult(Context c ){
		WifiManager wifiManager = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
		wifiManager.startScan();
		return wifiManager.getScanResults();
	}
}
