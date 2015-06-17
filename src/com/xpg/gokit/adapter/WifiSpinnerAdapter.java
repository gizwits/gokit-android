/**
 * Project Name:Gokit
 * File Name:WifiSpinnerAdapter.java
 * Package Name:com.xpg.gokit.adapter
 * Date:2014-11-18 10:05:26
 * Copyright (c) 2014~2015 Xtreme Programming Group, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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

/**
 * wifi列表下拉框数据适配器
 */
public class WifiSpinnerAdapter implements SpinnerAdapter {
	
	/** The results. */
	private List<ScanResult>results;
	
	/** The context. */
	private Context context;
	
	/**
	 * Instantiates a new wifi spinner adapter.
	 *
	 * @param c the c
	 * @param list the list
	 */
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
