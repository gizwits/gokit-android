/**
 * Project Name:Gokit
 * File Name:WApplication.java
 * Package Name:com.xpg.gokit
 * Date:2014-11-18 10:06:43
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
package com.xpg.gokit;

import java.io.IOException;

import android.app.Application;

import com.xpg.gokit.utils.AssertsUtils;
import com.xtremeprog.xpgconnect.XPGWifiConfig;
import com.xtremeprog.xpgconnect.XPGWifiLogLevel;
import com.xtremeprog.xpgconnect.XPGWifiSDK;


/**
 * The Class WApplication.
 */
public class WApplication extends Application {
	
	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	public void onCreate() {
		super.onCreate();
		
		try {
			//复制assert文件夹中的json文件到设备安装目录。json文件是解析数据点必备的文件，sdk根据该文件，把二进制数据转换为json字段并返回。
			AssertsUtils.copyAllAssertToCacheFolder(this.getApplicationContext());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
