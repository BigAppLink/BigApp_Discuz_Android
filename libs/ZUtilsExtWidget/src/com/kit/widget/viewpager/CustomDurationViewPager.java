package com.kit.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

public class CustomDurationViewPager extends ViewPager {

	public CustomDurationViewPager(Context context) {
		super(context);
		postInitViewPager();
	}

	public CustomDurationViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		postInitViewPager();
	}

	private CustomDurationScroller mScroller = null;

	/**
	 * Override the Scroller instance with our own class so we can change the
	 * duration
	 */
	private void postInitViewPager() {
		try {
			Class<?> viewpager = ViewPager.class;
			Field scroller = viewpager.getDeclaredField("mScroller");
			scroller.setAccessible(true);
			Field interpolator = viewpager.getDeclaredField("sInterpolator");
			interpolator.setAccessible(true);

			mScroller = new CustomDurationScroller(getContext(),
					(Interpolator) interpolator.get(null));

			double duration = 15;
			mScroller.setScrollDurationFactor(duration);
			scroller.set(this, mScroller);
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}

	/**
	 * Set the factor by which the duration will change
	 */
	public void setScrollDurationFactor(double scrollFactor) {
		mScroller.setScrollDurationFactor(scrollFactor);
	}

}