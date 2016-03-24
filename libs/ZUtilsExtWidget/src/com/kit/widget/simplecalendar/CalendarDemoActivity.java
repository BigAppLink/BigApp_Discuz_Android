package com.kit.widget.simplecalendar;


import com.kit.widget.simplecalendar.CalendarLayout.OnDayClickListener;
import com.kit.widget.simplecalendar.CalendarView.OnMonthChangeListener;
import com.kit.extend.widget.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarDemoActivity extends Activity implements OnMonthChangeListener,OnDayClickListener{
	private CalendarView mView;
	private CalendarLayout mCalendarLayout;
	private  TextView month;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_main);
        
        mCalendarLayout = (CalendarLayout)findViewById(R.id.mycalendar);
		mView = mCalendarLayout.getMainView();
		mView.setMonthChangeListener(this);
		mCalendarLayout.setDayClickListener(this);
		
		month = (TextView) findViewById(R.id.month);
		month.setText(mView.getYear() + "年" + mView.getMonth() + "月");
    }
	@Override
	public void onMonthChanged() {
		month.setText(mView.getYear() + "年" + mView.getMonth() + "月") ;
	}
	@Override
	public void onDayClick() {
		Cell day = mView.getmTouchedCell();
		Toast.makeText(
				this,
				day.getYear() + "-" + day.getMonth() + "-"
						+ day.getDayOfMonth(), Toast.LENGTH_SHORT).show();
	}
}