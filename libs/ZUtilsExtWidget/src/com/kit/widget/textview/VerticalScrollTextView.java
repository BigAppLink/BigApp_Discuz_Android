package com.kit.widget.textview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

public class VerticalScrollTextView extends TextView {
	private float step = 0f;
	private Paint mPaint;
	private TextPaint tp;
	private String text;
	private float width;
	private float textSize = 0;
	private float speed = 0.3f;
	private int textColor = 0x000000;
	float length = 0;

	// int line = 0;

	private List<String> textList = new ArrayList<String>(); // 分行保存textview的显示信息。

	public VerticalScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(textColor, textSize, speed);
	}

	public VerticalScrollTextView(Context context) {
		super(context);
		init(textColor, textSize, speed);
	}

	public VerticalScrollTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(textColor, textSize, speed);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ScrollLayout only canmCurScreen run at EXACTLY mode!");
		}

		// System.out.println("text: " + text);
		if (text == null || text.length() == 0) {
			return;
		}

		// 下面的代码是根据宽度和字体大小，来计算textview显示的行数。

		textList.clear();

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {

			if (length < width) {
				builder.append(text.charAt(i));
				length += textSize;
				if (i == text.length() - 1) {

					textList.add(builder.toString());
					// System.out.println("now i:" + i + " line:" +
					// textList.size()
					// + " addstr:" + builder.toString());

				}
			} else {

				if (builder.toString().length() > 0) {
					textList.add(builder.toString().substring(0,
							builder.toString().length() - 1));

					// System.out
					// .println("now i:" + i + " line:" + textList.size()
					// + " addstr:" + builder.toString());

					builder.delete(0, builder.length() - 1);

				}
				length = mPaint.measureText(text.substring(i, i + 1));
				i--;
			}

		}
	}

	public void init(int textColor, float textSize, float speed) {
		setFocusable(true);

		this.textColor = textColor;
		this.textSize = textSize;
		this.speed = speed;

		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		tp = new TextPaint();
		tp.setColor(textColor);
		tp.setTextSize(textSize);
		tp.setAntiAlias(true);
		tp.setTypeface(Typeface.MONOSPACE);

	}

	// 下面代码是利用上面计算的显示行数，将文字画在画布上，实时更新。
	@Override
	public void onDraw(Canvas canvas) {
		if (textList.size() == 0)
			return;
		for (int i = 0; i < textList.size(); i++) {

			if (step < this.getHeight() + textList.size() * tp.getTextSize()) {
				canvas.drawText(textList.get(i), 0, this.getHeight() + (i + 1)
						* tp.getTextSize() - step, tp);
				canvas.drawText(" ", 0, 40, tp);
			} else {
				step = 0;
			}
		}

		invalidate();
		// System.out.println("this.getHeight(): " + this.getHeight());
		if (this.getHeight() < textList.size() * textSize) {
			step = step + speed;
		} else {
			step = this.getHeight();
		}

		// if (step >= this.getHeight() + textList.size() * tp.getTextSize()) {
		// step = 0;
		// }
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

}