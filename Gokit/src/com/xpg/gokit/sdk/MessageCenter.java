package com.xpg.gokit.sdk;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.xpg.gokit.BuildConfig;
import com.xpg.gokit.setting.SettingManager;
import com.xtremeprog.xpgconnect.XPGWifiConfig;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiLogLevel;
import com.xtremeprog.xpgconnect.XPGWifiSDK;

/**
 * 信息管理器
 * 
 * @author Lien Li
 * */
public class MessageCenter {
	private static XPGWifiSDK xpgWifiGCC;
	private static MessageCenter mCenter;
	private SettingManager mSettingManager;

	private MessageCenter(Context c) {
		if (mCenter == null) {
			init(c);
		}
	}

	public static MessageCenter getInstance(Context c) {
		if (mCenter == null) {
			mCenter = new MessageCenter(c);
		}
		return mCenter;
	}

	private void init(Context c) {
		mSettingManager = new SettingManager(c);
		XPGWifiConfig.sharedInstance().SetDebug(BuildConfig.DEBUG);
		XPGWifiConfig.sharedInstance().SetProductPath(
				c.getFilesDir() + "/Devices");
		// 切换为Debug服务器
		XPGWifiConfig.sharedInstance().EnableProductFilter(false);
		xpgWifiGCC = XPGWifiSDK.sharedInstance();
		XPGWifiConfig.sharedInstance().SetSwitchService(false);
		xpgWifiGCC.SetAppID("7ac10dec7dba436785ac23949536a6eb");

		XPGWifiSDK.SetLogLevel(XPGWifiLogLevel.XPGWifiLogLevelAll);
		XPGWifiSDK.SetPrintDataLevel(true);
	}

	public XPGWifiSDK getXPGWifiSDK() {
		return xpgWifiGCC;
	}
//=================================================================
//	  
//	 关于账号的指令
//	  
//=================================================================
	/**
	 * 注册账号
	 * @param phone
	 *            注册手机号
	 * @param code
	 *            验证码
	 * @param password
	 *            注册密码
	 * */
	public void cRegisterPhoneUser(String phone, String code, String password) {
		xpgWifiGCC.RegisterPhoneUser(phone, password, code);
	}

	/**
	 * 注册匿名账号
	 * */
	public void cRegisterAnonymousUser() {
		xpgWifiGCC.RegisterAnonymousUser(mSettingManager.getPhoneId());
	}

	/**
	 * 账号注销
	 * */
	public void cLogout() {
		xpgWifiGCC.UserLogout(mSettingManager.getUid());
		xpgWifiGCC.UserLogout(mSettingManager.getHideUid());
		mSettingManager.clean();
	}

	/**
	 * 账号登陆
	 * @param name
	 *            用户名
	 * @param psw
	 *            密码
	 * */
	public void cLogin(String name, String psw) {
		xpgWifiGCC.UserLogin(name, psw);
	}

	/**
	 * 忘记密码
	 * */
	public void cChangeUserPasswordWithCode(String phone, String code,
			String password) {
		xpgWifiGCC.changeUserPasswordWithCode(phone, code, password);
	}

	/**
	 * 请求向手机发送验证码
	 * */
	public void cRequestSendVerifyCode(String phone) {
		xpgWifiGCC.RequestSendVerifyCode(phone);
	}

	/**
	 * 发送airlink广播，把需要连接的wifi的ssid和password发给模块。
	 * */
	public void cSetAirLink(String wifi, String password) {
		xpgWifiGCC.SetAirLink(wifi, password);
	}

	/**
	 * 设置SSID
	 * */
	public void cSetSSID(String ssid, String psw) {
		xpgWifiGCC.SetSSID(ssid, psw);
	}

	/**
	 * 绑定后刷新设备列表
	 * */
	public void cGetBoundDevices(String uid, String token) {
		xpgWifiGCC.GetBoundDevices(uid, token);
	}
	/**
	 * 绑定设备
	 * */
	public void cBindDevice(String uid, String token,String did,String passcode) {
		xpgWifiGCC.BindDevice(uid, token, did, passcode);
	}

	
//=================================================================
//	  
//	  关于控制设备的指令
//	  
//=================================================================
	
	/**
	 * 发送数据点
	 * */
	public void cWrite(XPGWifiDevice xpgWifiDevice,JSONObject jsonsend) {
		xpgWifiDevice.write(jsonsend.toString());
	}
	/**
	 * 获取设备状态
	 * */
	public void cGetStatus(XPGWifiDevice xpgWifiDevice) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("cmd", 2);
		xpgWifiDevice.write(json.toString());
	}
	/**
	 * 断开连接
	 * */
	public void cDisconnect(XPGWifiDevice xpgWifiDevice) {
		xpgWifiDevice.Disconnect();
	}
	/**
	 * 获取Passcode
	 * */
	public String cGetPasscode(XPGWifiDevice xpgWifiDevice) {
		return xpgWifiDevice.GetPasscode();
	}
	/**
	 * 解除绑定
	 * */
	public void cUnbindDevice(XPGWifiDevice xpgWifiDevice,String uid,String token) {
		xpgWifiDevice.UnbindDevice(uid, token);
	}

}