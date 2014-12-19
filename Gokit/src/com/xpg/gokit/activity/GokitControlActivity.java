/**
 * Project Name:Gokit
 * File Name:GokitControlActivity.java
 * Package Name:com.xpg.gokit.activity
 * Date:2014-11-18 10:05:13
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
package com.xpg.gokit.activity;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.xpg.gokit.R;
import com.xpg.gokit.bean.ControlDevice;
import com.xtremeprog.xpgconnect.XPGWifiDevice;

/**
 * gokit控制界面
 * <P>
 * 该Activity演示如何收发控制指令，断开连接，解绑设备.
 * 
 * @author Lien Li
 */
public class GokitControlActivity extends BaseActivity {

	/** The Constant TOAST. */
	protected static final int TOAST = 0;

	/** The Constant SETNULL. */
	protected static final int SETNULL = 1;

	/** The Constant UPDATE_UI. */
	protected static final int UPDATE_UI = 2;

	/** The Constant UNBAND_FAIL. */
	protected static final int UNBAND_FAIL = 3;

	/** The Constant DISCONNECT. */
	protected static final int DISCONNECT = 4;

	/** The Constant UNBIND_SUCCEED. */
	protected static final int UNBIND_SUCCEED = 5;
	
	/** The Constant LOG. */
	protected static final int LOG = 6;

	/** The Constant RESP. */
	protected static final int RESP = 7;
	
	/** The Constant HARDWARE. */
	protected static final int HARDWARE = 8;

	/*
	 * ===========================================================
	 * 以下key值对应http://site.gizwits.com/v2/datapoint?product_key={productKey}
	 * 中显示的数据点名称，sdk通过该名称作为json的key值来收发指令，demo中使用的key都是对应机智云实验室的微信宠物屋项目所用数据点
	 * ===========================================================
	 */
	/** led红灯开关 0=关 1=开. */
	private static final String KEY_RED_SWITCH = "attr0";

	/** 指定led颜色值 0=自定义 1=黄色 2=紫色 3=粉色. */
	private static final String KEY_LIGHT_COLOR = "attr1";

	/** led灯红色值 0-254. */
	private static final String KEY_LIGHT_RED = "attr2";

	/** led灯绿色值 0-254. */
	private static final String KEY_LIGHT_GREEN = "attr3";

	/** led灯蓝色值 0-254. */
	private static final String KEY_LIGHT_BLUE = "attr4";

	/** 电机转速 －5～－1 电机负转 0 停止 1～5 电机正转. */
	private static final String KEY_SPEED = "attr5";

	/** 红外探测 0无障碍 1有障碍. */
	private static final String KEY_INFRARED = "attr6";

	/** 环境温度. */
	private static final String KEY_TEMPLATE = "attr7";

	/** 环境湿度. */
	private static final String KEY_HUMIDITY = "attr8";

	/** 实体字段名，代表对应的项目. */
	private static final String KEY_ACTION = "entity0";

	/** The sw red. */
	private Switch swRed;

	/** The sw infrared. */
	private Switch swInfrared;

	/** The sp color. */
	private Spinner spColor;

	/** The tv red. */
	private TextView tvRed;

	/** The tv green. */
	private TextView tvGreen;

	/** The tv blue. */
	private TextView tvBlue;

	/** The tv speed. */
	private TextView tvSpeed;

	/** The tv template. */
	private TextView tvTemplate;

	/** The tv humidity. */
	private TextView tvHumidity;

	/** The sb red. */
	private SeekBar sbRed;

	/** The sb green. */
	private SeekBar sbGreen;

	/** The sb blue. */
	private SeekBar sbBlue;

	/** The sb speed. */
	private SeekBar sbSpeed;

	/** The control device. */
	ControlDevice controlDevice;

	/** The xpg wifi device. */
	XPGWifiDevice xpgWifiDevice;


	/** The is init finish. */
	Boolean isInitFinish = true;

	/** The title. */
	String title = "";

	/** The device statu. */
	private HashMap<String, Object> deviceStatu;

	/** The unbind statu. */
	private boolean isUnbind = false;

