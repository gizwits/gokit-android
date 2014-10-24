package com.xpg.gokit.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.xpg.ui.QElement;
import com.xpg.gokit.R;
import com.xpg.gokit.R.id;
import com.xpg.gokit.R.layout;
import com.xpg.gokit.R.menu;
import com.xpg.gokit.utils.Base64;
import com.xpg.gokit.utils.ByteUtils;
import com.xpg.gokit.utils.CRCUtils;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiDeviceList;
import com.xtremeprog.xpgconnect.XPGWifiDeviceListener;
import com.xtremeprog.xpgconnect.XPGWifiReceiveInfo;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class QMultilineActivity extends BaseActivity {
	protected static final int RESP = 0;
	private EditText edt_info;
	private TextView tv_name;
	String name = "";
	String id = "";
	String did = "";
	String mac = "";
	String action = "";
	int maxLength = 0;
	JSONObject idmaps;
	XPGWifiDevice xpgWifiDevice;
	XPGWifiDeviceListener deviceDelegate = new XPGWifiDeviceListener() {
		public void onBindDevice(int error, String errorMessage) {};
		@Override
		public void onUnbindDevice(int error, String errorMessage) {
			// TODO Auto-generated method stub
		}
		public void onQueryHardwareInfo(int error, com.xtremeprog.xpgconnect.XPGWifiQueryHardwareInfoStruct pInfo) {};
		public void onReceiveAlertsAndFaultsInfo(com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo alerts, com.xtremeprog.xpgconnect.Vector_XPGWifiReceiveInfo faults) {
			
		};
		public void onUpdateUI() {
		};
		public void onGetPasscode(int result) {};
		public void onDeviceLog(short nLevel, String tag, String source, String content) {};
		public void onSetSwitcher(int result) {};
		
		public void onDeviceOnline(boolean isOnline) {};
		public void onConnectFailed() {};
		public void onLogin(int result) {
		};
		
		public void onLoginMQTT(int result ){
		}
		public boolean onReceiveData(String data) {
			Log.i("info", data);
//			isInitFinish = false;
			Message msg = new Message();
			msg.obj = data;
			msg.what = RESP;
			handler.sendMessage(msg);
			
				
				

			
			return true;
		};
		public void onConnected(){
			
		}
		
		public void onDisconnected(){
		}
	};
	XPGWifiSDKListener gccDelegate = new XPGWifiSDKListener() {
		public void onDiscovered(int result,XPGWifiDeviceList devices) {
			Log.d("Main", "Device count:" + devices.GetCount());
			
		};
		public void onChangeUserEmail(int error, String errorMessage) {};
		public void onChangeUserPassword(int error, String errorMessage) {};
		public void onChangeUserPhone(int error, String errorMessage) {};
		public void onTransUser(int error, String errorMessage) {};
		public void onUserLogout(int error, String errorMessage) {};
		public void onRequestSendVerifyCode(int error, String errorMessage) {};
		public void onBindDevice(int error, String errorMessage) {};
		public void onRegisterUser(int error, String errorMessage, String uid, String token) {};
		public void onUnbindDevice(int error, String errorMessage) {};
		public void onUserLogin(int error, String errorMessage, String uid, String token) {};
		public void onGetDeviceInfo(int error, String errorMessage, String productKey, String did, String mac, String passCode, String host, int port, int isOnline) {};
		public void onUpdateProduct(int result) {
//			if(result== 0){
//				Message msg = new Message();
//				msg.what = UPDATE_UI;
//				handler.sendMessage(msg);
//			}
		};
		public long onCalculateCRC(byte[] data) {
			return CRCUtils.CalculateCRC(xpgWifiDevice.GetProductKey(),data);
		};
		public void onGetSSIDList(com.xtremeprog.xpgconnect.XPGWifiSSIDList list, int result) {};
		public void onSetAirLink(XPGWifiDevice device) {};
		
	};
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
//			switch(msg.what){
//			case RESP:
//				try {
//					showDataInUI((String)msg.obj);
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				break;
//			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qmultiline);
		Intent it = getIntent();
		id = it.getStringExtra("id");
		name = it.getStringExtra("name");
		did = it.getStringExtra("did");
		mac = it.getStringExtra("mac");
		action = it.getStringExtra("action");
		maxLength = it.getIntExtra("maxLength", 0);
		xpgWifiDevice = BaseActivity.findDeviceByMac(mac,did);
		this.mCenter.getXPGWifiSDK().setListener(gccDelegate);
		if(xpgWifiDevice!=null){
			try{
			String str = xpgWifiDevice.GetUI();
			JSONObject uiJsonObject = new JSONObject(str);
			JSONObject jsonObject = uiJsonObject.has("object")?uiJsonObject.getJSONObject("object"):null;
			idmaps = (jsonObject!=null&&jsonObject.has("externalkey"))?jsonObject.getJSONObject("externalkey"):null;
			}catch(Exception e){
				e.printStackTrace();
			}
			xpgWifiDevice.setListener( deviceDelegate);
		}
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		edt_info = (EditText)findViewById(R.id.edt_info);
		
		tv_name = (TextView)findViewById(R.id.tv_name);
		tv_name.setText(name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qmultiline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int menuid = item.getItemId();
		
		String sendvalue = edt_info.getText().toString();
		int length = sendvalue.length();
		if(length>=maxLength){
			sendvalue = sendvalue.substring(0,maxLength);
			edt_info.setText(sendvalue);
		}else{
			int addZero = maxLength-length;
			StringBuilder sb = new StringBuilder();
			for(int i = 0;i<addZero;i++){
				sb.append("0");
			}
			sendvalue = sendvalue+sb.toString();
			edt_info.setText(sendvalue);
		}
		
		
		if (menuid == R.id.action_send) {
			try{
				sendCMD(sendvalue);
			}catch(Exception e){
				e.printStackTrace();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void sendCMD(String sendvalue) throws JSONException {
		byte []input = ByteUtils.StringToBytes(sendvalue);
		String value = new String(Base64.encode(input));
		Log.i("base64encodevalue", value);
		final JSONObject jsonsend = new JSONObject();
		JSONObject jsonparam = new JSONObject();
		jsonsend.put("cmd", 1);
		String par = this.id.substring((this.id.indexOf(".")+1));
		jsonparam.put(par, value);
		jsonsend.put(action, jsonparam);
		xpgWifiDevice.write(jsonsend.toString());		
	}
	private  void showDataInUI(String data) throws JSONException, UnsupportedEncodingException {
		Log.i("revjson", data);
		
		
		JSONObject receive = new JSONObject(data);
		Iterator actions = receive.keys();
		while(actions.hasNext()){
			String action = actions.next().toString();
			//忽略特殊部分
			if(action.equals("cmd")||action.equals("qos")||action.equals("seq")||action.equals("version")){
				continue;
			}
			JSONObject params = receive.getJSONObject(action);
			Iterator it_params = params.keys();
			while(it_params.hasNext()){
				String param = it_params.next().toString();
				String value = params.getString(param);
					String id = action+"."+param;
					
					if(idmaps!=null){
						id = idmaps.has(id)?idmaps.getString(id):id;
					}
					if(id.equals(this.id)){
						byte[]arr = Base64.decode(value);
						String strinfo = ByteUtils.BytesToString(arr);
						edt_info.setText(strinfo);
						Log.i("value", value);
//						edt_info.setText(value);
					}
				}
		}
		
	}
}
