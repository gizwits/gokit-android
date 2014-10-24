package com.xpg.gokit.adapter;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xpg.gokit.R;

public class WifiListAdapter extends BaseAdapter {
	private Context context;
	private List<ScanResult>list;
	public WifiListAdapter(Context c,List<ScanResult>list){
		this.context = c;
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
		ScanResult sr = list.get(position);
		View v = LayoutInflater.from(context).inflate(R.layout.item_wifi_list, null);
	
		TextView tv_ssid = (TextView)v.findViewById(R.id.tv_ssid);
		tv_ssid.setText(sr.SSID	);
		return v;
	}

}
