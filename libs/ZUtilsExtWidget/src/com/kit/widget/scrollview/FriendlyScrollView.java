package com.kit.widget.scrollview;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;
 
public class FriendlyScrollView extends ScrollView {
 
	GestureDetector gestureDetector;
 
    public FriendlyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
 
	public FriendlyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
 
	public FriendlyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
 
	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}
 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    super.onTouchEvent(event);
	    return gestureDetector.onTouchEvent(event);
	}
 
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){
	    gestureDetector.onTouchEvent(ev);
	    super.dispatchTouchEvent(ev);
	    return true;
	} 
 
}
