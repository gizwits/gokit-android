package com.gizwits.opensource.gokit.ControlModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.gokit.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

@SuppressLint("HandlerLeak")
public class GosDeviceControlActivity extends GosControlModuleBaseActivity implements OnClickListener {

	/** The Constant TOAST. */
	protected static final int TOAST = 0;

	/** The Constant SETNULL. */
	protected static final int SETNULL = 1;

	/** The Constant UPDATE_UI. */
	protected static final int UPDATE_UI = 2;

	/** The Constant LOG. */
	protected static final int LOG = 3;

	/** The Constant RESP. */
	protected static final int RESP = 4;

	/** The Constant HARDWARE. */
	protected static final int HARDWARE = 5;

	/** The Disconnect */
	protected static final int DISCONNECT = 6;

	/*
	 * ===========================================================
	 * 以下key值对应http://site.gizwits.com/v2/datapoint?product_key={productKey}
	 * 中显示的数据点名称，sdk通过该名称作为json的key值来收发指令，demo中使用的key都是对应机智云实验室的微信宠物屋项目所用数据点
	 * ===========================================================
	 */
	/** led红灯开关 0=关 1=开. */
	private static final String KEY_RED_SWITCH = "LED_OnOff";

	/** 指定led颜色值 0=自定义 1=黄色 2=紫色 3=粉色. */
	private static final String KEY_LIGHT_COLOR = "LED_Color";

	/** led灯红色值 0-254. */
	private static final String KEY_LIGHT_RED = "LED_R";

	/** led灯绿色值 0-254. */
	private static final String KEY_LIGHT_GREEN = "LED_G";

	/** led灯蓝色值 0-254. */
	private static final String KEY_LIGHT_BLUE = "LED_B";

	/** 电机转速 －5～－1 电机负转 0 停止 1～5 电机正转. */
	private static final String KEY_SPEED = "Motor_Speed";

	/** 红外探测 0无障碍 1有障碍. */
	private static final String KEY_INFRARED = "Infrared";

	/** 环境温度. */
	private static final String KEY_TEMPLATE = "Temperature";

	/** 环境湿度. */
	private static final String KEY_HUMIDITY = "Humidity";
	/*
	 * ===========================================================
	 * 以下key值对应设备硬件信息各明细的名称，用与回调中提取硬件信息字段。
	 * ===========================================================
	 */

	/** The wifiHardVerKey */
	private static final String wifiHardVerKey = "wifiHardVersion";

	/** The wifiSoftVerKey */
	private static final String wifiSoftVerKey = "wifiSoftVersion";

	/** The mcuHardVerKey */
	private static final String mcuHardVerKey = "mcuHardVersion";

	/** The mcuSoftVerKey */
	private static final String mcuSoftVerKey = "mcuSoftVersion";

	/** The wifiFirmwareIdKey */
	private static final String FirmwareIdKey = "wifiFirmwareId";

	/** The wifiFirmwareVerKey */
	private static final String FirmwareVerKey = "wifiFirmwareVer";

	/** The productKey */
	private static final String productKey = "productKey";

	/** The sw red. */
	private Switch swRed;

	/** The sw infrared. */
	private Switch swInfrared;

	/** The ll color */
	private LinearLayout llColor;

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

	/** The tv ColorText */
	private TextView tvColorText;

	/** The sb red. */
	private SeekBar sbRed;

	/** The sb green. */
	private SeekBar sbGreen;

	/** The sb blue. */
	private SeekBar sbBlue;

	/** The sb speed. */
	private SeekBar sbSpeed;

	ImageView redsub, redadd, greensub, greenadd, bluesub, blueadd, speedsub, speedadd;

	/** The GizWifiDevice device */
	private GizWifiDevice mDevice;

	/** The device statu. */
	private HashMap<String, Object> deviceStatu;

	/** The First */
	// private boolean isFirst = true;

	/** The isUpDateUi */
	protected static boolean isUpDateUi = true;

	/** The Title */
	private String title;

