package com.xpg.ui;

import com.xpg.gokit.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class QFloatElement extends QElement implements OnSeekBarChangeListener, OnClickListener {
	private int max;
	private int min;
	SeekBar seekbar;
	TextView tv_value;
	Button btn_add;
	Button btn_sub;
	int step;
	public QFloatElement(Context c, String id, String title,int min,int max,int step) {
		super(c, id, title, QElement.TYPE_FLOAT);
		// TODO Auto-generated constructor stub
		this.min = min;
		this.max = max;
		this.step = step;
		seekbar = new SeekBar(c);
		seekbar.setMax((max-min)/step);
		seekbar.setOnSeekBarChangeListener(this);
		tv_value = new TextView(c);
		btn_add = new Button(c);
		btn_sub = new Button(c);
		btn_add.setOnClickListener(this);
		btn_sub.setOnClickListener(this);
		btn_add.setBackgroundResource(R.drawable.add);
		btn_sub.setBackgroundResource(R.drawable.sub);
		
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return (seekbar.getProgress()*step+min)+"";
	}
	
	@Override
	public View getContentView() {
		// TODO Auto-generated method stub
		return seekbar;
	}
	
	public View getValueView(){
		return tv_value;
	}
	public View getAddBtn(){
		return btn_add;
	}
	public View getSubBtn(){
		return btn_sub;
	}
	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		try{
			int n = Integer.parseInt(value);
			n-=min;
			n /=step;
			if(n>=0&&n<=(max-min)){
				seekbar.setProgress(n);
				tv_value.setText(getValue());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void setChangeEnable(boolean enabled){
		this.btn_add.setEnabled(enabled);
		this.btn_sub.setEnabled(enabled);
		this.seekbar.setEnabled(enabled);
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		int value = (seekBar.getProgress()*step+min);
		tv_value.setText(value+"");
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		QElement.isEdit = true;
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		if(this.listener!=null){
			QElement.isEdit = false;
//			this.listener.onValueChange(getValue(), this);
			Message msg = new Message();
			msg.what = VALUE;
			msg.obj = this;
			handler.sendMessage(msg);
			tv_value.setText(getValue());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(btn_add == v){
			int n = this.seekbar.getProgress();
			int max = this.seekbar.getMax();
			if(n<max){
				this.seekbar.setProgress(n+1);
				if(this.listener!=null){
//					this.listener.onValueChange(getValue(), this);
					Message msg = new Message();
					msg.what = VALUE;
					msg.obj = this;
					handler.sendMessage(msg);
					tv_value.setText(getValue());
				}
			}
		}else if(btn_sub == v){
			int n = this.seekbar.getProgress();
			if(n>0){
				this.seekbar.setProgress(n-1);
				if(this.listener!=null){
//					this.listener.onValueChange(getValue(), this);
					Message msg = new Message();
					msg.what = VALUE;
					msg.obj = this;
					handler.sendMessage(msg);
					tv_value.setText(getValue());
				}
			}
		}
	}

}