	/** The handler. */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {

			case DISCONNECT:
				Toast.makeText(GokitControlActivity.this, "设备已断开",
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			case UNBAND_FAIL:
				Toast.makeText(GokitControlActivity.this, "解绑失败",
						Toast.LENGTH_SHORT).show();
				break;
			case UNBIND_SUCCEED:
				Toast.makeText(GokitControlActivity.this, "解绑成功",
						Toast.LENGTH_SHORT).show();
				break;
			case UPDATE_UI:
				swRed.setChecked((Boolean) deviceStatu.get(KEY_RED_SWITCH));
				swInfrared.setChecked((Boolean) deviceStatu.get(KEY_INFRARED));
				tvBlue.setText((CharSequence) deviceStatu.get(KEY_LIGHT_BLUE));
				tvGreen.setText((CharSequence) deviceStatu.get(KEY_LIGHT_GREEN));
				tvRed.setText((CharSequence) deviceStatu.get(KEY_LIGHT_RED));
				tvSpeed.setText((CharSequence) deviceStatu.get(KEY_SPEED));
				tvTemplate
						.setText((CharSequence) deviceStatu.get(KEY_TEMPLATE));
				tvHumidity
						.setText((CharSequence) deviceStatu.get(KEY_HUMIDITY));
				sbBlue.setProgress(Integer.parseInt((String) deviceStatu
						.get(KEY_LIGHT_BLUE)));
				sbGreen.setProgress(Integer.parseInt((String) deviceStatu
						.get(KEY_LIGHT_GREEN)));
				sbRed.setProgress(Integer.parseInt((String) deviceStatu
						.get(KEY_LIGHT_RED)));
				sbSpeed.setProgress(5 + Integer.parseInt((String) deviceStatu
						.get(KEY_SPEED)));
				break;
			case SETNULL:
				if (xpgWifiDevice != null) {
					xpgWifiDevice.setListener(null);
				}
				mCenter.getXPGWifiSDK().setListener(null);
				break;
			case RESP:
				String data = msg.obj.toString();

				try {
					showDataInUI(data);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			case LOG:
				StringBuilder sb = new StringBuilder();
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject((String)msg.obj);
					for (int i = 0; i < jsonObject.length(); i++) {
						sb.append(jsonObject.names().getString(i) + " " + jsonObject.getInt(jsonObject.names().getString(i)) + "\r\n");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Toast.makeText(GokitControlActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
				break;
			case TOAST:
				String info = msg.obj + "";
				Toast.makeText(GokitControlActivity.this, info,
						Toast.LENGTH_SHORT).show();
				break;
			case HARDWARE:
				showHardwareInfo((String)msg.obj);
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gokit_control);
		initViews();
		initEvents();
		deviceStatu = new HashMap<String, Object>();
		actionBar.setDisplayHomeAsUpEnabled(true);
		controlDevice = (ControlDevice) getIntent().getSerializableExtra(
				"device");
		xpgWifiDevice = BaseActivity.findDeviceByMac(controlDevice.getMac(),
				controlDevice.getDid());
		if (xpgWifiDevice != null) {
			xpgWifiDevice.setListener(deviceListener);
		}
		actionBar.setTitle(controlDevice.getName());
		if (!xpgWifiDevice.isOnline()) {
			new AlertDialog.Builder(this).setTitle("警告")
					.setMessage("设备不在线，不可以做控制，但可以断开或解除绑定")
					.setPositiveButton("OK", null).show();
		}
		actionBar.setTitle(title);
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 3];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 3] = hexArray[v >>> 4];
	        hexChars[j * 3 + 1] = hexArray[v & 0x0F];
	        hexChars[j * 3 + 2] = ' ';
	    }
	    return new String(hexChars);
	}

	@Override
	public boolean didReceiveData(XPGWifiDevice device, java.util.concurrent.ConcurrentHashMap<String,Object> dataMap, int result) {
		if (dataMap.get("data") != null) {
			Log.i("info", (String)dataMap.get("data"));
			Message msg = new Message();
			msg.obj = dataMap.get("data");
			msg.what = RESP;
			handler.sendMessage(msg);
		}
		
		if (dataMap.get("alters") != null) {
			Log.i("info", (String)dataMap.get("alters"));
			Message msg = new Message();
			msg.obj = dataMap.get("alters");
			msg.what = LOG;
			handler.sendMessage(msg);
		}
		
		if (dataMap.get("faults") != null) {
			Log.i("info", (String)dataMap.get("faults"));
			Message msg = new Message();
			msg.obj = dataMap.get("faults");
			msg.what = LOG;
			handler.sendMessage(msg);
		}
		
		if (dataMap.get("binary") != null) {
			Log.i("info", "Binary data:" + bytesToHex((byte[])dataMap.get("binary")));
		}

		return true;
	};
	
