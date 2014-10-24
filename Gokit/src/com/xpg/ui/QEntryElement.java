package com.xpg.ui;

import android.content.Context;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class QEntryElement extends QElement{
	private EditText editText;
	private TextWatcher tw = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			String str = editText.getText().toString();
			Log.i("values", str);
			if(maxnumber!=minNumber&&!str.trim().equals("")&&!str.trim().equals("-")){
				int number = Integer.parseInt(str.trim());
				if(number>maxnumber){
					editText.setText(maxnumber+"");
				}
				if(number<minNumber){
					editText.setText(minNumber+"");
				}
				
			}
			if(maxlenght!=-1&&str.length()>maxlenght){
				String substr = str.substring(0,maxlenght);
				editText.setText(substr);
			}

		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	/**
	 * 
	 * @param c
	 * @param id
	 * @param title
	 * @param maxnumber 设置 max number   
	 * @param minnumber 设置为min number
	 * @param maxlength 设置 max lenght 表示只能输入 的最长长度  -1表示不做限制。
	 */
	private int maxnumber = 0;
	private int minNumber = 0;
	private int maxlenght = -1;
	public QEntryElement(Context c, String id, String title) {
		super(c, id, title, QElement.TYPE_INPUT_TEXT);
		// TODO Auto-generated constructor stub
		editText = new EditText(c);
		editText.addTextChangedListener(tw);
		editText.setSingleLine();
		editText.setImeOptions(EditorInfo.IME_ACTION_SEND);
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {  
		       public boolean onEditorAction(TextView v, int actionId,    
		               KeyEvent event)  {    
		           if (actionId==EditorInfo.IME_ACTION_SEND  
		                ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {    
		            //do something;  
		        	   Message msg = new Message();
		       		msg.what = VALUE;
		       		msg.obj = QEntryElement.this;
		       		handler.sendMessage(msg);
//		        	  listener.onValueChange(QEntryElement.this.getValue(), QEntryElement.this);
						
		            return true;  
		           }    
		           return false;    
		       }    
		   }); 
	}
	/**
	 * 
	 * @param inputtype InputType.TYPE_CLASS_NUMBER
	 */
	public void setInputType(int inputtype){
		editText.setInputType(inputtype|InputType.TYPE_NUMBER_FLAG_SIGNED);
	}
	public void setMaxLength(int maxlength){
		this.maxlenght = maxlength;
	}
	public void setMinNumber(int minnumber){
		this.minNumber = minnumber;
		editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_CLASS_NUMBER);
	}
	public void setMaxNumber(int maxnumber){
		this.maxnumber = maxnumber;
		editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_CLASS_NUMBER);
		
	}
	public void setHints(String hints){
		this.editText.setHint(hints);
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		String value = editText.getText().toString().trim();
		if(!value.equals("")&&!value.equals("-")){
			return editText.getText().toString().trim();
		}else{
			return "0";
		}
	}

	@Override
	public View getContentView() {
		// TODO Auto-generated method stub
		return editText;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		editText.setText(value);
	}

}
