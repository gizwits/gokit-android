package com.xpg.ui;

import com.xpg.ui.listener.ValueChangeListener;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public abstract class QElement {
	public static boolean isEdit = false;

	/**
	 * button 类型
	 */
	public static final int TYPE_BUTTON = 1;
	/**
	 * 日期类型
	 */ 
	public static final int TYPE_DATETIME = 2;
	/**
	 * on off 类型
	 */
	public static final int TYPE_BOOLEAN = 3;
	/**
	 * 标签类型
	 */
	public static final int TYPE_LABLE = 4;
	/**
	 * 选择类型（单选）
	 */
	public static final int TYPE_RADIO = 5;
	/**
	 * 文本输入
	 */
	public static final int TYPE_INPUT_TEXT = 6;
	/**
	 * 
	 */
	public static final int TYPE_FLOAT = 7;
	/**
	 * 
	 */
	public static final int TYPE_MULTILINE = 8;

	protected static final int VALUE = 0;
	protected Context context;
	/**
	 * id Element的ID
	 */
	protected String id;
	protected ValueChangeListener listener;
	
	protected TextView tv_title;
	protected String str_title;
	protected Object extdata;
	protected Object extdata1;
	protected Object extdata2;
	
	protected String[]keys;
	protected String[]values;
	/**
	 * 控件类型
	 */
	protected int type;
	public Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case VALUE:
				if(listener!=null){
					QElement element = (QElement)msg.obj;
					listener.onValueChange(element.getValue(), element);
				}
				break;
			}
		};
	};
	
	public QElement(Context c,String id,String title,int type){
		this.context = c;
		this.id = id;
		this.type = type;
		this.str_title = title;
		initTitle();
	}
	
	
	
	
	
	/**
	 * 控件的值
	 * @return
	 */
	public abstract String getValue();
	/**
	 * 设置控件的值
	 * @param value
	 */
	public abstract View getContentView();
	public  void initTitle(){
		
		tv_title = new TextView(context);
		tv_title.setText(str_title);
		
	}
	public abstract void setValue(String value);
	public  String getId(){
		return this.id;
	}
	public  void setId(String id) {
		this.id = id;
	}
	public  int getType() {
		return this.getType();
	}
	public  void setType(int type) {
		this.type = type;
	}
	public  TextView getTitleView(){
		return this.tv_title;
	}
	
	public  void setValueChangeListener(ValueChangeListener listener){
		this.listener = listener;
	}

	public String getStr_title() {
		return str_title;
	}

	public void setStr_title(String str_title) {
		this.str_title = str_title;
	}
	public Object getExtdata() {
		return extdata;
	}

	public void setExtdata(Object extdata) {
		this.extdata = extdata;
	}





	public Object getExtdata1() {
		return extdata1;
	}





	public void setExtdata1(Object extdata1) {
		this.extdata1 = extdata1;
	}





	public Object getExtdata2() {
		return extdata2;
	}





	public void setExtdata2(Object extdata2) {
		this.extdata2 = extdata2;
	}
	
	
	
	

}
