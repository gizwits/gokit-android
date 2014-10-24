package com.xpg.gokit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xpg.gokit.bean.ControlDevice;
import com.xpg.gokit.setting.SettingManager;
import com.xpg.gokit.R;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiDeviceListener;
@Deprecated
public class BindDeviceActivity extends BaseActivity implements OnClickListener {
	private static final int TOAST = 0;
	protected static final int LOGIN_FAIL = 1;
	private EditText edt_account;
	private EditText edt_password;
	private Button btn_login_band;
	private ControlDevice device;
	SettingManager settingManager;
	XPGWifiDevice xpgWifiDevice;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch(msg.what){
			case TOAST:
				Toast.makeText(BindDeviceActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				finish();
				break;
			case LOGIN_FAIL:
				Toast.makeText(BindDeviceActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	XPGWifiDeviceListener deviceDelegate = new XPGWifiDeviceListener() {
		public void onBindDevice(int error, String errorMessage) {};
		public void onUnbindDevice(int error, String errorMessage) {};
		public void onUpdateUI() {};
		public void onConnectFailed() {};
		public void onLogin(int result) {
			Log.i("result", result+"");
			Log.i("login", "login");
		};
		public void onQueryHardwareInfo(int error, com.xtremeprog.xpgconnect.XPGWifiQueryHardwareInfoStruct pInfo) {};
		public void onReceiveAlertsAndFaultsInfo(com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo alerts, com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo faults) {};
		public void onDeviceOnline(boolean isOnline) {};
		public void onGetPasscode(int result) {};;
		public void onDisconnected(){
			Log.i("disconnected", "disconnected");
		}
		public boolean onReceiveData(String data) {
			return true;
		};
		public void onConnected(){
			Log.i("connected", "connected");
				Log.i("login cloud", "login cloud");
				String username = edt_account.getText().toString().trim();
				String psw = edt_password.getText().toString().trim();
				Log.i("username", username);
				Log.i("psw", psw);
				xpgWifiDevice.Login(username, psw);
		}
		public void onSetSwitcher(int result) {};
		public void onDeviceLog(short nLevel, String tag, String source, String content) {};
		public void onLoginMQTT(int result){
			if(result==0){
				String name = edt_account.getText().toString().trim();
				String psw = edt_password.getText().toString().trim();
				settingManager.setPassword(psw);
				settingManager.setUserName(name);
				xpgWifiDevice.Bind(name);
				xpgWifiDevice.Disconnect();
				
				finish();
			}else{
				Message msg= new Message();
				msg.what = LOGIN_FAIL;
				msg.obj = "绑定失败，请检查用户名与密码或手机网络";
				handler.sendMessage(msg);
			}
			Log.i("result", result+"");
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_device);
		actionBar.setDisplayHomeAsUpEnabled(true);
		settingManager = new SettingManager(this);
		Intent it = getIntent();
		String produck_key = it.getStringExtra("product_key");
		String passcode = it.getStringExtra("passcode");
		String did = it.getStringExtra("did");
		if(produck_key!=null&&passcode!=null&&did!=null){
			
		}else{
			device = (ControlDevice)it.getSerializableExtra("device");
			
			xpgWifiDevice = BaseActivity.findDeviceByMac(device.getMac(),device.getDid());
		}
		
		if(xpgWifiDevice!=null){
			xpgWifiDevice.setListener(deviceDelegate);
		}
		initView();
		initData();
		initListener();
	}
	private void initListener() {
		// TODO Auto-generated method stub
		btn_login_band.setOnClickListener(this);
	}
	private void initData() {
		// TODO Auto-generated method stub
		edt_account.setText("show");
		edt_password.setText("123456");
//		edt_account.setText("mkzhang");
//		edt_password.setText("123456");
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		edt_account = (EditText)findViewById(R.id.edt_account);
		edt_password = (EditText)findViewById(R.id.edt_psw);
		btn_login_band = (Button)findViewById(R.id.btn_login_band);
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btn_login_band){
		String username = edt_account.getText().toString().trim();
		String psw = edt_password.getText().toString().trim();
		if(psw!=null&&!psw.equals("")&&username!=null&&!username.equals("")){
			if(xpgWifiDevice!=null){
				xpgWifiDevice.Disconnect();
				xpgWifiDevice.ConnectToMQTT();
				
			}
		}
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(xpgWifiDevice!=null){
			xpgWifiDevice.Disconnect();
		}
	}
	public boolean onOptionsItemSelected(MenuItem menu) {
		super.onOptionsItemSelected(menu);
		switch (menu.getItemId()) {
		case android.R.id.home:
			finish();
			if(xpgWifiDevice!=null){
			xpgWifiDevice.Disconnect();
			}
			
			break;

		default:
			break;
		}
		
		return true;
	}

	


}