	public boolean onReceiveData(String data) {
		Log.i("info", data);
		// isInitFinish = false;
		Message msg = new Message();
		msg.obj = data;
		msg.what = RESP;
		handler.sendMessage(msg);

		return true;
	}

	@Override
	public void didDisconnected(XPGWifiDevice device) {
		if (!isUnbind) {
			Message msg = new Message();
			msg.what = DISCONNECT;
			handler.sendMessage(msg);
		}
		isUnbind = false;
	}
	
	@Override
	public void didQueryHardwareInfo(XPGWifiDevice device, int result, java.util.concurrent.ConcurrentHashMap<String,String> hardwareInfo) {
		StringBuilder sb = new StringBuilder();
		if (0 == result) {
			sb.append("Wifi Hardware Version:" + hardwareInfo.get(XPGWifiDevice.XPGWifiDeviceHardwareWifiHardVerKey) + "\r\n");
			sb.append("Wifi Software Version:" + hardwareInfo.get(XPGWifiDevice.XPGWifiDeviceHardwareWifiSoftVerKey) + "\r\n");
			sb.append("MCU Hardware Version:" + hardwareInfo.get(XPGWifiDevice.XPGWifiDeviceHardwareMCUHardVerKey) + "\r\n");
			sb.append("MCU Software Version:" + hardwareInfo.get(XPGWifiDevice.XPGWifiDeviceHardwareMCUSoftVerKey) + "\r\n");
			sb.append("Firmware Id:" + hardwareInfo.get(XPGWifiDevice.XPGWifiDeviceHardwareFirmwareIdKey) + "\r\n");
			sb.append("Firmware Version:" + hardwareInfo.get(XPGWifiDevice.XPGWifiDeviceHardwareFirmwareVerKey) + "\r\n");
			sb.append("Product Key:" + hardwareInfo.get(XPGWifiDevice.XPGWifiDeviceHardwareProductKey) + "\r\n");
			sb.append("Device id:" + device.getDid()+"\r\n");
		} else {
			sb.append("获取失败，错误号：" + result);
		}
		
		Message msg = new Message();
		msg.what = HARDWARE;
		msg.obj = sb.toString();
		handler.sendMessage(msg);
	};

	/**
	 * 初始化控件.
	 */
	private void initViews() {
		swRed = (Switch) findViewById(R.id.sw_red);
		swInfrared = (Switch) findViewById(R.id.sw_infrared);
		spColor = (Spinner) findViewById(R.id.sp_color);
		tvRed = (TextView) findViewById(R.id.tv_red);
		tvGreen = (TextView) findViewById(R.id.tv_green);
		tvBlue = (TextView) findViewById(R.id.tv_blue);
		tvSpeed = (TextView) findViewById(R.id.tv_speed);
		tvTemplate = (TextView) findViewById(R.id.tv_template);
		tvHumidity = (TextView) findViewById(R.id.tv_humidity);
		sbRed = (SeekBar) findViewById(R.id.sb_red);
		sbGreen = (SeekBar) findViewById(R.id.sb_green);
		sbBlue = (SeekBar) findViewById(R.id.sb_blue);
		sbSpeed = (SeekBar) findViewById(R.id.sb_speed);

	}

