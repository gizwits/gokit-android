package com.xpg.gokit.bean;

import android.graphics.Color;

public class DeviceLog {
	
	private String text ="";
	private String tag ="";
	private String source ="";
	private int textcolor = Color.RED;
	private int backgroundcolor = Color.LTGRAY;
	private int level ;
	public DeviceLog(){}
	/**
	 * 
	 * @param text
	 * @param source
	 * @param tag
	 * @param level
	 */
	public DeviceLog(String text,String source ,String tag,int level){
		this.text = text;
		this.source = source;
		this.tag = tag;
		this.level = level;
		switch(level){
		case 1:
			textcolor = Color.RED;
			break;
		case 2:
			textcolor = Color.argb(255, 255, 77, 0);
			break;
		case 3:
			textcolor = Color.argb(255, 0, 77, 0);
			break;
		default:
			textcolor = Color.RED;
			break;
		}
		
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getTextcolor() {
		return textcolor;
	}
	public void setTextcolor(int textcolor) {
		this.textcolor = textcolor;
	}
	public int getBackgroundcolor() {
		return backgroundcolor;
	}
	public void setBackgroundcolor(int backgroundcolor) {
		this.backgroundcolor = backgroundcolor;
	}
	
}
