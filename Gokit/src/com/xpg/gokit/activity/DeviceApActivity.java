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

import com.xpg.gokit.adapter.WifiListAdapter;
import com.xpg.gokit.dialog.SetWifiDialog;
import com.xpg.gokit.dialog.listener.SetWifiListener;
import com.xpg.gokit.utils.NetUtils;
import com.xpg.gokit.utils.WifiScanUtils;
import com.xpg.gokit.R;
import com.xtremeprog.xpgconnect.XPGWifiSDK;

public class DeviceApActivity extends BaseActivity implements OnItemClickListener {
	ListView lv_wifi_list;
	List<ScanResult>rs;
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
		// TODO Auto-generated method stub
		lv_wifi_list = (ListView)findViewById(R.id.lv_wifi_list);
	}
	private void initData() {
		// TODO Auto-generated method stub
		rs = WifiScanUtils.getCurentWifiScanResult(this);
		adapter = new WifiListAdapter(this, rs);
		lv_wifi_list.setAdapter(adapter);
	}
	public void onResume(){
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mChangeBroadcast, filter);
	}
	public void onPause(){
		super.onPause();
		unregisterReceiver(mChangeBroadcast);
		
	}
	private void initListener() {
		// TODO Auto-generated method stub
		lv_wifi_list.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		// TODO Auto-generated method stub
		ScanResult sResult = rs.get(pos);
		dialog = new SetWifiDialog(this, sResult.SSID);
		dialog.setWifiListener(new SetWifiListener() {
			
			@Override
			public void set(String ssid, String psw) {
				// TODO Auto-generated method stub
				mCenter.getXPGWifiSDK().SetSSID(ssid, psw);
				
				Toast.makeText(DeviceApActivity.this, "配置成功", Toast.LENGTH_SHORT).show();
			}
		});
		dialog.show();
	}
	public class ConnecteChangeBroadcast extends BroadcastReceiver {
	
		  @Override
		  public void onReceive(Context context, Intent intent) {
		   // TODO 自动生成的方法存根
			  
			  boolean iswifi = NetUtils.isWifiConnected(context);
			  Log.i("networkchange", "change"+iswifi);
			  if(!iswifi){
				  finish();
			  }
		  }
	}


}
