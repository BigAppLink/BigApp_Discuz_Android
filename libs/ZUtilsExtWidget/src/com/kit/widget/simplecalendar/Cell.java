
package com.kit.widget.simplecalendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class Cell {
	protected Rect mBound = null;
	protected int year;
	protected int month;
	protected int mDayOfMonth = 1;	// from 1 to 31
	protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            |Paint.ANTI_ALIAS_FLAG);
	int dx, dy;
	public Cell(int year, int month, int dayOfMon, Rect rect, float textSize, boolean bold) {
		this.year = year;
		this.month = month;
		mDayOfMonth = dayOfMon;
		mBound = rect;
		mPaint.setTextSize(textSize);
		mPaint.setColor(Color.BLACK);
		if(bold) mPaint.setFakeBoldText(true);
		
		dx = (int) mPaint.measureText(String.valueOf(mDayOfMonth)) / 2;
		dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
	}
	
	public Cell(int year, int month, int dayOfMon, Rect rect, float textSize) {
		this(year, month, dayOfMon, rect, textSize, false);
	}
	
	protected void draw(Canvas canvas) {
		canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, mBound.centerY() + dy, mPaint);
	}
	
	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDayOfMonth() {
		return mDayOfMonth;
	}
	
	public boolean hitTest(int x, int y) {
		return mBound.contains(x, y); 
	}
	
	public Rect getBound() {
		return mBound;
	}
	
	public String toString() {
		return String.valueOf(mDayOfMonth)+"("+mBound.toString()+")";
	}
	
}

