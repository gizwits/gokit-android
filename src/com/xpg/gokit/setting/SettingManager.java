/**
 * Project Name:Gokit
 * File Name:SettingManager.java
 * Package Name:com.xpg.gokit.setting
 * Date:2014-11-18 10:06:10
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
package com.xpg.gokit.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.util.Log;

/**
 * SharePreference处理类.
 *
 * @author Sunny Ding
 */
public class SettingManager {

	/** The spf. */
	SharedPreferences spf;

	/** The c. */
	private Context c;

	// =================================================================
	//
	// SharePreference文件中的变量名字列表
	//
	// =================================================================

	// Sharepreference文件的名字
	/** The share preferences. */
	private final String SHARE_PREFERENCES = "set";
	// 用户名
	/** The user name. */
	private final String USER_NAME = "username";
	// 手机号码
	/** The phone num. */
	private final String PHONE_NUM = "phonenumber";
	// 匿名登录用户名
	/** The hide uid. */
	private final String HIDE_UID = "hideuid";
	// 匿名登录密码
	/** The hide token. */
	private final String HIDE_TOKEN = "hidetoken";
	// 密码
	/** The password. */
	private final String PASSWORD = "password";
	// 用户名
	/** The token. */
	private final String TOKEN = "token";
	// 用户ID
	/** The uid. */
	private final String UID = "uid";
	// 服务器域名
	/** The sever name. */
	private final String SEVER_NAME = "server";

	/** The filter. */
	static String filter = "=====";

	/**
	 * Instantiates a new setting manager.
	 *
	 * @param c
	 *            the c
	 */
	public SettingManager(Context c) {
		this.c = c;
		spf = c.getSharedPreferences(SHARE_PREFERENCES, Context.MODE_PRIVATE);
	}

	/**
	 * ANDROID_ID是设备第一次启动时产生和存储的64bit的一个数，当设备被恢复出厂设置后该数重置 ANDROID_ID并不唯一.
	 *
	 * @return the phone id
	 */
	public String getPhoneId() {
		String android_id = Secure.getString(c.getContentResolver(), Secure.ANDROID_ID);
		return android_id;
	}

	/**
	 * set一堆ProductKey在SharePreference的一个变量下.
	 *
	 * @param produck_key
	 *            the produck_key
	 */
	public void DownLoadProduct_key(String produck_key) {
		String allkeys = spf.getString("keys", "");
		if (allkeys.contains(produck_key)) {
			return;
		} else {
			synchronized (spf) {
				Log.i("add_poduct_key_in", produck_key);
				spf.edit().putString("keys", allkeys + produck_key + filter).commit();
			}
		}

	}

	/**
	 * 在SharePreference的一个变量下的一堆ProductKey中获取第一个ProductKey.
	 *
	 * @return the down load product_key
	 */
	public String getDownLoadProduct_key() {
		String allkeys = spf.getString("keys", "");
		String[] keys = allkeys.split(filter);
		if (!keys[0].equals("")) {
			String newkeys = allkeys.replace(keys[0] + filter, "");
			synchronized (spf) {
				Log.i("add_poduct_key", keys[0]);
				spf.edit().putString("keys", newkeys).commit();
			}

			return keys[0];
		} else {
			return null;
		}
	}

	/**
	 * SharePreference clean.
	 */
	public void clean() {
		setHideToken("");
		setHideUid("");
		setUid("");
		setToken("");
		setPhoneNumber("");
		setPassword("");
		setUserName("");
	}

	/**
	 * Sets the user name.
	 *
	 * @param name
	 *            the new user name
	 */
	public void setUserName(String name) {
		spf.edit().putString(USER_NAME, name).commit();

	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return spf.getString(USER_NAME, "");
	}

	/**
	 * Sets the phone number.
	 *
	 * @param phoneNumber
	 *            the new phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		spf.edit().putString(PHONE_NUM, phoneNumber).commit();
	}

	/**
	 * Gets the phone number.
	 *
	 * @return the phone number
	 */
	public String getPhoneNumber() {
		return spf.getString(PHONE_NUM, "");
	}

	/**
	 * Sets the hide uid.
	 *
	 * @param uid
	 *            the new hide uid
	 */
	public void setHideUid(String uid) {
		spf.edit().putString(HIDE_UID, uid).commit();
	}

	/**
	 * Gets the hide uid.
	 *
	 * @return the hide uid
	 */
	public String getHideUid() {
		return spf.getString(HIDE_UID, "");
	}

	/**
	 * Sets the hide token.
	 *
	 * @param token
	 *            the new hide token
	 */
	public void setHideToken(String token) {
		spf.edit().putString(HIDE_TOKEN, token).commit();
	}

	/**
	 * Gets the hide token.
	 *
	 * @return the hide token
	 */
	public String getHideToken() {
		return spf.getString(HIDE_TOKEN, "");
	}

	/**
	 * Sets the password.
	 *
	 * @param psw
	 *            the new password
	 */
	public void setPassword(String psw) {
		spf.edit().putString(PASSWORD, psw).commit();
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return spf.getString(PASSWORD, "");
	}

	/**
	 * Sets the token.
	 *
	 * @param token
	 *            the new token
	 */
	public void setToken(String token) {
		spf.edit().putString(TOKEN, token).commit();
	}

	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return spf.getString(TOKEN, "");
	}

	/**
	 * Sets the uid.
	 *
	 * @param uid
	 *            the new uid
	 */
	public void setUid(String uid) {
		spf.edit().putString(UID, uid).commit();
	}

	/**
	 * Gets the uid.
	 *
	 * @return the uid
	 */
	public String getUid() {
		return spf.getString(UID, "");
	}

	/**
	 * Gets the server name.
	 *
	 * @return the server name
	 */
	public String getServerName() {
		return spf.getString(SEVER_NAME, "api.gizwits.com");
	}

	/**
	 * Sets the server name.
	 *
	 * @param server
	 *            the new server name
	 */
	public void setServerName(String server) {
		spf.edit().putString(SEVER_NAME, server).commit();
	}

}
