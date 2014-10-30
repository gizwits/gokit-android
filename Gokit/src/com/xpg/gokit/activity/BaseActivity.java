package com.xpg.gokit.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.xpg.gokit.sdk.MessageCenter;
import com.xpg.gokit.setting.SettingManager;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiDeviceList;
import com.xtremeprog.xpgconnect.XPGWifiDeviceListener;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;
import com.xtremeprog.xpgconnect.XPGWifiSSIDList;

public class BaseActivity extends Activity {
	protected ActionBar actionBar;
	protected static XPGWifiDeviceList xpgwifidevicelist;
	protected static List<XPGWifiDevice> deviceslist = new ArrayList<XPGWifiDevice>();
	static boolean isInit = false;
	protected MessageCenter mCenter;
	protected SettingManager setmanager;

	private XPGWifiDeviceListener deviceListener = new XPGWifiDeviceListener() {
		public void onBindDevice(int error, String errorMessage) {
			BaseActivity.this.onSDKBindDevice(error, errorMessage);
		};

		public void onUnbindDevice(int error, String errorMessage) {
			BaseActivity.this.onUnbindDevice(error, errorMessage);
		};

		public void onUpdateUI() {
			BaseActivity.this.onUpdateUI();
		};

		public void onConnectFailed() {
			BaseActivity.this.onConnectFailed();
		};

		public void onLogin(int result) {
			BaseActivity.this.onLogin(result);
		};

		public void onQueryHardwareInfo(int error,
				com.xtremeprog.xpgconnect.XPGWifiQueryHardwareInfoStruct pInfo) {
			BaseActivity.this.onQueryHardwareInfo(error, pInfo);
		};

		public void onReceiveAlertsAndFaultsInfo(
				com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo alerts,
				com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo faults) {
			BaseActivity.this.onReceiveAlertsAndFaultsInfo(alerts, faults);
		};

		public void onDeviceOnline(boolean isOnline) {
			BaseActivity.this.onDeviceOnline(isOnline);
		};

		public void onGetPasscode(int result) {
			BaseActivity.this.onGetPasscode(result);
		};;

		public void onDisconnected() {
			BaseActivity.this.onDisconnected();
		}

		public boolean onReceiveData(String data) {
			return BaseActivity.this.onReceiveData(data);
		};

		public void onConnected() {
			BaseActivity.this.onConnected();
		}

		public void onSetSwitcher(int result) {
			BaseActivity.this.onSetSwitcher(result);
		};

		public void onDeviceLog(short nLevel, String tag, String source,
				String content) {
			BaseActivity.this.onDeviceLog(nLevel, tag, source, content);
		};

		public void onLoginMQTT(int result) {
			BaseActivity.this.onLoginMQTT(result);
		}
	};

