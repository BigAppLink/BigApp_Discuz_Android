package com.kit.app.core.style;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.kit.app.drawable.gif.GifDrawableExtend;

import java.util.List;

/**
 * Created by Zhao on 14/11/8.
 */
public class ImageSpanExtend extends ImageSpan {

    public ImageSpanExtend(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }


    public ImageSpanExtend(Drawable drawable) {
        super(drawable);
    }


    public interface SetImageSpan {
        public void setSpan(Context context, String str, SpannableStringBuilder builder,
                            List list, int lineHeight, GifDrawableExtend.UpdateListener updateListener,boolean isUseGifAnim);
    }
}
