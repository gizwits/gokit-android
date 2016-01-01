package com.xpg.gokit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xpg.gokit.R;
import com.xtremeprog.xpgconnect.XPGWifiSDK;

public class AboutVersionActivity extends BaseActivity implements OnClickListener {

	/** The Constant REQUEST_CODE. */
	private static final int REQUEST_CODE = 0;

	/** The btn_back. */
	Button btn_back;
	TextView tv_VersionCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version);
		actionBar.setDisplayHomeAsUpEnabled(true);
		// setManager = new SettingManager(this);
		initView();
		initEvents();

	}

	/**
	 * Inits the view.
	 */
	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back_about);
		tv_VersionCode = (TextView) findViewById(R.id.versionCode);
	}

	/**
	 * Inits the events.
	 */
	private void initEvents() {
		btn_back.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		tv_VersionCode.setText("V" + XPGWifiSDK.sharedInstance().getVersion().toString());
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

	}
}
