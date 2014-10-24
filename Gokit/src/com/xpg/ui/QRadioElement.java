package com.xpg.ui;

import android.R;
import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class QRadioElement extends QElement implements OnItemSelectedListener {
	private Spinner spn;
	public QRadioElement(Context c, String id, String title,String values[],String keys[]) {
		super(c, id, title, QElement.TYPE_RADIO);
		// TODO Auto-generated constructor stub
		spn = new Spinner(c);
		this.keys = keys;
		this.values = values;
		spn.setAdapter(new ArrayAdapter(c, R.layout.simple_spinner_dropdown_item,keys));
		spn.setOnItemSelectedListener(this);
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return values[spn.getSelectedItemPosition()];
	}

	@Override
	public View getContentView() {
		// TODO Auto-generated method stub
		return spn;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		int index = 0;
		for(int i = 0;i<values.length;i++){
			if(values[i].equals(value)){
				index = i;
				break;
			}
		}
		spn.setSelection(index);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
		// TODO Auto-generated method stub
//		if(listener!=null){
//			listener.onValueChange(values[pos], this);
//		}
		Message msg = new Message();
		msg.what = VALUE;
		msg.obj = this;
		handler.sendMessage(msg);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}


}
