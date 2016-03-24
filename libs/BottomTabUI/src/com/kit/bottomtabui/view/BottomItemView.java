package com.kit.bottomtabui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.kit.bottomtabui.R;


public class BottomItemView extends View {
    private static final int XIAODUI = 5;

    private int mNormalColor = 0xFF45C01A;
    private int mSelectedColor = 0xFF45C01A;
    private Bitmap mIconBitmapNormal;
    private Bitmap mIconBitmapSelected;
    private String mText = "微信";
    private int mTextSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;

    private float mAlpha;

    //图标所画的实际大小
    private Rect mIconRect;

    //图标画布的X轴开始位置
    int drawStartX;

    //图标画布的Y轴开始位置
    int drawStartY;

    //图标的画布大小
    private Rect mIconLayoutRect;

    private Rect mTextBound;

    private Paint mTextPaint;

    private int mIconMargin = 0;


    /**
     * 图片与文字的间距
     */
    private int mText2IconHeight;


    public BottomItemView(Context context) {
        this(context, null);
    }

    public BottomItemView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    /**
     * 获取自定义属性的值
     */
    public BottomItemView(Context context, AttributeSet attr,
                          int defStyleAttr) {
        super(context, attr, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attr,
                R.styleable.BottomItemView);

        BitmapDrawable drawableNormal = (BitmapDrawable) a.getDrawable(R.styleable.BottomItemView_BottomItemView_icon_normal);
        mIconBitmapNormal = drawableNormal.getBitmap();

        BitmapDrawable drawableSelected = (BitmapDrawable) a.getDrawable(R.styleable.BottomItemView_BottomItemView_icon_normal);
        mIconBitmapSelected = drawableSelected.getBitmap();

        mNormalColor = a.getColor(R.styleable.BottomItemView_BottomItemView_normal_color, getResources().getColor(R.color.material_blue_grey_800));
        mSelectedColor = a.getColor(R.styleable.BottomItemView_BottomItemView_selected_color, getResources().getColor(R.color.notify_red));
        mIconMargin = (int) a.getDimension(R.styleable.BottomItemView_BottomItemView_icon_margin, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
                        getResources().getDisplayMetrics()));

        mText = a.getString(R.styleable.BottomItemView_BottomItemView_text);
        mTextSize = (int) a.getDimension(R.styleable.BottomItemView_BottomItemView_text_size, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                        getResources().getDisplayMetrics()));

        a.recycle();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff555555);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
//        mTextPaint.measureText(mText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void measureIconAndText() {
        //测量文字的宽高
        if (!TextUtils.isEmpty(mText)) {
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        } else {
            mTextBound.set(0, 0, 0, 0);
        }


        int iconLayoutWidth = getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight() - 2 * mIconMargin;
        int iconLayoutHeight = getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom() - mTextBound.height() - mText2IconHeight - 2 * mIconMargin;

        drawStartX = getPaddingLeft() + mIconMargin;
        drawStartY = getPaddingTop() + mIconMargin;


        //图标的画布大小
        mIconLayoutRect = new Rect(drawStartX, drawStartY, drawStartX + iconLayoutWidth, drawStartY + iconLayoutHeight);

        float scale = getScale(mIconBitmapNormal, mIconLayoutRect.width(), mIconLayoutRect.height());
        try {
            mIconBitmapNormal = getScaleBitmap(mIconBitmapNormal, (int) (mIconBitmapNormal.getWidth() * scale), (int) (mIconBitmapNormal.getHeight() * scale));
        } catch (Exception e) {
            //      Log.e("APP", "BottomItemView create bitmap ERROR!!!!!!!!!!!!");
        }

        if (mIconBitmapSelected != null) {
            float scaleSelected = getScale(mIconBitmapSelected, mIconLayoutRect.width(), mIconLayoutRect.height());
            try {
                mIconBitmapSelected = getScaleBitmap(mIconBitmapSelected, (int) (mIconBitmapSelected.getWidth() * scaleSelected), (int) (mIconBitmapSelected.getHeight() * scaleSelected));
            } catch (Exception e) {
                //      Log.e("APP", "BottomItemView create bitmap ERROR!!!!!!!!!!!!");
            }
        }


