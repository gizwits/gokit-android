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

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.xpg.gokit.R;
import com.xpg.gokit.adapter.WifiListAdapter;
import com.xpg.gokit.dialog.SetWifiDialog;
import com.xpg.gokit.dialog.listener.SetWifiListener;
import com.xpg.gokit.utils.NetUtils;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiSDK;

// TODO: Auto-generated Javadoc
/**
 * softap模式配置类
 * <P>
 * 如果使用airlink模式配置不了模块，可以使用替代方案softap模式配置模块。该模式下，模块作为一个热点，app连接该热点，
 * 并调用相关的接口把需要连接的路由器ssid和密码写入模块。该Activity演示如何使用softap模式配置模块。.
 * 
 * @author Lien Li
 */
public class DeviceApActivity extends BaseActivity implements OnItemClickListener {
	protected static final int CONFIG_RESULT = 0;
	/** The lv_wifi_list. */
	ListView lv_wifi_list;

	/** wifi热点扫描结果 */
	List<ScanResult> rs;

	/** wifi热点列表数据适配器 */
	WifiListAdapter adapter;

	/** 设定wifi对话框 */
	SetWifiDialog dialog;

	// /** 网络状态广播接受器 */
	// ConnecteChangeBroadcast mChangeBroadcast = new ConnecteChangeBroadcast();

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CONFIG_RESULT:
				Toast.makeText(DeviceApActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_ap);
		initView();
		initData();
		initListener();

		XPGWifiSDK.sharedInstance().getSSIDList();

	}

	/**
	 * Inits the view.
	 */
	private void initView() {
		lv_wifi_list = (ListView) findViewById(R.id.lv_wifi_list);
	}

	/**
	 * Inits the data.
	 */
	private void initData() {
		// 获取当前手机范围内的wifi列表
		rs = NetUtils.getCurrentWifiScanResult(this);
		adapter = new WifiListAdapter(this, rs);
		lv_wifi_list.setAdapter(adapter);
	}

	public void onResume() {
		super.onResume();
		// IntentFilter filter = new IntentFilter();
		// filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		// registerReceiver(mChangeBroadcast, filter);
	}

	public void onPause() {
		super.onPause();
		// unregisterReceiver(mChangeBroadcast);

	}

	/**
	 * Inits the listener.
	 */
	private void initListener() {
		lv_wifi_list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		ScanResult sResult = rs.get(pos);
		dialog = new SetWifiDialog(this, sResult.SSID);
		dialog.setWifiListener(new SetWifiListener() {

			@Override
			public void set(String ssid, String psw) {

				String ssidAP = NetUtils.getCurentWifiSSID(DeviceApActivity.this);
				mCenter.cSetSSID(ssid, psw, ssidAP);

			}
		});
		dialog.show();
	}

	@Override
	protected void didSetDeviceWifi(int error, XPGWifiDevice device) {
		Message msg = new Message();
		msg.what = CONFIG_RESULT;
		if (0 == error) {
			msg.obj = "配置成功";
		} else {
			msg.obj = "配置失败";
		}
		handler.sendMessage(msg);
	}

	// /**
	// * 广播监听器，监听wifi连上的广播.
	// */
	// public class ConnecteChangeBroadcast extends BroadcastReceiver {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	//
	// boolean iswifi = NetUtils.isWifiConnected(context);
	// Log.i("networkchange", "change" + iswifi);
	// if (!iswifi) {
	// finish();
	// }
	// }
	// }

}
