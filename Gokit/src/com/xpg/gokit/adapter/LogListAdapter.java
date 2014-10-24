package com.xpg.gokit.adapter;

import java.util.List;

import com.xpg.gokit.R;
import com.xpg.gokit.bean.DeviceLog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LogListAdapter extends BaseAdapter {
	private Context c;
	private List<DeviceLog>loglist;
	public LogListAdapter(Context c ,List<DeviceLog>loglist){
		this.c = c;
		this.loglist= loglist;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return loglist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DeviceLog dl = loglist.get(position);
		View view = LayoutInflater.from(c).inflate(R.layout.item_device_log, null);
		RelativeLayout rl_device_log = (RelativeLayout)view.findViewById(R.id.rl_bg);
		rl_device_log.setBackgroundColor(dl.getBackgroundcolor());
		TextView tv_text = (TextView)view.findViewById(R.id.tv_device_log);
		tv_text.setText(dl.getText());
		tv_text.setTextColor(dl.getTextcolor());
		
		return view;
	}

}
