package com.kit.widget.floatingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.kit.extend.widget.R;
import com.kit.utils.AnimUtils;
import com.kit.utils.ZogUtils;
import com.kit.widget.scrollview.defaultscrollview.KitOnScrollListener;
import com.kit.widget.scrollview.defaultscrollview.KitScrollView;
import com.nineoldandroids.view.ViewHelper;

public class FloatingView extends FrameLayout {

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private final Paint mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mBitmap;
    private int mColor;
    private boolean mHidden = false;
    /**
     * The FAB button's Y position when it is displayed.
     */
    private float mYDisplayed = -1;
    /**
     * The FAB button's Y position when it is hidden.
     */
    private float mYHidden = -1;
    private Context context;
    private int resAnimInID, resAnimOutID;

    public FloatingView(Context context) {
        this(context, null);
        this.context = context;
    }

    public FloatingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        this.context = context;

    }


    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FloatingView);
        mColor = a.getColor(R.styleable.FloatingView_FloatingView_color, Color.TRANSPARENT);
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonPaint.setColor(mColor);
        float radius, dx, dy;
        radius = a.getFloat(R.styleable.FloatingView_FloatingView_shadowRadius, 10.0f);
        dx = a.getFloat(R.styleable.FloatingView_FloatingView_shadowDx, 0.0f);
        dy = a.getFloat(R.styleable.FloatingView_FloatingView_shadowDy, 3.5f);
        int color = a.getInteger(R.styleable.FloatingView_FloatingView_shadowColor, Color.TRANSPARENT);
        mButtonPaint.setShadowLayer(radius, dx, dy, color);

        Drawable drawable = a.getDrawable(R.styleable.FloatingView_FloatingView_drawable);
        if (null != drawable) {
            mBitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        setWillNotDraw(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        WindowManager mWindowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        Display display = mWindowManager.getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            mYHidden = size.y;
        } else mYHidden = display.getHeight();
    }

    public static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    public void setColor(int color) {
        mColor = color;
        mButtonPaint.setColor(mColor);
        invalidate();
    }

    public void setDrawable(Drawable drawable) {
        mBitmap = ((BitmapDrawable) drawable).getBitmap();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), mButtonPaint);
        if (null != mBitmap) {
            canvas.drawBitmap(mBitmap, (getWidth() - mBitmap.getWidth()) / 2,
                    (getHeight() - mBitmap.getHeight()) / 2, mDrawablePaint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // Perform the default behavior
        super.onLayout(changed, left, top, right, bottom);

        // Store the FAB button's displayed Y position if we are not already aware of it
        if (mYDisplayed == -1) {

            mYDisplayed = ViewHelper.getY(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int color;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            color = mColor;
        } else {
            color = darkenColor(mColor);
        }
        mButtonPaint.setColor(color);
        invalidate();
        return super.onTouchEvent(event);
    }

    public void hide(boolean hide) {
        if (resAnimInID == 0) {
            resAnimInID = R.anim.slide_bottom_out;
        }
        if (resAnimOutID == 0) {
            resAnimOutID = R.anim.slide_bottom_in;
        }


        // If the hidden state is being updated
        if (mHidden != hide) {

            // Store the new hidden state
            mHidden = hide;

            // Animate the FAB to it's new Y position
//            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "y", mHidden ? mYHidden : mYDisplayed).setDuration(500);
//            animator.setInterpolator(mInterpolator);
//            animator.start();
            ZogUtils.printLog(FloatingView.class, "mHidden:" + mHidden + " " + resAnimInID + "#" + resAnimOutID);

            if (mHidden) {//隐藏
                AnimUtils.hidden(context, this, resAnimOutID);
            } else {//显示
                AnimUtils.show(context, this, resAnimInID);
            }
        }
    }


    public void setAnimRes(int resAnimInID, int resAnimOutID) {
        this.resAnimInID = resAnimInID;
        this.resAnimOutID = resAnimOutID;
    }

    public void listenTo(AbsListView listView) {
        ZogUtils.printLog(FloatingView.class, "listenTo listView: " + listView);

        if (null != listView) {
            listView.setOnScrollListener(new DirectionScrollListener(this));
        }
    }

    public void listenTo(KitScrollView scrollView) {
        if (null != scrollView) {
            scrollView.setOnScrollListener(new KitOnScrollListener(this));
        }
    }
}