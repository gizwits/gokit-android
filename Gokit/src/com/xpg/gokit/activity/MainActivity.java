package com.xpg.gokit.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xpg.gokit.R;
import com.xpg.gokit.utils.NetUtils;

/**
 * 初始进入界面
 * <P>
 * 检查网络是否可用,如果连上的是路由器或移动网络，跳转到获取设备列表页面；
 * <P>
 * 如果连接的是模块的热点(GAgent)，跳转到softap配置页面；
 * <P>
 * 如果没有连上网络 ，则弹出提示。
 * 
 * @author Lien Li
 * 
 */
public class MainActivity extends BaseActivity implements OnClickListener {
	protected static final int CHECK_TYPE = 0;
	RelativeLayout rl_loading;
	RelativeLayout rl_net_unable;
	Button btn_retry;
	Timer timer;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CHECK_TYPE:
				checkNetType();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	private void initView() {
		// TODO Auto-generated method stub
		rl_loading = (RelativeLayout) findViewById(R.id.rl_laoding);
		rl_net_unable = (RelativeLayout) findViewById(R.id.rl_net_unable);
		btn_retry = (Button) findViewById(R.id.btn_retry);

	}

	private void initListener() {
		// TODO Auto-generated method stub
		btn_retry.setOnClickListener(this);
	}

	/**
	 * 检查网络是否可用，判断是用手机网络还是WIFI网络或者模块热点
	 * */
	private void checkNetType() {
		int type = NetUtils.getConnectedType(this);
		if (type != -1) {
			switch (type) {
			case ConnectivityManager.TYPE_WIFI:
				Log.i("wifi", "wifi");
				String ssid = NetUtils.getCurentWifiSSID(this);
				if (ssid.contains("XPG-GAgent")) {//连上了GAgent模块的热点，跳转到配置页面
					Intent it = new Intent();
					it.setClass(this, DeviceApActivity.class);
					startActivity(it);
					return;
				} else {
					Intent it = new Intent();//连上了路由器，跳转到获取设备列表页面
					it.setClass(this, DeviceListActivity.class);
					startActivity(it);
				}

				break;
			case ConnectivityManager.TYPE_MOBILE://连上了移动网络，跳转到获取设备列表页面
				Log.i("mobile", "mobile");
				Intent it = new Intent();
				it.setClass(this, DeviceListActivity.class);
				startActivity(it);
				break;

			default:
				break;
			}
		} else {
			showNetUnable();

		}

	}

	/**
	 * 显示网络不可用界面
	 */
	private void showNetUnable() {
		rl_loading.setVisibility(View.GONE);
		rl_net_unable.setVisibility(View.VISIBLE);
		timer.cancel();
	}

	/**
	 * 显示重试界面
	 */
	private void showRecheckType() {
		rl_loading.setVisibility(View.VISIBLE);
		rl_net_unable.setVisibility(View.GONE);
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(CHECK_TYPE);
			}
		}, 1000, 1000);

	}

	public void onResume() {
		super.onResume();
		initView();
		initListener();
		showRecheckType();
	}

	public void onPause() {
		super.onPause();
		timer.cancel();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btn_retry) {
			showRecheckType();
		}

	}

}
