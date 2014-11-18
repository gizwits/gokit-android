/**
 * Project Name:Gokit
 * File Name:SetWifiDialog.java
 * Package Name:com.xpg.gokit.dialog
 * Date:2014-11-18 10:05:58
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
package com.xpg.gokit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpg.gokit.dialog.listener.SetWifiListener;
import com.xpg.gokit.R;

/**
 * 设定wifi对话框
 * @author Lien Li
 */
public class SetWifiDialog extends Dialog implements android.view.View.OnClickListener{
	
	/** The edt_psw. */
	private EditText edt_psw;
	
	/** The ssid. */
	private String ssid;
	
	/** The rl_set. */
	private RelativeLayout rl_set;
	
	/** The rl_cancel. */
	private RelativeLayout rl_cancel;
	
	/** The listener. */
	SetWifiListener listener;
	
	/**
	 * Sets the wifi listener.
	 *
	 * @param listener the new wifi listener
	 */
	public void setWifiListener(SetWifiListener listener){
		this.listener = listener;
	}
	
	/**
	 * Instantiates a new sets the wifi dialog.
	 *
	 * @param context the context
	 * @param ssid the ssid
	 */
	public SetWifiDialog(Context context,String ssid) {
		super(context,R.style.alertdialog);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.dialog_set_wifi);
		initView(ssid);
		initListener();
	}
	
	/**
	 * Inits the listener.
	 */
	private void initListener() {
		// TODO Auto-generated method stub
		rl_cancel.setOnClickListener(this);
		rl_set.setOnClickListener(this);
	}
	
	/**
	 * Inits the view.
	 *
	 * @param ssid the ssid
	 */
	private void initView(String ssid) {
		this.ssid = ssid;
		TextView tv_ssid = (TextView)findViewById(R.id.tv_ssid);
		tv_ssid.setText(ssid);
		rl_cancel = (RelativeLayout)findViewById(R.id.rl_cancel);
		rl_set = (RelativeLayout)findViewById(R.id.rl_set);
		edt_psw  = (EditText)findViewById(R.id.edt_psw);
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
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
