package com.xpg.gokit.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.xpg.ui.QBooleanElement;
import com.xpg.ui.QButtonElement;
import com.xpg.ui.QDateTimeInlineElement;
import com.xpg.ui.QElement;
import com.xpg.ui.QEntryElement;
import com.xpg.ui.QFloatElement;
import com.xpg.ui.QLableElement;
import com.xpg.ui.QMultilineElement;
import com.xpg.ui.QPage;
import com.xpg.ui.QRadioElement;
import com.xpg.ui.QSession;
import com.xpg.ui.listener.ValueChangeListener;
import com.xpg.gokit.R;
import com.xpg.gokit.bean.ControlDevice;
import com.xpg.gokit.setting.SettingManager;
import com.xpg.gokit.utils.CRCUtils;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiDeviceList;
import com.xtremeprog.xpgconnect.XPGWifiDeviceListener;
import com.xtremeprog.xpgconnect.XPGWifiReceiveInfo;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;

/**
 * 控制界面
 * 
 * @author Lien Li
 * */
public class ControlDeviceActivity extends BaseActivity implements
		ValueChangeListener {
	protected static final int LOG = 6;
	protected static final int RESP = 7;
	protected static final int TOAST = 0;
	protected static final int SETNULL = 1;
	protected static final int UPDATE_UI = 2;
	protected static final int UNBAND_FAIL = 3;
	protected static final int DISCONNECT = 4;
	protected static final int LOGIN = 5;
	ControlDevice controlDevice;
	XPGWifiDevice xpgWifiDevice;
	JSONArray jArray;
	private QPage page;
	Boolean isInitFinish = true;
	boolean showEditButton = false;
	boolean isEdit = false;
	JSONObject idmaps;
	String title = "";
	ProgressDialog dialog;
	XPGWifiDeviceListener deviceDelegate = new XPGWifiDeviceListener() {
		public void onBindDevice(int error, String errorMessage) {
		};

		@Override
		public void onUnbindDevice(int error, String errorMessage) {
			// TODO Auto-generated method stub
			if (error == 0) {
				mCenter.cDisconnect(xpgWifiDevice);
				finish();
			} else {
				Message msg = new Message();
				msg.what = UNBAND_FAIL;
				handler.sendMessage(msg);
			}
		}

		public void onQueryHardwareInfo(int error,
				com.xtremeprog.xpgconnect.XPGWifiQueryHardwareInfoStruct pInfo) {
		};

		public void onReceiveAlertsAndFaultsInfo(
				com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo alerts,
				com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo faults) {
			List<XPGWifiReceiveInfo> rinfo = new ArrayList<XPGWifiReceiveInfo>();
			for (int i = 0; i < faults.size(); i++) {
				XPGWifiReceiveInfo info = new XPGWifiReceiveInfo();
				Log.i(faults.get(i).getName(), faults.get(i).getValue() + "");
				info.setName(faults.get(i).getName());
				info.setValue(faults.get(i).getValue());
				rinfo.add(info);
			}
			for (int i = 0; i < alerts.size(); i++) {
				XPGWifiReceiveInfo info = new XPGWifiReceiveInfo();
				info.setName(alerts.get(i).getName());
				info.setValue(alerts.get(i).getValue());
				Log.i(alerts.get(i).getName(), alerts.get(i).getValue() + "");
				rinfo.add(info);
			}
			Message msg = new Message();
			msg.what = LOG;
			msg.obj = rinfo;

			handler.sendMessage(msg);

		};

		public void onUpdateUI() {
			Message msg = new Message();
			msg.what = UPDATE_UI;
			handler.sendMessage(msg);
		};

		public void onGetPasscode(int result) {
		};

		public void onDeviceLog(short nLevel, String tag, String source,
				String content) {
		};

		public void onSetSwitcher(int result) {
		};

		public void onDeviceOnline(boolean isOnline) {
		};

		public void onConnectFailed() {
		};

		public void onLogin(int result) {

			if (result == 0) {
				Message msg = new Message();
				msg.what = TOAST;
				msg.obj = "小循环登陆成功";
				handler.sendMessage(msg);

				isInitFinish = true;
			}
		};

		public void onLoginMQTT(int result) {
			if (result == 0) {
				Message msg = new Message();
				msg.what = TOAST;
				msg.obj = "大循环登陆成功";
				handler.sendMessage(msg);

				isInitFinish = true;
			}
		}

		public boolean onReceiveData(String data) {
			Log.i("info", data);
			// isInitFinish = false;
			Message msg = new Message();
			msg.obj = data;
			msg.what = RESP;
			handler.sendMessage(msg);

			return true;
		};

		public void onConnected() {

			handler.sendEmptyMessage(LOGIN);
		}

		public void onDisconnected() {
			Message msg = new Message();
			msg.what = DISCONNECT;
			handler.sendMessage(msg);
		}
	};
	XPGWifiSDKListener gccDelegate = new XPGWifiSDKListener() {
		public void onDiscovered(int result, XPGWifiDeviceList devices) {
			Log.d("Main", "Device count:" + devices.GetCount());

		};

		public void onChangeUserEmail(int error, String errorMessage) {
		};

		public void onChangeUserPassword(int error, String errorMessage) {
		};

		public void onChangeUserPhone(int error, String errorMessage) {
		};

		public void onTransUser(int error, String errorMessage) {
		};

		public void onUserLogout(int error, String errorMessage) {
		};

		public void onRequestSendVerifyCode(int error, String errorMessage) {
		};

		public void onBindDevice(int error, String errorMessage) {
		};

		public void onRegisterUser(int error, String errorMessage, String uid,
				String token) {
		};

		public void onUnbindDevice(int error, String errorMessage) {
		};

		public void onUserLogin(int error, String errorMessage, String uid,
				String token) {
		};

		public void onGetDeviceInfo(int error, String errorMessage,
				String productKey, String did, String mac, String passCode,
				String host, int port, int isOnline) {
		};

		public void onUpdateProduct(int result) {
			// if(result== 0){
			// Message msg = new Message();
			// msg.what = UPDATE_UI;
			// handler.sendMessage(msg);
			// }
		};

		public long onCalculateCRC(byte[] data) {
			return CRCUtils.CalculateCRC(xpgWifiDevice.GetProductKey(), data);
		};

		public void onGetSSIDList(
				com.xtremeprog.xpgconnect.XPGWifiSSIDList list, int result) {
		};

		public void onSetAirLink(XPGWifiDevice device) {
		};

	};
	// TextView tv_title;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case LOGIN:
				Log.i("connected", "connected");
				if (xpgWifiDevice.IsLAN()) {
					xpgWifiDevice.Login("", mCenter.cGetPasscode(xpgWifiDevice));
				} else {
					String username = settingManager.getUserName();
					String password = settingManager.getPassword();
					xpgWifiDevice.Login(username, password);
				}
				break;
			case DISCONNECT:
				Toast.makeText(ControlDeviceActivity.this, "设备已断开",
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			case UNBAND_FAIL:
				Toast.makeText(ControlDeviceActivity.this, "解绑失败",
						Toast.LENGTH_SHORT).show();
				break;
			case UPDATE_UI:
				if (xpgWifiDevice != null) {
					try {
						initData();
						if (page != null) {
							setContentView(page.getScrollView());
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case SETNULL:
				if (xpgWifiDevice != null) {
					xpgWifiDevice.setListener(null);
				}
				mCenter.getXPGWifiSDK().setListener(null);
				break;
			case RESP:
				String data = msg.obj.toString();
				if (!isEdit) {
					try {
						showDataInUI(data);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case LOG:
				List<XPGWifiReceiveInfo> recinfo = (List<XPGWifiReceiveInfo>) msg.obj;
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < recinfo.size(); i++) {
					sb.append(recinfo.get(i).getName() + " "
							+ recinfo.get(i).getValue() + "\r\n");
				}
				Toast.makeText(ControlDeviceActivity.this, sb.toString(),
						Toast.LENGTH_SHORT).show();

				break;
			case TOAST:
				String info = msg.obj + "";
				Toast.makeText(ControlDeviceActivity.this, info,
						Toast.LENGTH_SHORT).show();
				break;
			}

		}
	};
	private boolean isLocal = true;
	SettingManager settingManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_device);
		actionBar.setDisplayHomeAsUpEnabled(true);

		settingManager = new SettingManager(this);
		controlDevice = (ControlDevice) getIntent().getSerializableExtra(
				"device");
		mCenter.getXPGWifiSDK().setListener(gccDelegate);
		xpgWifiDevice = BaseActivity.findDeviceByMac(controlDevice.getMac(),
				controlDevice.getDid());
		if (xpgWifiDevice != null) {
			xpgWifiDevice.setListener(deviceDelegate);
		}
		isLocal = getIntent().getBooleanExtra("islocal", true);
		actionBar.setTitle(controlDevice.getName());
		String producekty = xpgWifiDevice.GetProductKey();
		if (!xpgWifiDevice.IsOnline()) {
			new AlertDialog.Builder(this).setTitle("警告")
					.setMessage("设备不在线，不可以做控制，但可以断开或解除绑定")
					.setPositiveButton("OK", null).show();
		}
		Log.i("productkey", producekty);
		try {
			initData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actionBar.setTitle(title);
		if (page != null) {
			setContentView(page.getScrollView());
		}
		Log.i("islocal", isLocal + "");

	}

	public void onResume() {
		super.onResume();
		this.mCenter.getXPGWifiSDK().setListener(gccDelegate);
		this.xpgWifiDevice.setListener(deviceDelegate);
	}

	public void onPause() {
		handler.sendEmptyMessage(SETNULL);
		super.onPause();
	}

	private void initData() throws JSONException {
		// TODO Auto-generated method stub
		String str = xpgWifiDevice.GetUI();
		Log.e("str", str);
		if (str == null || str.equals("")) {
			Toast.makeText(this, "不支持该设备", Toast.LENGTH_SHORT).show();
		}
		JSONObject uiJsonObject = new JSONObject(str);
		Log.e("uiJsonObject", uiJsonObject.toString());
		JSONObject jsonObject = uiJsonObject.has("object") ? uiJsonObject
				.getJSONObject("object") : null;
		title = uiJsonObject.has("title") ? uiJsonObject.getString("title")
				: "";
		showEditButton = (jsonObject != null && jsonObject
				.has("showEditButton")) ? jsonObject
				.getBoolean("showEditButton") : false;
		idmaps = (jsonObject != null && jsonObject.has("externalkey")) ? jsonObject
				.getJSONObject("externalkey") : null;
		JSONArray sessionJsonArrays = uiJsonObject.getJSONArray("sections");

		List<QSession> sessions = new ArrayList<QSession>();

		for (int i = 0; i < sessionJsonArrays.length(); i++) {
			JSONObject session = sessionJsonArrays.getJSONObject(i);
			JSONArray elementjJsonArray = session.getJSONArray("elements");

			List<QElement> elements = new ArrayList<QElement>();

			for (int j = 0; j < elementjJsonArray.length(); j++) {

				JSONObject elementObject = elementjJsonArray.getJSONObject(j);
				String type = elementObject.getString("type");
				String key = elementObject.has("key") ? elementObject
						.getString("key") : "";
				String title = elementObject.has("title") ? elementObject
						.getString("title") : "";
				int j_min = 0;
				int j_max = 0;
				int j_step = 1;
				int maxLength = elementObject.has("maxLength") ? elementObject
						.getInt("maxLength") : 0;
				JSONObject object = new JSONObject();
				if (elementObject.has("object")) {
					object = elementObject.getJSONObject("object");
					if (object.has("uint_spec")) {
						JSONObject unit_json = object
								.getJSONObject("uint_spec");
						j_min = unit_json.has("min") ? unit_json.getInt("min")
								: 0;
						j_max = unit_json.has("max") ? unit_json.getInt("max")
								: 0;
						j_step = unit_json.has("step") ? unit_json
								.getInt("step") : 1;
					}
				}
				String action = object.has("action") ? object
						.getString("action") : "";
				JSONArray bind = object.has("bind") ? object
						.getJSONArray("bind") : null;
				String perm = object.has("perm") ? object.getString("perm")
						: "";

				String[] strs_bind = null;

				if (bind != null) {
					strs_bind = new String[bind.length()];
					for (int k = 0; k < bind.length(); k++) {
						strs_bind[k] = bind.getString(k);
					}
				}

				QElement element = null;
				if (type.equals("QBooleanElement")) {

					JSONArray valeus = object.has("values") ? object
							.getJSONArray("values") : null;
					String[] strs_values = null;
					if (valeus != null) {
						strs_values = new String[valeus.length()];
						for (int k = 0; k < valeus.length(); k++) {
							strs_values[k] = valeus.getString(k);
						}
					}

					element = new QBooleanElement(this, key, title, strs_values);

				} else if (type.equals("QLabelElement")) {
					element = new QLableElement(this, key, title);

				} else if (type.equals("QDateTimeInlineElement")) {
					element = new QDateTimeInlineElement(this, key, title);

				} else if (type.equals("QButtonElement")) {
					element = new QButtonElement(this, key, title);

				} else if (type.equals("QRadioElement")) {
					JSONArray valeus = elementObject.has("values") ? elementObject
							.getJSONArray("values") : null;
					JSONArray items = elementObject.has("items") ? elementObject
							.getJSONArray("items") : null;

					String[] strs_values = null;
					String[] strs_items = null;

					if (items != null) {
						strs_items = new String[items.length()];
						for (int k = 0; k < items.length(); k++) {
							strs_items[k] = items.getString(k);
						}
					} else {

					}
					if (valeus != null) {
						strs_values = new String[valeus.length()];
						for (int k = 0; k < valeus.length(); k++) {
							strs_values[k] = valeus.getString(k);
						}
					} else {
						strs_values = new String[items.length()];
						for (int k = 0; k < items.length(); k++) {
							strs_values[k] = "" + k;
						}
					}
					element = new QRadioElement(this, key, title, strs_values,
							strs_items);

				} else if (type.equals("QEntryElement")) {
					element = new QEntryElement(this, key, title);
					String keyboardType = elementObject.has("keyboardType") ? elementObject
							.getString("keyboardType") : "";
					String placeholder = elementObject.has("placeholder") ? elementObject
							.getString("placeholder") : "";
					QEntryElement el = (QEntryElement) element;
					int max = j_max;
					int min = j_min;

					if (keyboardType.equals("PhonePad")
							|| keyboardType.equals("NumbersAndPunctuation")) {
						el.setInputType(InputType.TYPE_CLASS_NUMBER);
					}
					el.setMaxNumber(max);
					el.setMinNumber(min);

				} else if (type.equals("QFloatElement")) {
					element = new QFloatElement(this, key, title, j_min, j_max,
							j_step);
					((QFloatElement) element).setChangeEnable(perm
							.contains("W"));

				} else if (type.equals("QMultilineElement")) {
					element = new QMultilineElement(this, key, title, maxLength);
				} else {
					element = new QLableElement(this, key, "不支持该控件");
				}
				if (element != null) {
					element.getContentView().setEnabled(perm.contains("W"));
					element.setExtdata(action);
					element.setExtdata1(strs_bind);
					element.setExtdata2(perm);
					element.setValueChangeListener(this);
					elements.add(element);
				}

			}
			int height = getResources().getDimensionPixelSize(
					R.dimen.item_height);
			int titlesize = getResources().getDimensionPixelSize(
					R.dimen.title_size);
			QSession qsession = new QSession(this,
					session.has("title") ? session.getString("title") : "",
					elements, titlesize, height);
			sessions.add(qsession);

		}
		page = new QPage(this, sessions);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.control_device, menu);

		return true;
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
			// xpgWifiDevice.UnBindFromService(settingManager.getUserName(),
			// settingManager.getPassword());
			String uid = settingManager.getUid();
			String token = settingManager.getToken();
			String hideuid = settingManager.getHideUid();
			String hidetoken = settingManager.getHideToken();
			if (!uid.equals("") && !token.equals("")) {
				mCenter.cUnbindDevice(xpgWifiDevice, uid, token);
			} else if (!hideuid.equals("") && !hidetoken.equals("")) {
				mCenter.cUnbindDevice(xpgWifiDevice, hideuid, hidetoken);

			} else {
				Toast.makeText(this, "请重新登录", Toast.LENGTH_SHORT).show();
			}

			break;
		case android.R.id.home:
			xpgWifiDevice.Disconnect();
			finish();
			break;
		// 获取设备状态
		case R.id.action_device_status:
			try {
				mCenter.cGetStatus(xpgWifiDevice);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}

		return true;
	}

	public void onBackPressed() {
		xpgWifiDevice.Disconnect();
		super.onBackPressed();

	}

	@Override
	public void onValueChange(String value, QElement element) {
		// TODO Auto-generated method stub
		if (element instanceof QMultilineElement) {
			Intent it = new Intent();
			it.setClass(this, QMultilineActivity.class);
			it.putExtra("id", element.getId());
			String id = element.getId();
			it.putExtra("name", element.getStr_title());
			it.putExtra("mac", xpgWifiDevice.GetMacAddress());
			it.putExtra("did", xpgWifiDevice.GetDid());
			it.putExtra("maxLength",
					((QMultilineElement) element).getMaxLength());
			String action = id.substring(0, id.indexOf("."));
			if (!element.getExtdata().equals("")) {
				action = (String) element.getExtdata();
			}
			it.putExtra("action", action);
			startActivity(it);
			return;
		}
		try {
			if (isInitFinish) {
				SendJson(element);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void SendJson(QElement element) throws JSONException {
		String action = (String) element.getExtdata();
		String[] bind = (String[]) element.getExtdata1();
		final JSONObject jsonsend = new JSONObject();
		JSONObject jsonparam = new JSONObject();
		String perm = (String) element.getExtdata2();
		jsonsend.put("cmd", 1);
		// }
		if (action != null && !action.equals("")) {
			if (bind != null) {

				for (int i = 0; i < bind.length; i++) {
					String key = bind[i];
					Log.i("key", key);
					QElement e = page.findElementById(key);
					String par = key.substring((key.indexOf(".") + 1));
					jsonparam.put(par, e.getValue());
				}
				jsonsend.put(action, jsonparam);
				Log.i("sendjson", jsonsend.toString());
				mCenter.cWrite(xpgWifiDevice, jsonsend);
			} else if (element.getId() != null && !element.getId().equals("")) {
				String key = element.getId();
				String par = key.substring((key.indexOf(".") + 1));

				jsonparam.put(par, element.getValue());
				jsonsend.put(action, jsonparam);
				Log.i("sendjson", jsonsend.toString());
				mCenter.cWrite(xpgWifiDevice, jsonsend);
			}
		} else if (action.equals("")) {
			String key = element.getId();
			String par = key.substring((key.indexOf(".") + 1));

			jsonparam.put(par, element.getValue());
			action = key.substring(0, key.indexOf("."));
			jsonsend.put(action, jsonparam);
			Log.i("sendjson", jsonsend.toString());
			mCenter.cWrite(xpgWifiDevice, jsonsend);
		}
	}

	private void showDataInUI(String data) throws JSONException {
		Log.i("revjson", data);

		if (QElement.isEdit) {
			return;
		}

		if (page != null) {
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
					String value = params.getString(param);
					// 处理含有_integer_part的部分
					if (param.contains("_integer_part")) {
						param = param.replace("_integer_part", "");

						String id = action + "." + param;

						if (idmaps != null) {
							id = idmaps.has(id) ? idmaps.getString(id) : id;
						}
						QElement qElement = page.findElementById(id);
						if (qElement != null) {
							String valueString = qElement.getValue();
							if (valueString.contains(".")) {
								String substr = valueString
										.substring(valueString.indexOf(".") + 1);
								valueString = value + "." + substr;
							} else {
								valueString = value + ".00";
							}
							qElement.setValue(valueString);
						}
					}
					// 处理含有_decimal_part的部分
					else if (param.contains("_decimal_part")) {
						param = param.replace("_decimal_part", "");
						String id = action + "." + param;

						if (idmaps != null) {
							id = idmaps.has(id) ? idmaps.getString(id) : id;
						}
						QElement qElement = page.findElementById(id);
						if (qElement != null) {
							String valueString = qElement.getValue();
							if (valueString.contains(".")) {
								String substr = valueString.substring(0,
										valueString.indexOf("."));
								valueString = substr + "." + value;
							} else {
								valueString = "00." + value;
							}

							qElement.setValue(valueString);
						}
						// 同样部分的处理
					} else {

						String id = action + "." + param;

						if (idmaps != null) {
							id = idmaps.has(id) ? idmaps.getString(id) : id;
						}
						QElement qElement = page.findElementById(id);
						if (qElement != null) {
							qElement.setValue(value);
						}
					}
				}
			}

		}
	}

}
