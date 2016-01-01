/**
 * Project Name:Gokit
 * File Name:ForgetPasswordActivity.java
 * Package Name:com.xpg.gokit.activity
 * Date:2014-11-18 10:04:44
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

import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xpg.gokit.R;
import com.xtremeprog.xpgconnect.XPGWifiSDK;

// TODO: Auto-generated Javadoc
/**
 * 忘记密码界面.
 * 
 * @author Lien Li
 */
public class ForgetPasswordActivity extends BaseActivity implements OnClickListener {

	/** 倒计时计数器. */
	protected static final int TIMER = 0;

	/** 修改成功. */
	protected static final int SUCCESS = 1;

	/** 修改失败. */
	protected static final int FAIL = 2;

	/** The Constant CODE_FINISH. */
	protected static final int CODE_FINISH = 3;

	/** 验证码发送成功. */
	protected static final int CODE_SUCCESS = 4;

	/** 验证码发送失败. */
	protected static final int CODE_FAIL = 5;

	protected static final int CaptchaCode = 6;

	/** The edt_password. */
	EditText edt_password;

	/** The edt_confirm_password. */
	EditText edt_confirm_password;

	/** The edt_verify_code. */
	EditText edt_verify_code;

	/** The btn_send_verify_code. */
	Button btn_send_verify_code;

	/** The edt_phone_number. */
	EditText edt_phone_number;

	/** The btn_reset. */
	Button btn_reset;

	ImageView ivGetCaptchaCode;
	EditText etInputCaptchaCode;
	Button btnGetCaptchaCode;

	/** The secode_left. */
	int secode_left = 60;

	/** The timer. */
	Timer timer;

	/** The handler. */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case TIMER:
				secode_left--;
				if (secode_left <= 0) {
					timer.cancel();
					btn_send_verify_code.setEnabled(true);
					btn_send_verify_code.setText("获取验证码");
				} else {
					btn_send_verify_code.setText("剩余" + secode_left + "秒");
				}
				break;
			case SUCCESS:
				dialog.cancel();
				Toast.makeText(ForgetPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
				finish();
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
			case CaptchaCode:
				XPGWifiSDK.sharedInstance().getCaptchaCode("dc5b945db45f427c97ec9ae881850623");
				break;

			}
		};
	};

	/** The dialog. */
	private ProgressDialog dialog;

	/*
	 * @Override protected void didRequestSendVerifyCode(int error, String
	 * errorMessage) { Log.i("error message ", error + " " + errorMessage); if
	 * (error == 0) { handler.sendEmptyMessage(CODE_SUCCESS); } else {
	 * handler.sendEmptyMessage(CODE_FAIL); } };
	 */
	@Override
	protected void didChangeUserPassword(int error, String errorMessage) {
		if (error == 0) {
			handler.sendEmptyMessage(SUCCESS);
		} else {
			handler.sendEmptyMessage(FAIL);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		handler.sendEmptyMessage(CaptchaCode);
		initView();
		initListener();

	}

	/**
	 * Inits the listener.
	 */
	private void initListener() {

		btnGetCaptchaCode.setOnClickListener(this);
		//
		btn_reset.setOnClickListener(this);
		btn_send_verify_code.setOnClickListener(this);

	}

	/**
	 * Inits the view.
	 */
	private void initView() {
		etInputCaptchaCode = (EditText) findViewById(R.id.etInputCaptchaCode_forget);
		btnGetCaptchaCode = (Button) findViewById(R.id.btnReGetCaptchaCode_forget);
		//
		btn_reset = (Button) findViewById(R.id.btn_reset);
		btn_send_verify_code = (Button) findViewById(R.id.btn_send_verify_code);
		edt_confirm_password = (EditText) findViewById(R.id.edt_con_password);
		edt_password = (EditText) findViewById(R.id.edt_password);
		edt_verify_code = (EditText) findViewById(R.id.edt_code);
		edt_phone_number = (EditText) findViewById(R.id.edt_phone);
		dialog = new ProgressDialog(this);
		dialog.setMessage("处理中，请稍候...");

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.forget_password, menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { int id =
	 * item.getItemId(); if (id == R.id.action_settings) { return true; } return
	 * super.onOptionsItemSelected(item); }
	 */

	@Override
	public void onClick(View v) {
		if (v == btn_reset) {
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
			sendRetUser(phone, code, password);
		}
		if (v == btn_send_verify_code) {
			String phone = edt_phone_number.getText().toString();
			phone = phone.trim();
			if (phone.length() == 11) {
				sendVerifyCode(phone);
			} else {
				Toast.makeText(this, "电话号码格式不正确", Toast.LENGTH_SHORT).show();
			}
		}
		if (v == btnGetCaptchaCode) {
			handler.sendEmptyMessage(CaptchaCode);
		}

	}

	/**
	 * 重置用户密码.
	 * 
	 * @param phone
	 *            已注册手机号码
	 * @param code
	 *            验证码
	 * @param password
	 *            新密码
	 */
	private void sendRetUser(final String phone, final String code, final String password) {
		dialog.show();
		mCenter.cChangeUserPasswordWithCode(phone, code, password);
	}

	/**
	 * 发送手机验证码.
	 * 
	 * @param phone
	 *            已注册手机号码
	 */
	private void sendVerifyCode(final String phone) {
		this.btn_send_verify_code.setEnabled(false);
		String CaptchaCode = etInputCaptchaCode.getText().toString();
		secode_left = 60;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(TIMER);
			}
		}, 1000, 1000);
		dialog.show();
		mCenter.cRequestSendVerifyCode(tokenString, captchaidString, CaptchaCode, phone);
	}

	/**
	 * 图片验证码回调
	 */
	private String tokenString, captchaidString, captcthishaURL_String;

	protected void didGetCaptchaCode(int result, java.lang.String errorMessage, java.lang.String token,
			java.lang.String captchaId, java.lang.String captcthishaURL) {
		Log.e("AppTest",
				"图片验证码回调" + result + ", " + errorMessage + ", " + token + ", " + captchaId + ", " + captcthishaURL);
		tokenString = token;
		captchaidString = captchaId;
		captcthishaURL_String = captcthishaURL;
		new load_image().execute(captcthishaURL_String);
	}

	class load_image extends AsyncTask<String, Void, Drawable> {

		/**
		 * 加载网络图片
		 * 
		 * @param url
		 * @return
		 */
		private Drawable LoadImageFromWebOperations(String url) {
			InputStream is = null;
			Drawable d = null;
			try {
				is = (InputStream) new URL(url).getContent();
				d = Drawable.createFromStream(is, "src name");
				return d;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected Drawable doInBackground(String... params) {
			Drawable drawable = LoadImageFromWebOperations(params[0]);
			return drawable;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			ivGetCaptchaCode = (ImageView) findViewById(R.id.ivGetCaptchaCode_forget);
			ivGetCaptchaCode.setImageDrawable(result);
		}

	}

	protected void didRequestSendPhoneSMSCode(int result, java.lang.String errorMessage) {
		Log.e("AppTest", result + ", " + errorMessage);
		if (result == 0) {
			handler.sendEmptyMessage(CODE_SUCCESS);
		} else {
			handler.sendEmptyMessage(CODE_FAIL);
		}
	}
}
