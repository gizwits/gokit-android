package com.xpg.gokit.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.xpg.gokit.R;
import com.xpg.gokit.R.id;
import com.xpg.gokit.R.layout;
import com.xpg.gokit.R.menu;
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
/**
 * 忘记密码界面
 * @author Lien Li
 * */
public class ForgetPasswordActivity extends BaseActivity implements OnClickListener {
	protected static final int TIMER = 0;
	protected static final int SUCCESS = 1;
	protected static final int FAIL = 2;
	protected static final int CODE_FINISH = 3;
	protected static final int CODE_SUCCESS = 4;
	protected static final int CODE_FAIL = 5;
	EditText edt_password ;
	EditText edt_confirm_password;
	EditText edt_verify_code;
	Button btn_send_verify_code;
	EditText edt_phone_number;
	Button btn_reset;
	int secode_left = 60;
	Timer timer;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case TIMER:
				secode_left--;
				if(secode_left<=0){
					timer.cancel();
					btn_send_verify_code.setEnabled(true);
					btn_send_verify_code.setText("获取验证码");
				}else{
					btn_send_verify_code.setText("剩余"+secode_left+"秒");
				}
				break;
			case SUCCESS:
				dialog.cancel();
				Toast.makeText(ForgetPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
				break;
			case FAIL:
				dialog.cancel();
				Toast.makeText(ForgetPasswordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
				break;
			case CODE_SUCCESS:
				dialog.cancel();
				Toast.makeText(ForgetPasswordActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
				break;
			case CODE_FAIL:
				dialog.cancel();
				Toast.makeText(ForgetPasswordActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
				break;
				
			}
		};
	};
	private ProgressDialog dialog;
	
	XPGWifiSDKListener listener = new XPGWifiSDKListener(){
		public void onBindDevice(int error, String errorMessage) {};
//		public long onCalculateCRC(byte[] data) {};
		public void onDiscovered(int result, com.xtremeprog.xpgconnect.XPGWifiDeviceList devices) {};
		public void onGetSSIDList(com.xtremeprog.xpgconnect.XPGWifiSSIDList list, int result) {};
		
		public void onRegisterUser(int error, String errorMessage, String uid, String token) {
			Log.i("error message uid token", error + " " +errorMessage +" " +uid+ " "+token);
		};
		public void onRequestSendVerifyCode(int error, String errorMessage) {
			
			Log.i("error message ", error + " "+ errorMessage);
			if(error==0){
				handler.sendEmptyMessage(CODE_SUCCESS);
			}else{
				handler.sendEmptyMessage(CODE_FAIL);
			}
		};
		public void onChangeUserEmail(int error, String errorMessage) {};
		public void onChangeUserPassword(int error, String errorMessage) {
			if(error == 0){
				handler.sendEmptyMessage(SUCCESS);
			}else{
				handler.sendEmptyMessage(FAIL);
			}
		};
		public void onChangeUserPhone(int error, String errorMessage) {};
		public void onTransUser(int error, String errorMessage) {};
		public void onUserLogout(int error, String errorMessage) {};
		public void onGetDeviceInfo(int error, String errorMessage, String productKey, String did, String mac, String passCode, String host, int port, int isOnline) {};
		
		public void onSetAirLink(com.xtremeprog.xpgconnect.XPGWifiDevice device) {};
		public void onUnbindDevice(int error, String errorMessage) {};
		public void onUpdateProduct(int result) {};
		
		public void onUserLogin(int error, String errorMessage, String uid, String token) {};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		this.mCenter.getXPGWifiSDK().setListener(listener);
		initView();
		initListener();
		
	}


	private void initListener() {
		btn_reset.setOnClickListener(this);
		btn_send_verify_code.setOnClickListener(this);
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		btn_reset = (Button)findViewById(R.id.btn_reset);
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
		getMenuInflater().inflate(R.menu.forget_password, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View v) {
		if(v == btn_reset){
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
			sendRetUser(phone,code,password);
		}
		if(v == btn_send_verify_code){
			String phone = edt_phone_number.getText().toString();
			phone = phone.trim();
			if(phone.length()==11){
				sendVerifyCode(phone);
			}else{
				Toast.makeText(this, "电话号码格式不正确", Toast.LENGTH_SHORT).show();
			}
		}
		
	}

	private void sendRetUser(final String phone, final String code,final  String password) {
		// TODO Auto-generated method stub
		dialog.show();
		mCenter.getXPGWifiSDK().changeUserPasswordWithCode(phone, code, password);
	}

	private void sendVerifyCode(final String phone) {
		// TODO Auto-generated method stub
		this.btn_send_verify_code.setEnabled(false);
		secode_left = 60;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(TIMER);
			}
		}, 1000, 1000);
		dialog.show();
		mCenter.getXPGWifiSDK().RequestSendVerifyCode(phone);
	}
}
