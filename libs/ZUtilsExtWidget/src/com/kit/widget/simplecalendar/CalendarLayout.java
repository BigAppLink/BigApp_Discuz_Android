package com.kit.widget.simplecalendar;

import com.kit.utils.ToastUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;

public class CalendarLayout extends FrameLayout implements OnGestureListener {

	private static final String TAG = "CalendarLayout";
	private CalendarView mainView;
	private CalendarView anotherView;
	private GestureDetector gestureDetector;
	private OnDayClickListener dayClickListener;
	private boolean hasmoved;
	private boolean isAnimationStarted = false;
	private static final int SWIPE_MIN_DISTANCE = 60;
	private static final int SWIPE_THRESHOLD_VELOCITY = 180;
	private Context context;

	public CalendarLayout(Context context) {
		this(context, null);
		this.context = context;
	}

	public CalendarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		mainView = new CalendarView(context, null);
		mainView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		anotherView = new CalendarView(context, null);
		anotherView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		this.addView(anotherView);
		this.addView(mainView);

		gestureDetector = new GestureDetector(this);
	}

	public void setDayClickListener(OnDayClickListener dayClickListener) {
		this.dayClickListener = dayClickListener;
	}

	public CalendarView getMainView() {
		return mainView;
	}

	public CalendarView getAnotherView() {
		return anotherView;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			hasmoved = false;
			mainView.setmTouchedCellAtPoint(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			hasmoved = true;
			mainView.setmTouchedCellAtPoint(x, y);
			break;

		case MotionEvent.ACTION_UP:
			if (!hasmoved && dayClickListener != null)
				dayClickListener.onDayClick();

			break;

		case MotionEvent.ACTION_CANCEL:
			mainView.setmTouchedCell(null);
			break;

		default:
			break;
		}

		postInvalidate();
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (isAnimationStarted)
			return false;
		if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
			anotherView.previousMonth();
			Animation hideAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f);
			hideAnimation.setDuration(250);
			mainView.startAnimation(hideAnimation);

			Animation showAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			showAnimation.setDuration(250);
			anotherView.startAnimation(showAnimation);
			showAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					isAnimationStarted = true;
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mainView.previousMonth();
					isAnimationStarted = false;
				}
			});
			Log.i(TAG, "velocityY=" + velocityY);
		}
		if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
			anotherView.nextMonth();
			Animation hideAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f);
			hideAnimation.setDuration(250);
			mainView.startAnimation(hideAnimation);

			Animation showAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			showAnimation.setDuration(250);
			anotherView.startAnimation(showAnimation);
			showAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					isAnimationStarted = true;
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mainView.nextMonth();
					isAnimationStarted = false;
				}
			});

			Log.i(TAG, "velocityY=" + velocityY);
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		ToastUtils.mkLongTimeToast(context, "long");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public interface OnDayClickListener {

		public void onDayClick();
	}

}
