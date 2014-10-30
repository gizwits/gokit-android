package com.xpg.gokit.activity;

import java.util.ArrayList;
import java.util.List;

import com.xpg.gokit.R;
import com.xpg.gokit.adapter.LogListAdapter;
import com.xpg.gokit.bean.DeviceLog;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiDeviceListener;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class DeviceLogActivity extends BaseActivity {
	
	protected static final int DEVICE_LOG = 0;
	private ListView lv_list;
	private LogListAdapter adapter;
	private List<DeviceLog>loglist;
	XPGWifiDeviceListener devcelistener = new XPGWifiDeviceListener() {
		public void onConnected() {};
		public void onConnectFailed() {};
		public void onBindDevice(int error, String errorMessage) {};
		public void onUnbindDevice(int error, String errorMessage) {};
		public void onUpdateUI() {};
		public void onDeviceLog(short nLevel, String tag, String source, String content) {
			Log.i("devicelog", nLevel+" "+ tag+ " "+ source + " " + content);
			Message msg = new Message();
			msg.what = DEVICE_LOG;
			msg.obj =  new DeviceLog(content,source,tag,nLevel); 
			handler.sendMessage(msg);
		};
		public void onReceiveAlertsAndFaultsInfo(com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo alerts, com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo faults) {};
		
		public void onDeviceOnline(boolean isOnline) {};
		public void onDisconnected() {};
		public void onGetPasscode(int result) {};
		public void onLogin(int result) {};
		public void onLoginMQTT(int result) {};
		public boolean onReceiveData(String data) {
			return true;
		};
		public void onQueryHardwareInfo(int error, com.xtremeprog.xpgconnect.XPGWifiQueryHardwareInfoStruct pInfo) {};
		public void onSetSwitcher(int result) {};
	};
	XPGWifiDevice xpgWifiDevice;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case DEVICE_LOG:
				synchronized (loglist) {
					
					DeviceLog devicelog = (DeviceLog)msg.obj;
					Log.i("devicelog", devicelog.getText());
					if(loglist.size()%2==0){
						devicelog.setBackgroundcolor(Color.argb(255, 204, 230, 204));
					}
					loglist.add(devicelog);
					adapter.notifyDataSetChanged();
//					lv_list.setSelection(loglist.size());
				}
				break;
				
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_log);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("设备日志");
		
		
		
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		lv_list = (ListView)findViewById(R.id.lv_device_log);
		
	}

	private void initData() {
		// TODO Auto-generated method stub
		String mac  = getIntent().getStringExtra("mac");
		String did = getIntent().getStringExtra("did");
		xpgWifiDevice = BaseActivity.findDeviceByMac(mac,did);
		if(xpgWifiDevice!=null){
			xpgWifiDevice.setListener(devcelistener);
		}
		
		
		
		loglist = new ArrayList<DeviceLog>();
		
		adapter = new LogListAdapter(this, loglist);
		lv_list.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.device_log, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_clean_log:
			synchronized (loglist) {
				loglist.clear();
				adapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}
		
		return true;
	}
}
