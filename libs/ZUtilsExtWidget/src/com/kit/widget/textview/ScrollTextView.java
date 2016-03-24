package com.kit.widget.textview;

import android.content.Context;
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
public class ScrollTextView extends TextView {
	private Scroller mScroller;
	private int mTouchSlop;
	private int mMinimumVelocity;
	private int mMaximumVelocity;

	private float mLastMotionY;
	private boolean mIsBeingDragged;
	private VelocityTracker mVelocityTracker;
	private int mActivePointerId = INVALID_POINTER;

	private static final int INVALID_POINTER = -1;

	public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public ScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ScrollTextView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		final Context cx = getContext();
		// 设置滚动减速器，在fling中会用到
		mScroller = new Scroller(cx, new DecelerateInterpolator(0.5f));
		final ViewConfiguration configuration = ViewConfiguration.get(cx);
		mTouchSlop = configuration.getScaledTouchSlop();
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

	}

	/**
	 * 此方法为最后机会来修改mScrollX,mScrollY. 这方法后将根据mScrollX,mScrollY来偏移Canvas已实现内容滚动
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();

		final Scroller scroller = mScroller;
		if (scroller.computeScrollOffset()) { // 正在滚动，让view滚动到当前位置
			int scrollY = scroller.getCurrY();
			final int maxY = (getLineCount() * getLineHeight()
					+ getPaddingTop() + getPaddingBottom())
					- getHeight();
			boolean toEdge = scrollY < 0 || scrollY > maxY;
			if (scrollY < 0)
				scrollY = 0;
			else if (scrollY > maxY)
				scrollY = maxY;

			/*
			 * 下面等同于： mScrollY = scrollY; awakenScrollBars(); //显示滚动条，必须在xml中配置。
			 * postInvalidate();
			 */
			scrollTo(0, scrollY);
			if (toEdge) // 移到两端，由于位置没有发生变化，导致滚动条不显示
				awakenScrollBars();
		}
	}

	public void fling(int velocityY) {
		final int maxY = (getLineCount() * getLineHeight() + getPaddingTop() + getPaddingBottom())
				- getHeight();

		mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0,
				Math.max(0, maxY));

		// 刷新，让父控件调用computeScroll()
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		/*
		 * 事件处理方式：先自己处理后交给父类处理。 PS:方式不同，可能导致效果不同。请根据需求自行修改。
		 */
		boolean handled = false;
		final int contentHeight = getLineCount() * getLineHeight();
		if (contentHeight > getHeight()) {
			handled = processScroll(ev);
		}

		//return false;
		//return true;
		return handled | super.onTouchEvent(ev);
	}

	private boolean processScroll(MotionEvent ev) {
		boolean handled = false;
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev); // 帮助类，用来在fling时计算移动初速度

		final int action = ev.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			if (!mScroller.isFinished()) {
				mScroller.forceFinished(true);
			}

			mLastMotionY = ev.getY();
			mActivePointerId = ev.getPointerId(0);
			mIsBeingDragged = true;
			handled = true;
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			final int pointerId = mActivePointerId;
			if (mIsBeingDragged && INVALID_POINTER != pointerId) {
				final int pointerIndex = ev.findPointerIndex(pointerId);
				final float y = ev.getY(pointerIndex);
				int deltaY = (int) (mLastMotionY - y);

				if (Math.abs(deltaY) > mTouchSlop) { // 移动距离(正负代表方向)必须大于ViewConfiguration设置的默认值
					mLastMotionY = y;

					/*
					 * 默认滚动时间为250ms，建议立即滚动，否则滚动效果不明显 或者直接使用scrollBy(0, deltaY);
					 */
					mScroller.startScroll(getScrollX(), getScrollY(), 0,
							deltaY, 0);
					invalidate();
					handled = true;
				}
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			final int pointerId = mActivePointerId;
			if (mIsBeingDragged && INVALID_POINTER != pointerId) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				int initialVelocity = (int) velocityTracker
						.getYVelocity(pointerId);

				if (Math.abs(initialVelocity) > mMinimumVelocity) {
					fling(-initialVelocity);
				}

				mActivePointerId = INVALID_POINTER;
				mIsBeingDragged = false;

				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}

				handled = true;
			}
			break;
		}
		}
		return handled;
	}

}