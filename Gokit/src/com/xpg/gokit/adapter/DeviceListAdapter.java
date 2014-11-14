package com.xpg.gokit.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpg.gokit.bean.ControlDevice;
import com.xpg.gokit.R;

/**
 * 设备列表数据适配器
 * */
public class DeviceListAdapter extends BaseAdapter {
	private List<ControlDevice> devicelist;
	private Context context;

	/**
	 * 设备列表数据适配器构造方法
	 * 
	 * @param c
	 *            上下文环境
	 * @param list
	 *            设备列表
	 * */
	public DeviceListAdapter(Context c, List<ControlDevice> list) {
		this.devicelist = list;
		this.context = c;

	}

	@Override
	public int getCount() {
		return devicelist.size();
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
		View v = LayoutInflater.from(context).inflate(R.layout.item_device,
				null);
		RelativeLayout rl_device = (RelativeLayout) v
				.findViewById(R.id.rl_device);
		RelativeLayout rl_title = (RelativeLayout) v
				.findViewById(R.id.rl_title);
		TextView tv_name = (TextView) v.findViewById(R.id.tv_device_name);
		TextView tv_info = (TextView) v.findViewById(R.id.tv_info);
		TextView tv_tips = (TextView) v.findViewById(R.id.tv_tips);
		TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
		ControlDevice cdevice = devicelist.get(position);
		if (!cdevice.isTitle()) {

			if (cdevice.isNew()) {// 设备未绑定
				tv_info.setText(cdevice.getMac());
				tv_name.setText((cdevice.getName().equals("") ? "未知设备"
						: cdevice.getName()));
				tv_tips.setText("未绑定");

			} else {// 设备已绑定
				if (cdevice.isOnline()) {// 设备在线
					tv_info.setText(cdevice.getMac());
					tv_name.setText((cdevice.getName().equals("") ? "未知设备"
							: cdevice.getName()));
					if (cdevice.getIp() != null && !cdevice.getIp().equals("")) {
						tv_tips.setText("局域网在线");
					} else {
						tv_tips.setText("远程在线");
					}
				} else {// 设备不在线
					tv_info.setText(cdevice.getMac());
					tv_info.setTextColor(context.getResources().getColor(
							R.color.gray));
					tv_name.setText((cdevice.getName().equals("") ? "未知设备"
							: cdevice.getName()));
					tv_name.setTextColor(context.getResources().getColor(
							R.color.gray));
					tv_tips.setText("离线");
					tv_tips.setTextColor(context.getResources().getColor(
							R.color.gray));

				}
			}
		} else {
			rl_device.setVisibility(View.GONE);
			rl_title.setVisibility(View.VISIBLE);
			tv_title.setText(cdevice.getTitleName());
		}
		return v;
	}

}
