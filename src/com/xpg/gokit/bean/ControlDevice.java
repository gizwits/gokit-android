/**
 * Project Name:Gokit
 * File Name:ControlDevice.java
 * Package Name:com.xpg.gokit.bean
 * Date:2014-11-18 10:05:40
 * Copyright (c) 2014~2015 Xtreme Programming Group, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.xpg.gokit.bean;

import java.io.Serializable;

import android.util.Log;

import com.xtremeprog.xpgconnect.XPGWifiDevice;

/**
 * 设备的IP地址，MAC地址等信息存储类.
 *
 * @author Sunny Ding
 */
@SuppressWarnings("serial")
public class ControlDevice implements Serializable {

	/** The is online. */
	private boolean isOnline;

	/** mac地址. */
	private String mac;

	/** ip地址. */
	private String ip;

	/** 设备名字. */
	private String name;

	/** 是否已绑定. */
	private boolean isBind;

	/** 设备did. */
	private String did;

	/** 设备的productKey. */
	private String productKey;

	/** 设备的passcode. */
	private String passcode;

	/** The is title. */
	private boolean isTitle = false;

	/** The title name. */
	private String titleName = "";

	/**
	 * Instantiates a new control device.
	 *
	 * @param titleName
	 *            the title name
	 */
	public ControlDevice(String titleName) {
		isTitle = true;
		this.titleName = titleName;

	}

	/**
	 * 把设备的IP地址，MAC地址等信息从XPGWifiDevice存进这个类.
	 *
	 * @param name
	 *            the name
	 * @param device
	 *            the device
	 * @param uid
	 *            user ID
	 */
	public ControlDevice(XPGWifiDevice device, boolean isBind) {
		if (device != null) {
			this.mac = device.getMacAddress();
			this.did = device.getDid();
			this.productKey = device.getProductKey();
			isOnline = true;
			this.isOnline = device.isOnline();
			this.ip = device.getIPAddress();
			this.passcode = device.getPasscode();
			Log.i("passcode", device.getMacAddress() + ":" + passcode);
			this.isBind = isBind;
			this.name = device.getProductName();
		}
	}

	/**
	 * Gets the did.
	 *
	 * @return the did
	 */
	public String getDid() {
		return did;
	}

	/**
	 * Sets the did.
	 *
	 * @param did
	 *            the new did
	 */
	public void setDid(String did) {
		this.did = did;
	}

	/**
	 * Gets the passcode.
	 *
	 * @return the passcode
	 */
	public String getPasscode() {
		return passcode;
	}

	/**
	 * Sets the passcode.
	 *
	 * @param passcode
	 *            the new passcode
	 */
	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}

	/**
	 * Checks if is bound.
	 *
	 * @return true, if is bound
	 */
	public boolean isBind() {
		return isBind;
	}

	/**
	 * Sets the bind.
	 *
	 * @param isBind
	 *            the isBind
	 */
	public void setNew(boolean isBind) {
		this.isBind = isBind;
	}

	/**
	 * Gets the mac.
	 *
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * Sets the mac.
	 *
	 * @param mac
	 *            the new mac
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * Gets the product key.
	 *
	 * @return the product key
	 */
	public String getProductKey() {
		return productKey;
	}

	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Sets the ip.
	 *
	 * @param ip
	 *            the new ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Checks if is title.
	 *
	 * @return true, if is title
	 */
	public boolean isTitle() {
		return isTitle;
	}

	/**
	 * Sets the title.
	 *
	 * @param isTitle
	 *            the new title
	 */
	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}

	/**
	 * Gets the title name.
	 *
	 * @return the title name
	 */
	public String getTitleName() {
		return titleName;
	}

	/**
	 * Sets the title name.
	 *
	 * @param titleName
	 *            the new title name
	 */
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	/**
	 * Checks if is online.
	 *
	 * @return true, if is online
	 */
	public boolean isOnline() {
		return isOnline;
	}

	/**
	 * Sets the online.
	 *
	 * @param isOnline
	 *            the new online
	 */
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	/**
	 * Sets the product key.
	 *
	 * @param productKey
	 *            the new product key
	 */
	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}

}
