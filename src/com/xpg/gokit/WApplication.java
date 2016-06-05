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
import com.xtremeprog.xpgconnect.XPGWifiSDK;

/**
 * The Class WApplication.
 */
public class WApplication extends Application {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	public void onCreate() {
		super.onCreate();

		try {
			// 复制assert文件夹中的json文件到设备安装目录。json文件是解析数据点必备的文件，sdk根据该文件，把二进制数据转换为json字段并返回。
			AssertsUtils.copyAllAssertToCacheFolder(this.getApplicationContext());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 启动SDK
		XPGWifiSDK.sharedInstance().startWithAppID(getApplicationContext(), "your_app_id");
		// "6f3074fe43894547a4f1314bd7e3ae0b");
		// 设定日志打印级别
		XPGWifiSDK.sharedInstance().setLogLevel(XPGWifiSDK.XPGWifiLogLevel.XPGWifiLogLevelAll, "GoKitDemo.log", true);
	}
}
