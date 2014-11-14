package com.xpg.gokit.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.xpg.gokit.R;
import com.xpg.gokit.setting.SettingManager;

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
 * 注册界面<P>
 * 该activity演示用户如何注册机智云账号以及发送手机验证码。
 * 
 * @author Lien Li
 * */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	protected static final int TOAST = 0;
	protected static final int REG_SUCCESS = 1;
	protected static final int TIMER = 2;
	EditText edt_password;
	EditText edt_confirm_password;
	EditText edt_verify_code;
	Button btn_send_verify_code;
	EditText edt_phone_number;
	Button btn_reg;
	SettingManager setmanager;
	int secondleft = 60;
	Timer timer ;
	ProgressDialog dialog;
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case TOAST:
				Toast.makeText(RegisterActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				dialog.cancel();
				break;
			case REG_SUCCESS:
				Toast.makeText(RegisterActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				dialog.cancel();
				setResult(0);
				finish();

				break;
			case TIMER:
				secondleft--;
				if (secondleft <= 0) {
					timer.cancel();
					btn_send_verify_code.setEnabled(true);
					btn_send_verify_code.setText("获取验证码");
				} else {
					btn_send_verify_code.setText("剩余" + secondleft + "秒");
				}
				break;

			}

		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setmanager = new SettingManager(this);
		initView();
		initEvents();
	}

	private void initEvents() {
		btn_reg.setOnClickListener(this);
		btn_send_verify_code.setOnClickListener(this);

	}

	private void initView() {
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
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_login) {
			finish();
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public void onClick(View v) {
		if(v == btn_reg){
			String phone = edt_phone_number.getText().toString();
			String code = edt_verify_code.getText().toString();
			String password = edt_password.getText().toString();
			String con_password = edt_confirm_password.getText().toString();
			phone = phone.trim();
			code = code.trim();
			password = password.trim();
			con_password = con_password.trim();
			if (phone.length() != 11) {
				Toast.makeText(this, "电话号码格式不正确", Toast.LENGTH_SHORT).show();
				return;
			}
			if (code.length() == 0) {
				Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
				return;
			}
			if (password.length() < 6) {
				Toast.makeText(this, "密码小于6位", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!password.equals(con_password)) {
				Toast.makeText(this, "前后输入的密码不一致", Toast.LENGTH_SHORT).show();
				return;
			}
			sendRegUser(phone, code, password);
			dialog.show();
		}
		if (v == btn_send_verify_code) {
			String phone = edt_phone_number.getText().toString();
			phone = phone.trim();
			if (phone.length() == 11) {
				//发送手机验证码
				mCenter.cRequestSendVerifyCode(phone);
				dialog.show();
			} else {
				Toast.makeText(this, "电话号码格式不正确", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void sendRegUser(final String phone, final String code,final  String password) {
		mCenter.cRegisterPhoneUser(phone, code, password);
	}

    @Override
	protected void onRegisterUser(int error, String errorMessage, String uid,
			String token) {
		Log.i("error message uid token", error + " " + errorMessage + " " + uid
				+ " " + token);
		if (!uid.equals("") && !token.equals("")) {//注册成功
			Message msg = new Message();
			msg.what = REG_SUCCESS;
			msg.obj = "注册成功";
			handler.sendMessage(msg);
			setmanager.setUid(uid);
			setmanager.setToken(token);

		} else {//注册失败
			Message msg = new Message();
			msg.what = TOAST;
			msg.obj = errorMessage;
			handler.sendMessage(msg);
		}
	};
	
	@Override
	protected void onRequestSendVerifyCode(int error, String errorMessage) {
		Log.i("error message ", error + " " + errorMessage);
		if (error == 0) {//发送成功
			Message msg = new Message();
			msg.what = TOAST;
			msg.obj = "发送成功";
			handler.sendMessage(msg);
		} else {//发送失败
			Message msg = new Message();
			msg.what = TOAST;
			msg.obj = errorMessage;
			handler.sendMessage(msg);
		}

	};
}
