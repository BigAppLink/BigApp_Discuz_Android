package com.kit.widget.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class MarqueeTextView extends TextView implements OnClickListener {
	public final static String TAG = MarqueeTextView.class.getSimpleName();

	private float textLength = 0f;// 文本长度
	private float viewWidth = 0f;
	private float step = 0f;// 文字的横坐标
	private float y = 0f;// 文字的纵坐标
	private float temp_view_plus_text_length = 0.0f;// 用于计算的临时变量
	private float temp_view_plus_two_text_length = 0.0f;// 用于计算的临时变量
	public boolean isStarting = false;// 是否开始滚动
	private Paint paint = null;// 绘图样式
	private CharSequence text = "";// 文本内容
	private float speed = 0.5f;
	private int textColor = 0xFF000000;

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int color) {
		this.textColor = color;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public MarqueeTextView(Context context) {
		super(context);
		initView();
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		setOnClickListener(this);
	}

	public void init(float width) {
		text = super.getText();
		paint = super.getPaint();
		// Paint paint=new Paint();
		text = getText().toString();
		textLength = paint.measureText(text.toString());
		// viewWidth = getWidth();
		// if (viewWidth == 0) {
		// if (windowManager != null) {
		// Display display = windowManager.getDefaultDisplay();
		// viewWidth = display.getWidth();
		// }
		// }
		viewWidth = width;
		step = textLength;
		temp_view_plus_text_length = viewWidth + textLength;
		temp_view_plus_two_text_length = viewWidth + textLength * 2;
		y = getTextSize() + getPaddingTop();
		paint.setColor(textColor);
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.step = step;
		ss.isStarting = isStarting;

		return ss;

	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		step = ss.step;
		isStarting = ss.isStarting;

	}

	public static class SavedState extends BaseSavedState {
		public boolean isStarting = false;
		public float step = 0.0f;

		SavedState(Parcelable superState) {
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBooleanArray(new boolean[] { isStarting });
			out.writeFloat(step);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}
		};

		private SavedState(Parcel in) {
			super(in);
			boolean[] b = null;
			in.readBooleanArray(b);
			if (b != null && b.length > 0)
				isStarting = b[0];
			step = in.readFloat();
		}
	}

	public void startScroll() {
		isStarting = true;
		invalidate();
	}

	public void stopScroll() {
		isStarting = false;
		invalidate();
	}

	@Override
	public void onDraw(Canvas canvas) {
		// super.onDraw(canvas);

		canvas.drawText(text, 0, text.length(), temp_view_plus_text_length
				- step, y, paint);
		if (!isStarting) {
			return;
		}
		step += speed;
		if (step > temp_view_plus_two_text_length)
			step = textLength;
		invalidate();

	}

	@Override
	public void onClick(View v) {
		if (isStarting)
			stopScroll();
		else
			startScroll();

	}
}
