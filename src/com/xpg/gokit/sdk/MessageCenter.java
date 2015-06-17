/**
 * Project Name:Gokit
 * File Name:MessageCenter.java
 * Package Name:com.xpg.gokit.sdk
 * Date:2014-11-18 10:06:04
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
package com.xpg.gokit.sdk;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.xpg.gokit.setting.SettingManager;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiSDK;
import com.xtremeprog.xpgconnect.XPGWifiSDK.XPGWifiConfigureMode;

/**
 * 指令管理器.
 *
 * @author Lien Li
 */
public class MessageCenter {
	
	/** The xpg wifi gcc. */
	private static XPGWifiSDK xpgWifiGCC;
	
	/** The m center. */
	private static MessageCenter mCenter;
	
	/** The m setting manager. */
	private SettingManager mSettingManager;

	/**
	 * Instantiates a new message center.
	 *
	 * @param c the c
	 */
	private MessageCenter(Context c) {
		if (mCenter == null) {
			init(c);
		}
	}

	/**
	 * Gets the single instance of MessageCenter.
	 *
	 * @param c the c
	 * @return single instance of MessageCenter
	 */
	public static MessageCenter getInstance(Context c) {
		if (mCenter == null) {
			mCenter = new MessageCenter(c);
		}
		return mCenter;
	}

	/**
	 * Inits the.
	 *
	 * @param c the c
	 */
	private void init(Context c) {
		mSettingManager = new SettingManager(c);
		
		xpgWifiGCC = XPGWifiSDK.sharedInstance();
		

		
	}

	/**
	 * Gets the XPG wifi sdk.
	 *
	 * @return the XPG wifi sdk
	 */
	public XPGWifiSDK getXPGWifiSDK() {
		return xpgWifiGCC;
	}

	// =================================================================
	//
	// 关于账号的指令
	//
	// =================================================================
	/**
	 * 注册账号.
	 *
	 * @param phone            注册手机号
	 * @param code            验证码
	 * @param password            注册密码
	 */
	public void cRegisterPhoneUser(String phone, String code, String password) {
		xpgWifiGCC.registerUserByPhoneAndCode(phone, password, code);
	}

	/**
	 * 匿名登录<P>
	 * 如果一开始不需要直接注册账号，则需要进行匿名登录.
	 */
	public void cUserLoginAnonymous() {
		xpgWifiGCC.userLoginAnonymous();
	}

	/**
	 * 账号注销.
	 */
	public void cLogout() {
		xpgWifiGCC.userLogout(mSettingManager.getUid());
		xpgWifiGCC.userLogout(mSettingManager.getHideUid());
		mSettingManager.clean();
	}

	/**
	 * 账号登陆.
	 *
	 * @param name            用户名
	 * @param psw            密码
	 */
	public void cLogin(String name, String psw) {
		xpgWifiGCC.userLoginWithUserName(name, psw);
	}

	/**
	 * 忘记密码.
	 *
	 * @param phone            手机号
	 * @param code            验证码
	 * @param password            密码
	 */
	public void cChangeUserPasswordWithCode(String phone, String code,
			String password) {
		xpgWifiGCC.changeUserPasswordByCode(phone, code, password);
	}

	/**
	 * 请求向手机发送验证码.
	 *
	 * @param phone            手机号
	 */
	public void cRequestSendVerifyCode(String phone) {
		xpgWifiGCC.requestSendVerifyCode(phone);
	}

	/**
	 * 发送airlink广播，把需要连接的wifi的ssid和password发给模块。.
	 *
	 * @param wifi            wifi名字
	 * @param password            wifi密码
	 */
	public void cSetAirLink(String wifi, String password) {
		xpgWifiGCC.setDeviceWifi(wifi, password, XPGWifiConfigureMode.XPGWifiConfigureModeAirLink, 60);
	}

	/**
	 * 设置SSID.
	 *
	 * @param ssid the ssid
	 * @param psw the psw
	 */
	public void cSetSSID(String ssid, String psw) {
		xpgWifiGCC.setDeviceWifi(ssid, psw, XPGWifiConfigureMode.XPGWifiConfigureModeSoftAP, 60);
	}

	/**
	 * 绑定后刷新设备列表，该方法会同时获取本地设备以及远程设备列表
	 * 由于该Demo只绘制了微信宠物屋（productKey为6f3074fe43894547a4f1314bd7e3ae0b）的界面
	 * 故只获取微信宠物屋
	 *
	 * @param uid            用户名
	 * @param token            密码
	 */
	public void cGetBoundDevices(String uid, String token) {
		xpgWifiGCC.getBoundDevices(uid, token, "6f3074fe43894547a4f1314bd7e3ae0b");
	}

	/**
	 * 绑定设备.
	 *
	 * @param uid            用户名
	 * @param token            密码
	 * @param did            did
	 * @param passcode            passcode
	 */
	public void cBindDevice(String uid, String token, String did,
			String passcode) {
		xpgWifiGCC.bindDevice(uid, token, did, passcode, null);
	}
	
	// =================================================================
	//
	// 关于控制设备的指令
	//
	// =================================================================

	/**
	 * 发送指令.
	 *
	 * @param xpgWifiDevice the xpg wifi device
	 * @param jsonsend the jsonsend
	 */
	public void cWrite(XPGWifiDevice xpgWifiDevice, JSONObject jsonsend) {
		xpgWifiDevice.write(jsonsend.toString());
	}

	/**
	 * 获取设备状态.
	 *
	 * @param xpgWifiDevice the xpg wifi device
	 * @throws JSONException the JSON exception
	 */
	public void cGetStatus(XPGWifiDevice xpgWifiDevice) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("cmd", 2);
		xpgWifiDevice.write(json.toString());
	}

	/**
	 * 断开连接.
	 *
	 * @param xpgWifiDevice the xpg wifi device
	 */
	public void cDisconnect(XPGWifiDevice xpgWifiDevice) {
		xpgWifiDevice.disconnect();
	}

	/**
	 * 获取Passcode.
	 *
	 * @param xpgWifiDevice the xpg wifi device
	 * @return the string
	 */
	public String cGetPasscode(XPGWifiDevice xpgWifiDevice) {
		return xpgWifiDevice.getPasscode();
	}

	/**
	 * 解除绑定.
	 *
	 * @param xpgWifiDevice the xpg wifi device
	 * @param uid the uid
	 * @param token the token
	 */
	public void cUnbindDevice(XPGWifiDevice xpgWifiDevice, String uid,
			String token) {
		xpgWifiGCC.unbindDevice(uid, token, xpgWifiDevice.getDid(), xpgWifiDevice.getPasscode());
	}

}