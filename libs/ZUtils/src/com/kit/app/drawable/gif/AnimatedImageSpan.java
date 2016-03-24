package com.kit.app.drawable.gif;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.style.DynamicDrawableSpan;

import com.kit.utils.ZogUtils;
import com.kit.utils.MathExtend;
import com.kit.utils.DrawableUtils;


public class AnimatedImageSpan extends DynamicDrawableSpan {
    private GifDrawableExtend mDrawable;


    /**
     * 行高
     */
    private int lineHeight;

    private int padding;
    /**
     */
    private int alignment;


    double scale = 1;

    public AnimatedImageSpan(GifDrawableExtend d) {
        super();
        mDrawable = d;

        setBound(d);

        // 用handler 通知继续下一帧
        init();
    }


    public AnimatedImageSpan(GifDrawableExtend d, int alignment, int lineHeight, int padding) {
        super();
        mDrawable = d;

        this.alignment = alignment;
        this.lineHeight = lineHeight;

        this.padding = padding;

        setBound(d);
        // 用handler 通知继续下一帧
        init();
//        this.mVerticalAlignment = alignment;
    }


    /**
     * 设置gif图片的尺寸
     *
     * @param drawable
     */
    private void setBound(Drawable drawable) {

        if (lineHeight > 0) {
            scale = MathExtend.divide(lineHeight, drawable.getIntrinsicHeight());
//            LogUtils.printLog(AnimatedImageSpan.class, "scale:" + scale, 10);

            int w = (int) (drawable.getIntrinsicWidth() * scale);
            int h = (int) (drawable.getIntrinsicHeight() * scale);
            drawable.setBounds(padding, padding, w, h);//这里设置图片的大小
        }

    }

    private void init() {
        final Handler mHandler = new Handler();
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mDrawable.nextFrame();
                // 设置下一个的延迟取决于当前帧的持续时间
                mHandler.postDelayed(this, mDrawable.getFrameDuration());
            }
        });

    }

    @Override
    public Drawable getDrawable() {
        return mDrawable.getDrawable();
    }

    /**
     * 代码跟父类代码相似，就是getCachedDrawable()替换成getDrawable（）,因为前者里面的图片是WeakReference，
     * 容易被gc回收，所以这里要避免这个问题
     */
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       FontMetricsInt fm) {
        Drawable d = getDrawable();

        if (lineHeight > 0) {
            return (int) (d.getIntrinsicWidth() * scale);
        } else {
            Rect rect = d.getBounds();

            if (fm != null) {
                fm.ascent = -rect.bottom;
                fm.descent = 0;

                fm.top = fm.ascent;
                fm.bottom = 0;
            }
            return rect.right;

        }
    }

    /**
     * 代码跟父类代码相似，就是getCachedDrawable()替换成getDrawable（）,因为前者里面的图片是WeakReference，
     * 容易被gc回收，所以这里要避免这个问题
     */
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        canvas.save();
        Drawable drawable = getDrawable();

        if (lineHeight > 0) {

            Bitmap bmp = DrawableUtils.zoomDrawable2Bitmap(drawable, 0, lineHeight - (padding * 2), true);

            int transY = bottom - lineHeight;

            transY -= paint.getFontMetricsInt().descent;

            if (alignment == ALIGN_BASELINE) {
            } else if (alignment == ALIGN_BOTTOM) {
            }

            canvas.translate(x, transY + padding);
            canvas.drawBitmap(bmp, padding, padding, paint);
            canvas.restore();
        } else {
//            Drawable drawable = DrawableUtils.zoomDrawable(getDrawable(), 0, lineHeight, true);

            int transY = bottom - drawable.getBounds().bottom;

            if (alignment == ALIGN_BASELINE) {
                transY -= paint.getFontMetricsInt().descent;
            } else if (alignment == ALIGN_BOTTOM) {
//            transY -= paint.getFontMetricsInt().descent;

                setBound(drawable);
//            transY = bottom - drawable.getBounds().bottom;
//            transY = transY

                transY -= paint.getFontMetricsInt().descent;
            }

            ZogUtils.printLog(AnimatedImageSpan.class, "transY:" + transY + " drawable:" + drawable, 30);

            canvas.translate(x, transY);

            drawable.draw(canvas);
            canvas.restore();
        }
    }
}
