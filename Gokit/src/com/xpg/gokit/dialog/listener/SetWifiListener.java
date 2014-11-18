/**
 * Project Name:Gokit
 * File Name:SetWifiListener.java
 * Package Name:com.xpg.gokit.dialog.listener
 * Date:2014-11-18 10:05:48
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
package com.xpg.gokit.dialog.listener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving setWifi events.
 * The class that is interested in processing a setWifi
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSetWifiListener<code> method. When
 * the setWifi event occurs, that object's appropriate
 * method is invoked.
 *
 * @see SetWifiEvent
 */
public interface SetWifiListener {
	
	/**
	 * Sets the.
	 *
	 * @param ssid the ssid
	 * @param psw the psw
	 */
	public void set(String ssid,String psw);
}
