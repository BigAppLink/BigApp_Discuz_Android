package com.kit.widget.calendar;

import java.util.*;
import android.content.*;
import android.view.*;
import android.widget.LinearLayout.LayoutParams;
import android.graphics.*;

public class DateWidgetDayHeader2 extends View {

	// fields
	private Paint pt = new Paint();
	private RectF rect = new RectF();
	private int iWeekDay = -1;
	private boolean bHoliday = false;

	//�вι���
	public DateWidgetDayHeader2(Context context, int iWidth, int iHeight) {
		super(context);
		setLayoutParams(new LayoutParams(iWidth, iHeight));
		
	}

	public void setData(int iWeekDay) {
		this.iWeekDay = iWeekDay;
		this.bHoliday = false;
		if ((iWeekDay == Calendar.SATURDAY) || (iWeekDay == Calendar.SUNDAY))
			this.bHoliday = true;
	}

	/**
	 * �������ͷ��
	 * @param canvas
	 */
	private void drawDayHeader(Canvas canvas) {
		if (iWeekDay != -1) {
			// background
			pt.setColor(DayStyle2.getColorFrameHeader(bHoliday));
			canvas.drawRect(rect, pt);

			// text
			pt.setTypeface(null);
			pt.setTextSize(18);
			pt.setAntiAlias(true);
			pt.setFakeBoldText(true);
			pt.setColor(DayStyle2.getColorTextHeader(bHoliday));

			final int iTextPosY = getTextHeight();
			final String sDayName = DayStyle2.getWeekDayName(iWeekDay);

			//���ñ�ͷ��ʾ
			final int iDayNamePosX = (int) rect.left
					+ ((int) rect.width() >> 1)
					- ((int) pt.measureText(sDayName) >> 1);
			canvas.drawText(sDayName, iDayNamePosX, rect.top + iTextPosY + 2,
					pt);
		}
	}

	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	/**
	 * ����
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// init rectangles
		rect.set(0, 0, this.getWidth(), this.getHeight());
		rect.inset(1, 1);

		// drawing
		drawDayHeader(canvas);
	}

}
