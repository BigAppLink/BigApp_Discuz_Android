package com.kit.widget.scrollview;

import com.kit.extend.widget.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class ScrollLayout extends LinearLayout implements
		GestureDetector.OnGestureListener {
	private int offset; // 相对距离
	private GestureDetector gestureDetector; // 手势事件
	private int childWidth; // 子View的宽度
	private int childCount; // 子视图的数量
	private int defaultWindow; // 默认窗口

	private boolean setShareWindowFlag = false; // 保证默认窗口的设置只执行一次

	public int whereIs = 0;

	public ScrollLayout(Context context) {
		super(context);
		init();
	}

	public ScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// 获取定义的defaultWindow的值
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.MyScrollWindow);
		defaultWindow = typedArray.getInteger(
				R.styleable.MyScrollWindow_defaultWindow, 0);
	}

	private void init() {
		gestureDetector = new GestureDetector(this.getContext(), this);
	}

	// 返回值为true 才能触发 手势事件
	public boolean onDown(MotionEvent e) {
		return true;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	// 顺手势滑动
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// 获取滑动的距离
		offset = (int) (offset - distanceX);
		// 防止滑出边界
		if (offset > 0) {
			offset = 0;
		} else if (offset < -1 * childWidth * (childCount - 1)) {
			offset = -1 * childWidth * (childCount - 1);
		}
		// 重绘布局
		requestLayout();
		return true;
	}

	public void onLongPress(MotionEvent e) {

	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	// 设置布局文件的宽高和每个桌面的宽高
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 给每个桌面设置和屏幕相同的宽度和高度
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

	}

	// 设置布局
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		childCount = getChildCount(); // 获取子视图数
		childWidth = childCount > 0 ? getChildAt(0).getMeasuredWidth() : 0; // 获取字节点的宽度
		if (!setShareWindowFlag && defaultWindow >= 0
				&& defaultWindow <= childCount - 1) {
			// 设置默认窗口的左端距离
			offset = -1 * defaultWindow * childWidth;
			setShareWindowFlag = true;
		}
		// 设置距离(0,0)点X轴方向的初始距离
		int left = 0 + offset;
		for (int i = 0; i < childCount; i++) {
			// 设置每个子视图在布局中的位置
			View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				child.layout(left, 0, childWidth + left,
						child.getMeasuredHeight());
				left = left + childWidth;
			}
		}
	}

	// 触屏事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = gestureDetector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// 当手指抬起来时 判断滑动距离显示整个子视图
			showOneDesktop();
		}
		return result;
	}

	// 判断当手指抬起时显示那个桌面
	private void showOneDesktop() {
		int index = Math.abs(offset) / childWidth;
		if (Math.abs(offset) - index * childWidth > childWidth / 2) {
			index++;
		}
		offset = offset > 0 ? index * childWidth : -1 * index * childWidth;
		whereIs = index;
		System.out.println("index index index: " + index);
		requestLayout();
	}

	public int getWhereIs() {
		return whereIs;
	}
}