	private XPGWifiSDKListener sdkListener = new XPGWifiSDKListener() {

		public void onChangeUserEmail(int error, String errorMessage) {
			BaseActivity.this.onChangeUserEmail(error, errorMessage);
		};

		public void onUserLogout(int error, String errorMessage) {
			BaseActivity.this.onUserLogout(error, errorMessage);
		};

		public void onTransUser(int error, String errorMessage) {
			BaseActivity.this.onTransUser(error, errorMessage);
		};

		public void onChangeUserPhone(int error, String errorMessage) {
			BaseActivity.this.onChangeUserPhone(error, errorMessage);
		};

		public void onChangeUserPassword(int error, String errorMessage) {
			BaseActivity.this.onChangeUserPassword(error, errorMessage);
		};

		@Override
		public void onRequestSendVerifyCode(int error, String errorMessage) {
			BaseActivity.this.onRequestSendVerifyCode(error, errorMessage);
		};

		public void onDiscovered(int result, XPGWifiDeviceList devices) {
			BaseActivity.this.onDiscovered(result, devices);
		}

		public void onGetDeviceInfo(int error, String errorMessage,
				String productKey, String did, String mac, String passCode,
				String host, int port, int isOnline) {
			BaseActivity.this.onGetDeviceInfo(error, errorMessage, productKey,
					did, mac, passCode, host, port, isOnline);
		};

		public void onBindDevice(int error, String errorMessage) {
			BaseActivity.this.onBindDevice(error, errorMessage);
		};

		public void onRegisterUser(int error, String errorMessage, String uid,
				String token) {
			BaseActivity.this.onRegisterUser(error, errorMessage, uid, token);
		};

		public void onUnbindDevice(int error, String errorMessage) {
			BaseActivity.this.onSDKUnbindDevice(error, errorMessage);
		};

		public void onUserLogin(int error, String errorMessage, String uid,
				String token) {
			BaseActivity.this.onUserLogin(error, errorMessage, uid, token);
		};

		public void onUpdateProduct(int result) {
			BaseActivity.this.onUpdateProduct(result);
		};

		@Override
		public void onSetAirLink(XPGWifiDevice device) {
			BaseActivity.this.onSetAirLink(device);
		}

		public void onGetSSIDList(XPGWifiSSIDList list, int result) {
			BaseActivity.this.onGetSSIDList(list, result);
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmanager = new SettingManager(this);
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		mCenter = MessageCenter.getInstance(this.getApplicationContext());
		mCenter.getXPGWifiSDK().setListener(sdkListener);
	}

	/**
	 * 通过did和mac在列表寻找对应的device
	 * */
	public static XPGWifiDevice findDeviceByMac(String mac, String did) {
		XPGWifiDevice xpgdevice = null;
		Log.i("count", BaseActivity.deviceslist.size() + "");
		for (int i = 0; i < BaseActivity.deviceslist.size(); i++) {
			XPGWifiDevice device = deviceslist.get(i);
			if (device != null) {
				Log.i("deivcemac", device.GetMacAddress());
				if (device != null && device.GetMacAddress().equals(mac)
						&& device.GetDid().equals(did)) {
					xpgdevice = device;
					break;
				}
			}

		}

		return xpgdevice;
	}

	public void onResume() {
		super.onResume();

	}

	public void onPause() {
		super.onPause();

	}

	protected void onChangeUserEmail(int error, String errorMessage) {
	};

	protected void onUserLogout(int error, String errorMessage) {
	};

	protected void onTransUser(int error, String errorMessage) {
	};

	protected void onChangeUserPhone(int error, String errorMessage) {
	};

	protected void onChangeUserPassword(int error, String errorMessage) {
	};

	/**
	 * 手机验证码已经发送 if error == 0
	 * */
	protected void onRequestSendVerifyCode(int error, String errorMessage) {
	};

	protected void onDiscovered(int result, XPGWifiDeviceList devices) {

	}

	protected void onGetDeviceInfo(int error, String errorMessage,
			String productKey, String did, String mac, String passCode,
			String host, int port, int isOnline) {
	};

	protected void onSDKBindDevice(int error, String errorMessage) {
	};

	/**
	 * 注册账号回调该函数。
	 * 
	 * @param error
	 * @param errorMessage
	 * @param uid
	 * @param token
	 * */
	protected void onRegisterUser(int error, String errorMessage, String uid,
			String token) {
	};

	protected void onSDKUnbindDevice(int error, String errorMessage) {
	};

	protected void onUserLogin(int error, String errorMessage, String uid,
			String token) {
	};

	protected void onUpdateProduct(int result) {
	};

	/**
	 * 通过airlink成功配置模块后，回调该函数。
	 * 
	 * @param device
	 *            成功配置的设备实体
	 * */
	protected void onSetAirLink(XPGWifiDevice device) {
	}

	protected void onGetSSIDList(XPGWifiSSIDList list, int result) {
	};

	public void onBindDevice(int error, String errorMessage) {
	};

	public void onUnbindDevice(int error, String errorMessage) {
	};

	public void onUpdateUI() {
	};

	/**
	 * socket 连接失败
	 * */
	public void onConnectFailed() {
	};

	/**
	 * 小循环授权成功如果 result == 0, 失败如果 result == 2
	 * */
	public void onLogin(int result) {
	};

	public void onQueryHardwareInfo(int error,
			com.xtremeprog.xpgconnect.XPGWifiQueryHardwareInfoStruct pInfo) {
	};

	public void onReceiveAlertsAndFaultsInfo(
			com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo alerts,
			com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo faults) {
	};

	public void onDeviceOnline(boolean isOnline) {
	};
	/**
	 * 获取Passcode
	 * */
	public void onGetPasscode(int result) {
	};;

	/**
	 * socket 连接被断开
	 * */
	public void onDisconnected() {
	}

	/**
	 * 收到设备数据
	 * */
	public boolean onReceiveData(String data) {
		return true;
	};

	/**
	 * socket 连接成功
	 * */
	public void onConnected() {
	}

	public void onSetSwitcher(int result) {
	};

	public void onDeviceLog(short nLevel, String tag, String source,
			String content) {
	};

	/**
	 * 大循环授权成功如果 result == 0, 失败如果 result == 2
	 * */
	public void onLoginMQTT(int result) {
	}

}
