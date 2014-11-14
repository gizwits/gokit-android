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
		return results.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_spn_wifi, null);
		TextView tv_wifi_name = (TextView)view.findViewById(R.id.tv_wifi);
		ScanResult rsResult = results.get(position);
		tv_wifi_name.setText(rsResult.SSID);
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_spn_wifi, null);
		TextView tv_wifi_name = (TextView)view.findViewById(R.id.tv_wifi);
		ScanResult rsResult = results.get(position);
		tv_wifi_name.setText(rsResult.SSID);
		return view;
	}



}
