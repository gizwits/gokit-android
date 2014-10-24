package com.xpg.ui;

import java.util.ArrayList;
import java.util.List;

import com.xpg.ui.listener.ValueChangeListener;
import com.xpg.gokit.R;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class QTestActivity extends Activity implements ValueChangeListener {
	private QPage page;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		List<QElement>elements = new ArrayList<QElement>();
		QButtonElement buttonElement = new QButtonElement(this, "stat.btn", "发送");
		buttonElement.setValueChangeListener(this);
		QBooleanElement booleanElement = new QBooleanElement(this, "status.switch", "开关", null);
		booleanElement.setValueChangeListener(this);
		QRadioElement qRadioElement = new QRadioElement(this, "status.select", "看下", new String[]{"123","345","5467"}, new String[]{"0","1","2"});
		qRadioElement.setValueChangeListener(this);
		elements.add(buttonElement);
		elements.add(qRadioElement);
		elements.add(booleanElement);
		
		List<QElement>elements1 = new ArrayList<QElement>();
		QButtonElement buttonElement1 = new QButtonElement(this, "stat.btn", "发送");
		buttonElement1.setValueChangeListener(this);
		QBooleanElement booleanElement1 = new QBooleanElement(this, "status.switch", "开关", null);
		booleanElement1.setValueChangeListener(this);
		QRadioElement qRadioElement1 = new QRadioElement(this, "status.select", "看下", new String[]{"123","345","5467"}, new String[]{"0","1","2"});
		qRadioElement1.setValueChangeListener(this);
		elements1.add(buttonElement1);
		elements1.add(qRadioElement1);
		elements1.add(booleanElement1);
		
		
		List<QElement>elements2 = new ArrayList<QElement>();
		QButtonElement buttonElement2 = new QButtonElement(this, "stat.btn", "发送");
		buttonElement2.setValueChangeListener(this);
		QBooleanElement booleanElement2 = new QBooleanElement(this, "status.switch", "开关", null);
		booleanElement2.setValueChangeListener(this);
		QRadioElement qRadioElement2 = new QRadioElement(this, "status.select", "看下", new String[]{"123","345","5467"}, new String[]{"0","1","2"});
		qRadioElement2.setValueChangeListener(this);
		elements2.add(buttonElement2);
		elements2.add(qRadioElement2);
		elements2.add(booleanElement2);
		
		
		
		List<QSession>sessions = new ArrayList<QSession>();
		QSession session = new QSession(this, "title",elements);
		QSession session1 = new QSession(this, "title",elements1);
		QSession session2 = new QSession(this,"sdfa",elements2);
		sessions.add(session);
		sessions.add(session1);
		sessions.add(session2);
		page = new QPage(this, sessions);
		setContentView(page.getScrollView());
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qtest, menu);
		return true;
	}

	@Override
	public void onValueChange(String value, QElement element) {
		// TODO Auto-generated method stub
		Log.i("value", value+" "+element.getId());
		if(element instanceof QButtonElement){
			QElement e = page.findElementById("status.switch");
			if(e!=null){
				e.setValue("1");
			}
		}
	}

}
