package com.kit.widget.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class GuideGallery extends Gallery {

	public GuideGallery(Context context) {
		super(context);
	}

	public GuideGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GuideGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int kEvent;

		if(isScrollingLeft(e1, e2))
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		else 
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;

		onKeyDown(kEvent, null); 
		
		return true;
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2){
		return e2.getX() > e1.getX();
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

}
