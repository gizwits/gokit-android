package com.xpg.gokit.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.xpg.gokit.sdk.MessageCenter;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiDeviceList;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;
import com.xtremeprog.xpgconnect.XPGWifiSSIDList;

public class BaseActivity extends Activity      {
	protected ActionBar actionBar;
	protected static XPGWifiDeviceList xpgwifidevicelist;
	protected static List<XPGWifiDevice>deviceslist = new ArrayList<XPGWifiDevice>();
	static boolean isInit = false; 
	MessageCenter mCenter;
	private XPGWifiSDKListener listener = new XPGWifiSDKListener(){
		
		public void onChangeUserEmail(int error, String errorMessage) {};
		public void onUserLogout(int error, String errorMessage) {};
		public void onTransUser(int error, String errorMessage) {};
		public void onChangeUserPhone(int error, String errorMessage) {};
		public void onChangeUserPassword(int error, String errorMessage) {};
		@Override
		public void onRequestSendVerifyCode(int error, String errorMessage) {};
		public void onDiscovered(int result, XPGWifiDeviceList devices) {
			// TODO Auto-generated method stub
		}
		public void onGetDeviceInfo(int error, String errorMessage, String productKey, String did, String mac, String passCode, String host, int port, int isOnline) {};
		public void onBindDevice(int error, String errorMessage) {};
//		public long onCalculateCRC(byte[] data) {};
		public void onRegisterUser(int error, String errorMessage, String uid, String token) {};
		public void onUnbindDevice(int error, String errorMessage) {};
		public void onUserLogin(int error, String errorMessage, String uid, String token) {};
		
		public void onUpdateProduct(int result) {};
		@Override
		public void onSetAirLink(XPGWifiDevice device) {
			// TODO Auto-generated method stub
		}
		
		public void onGetSSIDList(XPGWifiSSIDList list, int result) {};
		
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
	    mCenter = MessageCenter.getInstance(this.getApplicationContext());
	    
		mCenter.getXPGWifiSDK().setListener(listener);
		
	    
	    
		
	}
	public static XPGWifiDevice findDeviceByMac(String mac,String did){
		XPGWifiDevice xpgdevice = null;
		Log.i("count", BaseActivity.deviceslist.size()+"");
		for (int i = 0; i < BaseActivity.deviceslist.size(); i++) {
			XPGWifiDevice device = deviceslist.get(i);
			if(device!=null){
				Log.i("deivcemac", device.GetMacAddress());
				if(device!=null&&device.GetMacAddress().equals(mac)&&device.GetDid().equals(did)){
					xpgdevice = device;
					break;
				}
			}
			
		}
		
		return xpgdevice;
	}
	public void onResume(){
		super.onResume();
		
	}
	public void onPause(){
		super.onPause();
		
	}

	




}
