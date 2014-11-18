/**
 * Project Name:Gokit
 * File Name:LoginActivity.java
 * Package Name:com.xpg.gokit.activity
 * Date:2014-11-18 10:05:08
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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xpg.gokit.R;
import com.xpg.gokit.setting.SettingManager;

/**
 * 登陆账号界面.
 *
 * @author Lien Li
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	
	/** The Constant TOAST. */
	protected static final int TOAST = 0;
	
	/** The Constant LOGIN_SUCCESS. */
	protected static final int LOGIN_SUCCESS = 1;
	
	/** The Constant LOGIN_FAIL. */
	protected static final int LOGIN_FAIL = 2;
	
	/** The Constant REQUEST_CODE. */
	private static final int REQUEST_CODE = 0;


	/** The btn_back. */
	Button btn_back;
	
	/** The btn_login. */
	Button btn_login;
	
	/** The edt_account. */
	EditText edt_account;
	
	/** The edt_pwd. */
	EditText edt_pwd;
	
	/** The set manager. */
	SettingManager setManager;
	
	/** The dialog. */
	ProgressDialog dialog;
	
	/** The tv_forget. */
	TextView tv_forget;

	

	/** The handler. */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case TOAST:
				Toast.makeText(LoginActivity.this, msg.obj + "",
						Toast.LENGTH_SHORT).show();
				dialog.cancel();
				break;
			case LOGIN_SUCCESS:
				Toast.makeText(LoginActivity.this, msg.obj + "",
						Toast.LENGTH_SHORT).show();
				dialog.cancel();
				finish();
				break;
			case LOGIN_FAIL:
				Toast.makeText(LoginActivity.this, msg.obj + "",
						Toast.LENGTH_SHORT).show();
				dialog.cancel();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		actionBar.setDisplayHomeAsUpEnabled(true);
		// setManager = new SettingManager(this);
		initView();
		initEvents();

	}

	/**
	 * Inits the view.
	 */
	private void initView() {
		edt_account = (EditText) findViewById(R.id.edt_account);
		edt_pwd = (EditText) findViewById(R.id.edt_psw);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_login = (Button) findViewById(R.id.btn_login);
		tv_forget = (TextView) findViewById(R.id.tv_foget);

		dialog = new ProgressDialog(this);
		dialog.setMessage("登录中，请稍候...");

	}

	/**
	 * Inits the events.
	 */
	private void initEvents() {
		btn_back.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		tv_forget.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public boolean onOptionsItemSelected(MenuItem menu) {
		super.onOptionsItemSelected(menu);
		Intent it = new Intent();
		switch (menu.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_reg://注册新用户
			it.setClass(this, RegisterActivity.class);
			startActivityForResult(it, REQUEST_CODE);
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
			if (resultCode == 0) {
				finish();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btn_back) {
			finish();
		}
		if (v == btn_login) {
			final String psw = edt_pwd.getText().toString().trim();
			final String name = edt_account.getText().toString().trim();
			mCenter.cLogin(name, psw);
			dialog.show();
		}
		if (v == tv_forget) {
			Intent it = new Intent();
			it.setClass(this, ForgetPasswordActivity.class);
			startActivity(it);
		}
	}

	
	/* 
	 * 登录成功回调
	 */
	@Override
	protected void onUserLogin(int error, String errorMessage, String uid,
			String token) {
		if (uid!=null&&token!=null&&!uid.equals("") && !token.equals("")) {//登陆成功
			setManager.setUid(uid);
			setManager.setToken(token);
			Message msg = new Message();
			msg.what = LOGIN_SUCCESS;
			msg.obj = "登录成功";
			handler.sendMessage(msg);
		} else {//登陆失败
			Message msg = new Message();
			msg.what = LOGIN_FAIL;
			msg.obj = errorMessage;
			handler.sendMessage(msg);
		}

	};
	

}
