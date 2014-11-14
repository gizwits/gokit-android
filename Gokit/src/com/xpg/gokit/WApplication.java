package com.xpg.gokit;

import android.app.Application;

import com.xtremeprog.xpgconnect.XPGWifiConfig;
import com.xtremeprog.xpgconnect.XPGWifiLogLevel;
import com.xtremeprog.xpgconnect.XPGWifiSDK;


public class WApplication extends Application {
	public void onCreate() {
		super.onCreate();
		// 设定AppID，参数为机智云官网中查看产品信息得到的AppID
		XPGWifiConfig.sharedInstance().SetAppID(
				"42a7563f305342ae805cbb21d968a0ce");
		// 设定是否为debug版本
		XPGWifiConfig.sharedInstance().SetDebug(BuildConfig.DEBUG);
		// 设置设备json下载路径（该路径必须存在，开发者自行创建文件夹,最好是手机内存）,sdk根据下载的json文件解析出相关的设备描述等信息
		XPGWifiConfig.sharedInstance().SetProductPath(
				this.getFilesDir() + "/Devices");
		// 指定该app对应设备的product_key，如果设定了过滤，会过滤出该peoduct_key对应的设备
		XPGWifiConfig.sharedInstance().RegisterProductKey(
				"6f3074fe43894547a4f1314bd7e3ae0b");
		// 是否过滤指定 PRODUCT_KEY 的设备，NO 表示不过滤，会列出所有发现到的设备
		XPGWifiConfig.sharedInstance().EnableProductFilter(true);
		// 设定日志打印级别
		XPGWifiSDK.SetLogLevel(XPGWifiLogLevel.XPGWifiLogLevelAll);
		// 设定是否在后台输出收发包二进制数据
		XPGWifiSDK.SetPrintDataLevel(true);
	}
}
