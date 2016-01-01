/**
 * Project Name:Gokit
 * File Name:AssertsUtils.java
 * Package Name:com.xpg.gokit.utils
 * Date:2014-11-21 15:57:30
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
package com.xpg.gokit.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

/**
 * ClassName: AssertsUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014年11月21日 下午3:57:15 <br/>
 *
 * @author Lien
 * @version
 */
public class AssertsUtils {
	static public String getTextByName(Context c, String name) {
		String result = "";
		try {
			InputStream in = c.getResources().getAssets().open(name);
			BufferedReader brReader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = brReader.readLine()) != null) {
				result += line;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 从assert中复制出文件到某个文件
	 * 
	 * @param c
	 * @param orifile
	 * @param desfile
	 * @return
	 * @throws IOException
	 */
	static public boolean copyFileTo(Context c, String orifile, String desfile) throws IOException {
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(desfile);
		myInput = c.getAssets().open(orifile);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();

		return true;
	}

	/**
	 * 复制assert中的配置文件到app安装目录
	 */
	static public boolean copyAllAssertToCacheFolder(Context c) throws IOException {

		String[] files = c.getAssets().list("Devices");
		String filefolder = c.getFilesDir().toString();
		File devicefile = new File(filefolder + "/Devices/");
		devicefile.mkdirs();

		for (int i = 0; i < files.length; i++) {
			File devfile = new File(filefolder + "/Devices/" + files[i]);
			if (!devfile.exists()) {
				copyFileTo(c, "Devices/" + files[i], filefolder + "/Devices/" + files[i]);
			}
		}
		String[] filestr = devicefile.list();
		for (int i = 0; i < filestr.length; i++) {
			Log.i("file", filestr[i]);
		}

		return true;
	}
}
