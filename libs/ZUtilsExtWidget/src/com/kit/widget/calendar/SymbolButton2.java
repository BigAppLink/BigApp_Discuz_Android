package com.kit.widget.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.Button;

public class SymbolButton2 extends Button {
	// fields
	private static final int iColor = 0xffaaaaaa;
	private static final int iColorActive = 0xff442200;

	// fields
	public enum symbol {
		none, arrowLeft, arrowRight
	};

	// fields
	private Paint pt = new Paint();
	private RectF rect = new RectF();
	private RectF rectDraw = new RectF();
	private symbol symbolType = symbol.none;

	// methods
	public SymbolButton2(Context context, symbol symbolType) {
		super(context);
		this.symbolType = symbolType;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		pt.setAntiAlias(true);
		pt.setStrokeCap(Paint.Cap.ROUND);

		rectDraw.set(0, 0, getWidth(), getHeight());
		rectDraw.left += 6;
		rectDraw.right -= 6;
		rectDraw.top += 4;
		rectDraw.bottom -= 8;

		if (symbolType != symbol.none) {
			pt.setStrokeWidth(5);

			pt.setColor(iColor);
			if (this.isPressed() || this.isFocused())
				pt.setColor(iColorActive);

			drawArrow(canvas);
		}
	}

	private void drawArrow(Canvas canvas) {
		rect.set(rectDraw);
		rect.inset(15, 5);
		canvas.drawLine(rect.left, rect.centerY(), rect.right, rect.centerY(),
				pt);
		if (symbolType == symbol.arrowRight) {
			canvas.drawLine(rect.right, rect.centerY(), rect.right - 6,
					rect.top, pt);
			canvas.drawLine(rect.right, rect.centerY(), rect.right - 6,
					rect.bottom, pt);
		}
		if (symbolType == symbol.arrowLeft) {
			canvas.drawLine(rect.left, rect.centerY(), rect.left + 6, rect.top,
					pt);
			canvas.drawLine(rect.left, rect.centerY(), rect.left + 6,
					rect.bottom, pt);
		}
	}

}
