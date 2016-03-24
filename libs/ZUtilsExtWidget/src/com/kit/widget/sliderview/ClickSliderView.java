package com.kit.widget.sliderview;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
 
public class ClickSliderView extends ViewGroup {
 
    /**
     * 速度边界
     */
    private static final int VELOCITY_X_SPEED = 800;
    /**
     * 最后一次触摸的x位置
     */
    private float x;
 
    private Scroller scroller;
    /**
     * 触摸事件是否已分发给子view
     */
    private boolean dispatched;
    private boolean slided;
    private int startScrollLeftOffset;
    private VelocityTracker mVelocityTracker;
 
    public ClickSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }
 
    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View view = getChildAt(i);
            if (view.getVisibility() != View.GONE) {
                view.layout(0, 0, view.getMeasuredWidth(),
                        view.getMeasuredHeight());
            }
        }
    }
 
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return true;
//    }
// 

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (mVelocityTracker == null) {
//            mVelocityTracker = VelocityTracker.obtain();
//        }
//        mVelocityTracker.addMovement(event);
//        int action = event.getAction();
//        if (action == MotionEvent.ACTION_DOWN) {
//            x = event.getX();
//            //分发事件
//            if (isSlided()) {
//                dispatched = dispatchTouchEventToView(getChildAt(0), event);
//            } else {
//                dispatched = dispatchTouchEventToView(getChildAt(1), event);
//            }
//        } else if (action == MotionEvent.ACTION_MOVE) {
//            if (dispatched) {//分发事件
//                if (isSlided()) {
//                    dispatchTouchEventToView(getChildAt(0), event);
//                } else {
//                    dispatchTouchEventToView(getChildAt(1), event);
//                }
//            } else {//重置第一个子view位置
//                float dx = event.getX() - x;
//                View view = getChildAt(1);
//                int left = (int) (view.getLeft() + dx);
//                if (left >= 0) {
//                    view.layout(left, view.getTop(), view.getWidth() + left,
//                            view.getTop() + view.getHeight());
//                }
//            }
//            x = event.getX();
// 
//        } else if (action == MotionEvent.ACTION_CANCEL
//                || action == MotionEvent.ACTION_UP) {
//            if (dispatched) {//分发事件
//                if (isSlided()) {
//                    dispatchTouchEventToView(getChildAt(0), event);
//                } else {
//                    dispatchTouchEventToView(getChildAt(1), event);
//                }
//            } else {
//                //判断速度
//                mVelocityTracker.computeCurrentVelocity(1000);
//                int velocityX = (int) mVelocityTracker.getXVelocity();
//                if (velocityX > VELOCITY_X_SPEED) {
//                    setSlided(true);
//                } else if (velocityX < -VELOCITY_X_SPEED) {
//                    setSlided(false);
//                } else {
//                    View view = getChildAt(1);
//                    if (view.getLeft() >= view.getWidth() / 2) {
//                        setSlided(true);
//                    } else {
//                        setSlided(false);
//                    }
//                }
//                if (mVelocityTracker != null) {
//                    mVelocityTracker.recycle();
//                }
//            }
//        }
//        return true;
//    }
    
    
    public boolean isSlided() {
        return slided;
    }
 
    /**
     * 设置是否滑动显示菜单状态，并动画滑动效果
     * @param slided
     */
    public void setSlided(boolean slided) {
        View view = getChildAt(1);
        startScrollLeftOffset = view.getLeft();
        if (slided) {
            scroller.startScroll(0, getTop(), view.getWidth() * 3 / 4
                    - startScrollLeftOffset, 0);
        } else {
            scroller.startScroll(0, getTop(), -startScrollLeftOffset, 0);
        }
        this.slided = slided;
        postInvalidate();
    }
 
    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            View view = getChildAt(1);
            int left = startScrollLeftOffset + scroller.getCurrX();
            view.layout(left, view.getTop(), left + view.getWidth(),
                    view.getHeight());
            postInvalidate();
        }
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); ++i) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }
 
    public boolean dispatchTouchEventToView(View view, MotionEvent ev) {
        try {
            return view.dispatchTouchEvent(ev);
        } catch (Exception e) {
            // 部分机型会抛异常
            e.printStackTrace();
        }
        return false;
    }
}