	/** The colors list */
	ArrayList<String> colorsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_device_control);
		initDevice();
		// 设置ActionBar
		setActionBar(true, true, title);
		initData();
		initViews();
		initEvents();
		mDevice.setListener(gizWifiDeviceListener);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!isUpDateUi) {
			try {
				sendJson(KEY_LIGHT_COLOR, spf.getInt("COLOR", 0));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 初始化控件.
	 */
	private void initViews() {
		tvColorText = (TextView) findViewById(R.id.tvColorText);
		llColor = (LinearLayout) findViewById(R.id.ll_color);
		swRed = (Switch) findViewById(R.id.sw_red);
		swInfrared = (Switch) findViewById(R.id.sw_infrared);
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

		//
		redadd = (ImageView) findViewById(R.id.redadd);
		redsub = (ImageView) findViewById(R.id.redsub);
		greenadd = (ImageView) findViewById(R.id.greenadd);
		greensub = (ImageView) findViewById(R.id.greensub);
		blueadd = (ImageView) findViewById(R.id.blueadd);
		bluesub = (ImageView) findViewById(R.id.bluesub);
		speedadd = (ImageView) findViewById(R.id.speedadd);
		speedsub = (ImageView) findViewById(R.id.speedsub);

		String waitingText = (String) getText(R.string.waiting_device_ready);
		setProgressDialog(waitingText, true, false);
		progressDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (progressDialog.isShowing()) {
						GosDeviceControlActivity.this.finish();
						mDevice.setSubscribe(false);
						mDevice.setListener(null);
						return true;
					}
				}

				return false;
			}
		});
		progressDialog.show();
	}

	/**
	 * 初始化监听器.
	 */
	private void initEvents() {
		redadd.setOnClickListener(this);
		redsub.setOnClickListener(this);
		greenadd.setOnClickListener(this);
		greensub.setOnClickListener(this);
		blueadd.setOnClickListener(this);
		bluesub.setOnClickListener(this);
		speedadd.setOnClickListener(this);
		speedsub.setOnClickListener(this);
		//
		swRed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					sendJson(KEY_RED_SWITCH, swRed.isChecked());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		llColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GosDeviceControlActivity.this, GosColorsListActivity.class);
				startActivity(intent);
			}
		});

		sbRed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_LIGHT_RED, seekBar.getProgress());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvRed.setText(progress + "");

			}
		});
		sbBlue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_LIGHT_BLUE, seekBar.getProgress());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvBlue.setText(progress + "");

			}
		});
		sbGreen.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_LIGHT_GREEN, seekBar.getProgress());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvGreen.setText(progress + "");

			}
		});
		sbSpeed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_SPEED, seekBar.getProgress() - 5);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvSpeed.setText((progress - 5) + "");
			}
		});

	}

	private void initData() {
		String[] colors = getResources().getStringArray(R.array.color);
		colorsList = new ArrayList<String>();
		for (String str : colors) {
			colorsList.add(str);
		}
	}

	private void initDevice() {
		Intent intent = getIntent();
		mDevice = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
		deviceStatu = new HashMap<String, Object>();

		if (TextUtils.isEmpty(mDevice.getAlias())) {
			title = mDevice.getProductName();
		} else {
			title = mDevice.getAlias();
		}
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
		ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
		hashMap.put(key, value);
		mDevice.write(hashMap, 0);
		Log.i("Apptest", hashMap.toString());
	}

	/** The handler. */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {

			case UPDATE_UI:
				isUpDateUi = true;
				tvColorText.setText(colorsList.get(Integer.parseInt(deviceStatu.get(KEY_LIGHT_COLOR).toString())));
				spf.edit().putInt("COLOR", Integer.parseInt(deviceStatu.get(KEY_LIGHT_COLOR).toString())).commit();

				swRed.setChecked((Boolean) deviceStatu.get(KEY_RED_SWITCH));
				swInfrared.setChecked((Boolean) deviceStatu.get(KEY_INFRARED));
				tvBlue.setText(deviceStatu.get(KEY_LIGHT_BLUE).toString());
				tvGreen.setText(deviceStatu.get(KEY_LIGHT_GREEN).toString());
				tvRed.setText(deviceStatu.get(KEY_LIGHT_RED).toString());
				tvSpeed.setText(deviceStatu.get(KEY_SPEED).toString());
				tvTemplate.setText(deviceStatu.get(KEY_TEMPLATE).toString());
				tvHumidity.setText(deviceStatu.get(KEY_HUMIDITY).toString());
				if (deviceStatu.get(KEY_LIGHT_BLUE).toString() != null) {
					sbBlue.setProgress(Integer.parseInt(deviceStatu.get(KEY_LIGHT_BLUE).toString()));
				} else {
					sbBlue.setProgress(0);
				}

				if (deviceStatu.get(KEY_LIGHT_GREEN).toString() != null) {
					sbGreen.setProgress(Integer.parseInt(deviceStatu.get(KEY_LIGHT_GREEN).toString()));
				} else {
					sbBlue.setProgress(0);
				}

				if (deviceStatu.get(KEY_LIGHT_RED).toString() != null) {
					sbRed.setProgress(Integer.parseInt(deviceStatu.get(KEY_LIGHT_RED).toString()));
				} else {
					sbBlue.setProgress(0);
				}

				if (deviceStatu.get(KEY_SPEED).toString() != null) {
					sbSpeed.setProgress(5 + Integer.parseInt(deviceStatu.get(KEY_SPEED).toString()));
				} else {
					sbSpeed.setProgress(5);
				}
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
				int logText = 1;
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					for (int i = 0; i < jsonObject.length(); i++) {
						if (jsonObject.getBoolean(jsonObject.names().getString(i)) != false) {
							sb.append(jsonObject.names().getString(i) + " " + logText + "\r\n");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (sb.length() != 0) {
					Toast.makeText(GosDeviceControlActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
				}
				break;
			case TOAST:
				String info = msg.obj + "";
				Toast.makeText(GosDeviceControlActivity.this, info, Toast.LENGTH_SHORT).show();
				break;
			case HARDWARE:
				showHardwareInfo((String) msg.obj);
				break;

			case DISCONNECT:
				String disconnectText = (String) getText(R.string.disconnect);
				Toast.makeText(GosDeviceControlActivity.this, disconnectText, Toast.LENGTH_SHORT).show();
				mDevice.setSubscribe(false);
				mDevice.setListener(null);
				finish();
				break;
			}

		}
	};

	/**
	 * Show data in ui.
	 * 
	 * @param data
	 *            the data
	 * @throws JSONException
	 *             the JSON exception
	 */
	@SuppressWarnings("rawtypes")
	private void showDataInUI(String data) throws JSONException {
		Log.i("revjson", data);
		JSONObject receive = new JSONObject(data);
		Iterator actions = receive.keys();
		while (actions.hasNext()) {
			String param = actions.next().toString();
			Object value = receive.get(param);
			deviceStatu.put(param, value);
		}
		Message msg = new Message();
		msg.obj = data;
		msg.what = UPDATE_UI;
		handler.sendMessage(msg);
	}

	/**
	 * 展示设备硬件信息
	 * 
	 * @param hardwareInfo
	 */
	private void showHardwareInfo(String hardwareInfo) {
		String hardwareInfoTitle = (String) getText(R.string.hardwareInfo);
		new AlertDialog.Builder(this).setTitle(hardwareInfoTitle).setMessage(hardwareInfo)
				.setPositiveButton(R.string.besure, null).show();
	}

	protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
			java.util.concurrent.ConcurrentHashMap<String, Object> dataMap, int sn) {
		if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
			Message msg = new Message();
			msg.what = TOAST;
			msg.obj = toastError(result);
			return;
		}
		if (dataMap.isEmpty()) {
			return;
		}
		progressDialog.cancel();

		if (dataMap.get("data") != null) {
			Log.i("Apptest", dataMap.get("data").toString());
			Message msg = new Message();
			msg.obj = dataMap.get("data");
			msg.what = RESP;
			handler.sendMessage(msg);
		}

		if (dataMap.get("alerts") != null) {
			Message msg = new Message();
			msg.obj = dataMap.get("alerts");
			msg.what = LOG;
			handler.sendMessage(msg);
		}

		if (dataMap.get("faults") != null) {
			Message msg = new Message();
			msg.obj = dataMap.get("faults");
			msg.what = LOG;
			handler.sendMessage(msg);
		}

		if (dataMap.get("binary") != null) {
			Log.i("info", "Binary data:" + bytesToHex((byte[]) dataMap.get("binary")));
		}
	}

	protected void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
			java.util.concurrent.ConcurrentHashMap<String, String> hardwareInfo) {
		Log.i("Apptest", hardwareInfo.toString());
		StringBuffer sb = new StringBuffer();
		Message msg = new Message();
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
			msg.what = TOAST;
			msg.obj = toastError(result);
		} else {
			sb.append("Wifi Hardware Version:" + hardwareInfo.get(wifiHardVerKey) + "\r\n");
			sb.append("Wifi Software Version:" + hardwareInfo.get(wifiSoftVerKey) + "\r\n");
			sb.append("MCU Hardware Version:" + hardwareInfo.get(mcuHardVerKey) + "\r\n");
			sb.append("MCU Software Version:" + hardwareInfo.get(mcuSoftVerKey) + "\r\n");
			sb.append("Wifi Firmware Id:" + hardwareInfo.get(FirmwareIdKey) + "\r\n");
			sb.append("Wifi Firmware Version:" + hardwareInfo.get(FirmwareVerKey) + "\r\n");
			sb.append("Product Key:" + "\r\n" + hardwareInfo.get(productKey) + "\r\n");

			// 设备属性
			sb.append("Device ID:" + "\r\n" + mDevice.getDid() + "\r\n");
			sb.append("Device IP:" + mDevice.getIPAddress() + "\r\n");
			sb.append("Device MAC:" + mDevice.getMacAddress() + "\r\n");

			msg.what = HARDWARE;
			msg.obj = sb.toString();
		}

		handler.sendMessage(msg);
	}

	protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
		progressDialog.cancel();
		Message msg = new Message();
		msg.what = TOAST;
		String toastText;
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
			toastText = (String) getText(R.string.set_info_successful);
		} else {
			toastText = toastError(result);
		}
		msg.obj = toastText;
		handler.sendMessage(msg);
	}

	protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
		if (device == mDevice) {
			if (GizWifiDeviceNetStatus.GizDeviceUnavailable == netStatus
					|| GizWifiDeviceNetStatus.GizDeviceOffline == netStatus) {
				handler.sendEmptyMessage(DISCONNECT);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.redadd:
			int redNum1 = sbRed.getProgress();
			if (redNum1 < 254) {
				redNum1++;
				try {
					sendJson(KEY_LIGHT_RED, redNum1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.redsub:
			int redNum2 = sbRed.getProgress();
			if (redNum2 > 0) {
				redNum2--;
				try {
					sendJson(KEY_LIGHT_RED, redNum2);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.greenadd:
			int greenNum1 = sbGreen.getProgress();
			if (greenNum1 < 254) {
				greenNum1++;
				try {
					sendJson(KEY_LIGHT_GREEN, greenNum1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.greensub:
			int greenNum2 = sbGreen.getProgress();
			if (greenNum2 > 0) {
				greenNum2--;
				try {
					sendJson(KEY_LIGHT_GREEN, greenNum2);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.blueadd:
			int blueNum1 = sbBlue.getProgress();
			if (blueNum1 < 254) {
				blueNum1++;
				try {
					sendJson(KEY_LIGHT_BLUE, blueNum1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.bluesub:
			int blueNum2 = sbBlue.getProgress();
			if (blueNum2 > 0) {
				blueNum2--;
				try {
					sendJson(KEY_LIGHT_BLUE, blueNum2);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.speedadd:
			int speedNum1 = sbSpeed.getProgress();
			if (speedNum1 < 10) {
				speedNum1++;
				try {
					sendJson(KEY_SPEED, speedNum1 - 5);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.speedsub:
			int speedNum2 = sbSpeed.getProgress();
			if (speedNum2 > 0) {
				speedNum2--;
				try {
					sendJson(KEY_SPEED, speedNum2 - 5);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (mDevice.isLAN()) {
			getMenuInflater().inflate(R.menu.devicecontrol_lan, menu);
		} else {
			getMenuInflater().inflate(R.menu.devicecontrol, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem menu) {
		super.onOptionsItemSelected(menu);
		switch (menu.getItemId()) {
		case android.R.id.home:
			mDevice.setSubscribe(false);
			mDevice.setListener(null);
			finish();
			break;

		// 设置设备信息
		case R.id.action_setDeviceInfo:
			setDeviceInfo();
			break;

		// 获取设备硬件信息
		case R.id.action_getHardwareInfo:
			mDevice.getHardwareInfo();
			break;

		// 获取设备状态
		case R.id.action_getStatu:
			String getingStatuText = (String) getText(R.string.getStatu);
			progressDialog.setMessage(getingStatuText);
			progressDialog.show();
			mDevice.getDeviceStatus();
			break;

		default:
			break;
		}

		return true;
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 3];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 3] = hexArray[v >>> 4];
			hexChars[j * 3 + 1] = hexArray[v & 0x0F];
			hexChars[j * 3 + 2] = ' ';
		}
		return new String(hexChars);
	}

	private void setDeviceInfo() {

		final Dialog dialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
		dialog.show();

		Window window = dialog.getWindow();
		window.setContentView(R.layout.alert_gos_set_device_info);

		final EditText etAlias;
		final EditText etRemark;
		etAlias = (EditText) window.findViewById(R.id.etAlias);
		etRemark = (EditText) window.findViewById(R.id.etRemark);

		LinearLayout llNo, llSure;
		llNo = (LinearLayout) window.findViewById(R.id.llNo);
		llSure = (LinearLayout) window.findViewById(R.id.llSure);

		if (!TextUtils.isEmpty(mDevice.getAlias())) {
			etAlias.setText(mDevice.getAlias());
		}
		if (!TextUtils.isEmpty(mDevice.getRemark())) {
			etRemark.setText(mDevice.getRemark());
		}

		llNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		llSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDevice.setCustomInfo(etRemark.getText().toString(), etAlias.getText().toString());
				dialog.cancel();
				String loadingText = (String) getText(R.string.loadingtext);
				progressDialog.setMessage(loadingText);
				progressDialog.show();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mDevice.setSubscribe(false);
			mDevice.setListener(null);
			finish();
		}
		return false;
	}

}
