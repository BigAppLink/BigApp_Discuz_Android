package com.youzu.clan.base.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class ImageHelper {
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = null;
		try {
			int bitmapWidth = Math.min(bitmap.getWidth(), bitmap.getHeight());
			output = Bitmap.createBitmap(bitmapWidth, bitmapWidth, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmapWidth, bitmapWidth);
			final RectF rectF = new RectF(rect);
			final float roundPx = pixels;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (Exception e) {
		}
		return output;
	}

	public static Bitmap getCircleBitmap(Bitmap bitmap) {

		if (bitmap == null) {
			return null;
		}
		return getRoundedCornerBitmap(bitmap, bitmap.getWidth());
	}

}