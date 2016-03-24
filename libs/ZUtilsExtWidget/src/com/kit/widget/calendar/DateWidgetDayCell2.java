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

public class DateWidgetDayCell2 extends View {
	// ��д����¼�
	public interface OnItemClick {
		public void OnClick(DateWidgetDayCell2 item);
	}

	// ����ʱ��
	private int iDateYear = 0;
	private int iDateMonth = 0;
	private int iDateDay = 0;

	// ����
	private OnItemClick itemClick = null;
	private Paint pt = new Paint();
	private RectF rect = new RectF();
	private String sDate = "";

	// �ж���ʾ
	private boolean bSelected = false;
	private boolean bIsActiveMonth = false;
	private boolean bToday = false;
	private boolean bHoliday = false;
	private boolean bTouchedDown = false;

	// ��Ԫ�����
	public DateWidgetDayCell2(Context context, int iWidth, int iHeight) {
		super(context);
		setFocusable(true);
		setLayoutParams(new LayoutParams(iWidth, iHeight));
		setWillNotDraw(false);
	}

	// �ж��Ƿ�Ϊѡ��
	public boolean getSelected() {
		return this.bSelected;
	}

	// ����ѡ������
	public void setSelected(boolean bEnable) {
		if (this.bSelected != bEnable) {
			this.bSelected = bEnable;
			this.invalidate();
		}
	}

	// �������ʱ��
	public void setData(int iYear, int iMonth, int iDay, boolean bToday,
			boolean bHoliday, int iActiveMonth) {

		System.out.println("set " + iYear + " " + iMonth + " " + iDay + " "
				+ bToday + " " + bHoliday);

		iDateYear = iYear;
		iDateMonth = iMonth;
		iDateDay = iDay;

		this.sDate = Integer.toString(iDateDay);
		this.bIsActiveMonth = (iDateMonth == iActiveMonth);

		this.bToday = bToday;
		this.bHoliday = bHoliday;
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	// �����¼����¼�
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyDown(keyCode, event);
		if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
				|| (keyCode == KeyEvent.KEYCODE_ENTER)) {
			doItemClick();
		}
		return bResult;
	}

	// ��������������¼�
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyUp(keyCode, event);
		return bResult;
	}

	// ������¼�
	public void doItemClick() {
		if (itemClick != null)
			itemClick.OnClick(this);
	}

	// ������ת���¼��������ɫˢ��
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		invalidate();
	}

	// ���ʱ��
	public Calendar getDate() {
		Calendar calDate = Calendar.getInstance();
		calDate.clear();
		calDate.set(Calendar.YEAR, iDateYear);
		calDate.set(Calendar.MONTH, iDateMonth);
		calDate.set(Calendar.DAY_OF_MONTH, iDateDay);
		return calDate;
	}

	// ����Ԫ��ͼ
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		System.out.println(iDateYear + " " + iDateMonth + " " + iDateDay + " "
				+ bToday + " " + bHoliday + " "
				+ "drawed drawed drawed drawed ");

		// ���û����ı߿��С����
		rect.set(0, 0, this.getWidth(), this.getHeight());
		rect.inset(1, 1);

		// ���û���
		//final boolean bFocused = IsViewFocused();
		System.out.println("IsViewFocused: "+IsViewFocused());
		//final boolean bFocused = IsViewFocused();
		final boolean bFocused = true;
		// ��������ͼ
		drawDayView(canvas, bFocused);
		// �����ڵ���ͼ��
		drawDayNumber(canvas, bFocused);
	}

	/**
	 * ��������ͼ
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

			pt.setColor(DayStyle2.getColorBkg(bHoliday, bToday));
			if (!bIsActiveMonth)
				pt.setAlpha(0x88);
			canvas.drawRect(rect, pt);
		}
	}

	/**
	 * �����ڵ���ͼ��
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
			pt.setColor(DayStyle2.getColorText(bHoliday, bToday));
		}

		if (!bIsActiveMonth)
			pt.setAlpha(0x88);

		canvas.drawText(sDate, iTextPosX, iTextPosY + 1, pt);

		pt.setUnderlineText(false);
	}

	public boolean IsViewFocused() {
		return (this.isFocused() || bTouchedDown);
	}

	// ��������
	public boolean onTouchEvent(MotionEvent event) {
		boolean bHandled = false;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			bHandled = true;
			bTouchedDown = true;
			invalidate();
			startAlphaAnimIn(DateWidgetDayCell2.this);
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

	// ����
	public static void startAlphaAnimIn(View view) {
		AlphaAnimation anim = new AlphaAnimation(0.5F, 1);
		anim.setDuration(100);
		anim.startNow();
		view.startAnimation(anim);
	}

}
