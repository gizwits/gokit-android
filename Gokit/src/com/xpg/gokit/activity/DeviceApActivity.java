package com.xpg.gokit.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.os.Bundle;
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
import com.xtremeprog.xpgconnect.XPGWifiSDK;

/**
 * softap模式配置类
 * <P>
 * 如果使用airlink模式配置不了模块，可以使用替代方案softap模式配置模块。该模式下，模块作为一个热点，app连接该热点，
 * 并调用相关的接口把需要连接的路由器ssid和密码写入模块。该Activity演示如何使用softap模式配置模块。
 * @author Lien Li
 * */
public class DeviceApActivity extends BaseActivity implements
		OnItemClickListener {
	ListView lv_wifi_list;
	List<ScanResult> rs;
	WifiListAdapter adapter;
	SetWifiDialog dialog;
	ConnecteChangeBroadcast mChangeBroadcast = new ConnecteChangeBroadcast();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_ap);
		initView();
		initData();
		initListener();

		XPGWifiSDK.sharedInstance().GetSSIDList();

	}

	private void initView() {
		lv_wifi_list = (ListView) findViewById(R.id.lv_wifi_list);
	}

	private void initData() {
		//获取当前手机范围内的wifi列表
		rs = NetUtils.getCurrentWifiScanResult(this);
		adapter = new WifiListAdapter(this, rs);
		lv_wifi_list.setAdapter(adapter);
	}

	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mChangeBroadcast, filter);
	}

	public void onPause() {
		super.onPause();
		unregisterReceiver(mChangeBroadcast);

	}

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
				mCenter.cSetSSID(ssid, psw);

				Toast.makeText(DeviceApActivity.this, "配置成功",
						Toast.LENGTH_SHORT).show();
			}
		});
		dialog.show();
	}
    
	/**
	 * 广播监听器，监听wifi连上的广播
	 * */
	public class ConnecteChangeBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			boolean iswifi = NetUtils.isWifiConnected(context);
			Log.i("networkchange", "change" + iswifi);
			if (!iswifi) {
				finish();
			}
		}
	}

}
