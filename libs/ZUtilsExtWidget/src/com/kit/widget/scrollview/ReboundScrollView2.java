package com.kit.widget.scrollview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 自定义ScrollView 实现弹性回弹功能
 */
public class ReboundScrollView2 extends ScrollView {
	private View inner;// 孩子

	private float y;// 坐标

	private Rect normal = new Rect();// 矩形空白

	public ReboundScrollView2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/***
	 * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
	 * 方法，也应该调用父类的方法，使该方法得以执行.
	 */
	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0);// 获取其孩子
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (inner != null) {
			commOnTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}

	/***
	 * 触摸事件
	 * 
	 * @param ev
	 */
	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			y = ev.getY();// 获取点击y坐标
			break;
		case MotionEvent.ACTION_UP:
			if (isNeedAnimation()) {
				animation();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			final float preY = y;
			float nowY = ev.getY();
			int deltaY = (int) (preY - nowY);// 获取滑动距离

			y = nowY;
			// 当滚动到最上或者最下时就不会再滚动，这时移动布局
			if (isNeedMove()) {
				if (normal.isEmpty()) {
					// 填充矩形，目的：就是告诉this:我现在已经有了，你松开的时候记得要执行回归动画.
					normal.set(inner.getLeft(), inner.getTop(),
							inner.getRight(), inner.getBottom());
				}
				// 移动布局
				inner.layout(inner.getLeft(), inner.getTop() - deltaY / 4,
						inner.getRight(), inner.getBottom() - deltaY / 4);
			}
			break;

		default:
			break;
		}
	}

	/***
	 * 开启动画移动
	 */
	public void animation() {
		// 开启移动动画
		TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
				normal.top);
		ta.setDuration(300);
		inner.startAnimation(ta);
		// 设置回到正常的布局位置
		inner.layout(normal.left, normal.top, normal.right, normal.bottom);
		normal.setEmpty();// 清空矩形

	}

	/***
	 * 是否需要开启动画
	 * 
	 * 如果矩形不为空，返回true，否则返回false.
	 * 
	 * 
	 * @return
	 */
	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	/***
	 * 是否需要移动布局 inner.getMeasuredHeight():获取的是控件的高度
	 * getHeight()：获取的是当前控件在屏幕中显示的高度
	 * 
	 * @return
	 */
	public boolean isNeedMove() {
		int offset = inner.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();
		// 0是顶部，后面那个是底部
		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}

}
