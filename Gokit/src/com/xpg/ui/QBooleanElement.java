package com.xpg.ui;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class QBooleanElement extends QElement implements OnCheckedChangeListener  {
	

	private Switch tgb_onoff;
	
	/**
	 * 
	 * @param context
	 * @param title
	 * @param values off = value[0] on = value[1]  default values {"0","1"} ; 
	 */
	public QBooleanElement(Context c, String id, String title, String[]value) {
		super(c, id, title, QElement.TYPE_BOOLEAN);
		// TODO Auto-generated constructor stub
		if(value!=null){
			this.values = value;
		}else{
			this.values = new String[]{"0","1"};
		}
		tgb_onoff = new Switch(c);
		tgb_onoff.setOnCheckedChangeListener(this);
		
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		boolean ischeck = tgb_onoff.isChecked();
		int index = ischeck? 1:0;
		return values[index];
	}

	@Override
	public View getContentView() {
		// TODO Auto-generated method stub
		return tgb_onoff;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		if(value.toLowerCase().equals("true")||value.toLowerCase().equals("false")){
			if(value.toLowerCase().equals("true")){
				tgb_onoff.setChecked(true);
			}else{
				tgb_onoff.setChecked(false);
			}
		}else{
		int index = 0;
		for(int i = 0;i<this.values.length;i++){
			if(values[i].equals(value)){
				index = i;
				break;
			}
		}
		
		tgb_onoff.setChecked(index==1);
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = VALUE;
		msg.obj = this;
		handler.sendMessage(msg);
//		if(listener!=null){
//			int index = isChecked? 1:0;
//			listener.onValueChange(values[index],this );
//		}
	}
	
	

}
