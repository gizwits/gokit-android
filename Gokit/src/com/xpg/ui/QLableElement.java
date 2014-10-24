package com.xpg.ui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class QLableElement extends QElement {
	TextView tv_content;
	public QLableElement(Context c, String id, String title) {
		super(c, id, title, QElement.TYPE_LABLE);
		// TODO Auto-generated constructor stub
		tv_content = new TextView(c);
		
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return tv_content.getText().toString();
	}

	@Override
	public View getContentView() {
		// TODO Auto-generated method stub
		return tv_content;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		tv_content.setText(value);
	}


}
