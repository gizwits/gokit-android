package com.xpg.gokit.bean;

import java.io.Serializable;

import android.util.Log;

import com.xtremeprog.xpgconnect.XPGWifiDevice;

/**
 * 设备的IP地址，MAC地址等信息存储类
 * @author Sunny Ding
 * */
@SuppressWarnings("serial")
public class ControlDevice implements Serializable {
	private boolean isOnline ;
	/**
	 * mac地址
	 * */
	private String mac;
	/**
	 * ip地址
	 * */
	private String ip;
	/**
	 * 设备名字
	 * */
	private String name;
	/**
	 * 是否没有passcode
	 * */
	private boolean isNew;
	/**
	 * 设备did
	 * */
	private String did;
	/**
	 * 设备的productKey
	 * */
	private String productKey;
	/**
	 * 设备的passcode
	 * */
	private String passcode;
	
	private boolean isTitle = false;
	private String titleName = "";
	
	public ControlDevice(String titleName){
		isTitle = true;
		this.titleName = titleName;
		
	}
	/**
	 * 
	 * 把设备的IP地址，MAC地址等信息从XPGWifiDevice存进这个类
	 * 
	 * */
	public ControlDevice(String name,XPGWifiDevice device){
		if(device!=null){
			this.mac = device.GetMacAddress();			
			this.did = device.GetDid();
			this.productKey = device.GetProductKey();
			isOnline = true;
			this.isOnline = device.IsOnline();
			this.ip = device.GetIPAddress();
			this.passcode = device.GetPasscode();
			Log.i("passcode", passcode);
			if(passcode!=null&&!passcode.equals("")){
				this.isNew = false;
			}else{
				this.isNew = true;
			}
		}
		this.name = device.GetProductName();
	}


	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getPasscode() {
		return passcode;
	}

	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}

	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getProductKey(){
		return productKey;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isTitle() {
		return isTitle;
	}

	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}


	
}
