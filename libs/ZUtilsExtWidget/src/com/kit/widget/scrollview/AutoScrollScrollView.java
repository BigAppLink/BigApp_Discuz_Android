package com.kit.widget.scrollview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ScrollView;
import android.widget.Scroller;

public class AutoScrollScrollView extends ScrollView {

	private Scroller scroller;
	private float mLastMotionY;
	// private int mTouchSlop;

	private OnItemClickListener onItemClickListener;

	// private int currentScreenIndex = 0;

	private boolean moving = false;

	private boolean autoScroll = true;

	private int scrollTime = 6 * 1000;

	private int scrollToY = 0;// 滚动到y

	private boolean isAutoScroll = true;// 设置是否自动滚动

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (autoScroll) {

				try {

					System.out.println("start scroll");
					for (int i = 0; i < getHeight(); i++) {
						scrollTo(0, i);
					}

				} catch (Exception e) {
					e.printStackTrace();

				}

			}
		}
	};

	public AutoScrollScrollView(Context context) {
		super(context);

		initView(context);

	}

	public AutoScrollScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		initView(context);

	}

	public AutoScrollScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);

	}

	private void initView(final Context context) {
		this.scroller = new Scroller(context, new DecelerateInterpolator(4));// OvershootInterpolator(1.1f)

		handler.sendEmptyMessageDelayed(0, scrollTime);

		// final ViewConfiguration configuration = ViewConfiguration
		// .get(getContext());
		// mTouchSlop = configuration.getScaledTouchSlop();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int maxHeight = -1;

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);

			maxHeight = Math.max(maxHeight, getChildAt(i).getMeasuredHeight());

		}
		maxHeight = Math.min(maxHeight, MeasureSpec.getSize(heightMeasureSpec));

		// Log.e("TAG","onMeasure Height:"+maxHeight);

		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), maxHeight);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {

		final int count = getChildCount();

		int cLeft = 0;

		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			if (child.getVisibility() == View.GONE)
				continue;

			// child.setVisibility(View.VISIBLE);
			final int childWidth = child.getMeasuredWidth();
			child.layout(cLeft, 0, cLeft + childWidth,
					child.getMeasuredHeight());

			cLeft += childWidth;
		}

	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(0, scroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (getChildCount() == 0)
			return false;
		final int action = ev.getAction();
		final float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:

			autoScroll = false;

			scrollToY++;

			mLastMotionY = y;
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}

			moving = false;
			// Log.i("TAG","ACTION_DOWN");

			return true;

		case MotionEvent.ACTION_MOVE:
			final int deltaY = (int) (mLastMotionY - y);
			System.out.println("deltaX: " + deltaY);
			boolean xMoved = Math.abs(deltaY) > 4;
			if (!moving && !xMoved)
				break;
			mLastMotionY = y;

			scrollBy(0, deltaY);

			moving = true;

			return true;
		case MotionEvent.ACTION_UP:

			if (!autoScroll) {
				startScroll();
			}
			if (!moving && null != onItemClickListener) {
				final int screenHeight = getHeight();
				int index = (int) ((getScrollY() + y) / screenHeight);
				onItemClickListener.onClick(index, getChildAt(index));
			}

			break;
		case MotionEvent.ACTION_CANCEL:

			if (!autoScroll) {
				startScroll();
			}
			break;
		}
		return false;
	}

	private void scrollScreen(int whichScreen) {
		// if (!scroller.isFinished())
		// return;
		// Log.e("TAG","scrollToScreen:"+whichScreen);
		if (whichScreen >= getChildCount())
			whichScreen = getChildCount() - 1;

		int delta = 0;

		delta = whichScreen * getWidth() - getScrollX();

		// scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
		scroller.startScroll(getScrollX(), 0, delta, 0, 1500);
		invalidate();

	}

	public void startScroll() {

		autoScroll = true;
		handler.sendEmptyMessageDelayed(scrollToY, scrollTime);
	}

	public boolean isScrolling() {
		return autoScroll;
	}

	public void stopScroll() {
		autoScroll = false;
		scrollToY++;
	}

	@Override
	protected void finalize() throws Throwable {

		// Log.e("TAG", "finalize===");

		super.finalize();
	}

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public interface OnItemClickListener {
		public void onClick(int index, View childview);
	}
	// OnClickListener onclick;

}