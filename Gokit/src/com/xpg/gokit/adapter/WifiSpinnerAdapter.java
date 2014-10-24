package com.xpg.gokit.adapter;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.xpg.gokit.R;

public class WifiSpinnerAdapter implements SpinnerAdapter {
	private List<ScanResult>results;
	private Context context;
	public WifiSpinnerAdapter(Context c,List<ScanResult>list){
		this.results = list;
		this.context  = c;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return results.size();
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
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.item_spn_wifi, null);
		TextView tv_wifi_name = (TextView)view.findViewById(R.id.tv_wifi);
		ScanResult rsResult = results.get(position);
		tv_wifi_name.setText(rsResult.SSID);
		return view;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.item_spn_wifi, null);
		TextView tv_wifi_name = (TextView)view.findViewById(R.id.tv_wifi);
		ScanResult rsResult = results.get(position);
		tv_wifi_name.setText(rsResult.SSID);
		return view;
	}



}
