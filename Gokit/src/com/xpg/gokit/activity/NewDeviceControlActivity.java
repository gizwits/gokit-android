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

import com.xpg.gokit.bean.ControlDevice;
import com.xpg.gokit.setting.SettingManager;
import com.xpg.gokit.utils.CRCUtils;
import com.xpg.gokit.R;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiDeviceList;
import com.xtremeprog.xpgconnect.XPGWifiDeviceListener;
import com.xtremeprog.xpgconnect.XPGWifiQueryHardwareInfoStruct;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;

public class NewDeviceControlActivity extends BaseActivity implements  OnClickListener {
	protected static final int LOG = 0;
	protected static final int TOAST = 1;
	protected static final int PASSCODE = 2;
	protected static final int BAND_SUCCESS = 3;
	protected static final int BAND_FAIL = 4;
	protected static final int HARD_INFO = 5;
	private MultiAutoCompleteTextView edt_receive_device_msg;
	ControlDevice controlDevice ;
	Button btn_menu;
	Button btn_back;
	Button btn_get_psc;
	
	TextView tv_title;
	private RelativeLayout rl_login_band;
	RelativeLayout rl_content;
	XPGWifiDevice xpgdevice;
	String produck_key ="";// = it.getStringExtra("product_key");
	String passcode="";// = it.getStringExtra("passcode");
	String did ="";// = it.getStringExtra("did");
	SettingManager setmanager;
	ProgressDialog dialog ;
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch(msg.what){
			case HARD_INFO:
				XPGWifiQueryHardwareInfoStruct infost = (XPGWifiQueryHardwareInfoStruct)msg.obj;
				StringBuilder sb = new StringBuilder();
				sb.append("Wifi Hardware Version:"+infost.getWifiHardVer()+"\r\n");
				sb.append("Wifi Software Version:"+infost.getWifiSoftVer()+"\r\n");
				sb.append("MCU Hardware Version:"+infost.getMcuHardVer()+"\r\n");
				sb.append("MCU Software Version:"+infost.getMcuSoftVer()+"\r\n");
				sb.append("Firmware Id:"+infost.getFirmwareId()+"\r\n");
				sb.append("Firmware Version:"+infost.getFirmwareVer()+"\r\n");
				sb.append("Product Key:"+infost.getProductKey()+"\r\n");
				
				edt_receive_device_msg.setText(sb.toString());
				break;
			case LOG:
				
				break;
			case TOAST:
				String info = msg.obj+"";
				Toast.makeText(NewDeviceControlActivity.this, info, Toast.LENGTH_SHORT).show();
				break;
			case PASSCODE:
				edt_receive_device_msg.append((String)msg.obj+"\r\n");
				String uid = setmanager.getUid();
				String token = setmanager.getToken();
				String hideuid = setmanager.getHideUid();
				String hidetoken = setmanager.getHideToken();
				if(!uid.equals("")&&!token.equals("")){
					dialog.show();
					xpgdevice.BindDevice(uid, token);

				}else if(!hideuid.equals("")&&!hidetoken.equals("")){
					dialog.show();
					xpgdevice.BindDevice(hideuid, hidetoken);
				}else{
					mCenter.getXPGWifiSDK().RegisterAnonymousUser(setmanager.getPhoneId());
				}
				
				break;
			case BAND_SUCCESS:
				Toast.makeText(NewDeviceControlActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
				if(null != xpgdevice)
					xpgdevice.Disconnect();
				finish();
				break;
			case BAND_FAIL:
				Toast.makeText(NewDeviceControlActivity.this, "绑定设备失败", Toast.LENGTH_SHORT).show();
				
				break;
			}
		}
	};
	XPGWifiDeviceListener deviceDelegate = new XPGWifiDeviceListener() {
		public void onReceiveAlertsAndFaultsInfo(com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo alerts, com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo faults) {};
		public void onConnectFailed() {};
		
		public void onLogin(int result) {
			Log.i("result", result+"");
			if(result == 0){
				Message msg = new Message();
				msg.what = TOAST;
				msg.obj = "小循环登陆成功";
				handler.sendMessage(msg);
			}
		};
		
		public void onBindDevice(int error, String errorMessage) {
			dialog.dismiss();
			Log.i("error", errorMessage);
			if(error==0){
				handler.sendEmptyMessage(BAND_SUCCESS);
			}else{
				Message msg = new Message();
				msg.what = BAND_FAIL;
				msg.obj = errorMessage;
				handler.sendMessage(msg);
			}
		};
		public void onUnbindDevice(int error, String errorMessage) {};
		public void onUpdateUI() {};
		public void onDeviceOnline(boolean isOnline) {};
		public void onDeviceLog(short nLevel, String tag, String source, String content) {};
		public void onSetSwitcher(int result) {};
		public void onLoginMQTT(int result) {
			
		};
		public void onQueryHardwareInfo(int error, com.xtremeprog.xpgconnect.XPGWifiQueryHardwareInfoStruct pInfo) {
			if(error == 0){
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
		public void onGetPasscode(int result){
			if(result == 0){
			Message msg = new Message() ;
			msg.obj = xpgdevice.GetPasscode();
			msg.what = PASSCODE;
			handler.sendMessage(msg);
			}
		}
		public boolean onReceiveData(String data) {
			Log.i("data", data);
			return true;
		};
		public void onConnected(){
			Log.i("connected", "connected");
			Message msg1 = new Message();
			msg1.what = LOG;
			msg1.obj = "连接成功";
			handler.sendMessage(msg1);
			xpgdevice.GetHardwareInfo();
		}
		
		public void onDisconnected(){
			Log.i("disconnected", "disconnected");
			Message msg1 = new Message();
			msg1.what = LOG;
			msg1.obj = "连接断开";
			handler.sendMessage(msg1);
		}
		
		
	};
	XPGWifiSDKListener gccDelegate = new XPGWifiSDKListener() {
		public void onDiscovered(int result,XPGWifiDeviceList devices) {
			Log.d("Main", "Device count:" + devices.GetCount());
		};
		
		public long onCalculateCRC(byte[] data) {
			return CRCUtils.CalculateCRC(xpgdevice.GetProductKey(),data);
		};
		public void onChangeUserEmail(int error, String errorMessage) {};
		public void onChangeUserPassword(int error, String errorMessage) {};
		public void onChangeUserPhone(int error, String errorMessage) {};
		public void onUserLogout(int error, String errorMessage) {};
		public void onTransUser(int error, String errorMessage) {};
		public void onRequestSendVerifyCode(int error, String errorMessage) {};
		public void onGetDeviceInfo(int error, String errorMessage, String productKey, String did, String mac, String passCode, String host, int port, int isOnline) {};
		public void onBindDevice(int error, String errorMessage) {
			Log.i("error", errorMessage);
			dialog.dismiss();
			if(error==0){
				handler.sendEmptyMessage(BAND_SUCCESS);
			}else{
				Message msg = new Message();
				msg.what = BAND_FAIL;
				msg.obj = errorMessage;
				handler.sendMessage(msg);
			}
		};
		public void onRegisterUser(int error, String errorMessage, String uid, String token) {
			if(error==0&&!uid.equals("")&&!token.equals("")){
				setmanager.setHideUid(uid);
				setmanager.setHideToken(token);
			}else{
				Message msg = new Message();
				msg.what = TOAST;
				msg.obj = "网络较差，请检查网络连接";
				handler.sendMessage(msg);
			}
			
		};
		public void onUnbindDevice(int error, String errorMessage) {};
		public void onUserLogin(int error, String errorMessage, String uid, String token) {};
		public void onSetAirLink(XPGWifiDevice device) {};
		public void onUpdateProduct(int result) {};
		public void onGetSSIDList(com.xtremeprog.xpgconnect.XPGWifiSSIDList list, int result) {};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_device_control);
		actionBar.setDisplayHomeAsUpEnabled(true);
		setmanager = new SettingManager(this);
		
		Intent it = getIntent();
		 produck_key = it.getStringExtra("product_key");
		 passcode = it.getStringExtra("passcode");
		 did = it.getStringExtra("did");
		if(produck_key!=null&&passcode!=null&&did!=null){
			String uid = setmanager.getUid();
			String token = setmanager.getToken();
			String hideuid = setmanager.getHideUid();
			String hidetoken = setmanager.getHideToken();
			if(!uid.equals("")&&!token.equals("")){
				this.mCenter.getXPGWifiSDK().BindDevice(uid, token, did, passcode);
			}else if(!hideuid.equals("")&&!hidetoken.equals("")){
				this.mCenter.getXPGWifiSDK().BindDevice(hideuid, hidetoken, did, passcode);
			}
		}else{
			controlDevice = (ControlDevice)it.getSerializableExtra("device");
			
			xpgdevice = BaseActivity.findDeviceByMac(controlDevice.getMac(),controlDevice.getDid());
		}
		mCenter.getXPGWifiSDK().setListener(gccDelegate);
		initView();
		initData();
		initListener();
		
		if(xpgdevice!=null){
			xpgdevice.setListener(deviceDelegate);
			xpgdevice.GetHardwareInfo();
//			xpgdevice.ConnectToLAN();
			Log.i("login", "connected");
		}
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		tv_title = (TextView)findViewById(R.id.tv_title);
		btn_menu = (Button)findViewById(R.id.btn_menu);
		btn_back = (Button)findViewById(R.id.btn_back);
		btn_get_psc = (Button)findViewById(R.id.btn_get_psc);
		edt_receive_device_msg = (MultiAutoCompleteTextView)findViewById(R.id.edt_receive_device_msg);
		dialog = new ProgressDialog(this);
		  View v = getLayoutInflater().inflate(R.layout.menu_new, null);
		rl_login_band = (RelativeLayout)v.findViewById(R.id.rl_login_band);
		rl_login_band.setOnClickListener(this);
		
	}
	public void onResume(){
		super.onResume();
		this.mCenter.getXPGWifiSDK().setListener(gccDelegate);
		if(xpgdevice!=null){
			this.xpgdevice.setListener(deviceDelegate);
		}
	}
	private void initData() {
		// TODO Auto-generated method stub
		tv_title.setText("未知设备");
		edt_receive_device_msg.setText(passcode);
	}

	private void initListener() {
		// TODO Auto-generated method stub
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
//			if(xpgdevice!=null){
//				xpgdevice.Disconnect();
//			}
//			Intent it = new Intent();
//			it.setClass(this, BindDeviceActivity.class);
//			it.putExtra("device", controlDevice);
//			startActivity(it);
			bindDevice();
			break;
		case android.R.id.home:
			if(xpgdevice!=null){
				xpgdevice.Disconnect();
			}
			finish();
			break;
		default:
			break;
		}
		
		return true;
	}

	
	
	

	

	private void bindDevice() {
		// TODO Auto-generated method stub
		final String uid = setmanager.getUid();
		final String token = setmanager.getToken();
		final String hideuid = setmanager.getHideUid();
		final String hidetoken = setmanager.getHideToken();
		if(!uid.equals("")&&!token.equals("")){
		if(xpgdevice!=null){
			if(xpgdevice.GetPasscode().equals("")){
				xpgdevice.GetPasscodeFromDevice();
			}else{
				dialog.show();
				xpgdevice.BindDevice(uid, token);
			}
		}else{
			dialog.show();
			mCenter.getXPGWifiSDK().BindDevice(uid, token, did, passcode);
		}
		}else if(!hideuid.equals("")&&!hidetoken.equals("")){
			if(xpgdevice!=null){
				if(xpgdevice.GetPasscode().equals("")){
					xpgdevice.GetPasscodeFromDevice();
				}else{
					dialog.show();
					xpgdevice.BindDevice(hideuid, hidetoken);
				}
			}else{
				dialog.show();
				mCenter.getXPGWifiSDK().BindDevice(hideuid, hidetoken, did, passcode);
			}
		}
		else{
			mCenter.getXPGWifiSDK().RegisterAnonymousUser(setmanager.getPhoneId());
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btn_get_psc){
			if(xpgdevice!=null){
				xpgdevice.GetPasscodeFromDevice();
			}
		}
	}
	public void onDestroy(){
		super.onDestroy();
	}
	public void onBackPressed(){
		super.onBackPressed();
		if(xpgdevice!=null){
			xpgdevice.Disconnect();
		}
	}
	



}
