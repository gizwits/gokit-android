package com.xpg.gokit.activity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.xpg.gokit.R;
import com.xpg.gokit.adapter.DeviceListAdapter;
import com.xpg.gokit.bean.ControlDevice;
import com.xpg.gokit.setting.SettingManager;
import com.xpg.gokit.utils.CRCUtils;
import com.xpg.gokit.utils.NetUtils;
import com.xtremeprog.xpgconnect.XPGWifiConfig;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiDeviceList;

/**
 * 设备列表界面
 * 
 * @author Lien Li
 * 
 */
public class DeviceListActivity extends BaseActivity implements
		OnItemClickListener, OnItemLongClickListener {
	protected static final int NEW_DEVICE = 0;
	protected static final int LOGINSUCCESS = 1;
	protected static final int LOGINFAIL = 2;
	protected static final int CONNECTEDFAIL = 3;
	protected static final int LOG = 4;
	private static final int REFLASH = 5;
	String servername = "site.gizwits.com";
	private ListView lv_device_list;
	private List<ControlDevice> devicelist;
	private DeviceListAdapter adapter;
	private XPGWifiDevice xpgWifiDevice;
	ControlDevice device;
	ProgressDialog dialog;
	private Timer timer;
	Boolean isGettingDevice = false;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFLASH:
				if (!isGettingDevice) {
					initEmptyData();
					getDeviceList();
				}
				break;
			case NEW_DEVICE:
				UpdateUI();
				break;
			case LOGINSUCCESS:
				Toast.makeText(DeviceListActivity.this, "登陆设备成功",
						Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;
			case LOGINFAIL:
				Toast.makeText(DeviceListActivity.this, "登陆设备失败",
						Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;
			case CONNECTEDFAIL:
				Toast.makeText(DeviceListActivity.this, "连接设备失败",
						Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;
			case LOG:
				Toast.makeText(DeviceListActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				break;

			}
		}

	};
	boolean finishdownload = false;

	protected void onUpdateProduct(int result) {
		finishdownload = true;
	};

	protected void onDiscovered(int result, XPGWifiDeviceList devices) {
		Log.d("c", "Device count:" + devices.GetCount());
		storeDeviceList(devices);
		Message msg = new Message();
		msg.what = NEW_DEVICE;
		handler.sendMessage(msg);
	};

	protected void onUserLogout(int error, String errorMessage) {
		String uid = setmanager.getUid();
		String token = setmanager.getToken();
		String hideuid = setmanager.getHideUid();
		String hidetoken = setmanager.getHideToken();
		if (!uid.equals("") && !token.equals("")) {
			mCenter.cGetBoundDevices(uid, token);
		} else if (!hideuid.equals("") && !hidetoken.equals("")) {
			mCenter.cGetBoundDevices(hideuid, hidetoken);
		} else {
			mCenter.cRegisterAnonymousUser();
		}
	};

	public void onUserLogin(int error, String errorMessage, String uid,
			String token) {
		if (uid != null && token != null && !uid.equals("")
				&& !token.equals("") && error == 0) {
			final String fuid = uid;
			final String ftoken = token;
			setmanager.setHideUid(fuid);
			setmanager.setHideToken(ftoken);
			getDeviceList();
		} else {
			Message msg = new Message();
			msg.what = LOG;
			msg.obj = "网络较差，请检查网络连接";
			handler.sendMessage(msg);

		}
	};

	public long onCalculateCRC(byte[] data) {
		return CRCUtils.CalculateCRC(xpgWifiDevice.GetProductKey(), data);
	};

	private void storeDeviceList(XPGWifiDeviceList devices) {
		BaseActivity.deviceslist = new ArrayList<XPGWifiDevice>();
		for (int i = 0; i < devices.GetCount(); i++) {

			BaseActivity.deviceslist.add(devices.GetItem(i));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
		 //使左上角图标显示并且可以按
		this.actionBar.setDisplayShowHomeEnabled(true);
		this.actionBar.setHomeButtonEnabled(true);
		this.actionBar.setIcon(R.drawable.reflash_bt);

//		Log.i("androidid", setmanager.getPhoneId());
		// this.mCenter.getXPGWifiSDK().setListener(gccDelegate);

		// mCenter.getXPGWifiSDK().RegisterAnonymousUser(setmanager.getPhoneId());

		initView();
		initData();
		initListener();

	}

	private synchronized void UpdateUI() {
		EmptyData();
		for (int i = 0; i < BaseActivity.deviceslist.size(); i++) {

			XPGWifiDevice device = BaseActivity.deviceslist.get(i);
			XPGWifiConfig.sharedInstance().DownloadProduct(
					device.GetProductKey());
			// setmanager.DownLoadProduct_key(device.GetProductKey());

			if (device.IsOnline() && device.GetPasscode() != null
					&& !device.GetPasscode().equals("")) {
				ControlDevice controlDevice = new ControlDevice(
						device.GetMacAddress(), device);
				devicelist.add(controlDevice);
			}
		}
		boolean hasnew = false;
		for (int i = 0; i < BaseActivity.deviceslist.size(); i++) {
			XPGWifiDevice device = BaseActivity.deviceslist.get(i);

			if (device.GetPasscode() == null || device.GetPasscode().equals("")) {
				if (!hasnew) {
					ControlDevice controlDevice = new ControlDevice("发现新设备");
					devicelist.add(controlDevice);
					hasnew = true;
				}
				ControlDevice controlDevice = new ControlDevice(
						device.GetMacAddress(), device);
				devicelist.add(controlDevice);
			}
		}
		boolean hasoffline = false;
		for (int i = 0; i < BaseActivity.deviceslist.size(); i++) {
			XPGWifiDevice device = BaseActivity.deviceslist.get(i);

			if (!device.IsOnline()) {
				if (!hasoffline) {
					ControlDevice controlDevice = new ControlDevice("离线设备");
					devicelist.add(controlDevice);
					hasoffline = true;
				}
				ControlDevice controlDevice = new ControlDevice(
						device.GetMacAddress(), device);
				devicelist.add(controlDevice);
			}
		}

		adapter = new DeviceListAdapter(this, devicelist);
		lv_device_list.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.clear();
		String uid = setmanager.getUid();
		if (uid.equals("")) {
			getMenuInflater().inflate(R.menu.device_list, menu);
		} else {
			getMenuInflater().inflate(R.menu.device_list_with_logout, menu);
		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		String uid = setmanager.getUid();
		if (uid.equals("")) {
			getMenuInflater().inflate(R.menu.device_list, menu);
		} else {
			getMenuInflater().inflate(R.menu.device_list_with_logout, menu);
		}
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menu) {
		super.onOptionsItemSelected(menu);
		Intent it = new Intent();
		switch (menu.getItemId()) {
		case android.R.id.home:
			Message msg = new Message();
			msg.what = REFLASH;
			handler.sendMessage(msg);
			break;
		case R.id.action_add_device:
			boolean iswifi = NetUtils.isWifiConnected(this);
			if (iswifi) {
				it.setClass(this, AirLinkActivity.class);

				startActivity(it);
			} else {
				Toast.makeText(this, "请切换至Wifi环境", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.action_login:
			it.setClass(this, LoginActivity.class);
			startActivity(it);
			break;
		case R.id.action_logout:
			mCenter.cLogout();
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();

		timer.cancel();
		timer = null;

	}

	public void onResume() {
		super.onResume();

		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					final String product_key = setmanager
							.getDownLoadProduct_key();
					if (product_key != null) {
						AsyncHttpClient client = new AsyncHttpClient();

						// http://site.gizwits.com/v2/datapoint?product_key=be606a7b34d441b59d7eba2c080ff805&format=json
						client.get("http://" + servername
								+ "/v2/datapoint?product_key=" + product_key
								+ "&format=json",
								new JsonHttpResponseHandler() {
									@Override
									public void onSuccess(int arg0,
											JSONObject json) {
										File file = new File(
												DeviceListActivity.this
														.getFilesDir()
														+ "/Devices/"
														+ product_key + ".json");
										try {
											BufferedWriter writer = new BufferedWriter(
													new FileWriter(file));
											writer.write(json.toString());
											writer.close();
											XPGWifiConfig
													.sharedInstance()
													.SetProductPath(
															DeviceListActivity.this
																	.getFilesDir()
																	+ "/Devices");
											Log.i("filesuccess",
													file.toString());
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								});

					}
				}
			}, 1000, 1000);
		}

		if (dialog.isShowing()) {
			dialog.cancel();
		}
		getDeviceList();
	}



	private void EmptyData() {
		devicelist = new ArrayList<ControlDevice>();
	}

	private void initEmptyData() {
		devicelist = new ArrayList<ControlDevice>();
		adapter = new DeviceListAdapter(this, devicelist);
		lv_device_list.setAdapter(adapter);
	}

	private void initData() {
		devicelist = new ArrayList<ControlDevice>();
		adapter = new DeviceListAdapter(this, devicelist);
		lv_device_list.setAdapter(adapter);
	}

	private void initView() {
		lv_device_list = (ListView) findViewById(R.id.lv_device_list);
		dialog = new ProgressDialog(this);
	}

	private void initListener() {
		lv_device_list.setOnItemClickListener(this);
		lv_device_list.setOnItemLongClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		device = devicelist.get(pos);
		if (!device.isTitle()) {
			xpgWifiDevice = BaseActivity.findDeviceByMac(device.getMac(),
					device.getDid());
			xpgWifiDevice.setListener(deviceListener);
			if (xpgWifiDevice != null) {
				File file = new File(this.getFilesDir() + "/Devices/"
						+ xpgWifiDevice.GetProductKey() + ".json");
				if (!file.exists()) {
					return;
				}
			}
			if (xpgWifiDevice.IsConnected()) {
				xpgWifiDevice.Disconnect();
				return;
			}
			if (!device.isNew()) {
				if (xpgWifiDevice != null) {
					// xpgWifiDevice.setListener(deviceDelegate);
					dialog.show();
					if (xpgWifiDevice.IsLAN()) {

						// if(!xpgWifiDevice.ConnectToLAN()){
						// xpgWifiDevice.Disconnect();
						xpgWifiDevice.ConnectToLAN();
						// }

					} else {
						if (xpgWifiDevice.GetDid() != null
								&& !xpgWifiDevice.GetDid().equals("")) {

							// if(!xpgWifiDevice.ConnectToMQTT()){
							// xpgWifiDevice.Disconnect();
							xpgWifiDevice.ConnectToMQTT();
							// }
						} else {
							Toast.makeText(this, "设备Did 为空,查询设备是否连网",
									Toast.LENGTH_SHORT).show();
						}

					}
				}
			} else {
				if (xpgWifiDevice != null) {

					// xpgWifiDevice.setListener(deviceDelegate);
					dialog.show();
					if (xpgWifiDevice.IsLAN()) {
						// if(!xpgWifiDevice.ConnectToLAN()){
						// xpgWifiDevice.Disconnect();
						xpgWifiDevice.ConnectToLAN();
						// }

					} else {
						if (xpgWifiDevice.GetDid() != null
								&& !xpgWifiDevice.GetDid().equals("")) {
							// if(!xpgWifiDevice.ConnectToMQTT()){
							// xpgWifiDevice.Disconnect();
							xpgWifiDevice.ConnectToMQTT();
							// }
						} else {
							Toast.makeText(this, "设备Did 为空,查询设备是否连网",
									Toast.LENGTH_SHORT).show();
						}

					}
				}
			}
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View v, int pos,
			long id) {
		return true;
	}

	private void getDeviceList() {
		if (isGettingDevice)
			return;

		isGettingDevice = true;

		String uid = setmanager.getUid();
		String token = setmanager.getToken();
		String hideuid = setmanager.getHideUid();
		String hidetoken = setmanager.getHideToken();
		if (!uid.equals("") && !token.equals("")) {
			//绑定后刷新设备列表
			mCenter.cGetBoundDevices(uid, token);
		} else if (!hideuid.equals("") && !hidetoken.equals("")) {
			//绑定后刷新设备列表
			mCenter.cGetBoundDevices(hideuid, hidetoken);
		} else {
			mCenter.cRegisterAnonymousUser();
		}

		isGettingDevice = false;
	}

	@Override
	public void onLogin(int result) {
		Log.d("wifi", "onLogin:" + result);
		if (result == 0) {
			handler.sendEmptyMessage(LOGINSUCCESS);
			Intent it = new Intent();
			it.setClass(DeviceListActivity.this, GokitControlActivity.class);
			it.putExtra("device", device);
			it.putExtra("islocal", device.getIp() == null
					|| !device.getIp().equals(""));
			startActivity(it);
		} else {
			handler.sendEmptyMessage(LOGINFAIL);
		}
	};

	@Override
	public void onLoginMQTT(int result) {
		Log.d("wifi", "onLoginCloud:" + result);
		if (result == 0) {
			handler.sendEmptyMessage(LOGINSUCCESS);
			Intent it = new Intent();

			it.setClass(DeviceListActivity.this, GokitControlActivity.class);
			it.putExtra("device", device);
			it.putExtra("islocal", device.getIp() == null
					|| !device.getIp().equals(""));
			startActivity(it);

		} else {
			handler.sendEmptyMessage(LOGINFAIL);
		}
	};

	@Override
	public void onConnected() {
		Log.i("connected", "connected");
		if (xpgWifiDevice.IsLAN()) {
			if (xpgWifiDevice.GetPasscode() != null
					&& !xpgWifiDevice.GetPasscode().equals("")) {
				xpgWifiDevice.Login("", xpgWifiDevice.GetPasscode());
			} else {
				Intent it = new Intent();
				it.setClass(DeviceListActivity.this,
						NewDeviceControlActivity.class);
				it.putExtra("device", device);
				startActivity(it);
			}
		} else {
			String uid = setmanager.getUid();
			String token = setmanager.getToken();
			String hideuid = setmanager.getHideUid();
			String hidetoken = setmanager.getHideToken();
			if (!uid.equals("") && !token.equals("")) {
				xpgWifiDevice.Login(uid, token);
			} else if (!hideuid.equals("") && !hidetoken.equals("")) {
				xpgWifiDevice.Login(hideuid, hidetoken);
			}

		}
	}

	@Override
	public void onConnectFailed() {
		Log.i("conn fail", "conn fail");
		handler.sendEmptyMessage(CONNECTEDFAIL);
	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// notfinish = false;
	}
}
