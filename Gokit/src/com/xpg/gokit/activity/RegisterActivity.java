package com.xpg.gokit.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.xpg.gokit.R;
import com.xpg.gokit.R.id;
import com.xpg.gokit.R.layout;
import com.xpg.gokit.R.menu;
import com.xpg.gokit.setting.SettingManager;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;

import android.app.Activity;
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
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	protected static final int TOAST = 0;
	protected static final int REG_SUCCESS = 1;
	protected static final int TIMER = 2;
	EditText edt_password ;
	EditText edt_confirm_password;
	EditText edt_verify_code;
	Button btn_send_verify_code;
	EditText edt_phone_number;
	Button btn_reg;
	SettingManager setmanager;
	int secondleft = 60;
	Timer timer ;
	XPGWifiSDKListener listener = new XPGWifiSDKListener(){
		
		public void onBindDevice(int error, String errorMessage) {};
//		public long onCalculateCRC(byte[] data) {};
		public void onDiscovered(int result, com.xtremeprog.xpgconnect.XPGWifiDeviceList devices) {};
		public void onGetSSIDList(com.xtremeprog.xpgconnect.XPGWifiSSIDList list, int result) {};
		public void onChangeUserEmail(int error, String errorMessage) {};
		public void onChangeUserPassword(int error, String errorMessage) {};
		public void onChangeUserPhone(int error, String errorMessage) {};
		public void onTransUser(int error, String errorMessage) {};
		public void onUserLogout(int error, String errorMessage) {};
		public void onGetDeviceInfo(int error, String errorMessage, String productKey, String did, String mac, String passCode, String host, int port, int isOnline) {};
		public void onRegisterUser(int error, String errorMessage, String uid, String token) {
			Log.i("error message uid token", error + " " +errorMessage +" " +uid+ " "+token);
			if(!uid.equals("")&&!token.equals("")){
				Message msg = new Message();
				msg.what = REG_SUCCESS;
				msg.obj = "注册成功";
				handler.sendMessage(msg);
				setmanager.setUid(uid);
				setmanager.setToken(token);
				
			}else{
				Message msg = new Message();
				msg.what = TOAST;
				msg.obj = errorMessage;
				handler.sendMessage(msg);
			}
		};
		public void onRequestSendVerifyCode(int error, String errorMessage) {
			Log.i("error message ", error + " "+ errorMessage);
			if(error==0){
				Message msg = new Message();
				msg.what = TOAST;
				msg.obj = "发送成功";
				handler.sendMessage(msg);
			}else{
				Message msg = new Message();
				msg.what = TOAST;
				msg.obj = errorMessage;
				handler.sendMessage(msg);
			}
		};
		public void onSetAirLink(com.xtremeprog.xpgconnect.XPGWifiDevice device) {};
		public void onUnbindDevice(int error, String errorMessage) {};
		public void onUpdateProduct(int result) {};
		public void onUserLogin(int error, String errorMessage, String uid, String token) {};
	};
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case TOAST:
				Toast.makeText(RegisterActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				dialog.cancel();
				break;
			case REG_SUCCESS:
				Toast.makeText(RegisterActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				dialog.cancel();
				setResult(0);
				finish();
				
				break;
			case TIMER:
				secondleft--;
				if(secondleft<=0){
					timer.cancel();
					btn_send_verify_code.setEnabled(true);
					btn_send_verify_code.setText("获取验证码");
				}else{
					btn_send_verify_code.setText("剩余"+secondleft+"秒");
				}
				break;
			
			}
			
		};
	};
	ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		this.mCenter.getXPGWifiSDK().setListener(listener);
		setmanager = new SettingManager(this);
		initView();
		initListener();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		
	}

	private void initListener() {
		// TODO Auto-generated method stub
		btn_reg.setOnClickListener(this);
		btn_send_verify_code.setOnClickListener(this);
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		btn_reg = (Button)findViewById(R.id.btn_reg);
		btn_send_verify_code = (Button)findViewById(R.id.btn_send_verify_code);
		edt_confirm_password = (EditText)findViewById(R.id.edt_con_password);
		edt_password = (EditText)findViewById(R.id.edt_password);
		edt_verify_code = (EditText)findViewById(R.id.edt_code);
		edt_phone_number = (EditText)findViewById(R.id.edt_phone);
		dialog = new ProgressDialog(this);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}else if(id == R.id.action_login){
			finish();
		}
		return super.onOptionsItemSelected(item);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btn_reg){
			String phone = edt_phone_number.getText().toString();
			String code = edt_verify_code.getText().toString();
			String password = edt_password.getText().toString();
			String con_password = edt_confirm_password.getText().toString();
			phone = phone.trim();
			code = code.trim();
			password = password.trim();
			con_password = con_password.trim();
			if(phone.length()!=11){
				Toast.makeText(this, "电话号码格式不正确", Toast.LENGTH_SHORT).show();
				return;
			}
			if(code.length()==0){
				Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
				return;
			}
			if(password.length()<6){
				Toast.makeText(this, "密码小于6位",Toast.LENGTH_SHORT ).show();
				return;
			}
			if(!password.equals(con_password)){
				Toast.makeText(this, "前后输入的密码不一致", Toast.LENGTH_SHORT).show();
				return;
			}
			sendRegUser(phone,code,password);
			dialog.show();
		}
		if(v == btn_send_verify_code){
			String phone = edt_phone_number.getText().toString();
			phone = phone.trim();
			if(phone.length()==11){
				sendVerifyCode(phone);
				dialog.show();
			}else{
				Toast.makeText(this, "电话号码格式不正确", Toast.LENGTH_SHORT).show();
			}
		}
		
	}

	private void sendRegUser(final String phone, final String code,final  String password) {
		// TODO Auto-generated method stub
		mCenter.getXPGWifiSDK().RegisterPhoneUser(phone, password, code);
	}

	private void sendVerifyCode(final String phone) {
		// TODO Auto-generated method stub
		this.btn_send_verify_code.setEnabled(false);
		secondleft = 60;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(TIMER);
			}
		}, 1000, 1000);
		
		mCenter.getXPGWifiSDK().RequestSendVerifyCode(phone);
	}
}
