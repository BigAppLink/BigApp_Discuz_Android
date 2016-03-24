package com.kit.widget.simplecalendar;

import com.kit.utils.ToastUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

public class MyCalendarLayout extends CalendarLayout{


	private Context context;

	public MyCalendarLayout(Context context, AttributeSet attrs) {
		super(context);
		
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public void OnDayClickListener(){
		ToastUtils.mkShortTimeToast(context, "dgadgasdgas");
	}


}
