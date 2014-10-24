package com.xpg.ui;

import java.io.UnsupportedEncodingException;

import com.xpg.gokit.R;
import com.xpg.gokit.utils.Base64;
import com.xpg.gokit.utils.ByteUtils;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class QMultilineElement extends QElement implements OnClickListener{
	Button button;
	TextView tv_info;
	int maxLength = 0;
	public QMultilineElement(Context c, String id, String title,int maxLength) {
		super(c, id, title, QElement.TYPE_MULTILINE);
		// TODO Auto-generated constructor stub
		button = new Button(c);
		button.setText("      ");
		button.setBackgroundResource(R.drawable.btn_selec);
		button.setOnClickListener(this);
		tv_info = new TextView(c);
		tv_info.setText("");
		this.maxLength = maxLength;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return "null";
	}
	public int getMaxLength(){
		return this.maxLength;
	}
	@Override
	public View getContentView() {
		// TODO Auto-generated method stub
		return button;
	}
	public View getInfoView(){
		return tv_info;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		try {
			byte [] bs = Base64.decode(value);
			String str = ByteUtils.BytesToString(bs);
			tv_info.setText(str);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		if(listener!=null){
//			listener.onValueChange("null", this);
//		}
		Message msg = new Message();
		msg.what = VALUE;
		msg.obj = this;
		handler.sendMessage(msg);
	}

}
