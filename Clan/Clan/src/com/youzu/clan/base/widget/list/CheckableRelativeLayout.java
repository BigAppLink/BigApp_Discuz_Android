package com.youzu.clan.base.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.youzu.clan.R;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable{

	private boolean isChecked;
	private View view;
	
	public CheckableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setChecked(boolean checked) {
		if (view == null) {
			view = findViewById(R.id.checkbox);
		}
		this.isChecked = checked;
		if (view instanceof Checkable) {
			Checkable checkable = (Checkable) view;
			checkable.setChecked(checked);
		}
	}

	@Override
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void toggle() {
		
	}


}
