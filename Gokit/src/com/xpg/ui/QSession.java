package com.xpg.ui;

import java.util.HashMap;
import java.util.List;

import android.R;
import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.renderscript.Element;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.GridLayout.LayoutParams;

public class QSession {
	private TextView tv_title;
	private TableLayout tableLayout;
	private Context c;
	private String str_title;
	List<QElement>elements;
	private int Height = 0;
	private HashMap<String, QElement>map = new HashMap<String, QElement>();
	public  QSession(Context c,String title) {
		tv_title = new TextView(c);
		tv_title.setText(title);
		tableLayout = new TableLayout(c);
		this.c = c;
		this.str_title = title;
	}
	public QSession(Context c,String title,List<QElement>elements){
		
		tv_title = new TextView(c);
		tv_title.setText(title);
		tv_title.setTextSize(25);
		
		tableLayout = new TableLayout(c);
		this.c = c;
		this.str_title = title;
		tableLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
		this.elements = elements;
		for(QElement element : elements){
			map.put(element.getId(), element);
			TableRow tr = new TableRow(c);
			tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
			TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
			params.weight = 1;
			
			//设置标题的Layout
			RelativeLayout title_layout = new RelativeLayout(c);
			title_layout.setLayoutParams(params);
			RelativeLayout.LayoutParams rl_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			rl_param.alignWithParent = true;
			rl_param.addRule(RelativeLayout.ALIGN_LEFT);
		
			element.getTitleView().setLayoutParams(rl_param);
			title_layout.addView(element.getTitleView());
			tr.addView(title_layout);
			
			
			
			
			
			
			//设置内容Layout 
			RelativeLayout content_layout = new RelativeLayout(c);
			content_layout.setLayoutParams(params);
			 rl_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			rl_param.alignWithParent = true;
			rl_param.addRule(RelativeLayout.ALIGN_RIGHT);
			element.getContentView().setLayoutParams(rl_param);
			content_layout.addView(element.getContentView());
			tr.addView(content_layout);
			tableLayout.addView(tr);
			
			//TableRow 下面的黑线
			tr = new TableRow(c);
			tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
			tr.setBackgroundColor(Color.BLACK);
			RelativeLayout line_layout = new RelativeLayout(c);
			line_layout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,1));
			line_layout.setBackgroundColor(Color.BLACK);
			tr.addView(line_layout);
			tableLayout.addView(tr);
		}
		
		
	}
	public QSession(Context c,String title,List<QElement>elements,int titleSize,int Itemheight){
		
		tv_title = new TextView(c);
		tv_title.setText(title);
		tv_title.setTextSize(titleSize);
		Height = Itemheight;
		
		tableLayout = new TableLayout(c);
		this.c = c;
		this.str_title = title;
		tableLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
		this.elements = elements;
		for(QElement element : elements){
			map.put(element.getId(), element);
			TableRow tr = new TableRow(c);
			tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
			TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,Height);
			params.weight = 1;
			
			if(element instanceof QButtonElement){
				//设置标题的Layout
				RelativeLayout all_layout = new RelativeLayout(c);
				all_layout.setLayoutParams(params);
				RelativeLayout.LayoutParams rl_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
				element.getContentView().setLayoutParams(rl_param);
				
				 rl_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				rl_param.alignWithParent = true;
				rl_param.addRule(RelativeLayout.ALIGN_LEFT);
				rl_param.addRule(RelativeLayout.CENTER_VERTICAL);
				rl_param.leftMargin = 20;
				element.getTitleView().setLayoutParams(rl_param);
				
				all_layout.addView(element.getContentView());
				all_layout.addView(element.getTitleView());
				
				tr.addView(all_layout);
				tableLayout.addView(tr);
				
				//TableRow 下面的黑线
				tr = new TableRow(c);
				tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
				tr.setBackgroundColor(Color.BLACK);
				RelativeLayout line_layout = new RelativeLayout(c);
				line_layout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,1));
				line_layout.setBackgroundColor(Color.BLACK);
				tr.addView(line_layout);
				tableLayout.addView(tr);
			}else if(element instanceof QMultilineElement){
				QMultilineElement qmuele = (QMultilineElement)element;
				//设置标题的Layout
				RelativeLayout all_layout = new RelativeLayout(c);
				all_layout.setLayoutParams(params);
				RelativeLayout.LayoutParams rl_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
				element.getContentView().setLayoutParams(rl_param);
				
				 rl_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				rl_param.alignWithParent = true;
				rl_param.addRule(RelativeLayout.ALIGN_LEFT);
				rl_param.addRule(RelativeLayout.CENTER_VERTICAL);
				rl_param.setMargins(20, 0, 0, 0);
				element.getTitleView().setLayoutParams(rl_param);
				
				RelativeLayout.LayoutParams rl_param1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				rl_param1.alignWithParent = true;
				rl_param1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rl_param1.addRule(RelativeLayout.CENTER_VERTICAL);
				rl_param1.setMargins(0, 0, 20, 0);
				
				qmuele.getInfoView().setLayoutParams(rl_param1);
				
				all_layout.addView(element.getContentView());
				all_layout.addView(element.getTitleView());
				all_layout.addView(qmuele.getInfoView());
				
				tr.addView(all_layout);
				tableLayout.addView(tr);
				
				//TableRow 下面的黑线
				tr = new TableRow(c);
				tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
				tr.setBackgroundColor(Color.BLACK);
				RelativeLayout line_layout = new RelativeLayout(c);
				line_layout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,1));
				line_layout.setBackgroundColor(Color.BLACK);
				tr.addView(line_layout);
				tableLayout.addView(tr);
			}else if(element instanceof QFloatElement){
				params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,(Height/3*2));
				params.weight = 1;
				QFloatElement floatElement = (QFloatElement)element;
				//设置标题的Layout
				RelativeLayout title_layout = new RelativeLayout(c);
				title_layout.setLayoutParams(params);
				RelativeLayout.LayoutParams rl_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				rl_param.alignWithParent = true;
				rl_param.addRule(RelativeLayout.ALIGN_LEFT);
				rl_param.addRule(RelativeLayout.CENTER_VERTICAL);
				rl_param.leftMargin = 20;
				element.getTitleView().setLayoutParams(rl_param);
				title_layout.addView(element.getTitleView());
				tr.addView(title_layout);
				
				//设置内容Layout 
				RelativeLayout content_layout = new RelativeLayout(c);
				content_layout.setLayoutParams(params);
				 rl_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				rl_param.alignWithParent = true;
				rl_param.addRule(RelativeLayout.ALIGN_RIGHT);
				rl_param.addRule(RelativeLayout.CENTER_VERTICAL);
				rl_param.rightMargin = 20;
				floatElement.getValueView().setLayoutParams(rl_param);
				content_layout.addView(floatElement.getValueView());
				tr.addView(content_layout);
				tableLayout.addView(tr);
				
				//seekbar
				tr = new TableRow(c);
				tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
				RelativeLayout seekbar_layout = new RelativeLayout(c);
				seekbar_layout.setLayoutParams(params);
				 rl_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
				 rl_param.setMargins(100, 0, 100, 0);
				floatElement.getContentView().setLayoutParams(rl_param);
				
				RelativeLayout.LayoutParams add_param = new RelativeLayout.LayoutParams(Height/3,Height/3);
				add_param.alignWithParent = true;
				add_param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				add_param.addRule(RelativeLayout.CENTER_VERTICAL);
				add_param.setMargins(0, 0, 30, 0);
				floatElement.getAddBtn().setLayoutParams(add_param);
				
				RelativeLayout.LayoutParams sub_param = new RelativeLayout.LayoutParams(Height/3,Height/3);
				sub_param.alignWithParent = true;
				sub_param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				sub_param.addRule(RelativeLayout.CENTER_VERTICAL);
				sub_param.setMargins(30, 0, 0, 0);
				floatElement.getSubBtn().setLayoutParams(sub_param);
				
				seekbar_layout.addView(floatElement.getSubBtn());
				seekbar_layout.addView(floatElement.getContentView());
				seekbar_layout.addView(floatElement.getAddBtn());
				tr.addView(seekbar_layout);
				tableLayout.addView(tr);
				
				//TableRow 下面的黑线
				tr = new TableRow(c);
				tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
				tr.setBackgroundColor(Color.BLACK);
				RelativeLayout line_layout = new RelativeLayout(c);
				line_layout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,1));
				line_layout.setBackgroundColor(Color.BLACK);
				tr.addView(line_layout);
				tableLayout.addView(tr);
			}
			
			else {
			
				//设置标题的Layout
				RelativeLayout title_layout = new RelativeLayout(c);
				title_layout.setLayoutParams(params);
				RelativeLayout.LayoutParams rl_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				rl_param.alignWithParent = true;
				rl_param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rl_param.addRule(RelativeLayout.CENTER_VERTICAL);
				rl_param.leftMargin = 20;
				element.getTitleView().setLayoutParams(rl_param);
				title_layout.addView(element.getTitleView());
				tr.addView(title_layout);
				
				//设置内容Layout 
				RelativeLayout content_layout = new RelativeLayout(c);
				content_layout.setLayoutParams(params);
				 rl_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				rl_param.alignWithParent = true;
				rl_param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rl_param.addRule(RelativeLayout.CENTER_VERTICAL);
				rl_param.rightMargin = 20;
				element.getContentView().setLayoutParams(rl_param);
				content_layout.addView(element.getContentView());
				tr.addView(content_layout);
				tableLayout.addView(tr);
				
				//TableRow 下面的黑线
				tr = new TableRow(c);
				tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
				tr.setBackgroundColor(Color.BLACK);
				RelativeLayout line_layout = new RelativeLayout(c);
				line_layout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,1));
				line_layout.setBackgroundColor(Color.BLACK);
				tr.addView(line_layout);
				tableLayout.addView(tr);
			}
		}
		
		
	}

	public TableLayout getTableLayout(){
		return this.tableLayout;
	}
	public TextView getTitleView() {
		return this.tv_title;
	}
	public QElement findElementById(String id){
		QElement result = null;
		result = map.get(id);
//		for(QElement element: elements){
//			if(element.getId().equals(id)){
//				result = element;
//				break;
//			}
//		}
		return result;
	}
	
}
