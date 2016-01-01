/**
 * Project Name:Gokit
 * File Name:DeviceApActivity.java
 * Package Name:com.xpg.gokit.activity
 * Date:2014-11-18 10:04:11
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.xpg.gokit.R;
import com.xpg.gokit.adapter.DeviceListAdapter;
import com.xpg.gokit.bean.ControlDevice;
import com.xpg.gokit.utils.NetUtils;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiSDK;

/**
 * 设备列表界面
 * <P>
 * 该Activity演示如何获取设备列表，登陆大小循环，控制设备流程
 * 
 * @author Lien Li
 * 
 */
public class DeviceListActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener {
	/** 新设备 */
	protected static final int NEW_DEVICE = 0;
	/** 绑定成功 */
	private static final int BINDSUCCESS = 1;
	/** 绑定失败 */
	private static final int BINDFAIL = 2;
	/** 登陆成功 */
	protected static final int LOGINSUCCESS = 3;
	/** 登陆失败 */
	protected static final int LOGINFAIL = 4;

	protected static final int LOG = 5;
	private static final int REFLASH = 6;
	String servername = "site.gizwits.com";
	private ListView lv_device_list;
	private List<ControlDevice> devicelist;
	private DeviceListAdapter adapter;
	private XPGWifiDevice xpgWifiDevice;
	ControlDevice device;
	ProgressDialog dialog;
	Boolean isGettingDevice = false;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFLASH:
				if (!isGettingDevice) {
					initData();
					getDeviceList();
					UpdateUI();
				}
				break;
			case NEW_DEVICE:
				UpdateUI();
				break;
			case BINDSUCCESS:
				Toast.makeText(DeviceListActivity.this, "绑定设备成功", Toast.LENGTH_SHORT).show();
				UpdateUI();
				dialog.dismiss();
				break;
			case BINDFAIL:
				Toast.makeText(DeviceListActivity.this, "绑定设备失败", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;
			case LOGINSUCCESS:
				Toast.makeText(DeviceListActivity.this, "登陆设备成功", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;
			case LOGINFAIL:
				Toast.makeText(DeviceListActivity.this, "登陆设备失败", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;
			case LOG:
				Toast.makeText(DeviceListActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;

			}
		}

	};
	boolean finishdownload = false;

	protected void onUpdateProduct(int result) {
		finishdownload = true;
	};

	protected void didDiscovered(int error, List<XPGWifiDevice> devicesList) {
		Log.d("onDiscovered", "Device count:" + devicesList.size());
		storeDeviceList(devicesList);
		Message msg = new Message();
		msg.what = NEW_DEVICE;
		handler.sendMessage(msg);
	}

	protected void didUserLogin(int error, String errorMessage, String uid, String token) {
		if (uid != null && token != null && !uid.equals("") && !token.equals("") && error == 0) {
			final String fuid = uid;
			final String ftoken = token;
			setmanager.setHideUid(fuid);
			setmanager.setHideToken(ftoken);
			getDeviceList();
		} else {
			Message msg = new Message();
			msg.what = LOG;
			msg.obj = "网络较差，请检查网络连接";
			handler.sendMessage(msg);

		}
	};

	protected void didUserLogout(int error, String errorMessage) {
		String uid = setmanager.getUid();
		String token = setmanager.getToken();
		String hideuid = setmanager.getHideUid();
		String hidetoken = setmanager.getHideToken();
		if (!uid.equals("") && !token.equals("")) {
			mCenter.cGetBoundDevices(uid, token);
		} else if (!hideuid.equals("") && !hidetoken.equals("")) {
			mCenter.cGetBoundDevices(hideuid, hidetoken);
		} else {
			mCenter.cUserLoginAnonymous();
		}
	};

	private void storeDeviceList(List<XPGWifiDevice> devices) {
		BaseActivity.deviceslist = new ArrayList<XPGWifiDevice>();
		for (int i = 0; i < devices.size(); i++) {
			BaseActivity.deviceslist.add(devices.get(i));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
		// 使左上角图标显示并且可以按
		this.actionBar.setDisplayShowHomeEnabled(true);
		this.actionBar.setHomeButtonEnabled(true);
		this.actionBar.setIcon(R.drawable.reflash_bt);
		initView();
		initData();
		initListener();

	}

	private synchronized void UpdateUI() {
		EmptyData();

		String uid = setmanager.getUid();
		if (0 == uid.length()) {
			uid = setmanager.getHideUid();
		}

		for (int i = 0; i < BaseActivity.deviceslist.size(); i++) {

			XPGWifiDevice device = BaseActivity.deviceslist.get(i);

			if (device.isOnline() && device.getPasscode() != null && !device.getPasscode().equals("")
					&& device.isBind(uid)) {
				ControlDevice controlDevice = new ControlDevice(device, true);
				devicelist.add(controlDevice);
			}
		}
		boolean hasnew = false;
		for (int i = 0; i < BaseActivity.deviceslist.size(); i++) {
			XPGWifiDevice device = BaseActivity.deviceslist.get(i);

			if (device.getPasscode() == null || device.getPasscode().equals("") || !device.isBind(uid)) {
				if (!hasnew) {
					ControlDevice controlDevice = new ControlDevice("发现新设备");
					devicelist.add(controlDevice);
					hasnew = true;
				}
				ControlDevice controlDevice = new ControlDevice(device, false);
				devicelist.add(controlDevice);
			}
		}
		boolean hasoffline = false;
		for (int i = 0; i < BaseActivity.deviceslist.size(); i++) {
			XPGWifiDevice device = BaseActivity.deviceslist.get(i);

			if (!device.isOnline()) {
				if (!hasoffline) {
					ControlDevice controlDevice = new ControlDevice("离线设备");
					devicelist.add(controlDevice);
					hasoffline = true;
				}
				ControlDevice controlDevice = new ControlDevice(device, true);
				devicelist.add(controlDevice);
			}
		}

		adapter = new DeviceListAdapter(this, devicelist);
		lv_device_list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.clear();
		String uid = setmanager.getUid();
		if (uid.equals("")) {
			getMenuInflater().inflate(R.menu.device_list, menu);
		} else {
			getMenuInflater().inflate(R.menu.device_list_with_logout, menu);
		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		String uid = setmanager.getUid();
		if (uid.equals("")) {
			getMenuInflater().inflate(R.menu.device_list, menu);
		} else {
			getMenuInflater().inflate(R.menu.device_list_with_logout, menu);
		}
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menu) {
		super.onOptionsItemSelected(menu);
		Intent it = new Intent();
		switch (menu.getItemId()) {
		case android.R.id.home:
			Message msg = new Message();
			msg.what = REFLASH;
			handler.sendMessage(msg);
			break;
		case R.id.action_add_device:
			boolean iswifi = NetUtils.isWifiConnected(this);
			if (iswifi) {
				it.setClass(this, AirLinkActivity.class);

				startActivity(it);
			} else {
				Toast.makeText(this, "请切换至Wifi环境", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.action_about:
			it.setClass(this, AboutVersionActivity.class);
			startActivity(it);
			break;
		case R.id.action_login:
			it.setClass(this, LoginActivity.class);
			startActivity(it);
			break;
		case R.id.action_logout:
			mCenter.cLogout();

			// 重启程序
			System.exit(0);
			it.setClass(getApplication(), MainActivity.class);
			startActivity(it);
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	public void onResume() {
		super.onResume();
		initData();
		getDeviceList();
		UpdateUI();
		if (dialog.isShowing()) {
			dialog.cancel();
		}
	}

	private void EmptyData() {
		devicelist = new ArrayList<ControlDevice>();
	}

	private void initData() {
		devicelist = new ArrayList<ControlDevice>();
		adapter = new DeviceListAdapter(this, devicelist);
		lv_device_list.setAdapter(adapter);
	}

	private void initView() {
		lv_device_list = (ListView) findViewById(R.id.lv_device_list);
		dialog = new ProgressDialog(this);
		dialog.setMessage("刷新列表中，请稍候...");
	}

	private void initListener() {
		lv_device_list.setOnItemClickListener(this);
		lv_device_list.setOnItemLongClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		device = devicelist.get(pos);
		if (!device.isTitle()) {
			xpgWifiDevice = BaseActivity.findDeviceByMac(device.getMac(), device.getDid());
			if (null == xpgWifiDevice) {
				return;
			}

			xpgWifiDevice.setListener(deviceListener);
			File file = new File(this.getFilesDir() + "/Devices/" + xpgWifiDevice.getProductKey() + ".json");
			if (!file.exists()) {
				return;
			}

			if (xpgWifiDevice.isConnected()) {
				xpgWifiDevice.disconnect();
				return;
			}

			dialog.show();
			if (xpgWifiDevice.getDid() != null && !xpgWifiDevice.getDid().equals("")) {
				if (setmanager.getUid() != null && setmanager.getUid().length() > 0) {
					if (xpgWifiDevice.isBind(setmanager.getUid())) {
						dialog.setMessage("正在登录" + xpgWifiDevice.getMacAddress() + "...");
						xpgWifiDevice.login(setmanager.getUid(), setmanager.getToken());
					} else {
						dialog.setMessage("正在绑定" + xpgWifiDevice.getMacAddress() + "...");
						XPGWifiSDK.sharedInstance().bindDevice(setmanager.getUid(), setmanager.getToken(),
								xpgWifiDevice.getDid(), xpgWifiDevice.getPasscode(), null);
					}
				} else {
					if (xpgWifiDevice.isBind(setmanager.getHideUid())) {
						dialog.setMessage("正在登录" + xpgWifiDevice.getMacAddress() + "...");
						xpgWifiDevice.login(setmanager.getHideUid(), setmanager.getHideToken());
					} else {
						dialog.setMessage("正在绑定" + xpgWifiDevice.getMacAddress() + "...");
						XPGWifiSDK.sharedInstance().bindDevice(setmanager.getHideUid(), setmanager.getHideToken(),
								xpgWifiDevice.getDid(), xpgWifiDevice.getPasscode(), null);
					}
				}
			} else {
				Toast.makeText(this, "设备Did 为空,查询设备是否连网", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
		return true;
	}

	/**
	 * 获取设备列表
	 */
	private void getDeviceList() {
		if (isGettingDevice)
			return;

		isGettingDevice = true;

		String uid = setmanager.getUid();
		String token = setmanager.getToken();
		String hideuid = setmanager.getHideUid();
		String hidetoken = setmanager.getHideToken();
		if (!uid.equals("") && !token.equals("")) {
			// 绑定后刷新设备列表
			mCenter.cGetBoundDevices(uid, token);
		} else if (!hideuid.equals("") && !hidetoken.equals("")) {
			// 绑定后刷新设备列表
			mCenter.cGetBoundDevices(hideuid, hidetoken);
		} else {
			// 匿名登录,回调onUserLogin
			mCenter.cUserLoginAnonymous();
		}

		isGettingDevice = false;
	}

	@Override
	public void didLogin(XPGWifiDevice device, int result) {// 登录设备成功，进入控制界面
		Log.d("wifi", "onLogin:" + result);
		if (result == 0) {
			handler.sendEmptyMessage(LOGINSUCCESS);
			Intent it = new Intent();
			it.setClass(DeviceListActivity.this, GokitControlActivity.class);
			it.putExtra("device", this.device);
			it.putExtra("islocal", device.getDid() == null || !device.getDid().equals(""));
			startActivity(it);
		} else {
			handler.sendEmptyMessage(LOGINFAIL);
		}
	};

	@Override
	public void didBindDevice(int error, String errorMessage, String did) {
		if (0 == error) {
			handler.sendEmptyMessage(BINDSUCCESS);
		} else {
			handler.sendEmptyMessage(BINDFAIL);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