//      Log.e("APP", "-------mIconBitmapNormal.getWidth():" + mIconBitmapNormal.getWidth() + " mIconBitmapNormal.getHeight():" + mIconBitmapNormal.getHeight());
//      Log.e("APP", "mIconLayoutRect.width():" + mIconLayoutRect.width() + " mIconLayoutRect.height():" + mIconLayoutRect.height());

        //图标所画的实际大小
        mIconRect = new Rect(drawStartX + (mIconLayoutRect.width() - mIconBitmapNormal.getWidth()) / 2,
                drawStartY + (mIconLayoutRect.height() - mIconBitmapNormal.getHeight()) / 2,
                drawStartX + (mIconLayoutRect.width() + mIconBitmapNormal.getWidth()) / 2,
                drawStartY + (mIconLayoutRect.height() + mIconBitmapNormal.getHeight()) / 2);


    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.e("APP", "onDraw onDraw onDraw onDraw mAlpha：" + mAlpha);

        measureIconAndText();


        //算出透明度
        int alpha = (int) Math.ceil(255 * mAlpha);

        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();

        // 绘制原图标和选中图标
        drawSourceBitmap(canvas,alpha);
        drawTargetBitmap(canvas, alpha);

        // 1.绘制源文本,2. 绘制变色文本

        drawSourceText(canvas, alpha);
        drawTargetText(canvas, alpha);


    }
    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mNormalColor);
        mTextPaint.setAlpha(255 - alpha);
        mTextPaint.setAntiAlias(true);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconLayoutRect.bottom + mIconMargin + mTextBound.height() + mText2IconHeight;

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawText(mText, x, y - XIAODUI, mTextPaint);
    }

    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mSelectedColor);
        mTextPaint.setAlpha(alpha);
        mTextPaint.setAntiAlias(true);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconLayoutRect.bottom + mIconMargin + mTextBound.height() + mText2IconHeight;

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawText(mText, x, y - XIAODUI, mTextPaint);
    }

    private void drawSourceBitmap(Canvas canvas,int alpha) {
        mPaint.setColor(mNormalColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(255);
        mCanvas.drawRect(mIconRect, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255-alpha);

        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        mCanvas.drawBitmap(mIconBitmapNormal, mIconRect.left, mIconRect.top, mPaint);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }


    private void drawTargetBitmap(Canvas canvas,int alpha) {
//        mCanvas =canvas;
        mPaint = new Paint();
        mPaint.setColor(mSelectedColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconRect, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);

        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        if (mIconBitmapSelected != null)
            mCanvas.drawBitmap(mIconBitmapSelected, mIconRect.left, mIconRect.top, mPaint);
        else
            mCanvas.drawBitmap(mIconBitmapNormal, mIconRect.left, mIconRect.top, mPaint);
        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        invalidateView();
    }

    /**
     * 重绘
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_ALPHA = "status_alpha";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_ALPHA, mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mAlpha = bundle.getFloat(STATUS_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            return;
        }
        super.onRestoreInstanceState(state);
    }


    private Bitmap getScaleBitmap(Bitmap viewBg, float scalX, float scalY) {
        Matrix matrix = getBitmapMatrix(viewBg, scalX, scalY);

        int width = viewBg.getWidth();//获取资源位图的宽
        int height = viewBg.getHeight();//获取资源位图的高

        Bitmap dstbmp = Bitmap.createBitmap(viewBg, 0, 0,
                width, height, matrix, true); //根据缩放比例获取新的位图

        return dstbmp;
    }


    private Matrix getBitmapMatrix(Bitmap viewBg, float scalX, float scalY) {
        Matrix matrix = new Matrix();
        float max = getScale(viewBg, scalX, scalY);
        matrix.postScale(max, max);//获取缩放比例
        return matrix;
    }

    private float getScale(Bitmap viewBg, float scalX, float scalY) {
        float w = scalX / viewBg.getWidth();
        float h = scalY / viewBg.getHeight();
        float scale = Math.min(w, h);
        return scale;
    }


    public Bitmap getNormalIcon() {
        return mIconBitmapNormal;
    }

    public void setNormalIcon(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        mIconBitmapNormal = bd.getBitmap();
    }

    public Bitmap getSelectedIcon() {
        return mIconBitmapSelected;
    }

    public void setSelectedIcon(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        mIconBitmapSelected = bd.getBitmap();
    }


    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }


    public int getNormalColor() {
        return mNormalColor;
    }

    public void setNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int mSelectedColor) {
        this.mSelectedColor = mSelectedColor;
    }


    public int getText2IconHeight() {
        return mText2IconHeight;
    }

    public void setText2IconHeight(int mText2IconHeight) {
        this.mText2IconHeight = mText2IconHeight;
        if (mText == null || mText.length() < 1) {
            this.mText2IconHeight = 0;
        }
    }
}
