/**
 * Project Name:Gokit
 * File Name:WifiListAdapter.java
 * Package Name:com.xpg.gokit.adapter
 * Date:2014-11-18 10:05:31
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
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xpg.gokit.R;

/**
 * wifi热点列表数据适配器
 */
public class WifiListAdapter extends BaseAdapter {

	/** The context. */
	private Context context;

	/** The list. */
	private List<ScanResult> list;

	/**
	 * Instantiates a new wifi list adapter.
	 *
	 * @param c
	 *            the c
	 * @param list
	 *            the list
	 */
	public WifiListAdapter(Context c, List<ScanResult> list) {
		this.context = c;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ScanResult sr = list.get(position);
		View v = LayoutInflater.from(context).inflate(R.layout.item_wifi_list, null);

		TextView tv_ssid = (TextView) v.findViewById(R.id.tv_ssid);
		tv_ssid.setText(sr.SSID);
		return v;
	}

}
