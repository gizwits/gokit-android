package com.xpg.gokit.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xpg.gokit.R;
import com.xpg.gokit.utils.NetUtils;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;

public class AirLinkActivity extends BaseActivity implements OnClickListener {
	protected static final int SUCCESS = 0;
	protected static final int FAIL = 1;
	protected static final int TIEMOUT = 2;
	private TextView tv_wifi;
	private EditText edt_psw;
	private Button btn_set;
	private Button btn_back;
	private String wifi;
	private ProgressDialog dialog;
	private XPGWifiSDKListener listener = new XPGWifiSDKListener(){
		public void onSetAirLink(com.xtremeprog.xpgconnect.XPGWifiDevice device) {
			Log.i("air link device", ""+device.GetMacAddress()+" ");
			handler.sendEmptyMessage(SUCCESS);
		};
		public void onChangeUserEmail(int error, String errorMessage) {};
		public void onChangeUserPassword(int error, String errorMessage) {};
		public void onChangeUserPhone(int error, String errorMessage) {};
		public void onGetDeviceInfo(int error, String errorMessage, String productKey, String did, String mac, String passCode, String host, int port, int isOnline) {};
		public void onTransUser(int error, String errorMessage) {};
		public void onUserLogout(int error, String errorMessage) {};
		public void onRequestSendVerifyCode(int error, String errorMessage) {};
		public void onBindDevice(int error, String errorMessage) {};
//		public long onCalculateCRC(byte[] data) {};
		public void onRegisterUser(int error, String errorMessage, String uid, String token) {};
		public void onUnbindDevice(int error, String errorMessage) {};
		public void onUserLogin(int error, String errorMessage, String uid, String token) {};
		public void onUpdateProduct(int result) {}; 
		public void onDiscovered(int result, com.xtremeprog.xpgconnect.XPGWifiDeviceList devices) {};
		public void onGetSSIDList(com.xtremeprog.xpgconnect.XPGWifiSSIDList list, int result) {};
		
	};
	
//	List<ScanResult>scList;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FAIL:
				dialog.cancel();
				Toast.makeText(AirLinkActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
				break;
			case SUCCESS:
				dialog.cancel();
				Toast.makeText(AirLinkActivity.this, "配置成功", Toast.LENGTH_SHORT).show();
				finish();
				break;

			case TIEMOUT:
				dialog.cancel();
				Toast.makeText(AirLinkActivity.this, "配置超时", Toast.LENGTH_SHORT).show();
				break;
			}

		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_air_link);
		actionBar.setDisplayHomeAsUpEnabled(true);
		mCenter.getXPGWifiSDK().setListener(listener);
		
		initView();
		initData();
		initListener();
		
	}
	public boolean onOptionsItemSelected(MenuItem menu) {
		super.onOptionsItemSelected(menu);
		switch (menu.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		
		return true;
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		tv_wifi = (TextView)findViewById(R.id.tv_wifi);
		edt_psw = (EditText)findViewById(R.id.edt_psw);
		btn_set = (Button)findViewById(R.id.btn_set);
		btn_back = (Button)findViewById(R.id.btn_back);	
		dialog = new ProgressDialog(this);
	}
	private void initData() {
		// TODO Auto-generated method stub
		wifi = NetUtils.getCurentWifiSSID(this);
		if(wifi.substring(0,1).equals("\"")&&wifi.substring(wifi.length()-1).equals("\"")){
			wifi = wifi.substring(1,wifi.length()-1);
		}
		
		tv_wifi.setText(wifi);
		
	}

	private void initListener() {
		// TODO Auto-generated method stub
		btn_back.setOnClickListener(this);
		btn_set.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btn_back){
			finish();
		}
		if(v == btn_set){
			dialog.show();
			String password = edt_psw.getText().toString().trim();
			mCenter.getXPGWifiSDK().SetAirLink(wifi, password);
		}
	}

	


}
