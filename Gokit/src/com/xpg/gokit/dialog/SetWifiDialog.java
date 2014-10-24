package com.xpg.gokit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpg.gokit.dialog.listener.SetWifiListener;
import com.xpg.gokit.R;

public class SetWifiDialog extends Dialog implements android.view.View.OnClickListener{
	private EditText edt_psw;
	private String ssid;
	private RelativeLayout rl_set;
	private RelativeLayout rl_cancel;
	SetWifiListener listener;
	public void setWifiListener(SetWifiListener listener){
		this.listener = listener;
	}
	public SetWifiDialog(Context context,String ssid) {
		super(context,R.style.alertdialog);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.dialog_set_wifi);
		initView(ssid);
		initListener();
	}
	private void initListener() {
		// TODO Auto-generated method stub
		rl_cancel.setOnClickListener(this);
		rl_set.setOnClickListener(this);
	}
	private void initView(String ssid) {
		this.ssid = ssid;
		TextView tv_ssid = (TextView)findViewById(R.id.tv_ssid);
		tv_ssid.setText(ssid);
		rl_cancel = (RelativeLayout)findViewById(R.id.rl_cancel);
		rl_set = (RelativeLayout)findViewById(R.id.rl_set);
		edt_psw  = (EditText)findViewById(R.id.edt_psw);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == rl_cancel){
			cancel();
		}
		if(v == rl_set){
			if(this.listener!=null){
				String psw = edt_psw.getText().toString().trim();
				listener.set(ssid, psw);
				cancel();
			}
		}
	}
	

}
