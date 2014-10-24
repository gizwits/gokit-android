package com.xpg.ui;

import com.xpg.gokit.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class QButtonElement extends QElement implements OnClickListener {
	Button button;
	public QButtonElement(Context c, String id, String title ) {
		super(c, id, title, QElement.TYPE_BUTTON);
		// TODO Auto-generated constructor stub
		button = new Button(c);
		button.setText("      ");
		button.setBackgroundResource(R.drawable.btn_selec);
		button.setOnClickListener(this);
		
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return "null";
	}

	@Override
	public View getContentView() {
		// TODO Auto-generated method stub
		return button;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		
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
