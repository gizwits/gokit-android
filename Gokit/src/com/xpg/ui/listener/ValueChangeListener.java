package com.xpg.ui.listener;

import com.xpg.ui.QElement;

public interface ValueChangeListener {
	/**
	 * 控件值改变时 的回调
	 * @param value
	 * @param Qtype
	 */
	public void onValueChange(String value ,QElement element);
}
