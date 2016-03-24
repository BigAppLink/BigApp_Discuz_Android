
package com.kit.bottomtabui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wuyexiong on 4/25/15.
 */
public class TabIconView extends ImageView {

    private Paint mPaint;
    private Bitmap mSelectedIcon;
    private Bitmap mNormalIcon;
    private Rect mSelectedRect;
    private Rect mNormalRect;
    private int mSelectedAlpha = 0;

    public TabIconView(Context context) {
        super(context);
    }

    public TabIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public final void init(int normal, int selected) {
        this.mNormalIcon = createBitmap(normal);
        this.mSelectedIcon = createBitmap(selected);


        this.mNormalRect = new Rect(0, 0, this.mNormalIcon.getWidth(), this.mNormalIcon.getHeight());
        this.mSelectedRect = new Rect(0, 0, this.mSelectedIcon.getWidth(),
                this.mSelectedIcon.getHeight());
        this.mPaint = new Paint(1);
    }

    public final void init(Drawable normal, Drawable selected) {
        this.mNormalIcon = drawableToBitmap(normal);
        this.mSelectedIcon = drawableToBitmap(selected);
        this.mNormalRect = new Rect(0, 0, this.mNormalIcon.getWidth(), this.mNormalIcon.getHeight());
        this.mSelectedRect = new Rect(0, 0, this.mSelectedIcon.getWidth(),
                this.mSelectedIcon.getHeight());
        this.mPaint = new Paint(1);
    }


    private Bitmap createBitmap(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }


    /**
     * Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * Bitmap to Drawable
     *
     * @param bitmap
     * @param mcontext
     * @return
     */
    public static Drawable bitmapToDrawble(Bitmap bitmap, Context mcontext) {
        Drawable drawable = new BitmapDrawable(mcontext.getResources(), bitmap);
        return drawable;
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (this.mPaint == null) {
//            return;
//        }
//
////        Log.e("APP", "getMeasuredWidth:" + getMeasuredWidth() + "  mNormalIcon.getWidth():" + mNormalIcon.getWidth());
//
//        int startNormalX = (getMeasuredWidth() - mNormalRect.width()) / 2 ;
//        int startNormalY = (getMeasuredHeight() - mNormalRect.height()) / 2;
//        this.mPaint.setAlpha(255 - this.mSelectedAlpha);
//        canvas.drawBitmap(this.mNormalIcon, startNormalX, startNormalY, this.mPaint);
//
//
//        int startSelectedX = (getMeasuredWidth() - mSelectedRect.width()) / 2;
//        int startSelectedY = (getMeasuredHeight() - mSelectedRect.height()) / 2;
//        this.mPaint.setAlpha(this.mSelectedAlpha);
//        canvas.drawBitmap(this.mSelectedIcon, startSelectedX, startSelectedY, this.mPaint);
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mNormalIcon, null, mNormalRect, null);
//        int alpha = (int) Math.ceil(255 * mAlpha);
        // 内存准备mBitmap, setAlpha, 纯色，xfermode, 图标
//        setupTargetBitmap(alpha);
                this.mPaint.setAlpha(255 - this.mSelectedAlpha);
        // 1.绘制源文本,2. 绘制变色文本
        canvas.drawBitmap(mNormalIcon, 0, 0, null);
    }






    public final void changeSelectedAlpha(int alpha) {
        this.mSelectedAlpha = alpha;
        invalidate();
    }

    public final void transformPage(float offset) {
        changeSelectedAlpha((int) (255 * (1 - offset)));
    }
}
