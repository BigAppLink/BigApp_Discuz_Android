package com.kit.utils;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.view.KeyEvent;

public class DrawTextUtils {

	private int mTextPosx = 0;// x坐标
	private int mTextPosy = 0;// y坐标
	private int mTextWidth = 0;// 绘制宽度
	private int mTextHeight = 0;// 绘制高度
	private int mFontHeight = 0;// 绘制字体高度
	private int mPageLineNum = 0;// 每一页显示的行数
	private int mCanvasBGColor = 0;// 背景颜色
	private int mFontColor = 0;// 字体颜色
	private int mAlpha = 0;// Alpha值
	private int mRealLine = 0;// 字符串真实的行数
	private int mCurrentLine = 0;// 当前行
	private int mTextSize = 0;// 字体大小
	private String mStrText = "";
	private Vector mString = null;
	private Paint mPaint = null;

	public DrawTextUtils(String StrText, int x, int y, int w, int h, int bgcolor,
			int textcolor, int alpha, int textsize) {
		mPaint = new Paint();
		mString = new Vector();
		this.mStrText = StrText;
		this.mTextPosx = x;
		this.mTextPosy = y;
		this.mTextWidth = w;
		this.mTextHeight = h;
		this.mCanvasBGColor = bgcolor;
		this.mFontColor = textcolor;
		this.mAlpha = alpha;
		this.mTextSize = textsize;
	}

	public void InitText() {
		mString.clear();// 清空Vector
		// 对画笔属性的设置
//		mPaint.setARGB(this.mAlpha, Color.red(this.mFontColor), Color
//				.green(this.mFontColor), Color.blue(this.mFontColor));
		mPaint.setTextSize(this.mTextSize);
		mPaint.setColor(Color.BLACK);
		this.GetTextIfon();
	}

	/**
	 * 得到字符串信息包括行数，页数等信息
	 */
	public void GetTextIfon() {
		char ch;
		int w = 0;
		int istart = 0;
		FontMetrics fm = mPaint.getFontMetrics();// 得到系统默认字体属性
		mFontHeight = (int) (Math.ceil(fm.descent - fm.top) + 2);// 获得字体高度
		mPageLineNum = mTextHeight / mFontHeight;// 获得行数
		int count = this.mStrText.length();
		for (int i = 0; i < count; i++) {
			ch = this.mStrText.charAt(i);
			float[] widths = new float[1];
			String str = String.valueOf(ch);
			mPaint.getTextWidths(str, widths);
			if (ch == '\n') {//原为\n
				mRealLine++;// 真实的行数加一
				mString.addElement(this.mStrText.substring(istart, i));
				istart = i + 1;
				w = 0;
			} else {
				w += (int) Math.ceil(widths[0]);
				if (w > this.mTextWidth) {
					mRealLine++;// 真实的行数加一
					mString.addElement(this.mStrText.substring(istart, i));
					istart = i;
					i--;
					w = 0;
				} else {
					if (i == count - 1) {
						mRealLine++;// 真实的行数加一
						mString.addElement(this.mStrText.substring(istart,
								count));
					}
				}
			}
		}
	}

	/**
	 * 绘制字符串
	 * 
	 * @param canvas
	 */
	public void DrawText(Canvas canvas) {
		for (int i = this.mCurrentLine, j = 0; i < this.mRealLine; i++, j++) {
			if (j > this.mPageLineNum) {
				break;
			}
			canvas.drawText((String) (mString.elementAt(i)), this.mTextPosx,
					this.mTextPosy + this.mFontHeight * j, mPaint);
		}
	}
	/**
	 * 翻页等按键处理
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean KeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
		{
			if (this.mCurrentLine > 0)
			{
				this.mCurrentLine--;
			}
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			if ((this.mCurrentLine + this.mPageLineNum) < (this.mRealLine - 1))
			{
				this.mCurrentLine++;
			}
		}
		return false;
	}
}

