package com.kit.widget.expandablelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

import com.kit.utils.KeyboardUtils;

/**
 * @author Joey.Zhao Email: laozhao1005@gmail.com
 * 
 * @date 2014年4月3日
 */
public class OnlyOneGroupExpandExpandableListView extends ExpandableListView {
	

	private Context mContext;
	
	
	public OnlyOneGroupExpandExpandableListView(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		setOnlyOneExpand(this);
	}




	public OnlyOneGroupExpandExpandableListView(Context context) {
		super(context);
		mContext = context;
		setOnlyOneExpand(this);
		
	}
	
	public void setOnlyOneExpand(final View view){

		this.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				KeyboardUtils.hiddenKeyboard(mContext,view);
				for (int i = 0; i < getCount(); i++) {
					if (i != groupPosition && isGroupExpanded(groupPosition)) {
						collapseGroup(i);
					}
				}
			}
		});
	}
	
}
