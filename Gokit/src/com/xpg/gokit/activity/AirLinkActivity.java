/**
 * Project Name:Gokit
 * File Name:AirLinkActivity.java
 * Package Name:com.xpg.gokit.activity
 * Date:2014-11-18 10:03:44
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

/**
 * airlink 配置类
 * <P>
 * 发送airlink广播.使用设备之前，需要先把设备接入网络，该activity演示app通过airlink的方式使模块连上路由器。
 * 
 * @author Lien Li
 * */
public class AirLinkActivity extends BaseActivity implements OnClickListener {

	/** 配置成功. */
	protected static final int SUCCESS = 0;

	/** 配置失败. */
	protected static final int FAIL = 1;

	/** 配置超时. */
	protected static final int TIEMOUT = 2;

	/** The tv_wifi. */
	private TextView tv_wifi;

	/** The edt_psw. */
	private EditText edt_psw;

	/** The btn_set. */
	private Button btn_set;

	/** The btn_back. */
	private Button btn_back;

	/** The wifi. */
	private String wifi;

	/** The dialog. */
	private ProgressDialog dialog;

	/** The handler. */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FAIL:
				dialog.cancel();
				Toast.makeText(AirLinkActivity.this, "配置失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case SUCCESS:
				dialog.cancel();
				Toast.makeText(AirLinkActivity.this, "配置成功", Toast.LENGTH_SHORT)
						.show();
				finish();
				break;

			case TIEMOUT:
				dialog.cancel();
				Toast.makeText(AirLinkActivity.this, "配置超时", Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_air_link);
		actionBar.setDisplayHomeAsUpEnabled(true);

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

	/**
	 * Inits the view.
	 */
	private void initView() {
		tv_wifi = (TextView) findViewById(R.id.tv_wifi);
		edt_psw = (EditText) findViewById(R.id.edt_psw);
		btn_set = (Button) findViewById(R.id.btn_set);
		btn_back = (Button) findViewById(R.id.btn_back);
		dialog = new ProgressDialog(this);
		dialog.setMessage("配置中，请稍候...");
	}

	/**
	 * Inits the data.
	 */
	private void initData() {
		// 获取当前手机连接的wifi的ssid
		wifi = NetUtils.getCurentWifiSSID(this);
		tv_wifi.setText(wifi);
	}

	/**
	 * Inits the listener.
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		btn_set.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		if (v == btn_back) {
			finish();
		}
		if (v == btn_set) {
			dialog.show();
			String password = edt_psw.getText().toString().trim();
			// 发送airlink广播，把需要连接的wifi的ssid和password发给模块。
			mCenter.cSetAirLink(wifi, password);
		}
	}

	/*
	 * 配置成功回调
	 * 
	 * @see
	 * com.xpg.gokit.activity.BaseActivity#onSetAirLink(com.xtremeprog.xpgconnect
	 * .XPGWifiDevice)
	 */
	@Override
	protected void onSetAirLink(com.xtremeprog.xpgconnect.XPGWifiDevice device) {
		// 通过airlink配置模块成功连上路由器后，回调该函数。
		Log.i("air link device", "" + device.GetMacAddress() + " ");
		handler.sendEmptyMessage(SUCCESS);
	};

}
