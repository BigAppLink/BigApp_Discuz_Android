package com.kit.widget.calendar;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout.LayoutParams;

public class DateWidgetDayCell extends View {
	// 编写点击事件
	public interface OnItemClick {
		public void OnClick(DateWidgetDayCell item);
	}

	// 设置时间
	private int iDateYear = 0;
	private int iDateMonth = 0;
	private int iDateDay = 0;

	// 画布
	private OnItemClick itemClick = null;
	private Paint pt = new Paint();
	private RectF rect = new RectF();
	private String sDate = "";

	// 判断显示
	private boolean bSelected = false;
	private boolean bIsActiveMonth = false;
	private boolean bToday = false;
	private boolean bHoliday = false;
	private boolean bTouchedDown = false;
	private boolean bMark = false;

	// 单元格填充
	public DateWidgetDayCell(Context context, int iWidth, int iHeight) {
		super(context);
		setFocusable(true);
		setLayoutParams(new LayoutParams(iWidth, iHeight));
	}

	// 判断是否为选中
	public boolean getSelected() {
		return this.bSelected;
	}

	// 设置选中属性
	public void setSelected(boolean bEnable) {
		if (this.bSelected != bEnable) {
			this.bSelected = bEnable;
			this.invalidate();
		}
	}

	// 设置填充时间
	public void setData(int iYear, int iMonth, int iDay, boolean bToday,
			boolean bMark, boolean bHoliday, int iActiveMonth) {
		iDateYear = iYear;
		iDateMonth = iMonth;
		iDateDay = iDay;

		this.sDate = Integer.toString(iDateDay);
		this.bIsActiveMonth = (iDateMonth == iActiveMonth);
		this.bToday = bToday;
		this.bMark = bMark;
		this.bHoliday = bHoliday;
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	// 当按下键盘事件
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyDown(keyCode, event);
		if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
				|| (keyCode == KeyEvent.KEYCODE_ENTER)) {
			doItemClick();
		}
		return bResult;
	}

	// 当按下升起键盘事件
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyUp(keyCode, event);
		return bResult;
	}

	// 当点击事件
	public void doItemClick() {
		if (itemClick != null)
			itemClick.OnClick(this);
	}

	// 当焦点转移事件，更改颜色刷新
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		invalidate();
	}

	// 获得时间
	public Calendar getDate() {
		Calendar calDate = Calendar.getInstance();
		calDate.clear();
		calDate.set(Calendar.YEAR, iDateYear);
		calDate.set(Calendar.MONTH, iDateMonth);
		calDate.set(Calendar.DAY_OF_MONTH, iDateDay);
		return calDate;
	}

	// 画单元视图
	protected void onDraw(Canvas canvas) {
		invalidate();
		super.onDraw(canvas);

		// 设置画布的边框大小属性
		rect.set(0, 0, this.getWidth(), this.getHeight());
		rect.inset(1, 1);

		// 启用画布
		final boolean bFocused = IsViewFocused();

		// 画日期视图
		drawDayView(canvas, bFocused);
		// 画日期到视图中
		drawDayNumber(canvas, bFocused);
	}

	/**
	 * 画日期视图
	 * 
	 * @param canvas
	 * @param bFocused
	 */
	private void drawDayView(Canvas canvas, boolean bFocused) {
		if (bSelected || bFocused) {
			LinearGradient lGradBkg = null;

			if (bFocused) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						0xffaa5500, 0xffffddbb, Shader.TileMode.CLAMP);
			}

			if (bSelected) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						0xff225599, 0xffbbddff, Shader.TileMode.CLAMP);
			}

			if (lGradBkg != null) {
				pt.setShader(lGradBkg);
				canvas.drawRect(rect, pt);
			}

			pt.setShader(null);

		} else {

			pt.setColor(DayStyle.getColorBkg(bHoliday, bToday, bMark));
			if (!bIsActiveMonth)
				pt.setAlpha(0x88);
			canvas.drawRect(rect, pt);
		}
	}

	/**
	 * 画日期到视图中
	 * 
	 * @param canvas
	 * @param bFocused
	 */
	public void drawDayNumber(Canvas canvas, boolean bFocused) {
		pt.setTypeface(null);
		pt.setAntiAlias(true);
		pt.setShader(null);
		pt.setFakeBoldText(true);
		pt.setTextSize(22);

		pt.setUnderlineText(false);
		if (bToday)
			pt.setUnderlineText(true);

		int iTextPosX = (int) rect.right - (int) pt.measureText(sDate);
		int iTextPosY = (int) rect.bottom + (int) (-pt.ascent())
				- getTextHeight();

		iTextPosX -= ((int) rect.width() >> 1)
				- ((int) pt.measureText(sDate) >> 1);
		iTextPosY -= ((int) rect.height() >> 1) - (getTextHeight() >> 1);

		// draw text
		if (bSelected || bFocused) {
			if (bSelected)
				pt.setColor(0xff001122);
			if (bFocused)
				pt.setColor(0xff221100);
		} else {
			pt.setColor(DayStyle.getColorText(bHoliday, bToday, bMark));
		}

		if (!bIsActiveMonth)
			pt.setAlpha(0x88);

		canvas.drawText(sDate, iTextPosX, iTextPosY + 1, pt);

		pt.setUnderlineText(false);
	}

	public boolean IsViewFocused() {
		return (this.isFocused() || bTouchedDown);
	}

	// 触屏设置
	public boolean onTouchEvent(MotionEvent event) {
		boolean bHandled = false;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			bHandled = true;
			bTouchedDown = true;
			invalidate();
			startAlphaAnimIn(DateWidgetDayCell.this);
		}
		if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
			doItemClick();
		}
		return bHandled;
	}

	// 开启
	public static void startAlphaAnimIn(View view) {
		AlphaAnimation anim = new AlphaAnimation(0.5F, 1);
		anim.setDuration(100);
		anim.startNow();
		view.startAnimation(anim);
	}

}
