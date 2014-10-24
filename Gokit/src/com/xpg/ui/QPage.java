package com.xpg.ui;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


public class QPage {
	String pageTitle;
	List<QSession>sessions;
	ScrollView scrollView;
	LinearLayout ll_layout;
	Context c ;
	public QPage(Context c){
		this.c = c;
		scrollView = new ScrollView(c);
		ll_layout = new LinearLayout(c);
		ll_layout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(ll_layout);
		
	}
	public QPage(Context c,List<QSession>sessions){
		this.c = c;
		this.sessions = sessions;
		scrollView = new ScrollView(c);
		
		ll_layout = new LinearLayout(c);
		ll_layout.setOrientation(LinearLayout.VERTICAL);
		ll_layout.setBackgroundColor(Color.argb(60, 211, 211, 211));
		scrollView.addView(ll_layout);
		
		for(QSession session:sessions){
			RelativeLayout rl_lLayout = new RelativeLayout(c);
			
			rl_lLayout.addView(session.getTitleView());
			RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams)session.getTitleView().getLayoutParams();
			p.addRule(RelativeLayout.ALIGN_LEFT);
			p.addRule(RelativeLayout.CENTER_VERTICAL);
			p.setMargins(20, 20, 20, 20);
			session.getTitleView().setLayoutParams(p);
			
			ll_layout.addView(rl_lLayout);
			
			
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)session.getTableLayout().getLayoutParams();
//			lp.setMargins(20, 20, 20, 20);
			session.getTableLayout().setLayoutParams(lp);
			session.getTableLayout().setBackgroundColor(Color.WHITE);
			ll_layout.addView(session.getTableLayout());
			
		}
	}
	public ScrollView getScrollView(){
		return scrollView;
	}
	public QElement findElementById(String id){
		QElement result = null;
		for(QSession session:sessions){
			QElement qElement = session.findElementById(id);
			if(qElement!= null){
				result = qElement;
				break;
			}
		}
		return result;
	}
}