	/**
	 * 初始化监听器.
	 */
	private void initEvents() {
		swRed.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				try {
					sendJson(KEY_RED_SWITCH, isChecked);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		swInfrared.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				try {

					sendJson(KEY_INFRARED, isChecked);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		spColor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					sendJson(KEY_LIGHT_COLOR, position);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		sbRed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_LIGHT_RED, seekBar.getProgress());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvRed.setText(progress + "");

			}
		});
		sbBlue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_LIGHT_BLUE, seekBar.getProgress());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvBlue.setText(progress + "");

			}
		});
		sbGreen.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_LIGHT_GREEN, seekBar.getProgress());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvGreen.setText(progress + "");

			}
		});
		sbSpeed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_SPEED, seekBar.getProgress() - 5);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				tvSpeed.setText((progress - 5) + "");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.control_device, menu);

		return true;
	}

	public void onResume() {
		super.onResume();
		this.xpgWifiDevice.setListener(deviceListener);
	}

	public boolean onOptionsItemSelected(MenuItem menu) {
		super.onOptionsItemSelected(menu);
		switch (menu.getItemId()) {
		// 断开连接
		case R.id.action_disconnect:
			mCenter.cDisconnect(xpgWifiDevice);
			xpgWifiDevice = null;
			finish();
			break;
		// 解除绑定
		case R.id.action_unbind:
			String uid = setmanager.getUid();
			String token = setmanager.getToken();
			String hideuid = setmanager.getHideUid();
			String hidetoken = setmanager.getHideToken();
			if (!uid.equals("") && !token.equals("")) {
				mCenter.cUnbindDevice(xpgWifiDevice, uid, token);
			} else if (!hideuid.equals("") && !hidetoken.equals("")) {
				mCenter.cUnbindDevice(xpgWifiDevice, hideuid, hidetoken);

			} else {
				Toast.makeText(this, "请重新登录", Toast.LENGTH_SHORT).show();
			}

			break;
		// 获取设备状态
		case R.id.action_device_status:
			try {
				mCenter.cGetStatus(xpgWifiDevice);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// 获取设备硬件信息
		case R.id.action_device_hardwareinfo:
			if (xpgWifiDevice.isLAN()) {
				xpgWifiDevice.getHardwareInfo();
			} else {
				Message msg = new Message();
				msg.what = HARDWARE;
				msg.obj = "远程设备无法获取设备硬件信息，请切换到本地获取";
				handler.sendMessage(msg);
			}
			break;

		default:
			break;
		}

		return true;
	}

	public void onBackPressed() {
		xpgWifiDevice.disconnect();
		super.onBackPressed();

	}

	/**
	 * Show data in ui.
	 * 
	 * @param data
	 *            the data
	 * @throws JSONException
	 *             the JSON exception
	 */
	private void showDataInUI(String data) throws JSONException {
		Log.i("revjson", data);
		JSONObject receive = new JSONObject(data);
		Iterator actions = receive.keys();
		while (actions.hasNext()) {
			String action = actions.next().toString();
			// 忽略特殊部分
			if (action.equals("cmd") || action.equals("qos")
					|| action.equals("seq") || action.equals("version")) {
				continue;
			}
			JSONObject params = receive.getJSONObject(action);
			Iterator it_params = params.keys();
			while (it_params.hasNext()) {
				String param = it_params.next().toString();
				Object value = params.get(param);
				deviceStatu.put(param, value);
			}
		}
		Message msg = new Message();
		msg.obj = data;
		msg.what = UPDATE_UI;
		handler.sendMessage(msg);
	}
	
	/**
	 * 展示设备硬件信息
	 * @param hardwareInfo
	 */
	private void showHardwareInfo(String hardwareInfo) {
		new AlertDialog.Builder(this).setTitle("设备硬件信息").setMessage(hardwareInfo).setPositiveButton("确定", null).show();
	}

	/**
	 * 发送指令。格式为json。
	 * <p>
	 * 例如 {"entity0":{"attr2":74},"cmd":1}
	 * 其中entity0为gokit所代表的实体key，attr2代表led灯红色值，cmd为1时代表写入
	 * 。以上命令代表改变gokit的led灯红色值为74.
	 * 
	 * @param key
	 *            数据点对应的的json的key
	 * @param value
	 *            需要改变的值
	 * @throws JSONException
	 *             the JSON exception
	 */
	private void sendJson(String key, Object value) throws JSONException {
		final JSONObject jsonsend = new JSONObject();
		JSONObject jsonparam = new JSONObject();
		jsonsend.put("cmd", 1);
		jsonparam.put(key, value);
		jsonsend.put(KEY_ACTION, jsonparam);
		Log.i("sendjson", jsonsend.toString());
		mCenter.cWrite(xpgWifiDevice, jsonsend);
	}

	@Override
	public void didUnbindDevice(int error, String errorMessage, String did) {
		if (error == 0) {
			Message msg = new Message();
			msg.what = UNBIND_SUCCEED;
			handler.sendMessage(msg);
			isUnbind = true;
			xpgWifiDevice.disconnect();
			finish();
		} else {
			Message msg = new Message();
			msg.what = UNBAND_FAIL;
			handler.sendMessage(msg);
		}
	}

}
