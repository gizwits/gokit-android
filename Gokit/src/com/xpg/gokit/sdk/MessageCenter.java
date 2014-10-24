package com.xpg.gokit.sdk;

import java.io.IOException;
import android.content.Context;
import android.util.Log;

import com.xpg.gokit.BuildConfig;
import com.xpg.gokit.setting.SettingManager;
import com.xpg.gokit.utils.FileUitls;
import com.xtremeprog.xpgconnect.XPGWifiConfig;
import com.xtremeprog.xpgconnect.XPGWifiLogLevel;
import com.xtremeprog.xpgconnect.XPGWifiSDK;

public class MessageCenter {
	private static XPGWifiSDK xpgWifiGCC;
	private static MessageCenter mCenter;
	private static SettingManager setmManager;

	private MessageCenter(Context c) {
		// TODO Auto-generated constructor stub
		if (mCenter == null) {
			init(c);
		}
	}

	private void init(Context c) {
		// TODO Auto-generated method stub

		setmManager = new SettingManager(c);
		XPGWifiConfig.sharedInstance().SetDebug(BuildConfig.DEBUG);
		String file[] = c.getFilesDir().list();

		// try {
		// String tt =
		// FileUitls.readFileAsText(c.getFilesDir()+"/XPGWifiConfig.json");
		// Log.i("info", tt);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// XPGWifiConfig.sharedInstance().SetConfigureFile(c.getFilesDir()+"/XPGWifiConfig.json");
		XPGWifiConfig.sharedInstance().SetProductPath(
				c.getFilesDir() + "/Devices");
		// 切换为Debug服务器
		XPGWifiConfig.sharedInstance().EnableProductFilter(false);
		xpgWifiGCC = XPGWifiSDK.sharedInstance();
		XPGWifiConfig.sharedInstance().SetSwitchService(false);
		xpgWifiGCC.SetAppID("7ac10dec7dba436785ac23949536a6eb");

		XPGWifiSDK.SetLogLevel(XPGWifiLogLevel.XPGWifiLogLevelAll);
		XPGWifiSDK.SetPrintDataLevel(true);
	}

	public static MessageCenter getInstance(Context c) {
		if (mCenter != null) {
			return mCenter;
		}
		mCenter = new MessageCenter(c);
		return mCenter;
	}

	public XPGWifiSDK getXPGWifiSDK() {
		return xpgWifiGCC;
	}

}