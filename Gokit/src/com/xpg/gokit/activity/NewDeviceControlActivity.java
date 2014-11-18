/**
 * Project Name:Gokit
 * File Name:NewDeviceControlActivity.java
 * Package Name:com.xpg.gokit.activity
 * Date:2014-11-18 10:05:02
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
package com.xpg.gokit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xpg.gokit.R;
import com.xpg.gokit.bean.ControlDevice;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiDeviceList;
import com.xtremeprog.xpgconnect.XPGWifiQueryHardwareInfoStruct;

/**
 * 新设备绑定界面
 * <P>
 * 该Activity演示如何绑定设备，完成设备的绑定后，才能控制设备。.
 * 
 * @author Lien Li
 */
public class NewDeviceControlActivity extends BaseActivity implements
		OnClickListener {

	/** The Constant LOG. */
	protected static final int LOG = 0;

	/** The Constant TOAST. */
	protected static final int TOAST = 1;

	/** The Constant PASSCODE. */
	protected static final int PASSCODE = 2;

	/** The Constant BAND_SUCCESS. */
	protected static final int BAND_SUCCESS = 3;

	/** The Constant BAND_FAIL. */
	protected static final int BAND_FAIL = 4;

	/** The Constant HARD_INFO. */
	protected static final int HARD_INFO = 5;

	/** The edt_receive_device_msg. */
	private MultiAutoCompleteTextView edt_receive_device_msg;

	/** The control device. */
	ControlDevice controlDevice;

	/** The btn_menu. */
	Button btn_menu;

	/** The btn_back. */
	Button btn_back;

	/** The btn_get_psc. */
	Button btn_get_psc;

	/** The tv_title. */
	TextView tv_title;

	/** The rl_login_band. */
	private RelativeLayout rl_login_band;

	/** The rl_content. */
	RelativeLayout rl_content;

	/** The xpgdevice. */
	XPGWifiDevice xpgdevice;

	/** The produck_key. */
	String produck_key = "";// = it.getStringExtra("product_key");

	/** The passcode. */
	String passcode = "";// = it.getStringExtra("passcode");

	/** The did. */
	String did = "";// = it.getStringExtra("did");

	/** The dialog. */
	ProgressDialog dialog;

	/** The handler. */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HARD_INFO:
				XPGWifiQueryHardwareInfoStruct infost = (XPGWifiQueryHardwareInfoStruct) msg.obj;
				StringBuilder sb = new StringBuilder();
				sb.append("Wifi Hardware Version:" + infost.getWifiHardVer()
						+ "\r\n");
				sb.append("Wifi Software Version:" + infost.getWifiSoftVer()
						+ "\r\n");
				sb.append("MCU Hardware Version:" + infost.getMcuHardVer()
						+ "\r\n");
				sb.append("MCU Software Version:" + infost.getMcuSoftVer()
						+ "\r\n");
				sb.append("Firmware Id:" + infost.getFirmwareId() + "\r\n");
				sb.append("Firmware Version:" + infost.getFirmwareVer()
						+ "\r\n");
				sb.append("Product Key:" + infost.getProductKey() + "\r\n");

				edt_receive_device_msg.setText(sb.toString());
				break;
			case LOG:

				break;
			case TOAST:
				String info = msg.obj + "";
				Toast.makeText(NewDeviceControlActivity.this, info,
						Toast.LENGTH_SHORT).show();
				break;
			case PASSCODE:
				edt_receive_device_msg.append((String) msg.obj + "\r\n");
				String uid = setmanager.getUid();
				String token = setmanager.getToken();
				String hideuid = setmanager.getHideUid();
				String hidetoken = setmanager.getHideToken();
				if (!uid.equals("") && !token.equals("")) {
					dialog.show();
					xpgdevice.BindDevice(uid, token);

				} else if (!hideuid.equals("") && !hidetoken.equals("")) {
					dialog.show();
					xpgdevice.BindDevice(hideuid, hidetoken);
				} else {
					mCenter.cRegisterAnonymousUser();
				}

				break;
			case BAND_SUCCESS:
				Toast.makeText(NewDeviceControlActivity.this, "绑定成功",
						Toast.LENGTH_SHORT).show();
				if (null != xpgdevice)
					xpgdevice.Disconnect();
				finish();
				break;
			case BAND_FAIL:
				Toast.makeText(NewDeviceControlActivity.this, "绑定设备失败",
						Toast.LENGTH_SHORT).show();

				break;
			}
		}
	};

	/*
	 * 设备登录成功回调
	 */
	@Override
	public void onLogin(int result) {
		Log.i("result", result + "");
		if (result == 0) {
			Message msg = new Message();
			msg.what = TOAST;
			msg.obj = "小循环登陆成功";
			handler.sendMessage(msg);
		}
	};

	/*
	 * 设备绑定成功回调
	 */
	@Override
	public void onBindDevice(int error, String errorMessage) {
		Log.i("error", errorMessage);
		dialog.dismiss();
		if (error == 0) {
			handler.sendEmptyMessage(BAND_SUCCESS);
		} else {
			Message msg = new Message();
			msg.what = BAND_FAIL;
			msg.obj = errorMessage;
			handler.sendMessage(msg);
		}
	};

	/*
	 * 获取模块和mcu协议版本等属性.
	 */
	@Override
	public void onQueryHardwareInfo(int error,
			XPGWifiQueryHardwareInfoStruct pInfo) {
		if (error == 0) {
			XPGWifiQueryHardwareInfoStruct info = new XPGWifiQueryHardwareInfoStruct();
			info.setFirmwareId(pInfo.getFirmwareId());
			info.setFirmwareVer(pInfo.getFirmwareVer());
			info.setMcuHardVer(pInfo.getMcuHardVer());
			info.setMcuSoftVer(pInfo.getMcuSoftVer());
			info.setP0Ver(pInfo.getP0Ver());
			info.setProductKey(pInfo.getProductKey());
			info.setWifiHardVer(pInfo.getWifiHardVer());
			info.setWifiSoftVer(pInfo.getWifiSoftVer());
			Log.i("info", info.getProductKey());
			Message msg = new Message();
			msg.what = HARD_INFO;
			msg.obj = info;
			handler.sendMessage(msg);
		}

	};

	/*
	 *获取Passcode
	 */
	@Override
	public void onGetPasscode(int result) {
		if (result == 0) {
			Message msg = new Message();
			msg.obj = xpgdevice.GetPasscode();
			msg.what = PASSCODE;
			handler.sendMessage(msg);
		}
	}

	/*
	 * 收到设备数据.
	 */
	@Override
	public boolean onReceiveData(String data) {
		Log.i("data", data);
		return true;
	};

	/*
	 *socket 连接成功.
	 */
	@Override
	public void onConnected() {
		Log.i("connected", "connected");
		Message msg1 = new Message();
		msg1.what = LOG;
		msg1.obj = "连接成功";
		handler.sendMessage(msg1);
		xpgdevice.GetHardwareInfo();
	}

	/*
	 * socket 连接被断开.
	 */
	@Override
	public void onDisconnected() {
		Log.i("disconnected", "disconnected");
		Message msg1 = new Message();
		msg1.what = LOG;
		msg1.obj = "连接断开";
		handler.sendMessage(msg1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_device_control);
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent it = getIntent();
		produck_key = it.getStringExtra("product_key");
		passcode = it.getStringExtra("passcode");
		did = it.getStringExtra("did");
		if (produck_key != null && passcode != null && did != null) {
			String uid = setmanager.getUid();
			String token = setmanager.getToken();
			String hideuid = setmanager.getHideUid();
			String hidetoken = setmanager.getHideToken();
			if (!uid.equals("") && !token.equals("")) {
				mCenter.cBindDevice(uid, token, did, passcode);
			} else if (!hideuid.equals("") && !hidetoken.equals("")) {
				mCenter.cBindDevice(hideuid, hidetoken, did, passcode);
			}
		} else {
			controlDevice = (ControlDevice) it.getSerializableExtra("device");

			xpgdevice = BaseActivity.findDeviceByMac(controlDevice.getMac(),
					controlDevice.getDid());
		}
		initView();
		initData();
		initListener();

		if (xpgdevice != null) {
			xpgdevice.setListener(deviceListener);
			xpgdevice.GetHardwareInfo();
			Log.i("login", "connected");
		}

	}

	/**
	 * Inits the view.
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_menu = (Button) findViewById(R.id.btn_menu);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_get_psc = (Button) findViewById(R.id.btn_get_psc);
		edt_receive_device_msg = (MultiAutoCompleteTextView) findViewById(R.id.edt_receive_device_msg);
		dialog = new ProgressDialog(this);
		dialog.setMessage("绑定中，请稍候...");
		View v = getLayoutInflater().inflate(R.layout.menu_new, null);
		rl_login_band = (RelativeLayout) v.findViewById(R.id.rl_login_band);
		rl_login_band.setOnClickListener(this);

	}

	public void onResume() {
		super.onResume();
		if (xpgdevice != null) {
			this.xpgdevice.setListener(deviceListener);
		}
	}

	/**
	 * Inits the data.
	 */
	private void initData() {
		tv_title.setText("未知设备");
		edt_receive_device_msg.setText(passcode);
	}

	/**
	 * Inits the listener.
	 */
	private void initListener() {
		btn_menu.setOnClickListener(this);
		btn_get_psc.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ned_device_control, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menu) {
		super.onOptionsItemSelected(menu);
		switch (menu.getItemId()) {
		case R.id.action_login_bind:

			bindDevice();
			break;
		case android.R.id.home:
			if (xpgdevice != null) {
				xpgdevice.Disconnect();
			}
			finish();
			break;
		default:
			break;
		}

		return true;
	}

	/**
	 * Bind device.
	 */
	private void bindDevice() {
		final String uid = setmanager.getUid();
		final String token = setmanager.getToken();
		final String hideuid = setmanager.getHideUid();
		final String hidetoken = setmanager.getHideToken();
		if (!uid.equals("") && !token.equals("")) {
			if (xpgdevice != null) {
				if (xpgdevice.GetPasscode().equals("")) {
					xpgdevice.GetPasscodeFromDevice();
				} else {
					dialog.show();
					xpgdevice.BindDevice(uid, token);
				}
			} else {
				dialog.show();
				mCenter.cBindDevice(uid, token, did, passcode);
			}
		} else if (!hideuid.equals("") && !hidetoken.equals("")) {
			if (xpgdevice != null) {
				if (xpgdevice.GetPasscode().equals("")) {
					xpgdevice.GetPasscodeFromDevice();
				} else {
					dialog.show();
					xpgdevice.BindDevice(hideuid, hidetoken);
				}
			} else {
				dialog.show();
				mCenter.cBindDevice(hideuid, hidetoken, did, passcode);
			}
		} else {
			mCenter.cRegisterAnonymousUser();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btn_get_psc) {
			if (xpgdevice != null) {
				xpgdevice.GetPasscodeFromDevice();
			}
		}
	}

	public void onDestroy() {
		super.onDestroy();
	}

	public void onBackPressed() {
		super.onBackPressed();
		if (xpgdevice != null) {
			xpgdevice.Disconnect();
		}
	}

	@Override
	public void onDiscovered(int result, XPGWifiDeviceList devices) {
		Log.d("Main", "Device count:" + devices.GetCount());
	};

	/*
	 * 用户注册结果回调接口.
	 */
	@Override
	public void onRegisterUser(int error, String errorMessage, String uid,
			String token) {
		if (error == 0 && !uid.equals("") && !token.equals("")) {
			setmanager.setHideUid(uid);
			setmanager.setHideToken(token);
		} else {
			Message msg = new Message();
			msg.what = TOAST;
			msg.obj = "网络较差，请检查网络连接";
			handler.sendMessage(msg);
		}

	};

}
