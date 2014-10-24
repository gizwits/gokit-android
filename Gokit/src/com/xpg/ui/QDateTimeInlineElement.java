package com.xpg.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;

public class QDateTimeInlineElement extends QElement {
	private Button btn_dialog;
	public QDateTimeInlineElement(Context c, String id, String title) {
		super(c, id, title, QElement.TYPE_DATETIME);
		// TODO Auto-generated constructor stub
		btn_dialog = new Button(c);
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public View getContentView() {
		// TODO Auto-generated method stub
		return btn_dialog;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		
	}
	
	

	

	

}
