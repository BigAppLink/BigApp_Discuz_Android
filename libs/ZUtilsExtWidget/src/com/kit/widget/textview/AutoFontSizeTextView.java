package com.kit.widget.textview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * 配置android:scrollbars="vertical"，启用垂直滚动条
 * 
 * 实现水平滚动就是修改mScrollX，可参考HorizontalScrollView
 * 
 * 滚动原理： 在绘制前对画布进行偏移操作
 * 
 * 下面是View的绘制机制： |- view.computeScroll()
 * --用来对mScrollX/Y进行修改。由于在绘制前调用，可调用invalite()来触发 |-
 * canvas.translate(-mScrollX,-mScrollY) --偏移画布 |- view.draw() --绘制
 * 
 * 上述内容可以在View.buildDrawingCache()或ViewGroup.dispatchDraw()->drawChild()中找到.
 * 直接查看方法名即可
 * 
 * 滚动帮助类： Scroller --用来计算滚动后的偏移值.具体请参考ScrollView和HorizontalScrollView
 * VelocityTracker --速度计算类。根据fling时的按下、抬起动作，计算滚动初速度
 * 
 * ScrollTextView--流程解析： 1、onTouchEvent() --使用Scroller来计算滚动偏移值
 * 2、重写computeScroll() --对View的mScrollY进行修改， 此处控制滚动范围
 * 
 * 滚动范围： 最小值：0 最大值：所有文本高度+内边距-View高度。也就是超出屏幕的文本高度
 */
public class AutoFontSizeTextView extends TextView {

	public int speed = 1;
	public int howLong = 100;

	private static final int INVALID_POINTER = -1;
	private final Handler mHandler = new Handler();

	public AutoFontSizeTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public AutoFontSizeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public AutoFontSizeTextView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		final Context context = getContext();
		
		System.out.println("getText().length(): " + getText().length());
		
		if (getText().length() > 100) {
			setTextSize(10);
		}

	}

}