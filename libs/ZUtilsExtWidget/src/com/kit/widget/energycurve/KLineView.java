package com.kit.widget.energycurve;

import java.util.ArrayList;

import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.kit.utils.DensityUtils;
import com.kit.utils.DeviceUtils;
import com.kit.utils.MathExtend;
import com.kit.extend.widget.R;
import com.kit.utils.bitmap.BitmapUtils;

/**
 * 
 * @author keven.cheng
 * 
 */
public class KLineView extends View implements OnTouchListener {

	private static final float SPACING_SCALE = 40f; // 缩放间距 用于背景图过大
	private static final float SPACING_HEIGHT = 40f; // 距离顶部的高度
	private static final float WEIGHT = 20f; // y轴最高刻度 距离 y轴绘制的 间距
	private static final float SPACING = 35f; // 距离左边的间距
	private Bitmap mGradientLine; // 渐变线条,也就是渐变的北京图
	private Bitmap mDegree; // 度数显示框
	private Bitmap mTitleLine; // 点击 或 触摸后的线
	private Bitmap mTrendLine; // 点击显示的竖线
	private Bitmap mLastPoint; // 曲线图最后一个绘制灰色圆点
	private Bitmap mMovePoint; // 曲线图移动中绘制的圆点
	private float mGradientWidth; // 渐变条的宽度
	private float mGradientHeight; // 渐变条的高度

	private float layoutWidth; // 渐变条的宽度
	private float layoutHeight; // 渐变条的高度

	private DisplayMetrics dm; // 手机屏幕的宽高
	private ArrayList<PointF> points; // 有消耗的电量时间点
	private ArrayList<EnergyItem> energyItems;
	private float spacingOfX; // X间距
	private float spacingOfY; // Y间距,每一度的间距
	private int type = 0;
	private EnergyItem maxEnergy;
	private EnergyItem minEnergy;
	private float downXOfFirst; // 单个按下的X坐标
	private float downYOfFirst; // 单个按下的Y的坐标
	private float moveXOfFirst; // 单个按下的X坐标
	private float moveYOfFirst; // 单个按下的Y的坐标
	private float downXOfSecond; // 多个按下的X坐标
	private float downYOfSecond; // 多个按下的Y的坐标
	private float moveXOfSecond; // 多个按下的X坐标
	private float moveYOfSecond; // 多个按下的Y的坐标
	private boolean isDown = false; // 单独按下状态
	private boolean canMove = true; // 可以移动
	private int eventCount; // 总共有几个点被点击了

	private int lineNum = 4;// 行数

	private float startNum = 0;// 画点起始高度

	private float lineHeightNum = 0;// 每行间隔数

	private float perJiange;

	private boolean isMultiNum = true;

	private boolean touchEnabled = true;

	private String energyText;
	/**
	 * 刻度尺上数字的颜色
	 * */
	private int rulerNumColor = Color.parseColor("#854a0b");
	/**
	 * 刻度尺的颜色
	 * */
	private int rulerColor = Color.parseColor("#FFFFFF");
	/**
	 * 曲线的颜色
	 * */
	private int kLineColor = Color.parseColor("#854a0b");
	/**
	 * 底纹的颜色
	 * */
	private int shadeColor = Color.parseColor("#50909090");
	/**
	 * 刻度线粗细
	 * */
	private int kLineThickness = 6;

	private Context mContext;

	public KLineView(Context context) {
		super(context);
		this.mContext = context;
		init(context);
	}

	public KLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;

		this.dm = new DisplayMetrics();

		// 来跑一下，把dm的值得到
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		System.out.println("dm.widthPixels:" + dm.widthPixels
				+ " dm.heightPixels:" + dm.heightPixels);
		// 方式1获取属性
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.KLineView);

		layoutWidth = a.getDimension(R.styleable.KLineView_KLineView_width,
				dm.widthPixels);

		layoutHeight = a.getDimension(R.styleable.KLineView_KLineView_height,
				dm.heightPixels);

		Drawable gradientLineDrawable = a
				.getDrawable(R.styleable.KLineView_KLineView_gradientLine);
		if (gradientLineDrawable != null)
			mGradientLine = BitmapUtils.drawable2Bitmap(gradientLineDrawable);

		touchEnabled = a.getBoolean(
				R.styleable.KLineView_KLineView_touchEnabled, true);

		/**
		 * 刻度尺上数字的颜色
		 * */

		rulerNumColor = a.getColor(
				R.styleable.KLineView_KLineView_rulerNumColor, rulerNumColor);// "#854a0b"
		/**
		 * 刻度尺的颜色
		 * */
		rulerColor = a.getColor(R.styleable.KLineView_KLineView_rulerColor,
				rulerColor);// "#FFFFFF";
		/**
		 * 曲线的颜色
		 * */
		kLineColor = a.getColor(R.styleable.KLineView_KLineView_kLineColor,
				kLineColor);// "#854a0b";
		/**
		 * 底纹的颜色
		 * */
		shadeColor = a.getColor(R.styleable.KLineView_KLineView_shadeColor,
				shadeColor);// "#50909090";
		/**
		 * 刻度线粗细
		 * */
		kLineThickness = 6;

		init(context);
	}

	private void init(Context context) {

		this.setWindowsWH();

		setImages();
		setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		eventCount = event.getPointerCount();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downXOfFirst = event.getX(0);
			downYOfFirst = event.getY(0);
			isDown = !isDown;
			if (eventCount >= 2) {
				downXOfSecond = event.getX(1);
				downYOfSecond = event.getY(1);
			}
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			moveXOfFirst = event.getX(0);
			moveYOfFirst = event.getY(0);
			if (eventCount >= 2) {
				moveXOfSecond = event.getX(1);
				moveYOfSecond = event.getY(1);
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (eventCount >= 2) {
				Log.i("isDownOfDouble ACTION_UP", downYOfSecond + "");
				return true;
			}
			isDown = !isDown;
		}
		invalidate();
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if (touchEnabled) {
			return super.dispatchTouchEvent(event);
		} else {
			return false;
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint();
		if (energyItems == null || energyItems.isEmpty()) {
			energyItems = new ArrayList<EnergyItem>();
			EnergyItem eItem = new EnergyItem("1", 0.0f, "无");
			energyItems.add(eItem);
			initPoints(energyItems);
		}

		// 初始化绘制
		if (isMultiNum) {
			initDraw2(canvas, paint);
		} else {
			initDraw(canvas, paint);
		}
		// 点击屏幕时 进行的操作, 单点，多点

		if (touchEnabled) {
			if (isDown) {
				// if (eventCount >= 2) {
				// multiPointTouch(canvas, paint);
				// } else {
				SinglePointTouch(canvas, paint);
				// }
			}
		}
	}

	/**
	 * 初始化绘制
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void initDraw2(Canvas canvas, Paint paint) {
		paint.setColor(rulerColor);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		/* 裁剪出一个需要的矩阵图 */
		Path path = new Path();
		PointF point = null;
		path.moveTo(SPACING, mGradientHeight + SPACING_HEIGHT);
		for (int i = 0; i < points.size(); i++) {
			point = points.get(i);
			path.lineTo(point.x, point.y - SPACING_HEIGHT);
		}
		path.lineTo(point.x, mGradientHeight + SPACING_HEIGHT);
		path.lineTo(SPACING, mGradientHeight + SPACING_HEIGHT);
		path.close();
		Bitmap btm = Bitmap.createBitmap((int) mGradientWidth,
				(int) mGradientHeight, Config.ARGB_8888);
		Bitmap creatBitmap = creatBitmap(paint, path, btm);
		/* 绘制底部的横线、文字、以及向上的线条 */
		canvas.drawLine(SPACING, mGradientHeight + SPACING_HEIGHT,
				mGradientWidth + SPACING * 2, mGradientHeight + SPACING_HEIGHT,
				paint);
		for (int i = 0; i < energyItems.size(); i++) {
			EnergyItem energy = energyItems.get(i);
			PointF textPoint = points.get(i);
			paint.setColor(shadeColor);
			paint.setStrokeWidth(1);
			// 绘制底部 到上面的线
			canvas.drawLine(textPoint.x, mGradientHeight + SPACING_HEIGHT,
					textPoint.x, SPACING_HEIGHT + WEIGHT + 3, paint);
			// paint.setColor(keduchiNumColor));
			// TODO 可能需要删掉
			// 绘制底部的 文字
			// canvas.drawText(energy.date, textPoint.x - 15, mGradientHeight
			// + SPACING_HEIGHT + 20, paint);
		}
		/* 绘制虚线 */
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		PathEffect effects = new DashPathEffect(new float[] { 3, 3, 3, 3 }, 3);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(1);
		mPaint.setPathEffect(effects);
		float dottedSpacing = (mGradientHeight - WEIGHT) / lineNum;
		float smallDotted = dottedSpacing / 10;
		for (int i = 1; i <= lineNum; i++) {
			Path dottedPath = new Path();
			mPaint.setColor(shadeColor);
			mPaint.setAlpha(0x50);
			dottedPath.moveTo(SPACING, mGradientHeight + SPACING_HEIGHT
					- dottedSpacing * i);
			dottedPath.lineTo(mGradientWidth, mGradientHeight + SPACING_HEIGHT
					- dottedSpacing * i);
			canvas.drawPath(dottedPath, mPaint);
		}
		/* 左边刻度尺 */
		// float digit = ((maxEnergy.value) / lineNum + maxEnergy.value) /
		// lineNum;

		for (int i = 1; i <= lineNum; i++) {
			paint.setColor(rulerNumColor);
			paint.setStrokeWidth(1);
			// int digitInt = (int) (digit * i + 1.0e-6);
			float digitInt = (minEnergy.value - perJiange + ((i) * lineHeightNum));

			// System.out.println("#######digitInt:" + digitInt + " startNum:"
			// + startNum);
			// 左边刻度数
			canvas.drawText(
					String.valueOf(MathExtend.divideGetIntMore(digitInt, 1)),
					SPACING - 35, mGradientHeight + SPACING_HEIGHT
							- dottedSpacing * i + 5, paint);
			canvas.drawLine(SPACING, mGradientHeight + SPACING_HEIGHT
					- dottedSpacing * i, SPACING + 10, mGradientHeight
					+ SPACING_HEIGHT - dottedSpacing * i, paint);
			paint.setColor(rulerColor);

			for (int j = 0; j <= 10; j++) {
				if (j == 5) {
					canvas.drawLine(SPACING, mGradientHeight + SPACING_HEIGHT
							- dottedSpacing * i + smallDotted * j,
							SPACING + 10, mGradientHeight + SPACING_HEIGHT
									- dottedSpacing * i + smallDotted * j,
							paint);
				} else {
					canvas.drawLine(SPACING, mGradientHeight + SPACING_HEIGHT
							- dottedSpacing * i + smallDotted * j, SPACING + 5,
							mGradientHeight + SPACING_HEIGHT - dottedSpacing
									* i + smallDotted * j, paint);
				}
			}
		}
		// 绘制裁剪后的矩阵图
		canvas.drawBitmap(creatBitmap, 0, SPACING_HEIGHT, paint);

		paint.setColor(kLineColor);
		/* 绘制曲线 覆盖 剪切后的锯齿 */
		for (int i = 0; i < points.size(); i++) {
			paint.setStrokeWidth(kLineThickness);
			PointF startPoint = points.get(i);
			if (i + 1 == points.size()) {
				// 绘制最后一个圆点到底部的 竖线
				paint.setStrokeWidth(1);
				canvas.drawLine(startPoint.x, startPoint.y, startPoint.x,
						mGradientHeight + SPACING_HEIGHT, paint);
				// 绘制 最后一个圆点 为剪切的图片
				canvas.drawBitmap(mLastPoint,
						startPoint.x - mLastPoint.getWidth() / 2, startPoint.y
								- mLastPoint.getHeight() / 2, paint);
				break;
			}
			PointF endPoint = points.get(i + 1);
			// 绘制曲线，并且覆盖剪切后的锯齿
			canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y,
					paint);
			// 为了使线条圆滑 绘一个点 每个坐标系 绘制一个圆点
			canvas.drawPoint(startPoint.x, startPoint.y, paint);
		}

		paint.setColor(rulerColor);
		// 绘制左边的竖线,以及温度的刻度 （由于需要与顶边 产生间距SPACING_HEIGHT）———— 目前模拟 待修改
		canvas.drawLine(SPACING, SPACING_HEIGHT, SPACING, mGradientHeight
				+ SPACING_HEIGHT, paint);
		// TODO 可能需要删除此段代码
		// 绘制右边的 字（时、天、月）通过Type来进行修改———— 目前模拟 待修改
		// paint.setTextSize(16);
		// canvas.drawText("天", mGradientWidth + 5, mGradientHeight
		// + SPACING_HEIGHT + 5, paint);
		// canvas.drawText("度", SPACING - 8, 30, paint);
	}

	/**
	 * 初始化绘制
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void initDraw(Canvas canvas, Paint paint) {
		paint.setColor(rulerColor);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		/* 裁剪出一个需要的矩阵图 */
		Path path = new Path();
		PointF point = null;
		path.moveTo(SPACING, mGradientHeight + SPACING_HEIGHT);
		for (int i = 0; i < points.size(); i++) {
			point = points.get(i);
			path.lineTo(point.x, point.y - SPACING_HEIGHT);
		}
		path.lineTo(point.x, mGradientHeight + SPACING_HEIGHT);
		path.lineTo(SPACING, mGradientHeight + SPACING_HEIGHT);
		path.close();
		Bitmap btm = Bitmap.createBitmap((int) mGradientWidth,
				(int) mGradientHeight, Config.ARGB_8888);
		Bitmap creatBitmap = creatBitmap(paint, path, btm);
		/* 绘制底部的横线、文字、以及向上的线条 */
		canvas.drawLine(SPACING, mGradientHeight + SPACING_HEIGHT,
				mGradientWidth, mGradientHeight + SPACING_HEIGHT, paint);
		for (int i = 0; i < energyItems.size(); i++) {
			EnergyItem energy = energyItems.get(i);
			PointF textPoint = points.get(i);
			paint.setColor(shadeColor);
			paint.setStrokeWidth(1);
			// 绘制底部 到上面的线
			canvas.drawLine(textPoint.x, mGradientHeight + SPACING_HEIGHT,
					textPoint.x, SPACING_HEIGHT + WEIGHT + 3, paint);
			// paint.setColor(keduchiNumColor));
			// TODO 可能需要删掉
			// 绘制底部的 文字
			// canvas.drawText(energy.date, textPoint.x - 15, mGradientHeight
			// + SPACING_HEIGHT + 20, paint);
		}
		/* 绘制虚线 */
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		PathEffect effects = new DashPathEffect(new float[] { 3, 3, 3, 3 }, 3);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(1);
		mPaint.setPathEffect(effects);
		float dottedSpacing = (mGradientHeight - WEIGHT) / 4;
		float smallDotted = dottedSpacing / 10;
		for (int i = 1; i <= 4; i++) {
			Path dottedPath = new Path();
			paint.setColor(shadeColor);
			mPaint.setAlpha(0x50);
			dottedPath.moveTo(SPACING, mGradientHeight + SPACING_HEIGHT
					- dottedSpacing * i);
			dottedPath.lineTo(mGradientWidth, mGradientHeight + SPACING_HEIGHT
					- dottedSpacing * i);
			canvas.drawPath(dottedPath, mPaint);
		}
		/* 左边刻度尺 */
		float digit = ((maxEnergy.value) / 4 + maxEnergy.value) / 4;
		for (int i = 1; i <= 4; i++) {
			paint.setColor(rulerNumColor);
			paint.setStrokeWidth(1);
			int digitInt = (int) (digit * i + 1.0e-6);
			// 左边刻度数
			canvas.drawText(String.valueOf(digitInt), SPACING - 35,
					mGradientHeight + SPACING_HEIGHT - dottedSpacing * i + 5,
					paint);
			canvas.drawLine(SPACING, mGradientHeight + SPACING_HEIGHT
					- dottedSpacing * i, SPACING + 10, mGradientHeight
					+ SPACING_HEIGHT - dottedSpacing * i, paint);

			paint.setColor(rulerColor);
			for (int j = 0; j <= 10; j++) {
				if (j == 5) {
					canvas.drawLine(SPACING, mGradientHeight + SPACING_HEIGHT
							- dottedSpacing * i + smallDotted * j,
							SPACING + 10, mGradientHeight + SPACING_HEIGHT
									- dottedSpacing * i + smallDotted * j,
							paint);
				} else {
					canvas.drawLine(SPACING, mGradientHeight + SPACING_HEIGHT
							- dottedSpacing * i + smallDotted * j, SPACING + 5,
							mGradientHeight + SPACING_HEIGHT - dottedSpacing
									* i + smallDotted * j, paint);
				}
			}
		}
		// 绘制裁剪后的矩阵图
		canvas.drawBitmap(creatBitmap, 0, SPACING_HEIGHT, paint);

		/* 绘制曲线 覆盖 剪切后的锯齿 */
		paint.setColor(kLineColor);
		for (int i = 0; i < points.size(); i++) {
			paint.setStrokeWidth(kLineThickness);
			PointF startPoint = points.get(i);
			if (i + 1 == points.size()) {
				// 绘制最后一个圆点到底部的 竖线
				paint.setStrokeWidth(1);
				canvas.drawLine(startPoint.x, startPoint.y, startPoint.x,
						mGradientHeight + SPACING_HEIGHT, paint);
				// 绘制 最后一个圆点 为剪切的图片
				canvas.drawBitmap(mLastPoint,
						startPoint.x - mLastPoint.getWidth() / 2, startPoint.y
								- mLastPoint.getHeight() / 2, paint);
				break;
			}
			PointF endPoint = points.get(i + 1);
			// 绘制曲线，并且覆盖剪切后的锯齿
			canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y,
					paint);
			// 为了使线条圆滑 绘一个点 每个坐标系 绘制一个圆点
			canvas.drawPoint(startPoint.x, startPoint.y, paint);
		}
		paint.setColor(rulerColor);
		// 绘制左边的竖线,以及温度的刻度 （由于需要与顶边 产生间距SPACING_HEIGHT）———— 目前模拟 待修改
		canvas.drawLine(SPACING, SPACING_HEIGHT, SPACING, mGradientHeight
				+ SPACING_HEIGHT, paint);
		// TODO 可能需要删除此段代码
		// 绘制右边的 字（时、天、月）通过Type来进行修改———— 目前模拟 待修改
		// paint.setTextSize(16);
		// canvas.drawText("天", mGradientWidth + 5, mGradientHeight
		// + SPACING_HEIGHT + 5, paint);
		// canvas.drawText("度", SPACING - 8, 30, paint);
	}

	public void setGradientLine(Bitmap gradientLine) {
		this.mGradientLine = gradientLine;
	}

	public Bitmap getGradientLine() {
		// if(mGradientLine==null){
		// mGradientLine = BitmapFactory.decodeResource(getResources(),
		// R.drawable.energy_gradient_line3);
		// }
		return this.mGradientLine;
	}

	/**
	 * 单点触控操作
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void SinglePointTouch(Canvas canvas, Paint paint) {
		// 顶部的度数框
		if (moveXOfFirst < points.get(0).x) {
			moveXOfFirst = points.get(0).x;
		}
		if (moveXOfFirst > points.get(points.size() - 1).x) {
			moveXOfFirst = points.get(points.size() - 1).x;
		}
		float moveX = moveXOfFirst;
		// 绘制度数框 背景后的 横线
		canvas.drawBitmap(mTitleLine, SPACING, mGradientHeight + SPACING_HEIGHT
				- mTrendLine.getHeight() + 5, paint);
		// 绘制 移动的 点
		onPointMove(canvas, paint, moveXOfFirst, isDown);
		canvas.drawBitmap(
				mDegree,
				moveX - mDegree.getWidth() / 2,
				(mGradientHeight + SPACING_HEIGHT - mTrendLine.getHeight() + 5) / 2,
				paint);

		// 滑动时候顶部的指示器 绘制 变动的 能耗为多少

		// float moveY = getMoveY(moveXOfFirst);
		// float energyHeight = (float) (mGradientHeight + SPACING_HEIGHT) -
		// moveY;
		// String energyText = String.valueOf(energyHeight / spacingOfY);

		// 为了避免误差 如果单点 手指在X轴 在预定的 X点上 那么直接将显示读书设置为 服务器传回的数据
		EnergyItem energy = isInPoint(moveXOfFirst);
		if (energy != null) {
			energyText = String.valueOf(energy.value);
		}

		System.out.println("energyText:" + energyText);
		int indexOf = energyText.indexOf(".");
		String substring = energyText.substring(0, indexOf + 2);
		// String substring = energyText;
		paint.setTextSize(25);
		paint.setColor(Color.BLACK);
		// canvas.drawText(substring + "度", moveX - mDegree.getWidth() / 3,
		// mGradientHeight + SPACING_HEIGHT - mTrendLine.getHeight()
		// + mDegree.getHeight() / 3, paint);
		if (moveX > (mDegree.getWidth() / 3)) {
			canvas.drawText(substring, moveX - mDegree.getWidth() / 3,
					mGradientHeight + SPACING_HEIGHT - mTrendLine.getHeight()
							+ mDegree.getHeight() / 3, paint);
		} else {
			canvas.drawText(substring, moveX, mGradientHeight + SPACING_HEIGHT
					- mTrendLine.getHeight() + mDegree.getHeight() / 3, paint);
		}
	}

	// /**
	// * 多点触控操作
	// *
	// * @param canvas
	// * @param paint
	// */
	// private void multiPointTouch(Canvas canvas, Paint paint) {
	// paint.setColor(Color.rgb(240, 150, 40));
	// paint.setStrokeWidth(lineNum);
	// // 计算 多点选择 的两个点 分别处在 屏幕的 某个区域内 并且计算出区域中的点
	// float pointSpacing = 0.0f; // 两点之间的间距
	// if (moveXOfFirst < points.get(0).x) {
	// moveXOfFirst = points.get(0).x;
	// }
	// if (moveXOfFirst > points.get(points.size() - 1).x) {
	// moveXOfFirst = points.get(points.size() - 1).x;
	// }
	// if (moveXOfSecond < points.get(0).x) {
	// moveXOfSecond = points.get(0).x;
	// }
	// if (moveXOfSecond > points.get(points.size() - 1).x) {
	// moveXOfSecond = points.get(points.size() - 1).x;
	// }
	// int moveXOfOne = getLocation(moveXOfFirst);
	// int moveXOfTwo = getLocation(moveXOfSecond);
	// // 第一个点 小于 第二个点
	// if (moveXOfOne < moveXOfTwo) {
	// if (!(moveXOfSecond == points.get(points.size() - 1).x)) {
	// moveXOfTwo = moveXOfTwo - 1;
	// }
	// // 重绘 两点间的 的连接线
	// canvas.drawLine(moveXOfFirst, getMoveY(moveXOfFirst),
	// points.get(moveXOfOne).x, points.get(moveXOfOne).y, paint);
	// for (int j = moveXOfOne; j < moveXOfTwo; j++) {
	// PointF startPoint = points.get(j);
	// if (j + 1 == points.size()) {
	// // 绘制 最后一个圆点 为剪切的图片
	// canvas.drawBitmap(mLastPoint,
	// startPoint.x - mLastPoint.getWidth() / 2,
	// startPoint.y - mLastPoint.getHeight() / 2, paint);
	// break;
	// }
	// PointF endPoint = points.get(j + 1);
	// // 绘制曲线，并且覆盖剪切后的锯齿
	// canvas.drawLine(startPoint.x, startPoint.y, endPoint.x,
	// endPoint.y, paint);
	// // 为了使线条圆滑 绘一个点 每个坐标系 绘制一个圆点
	// canvas.drawPoint(startPoint.x, startPoint.y, paint);
	// }
	// canvas.drawLine(points.get(moveXOfTwo).x, points.get(moveXOfTwo).y,
	// moveXOfSecond, getMoveY(moveXOfSecond), paint);
	// }
	// // 第一个点 大于 第二个点
	// if (moveXOfOne > moveXOfTwo) {
	// if (!(moveXOfFirst == points.get(points.size() - 1).x)) {
	// moveXOfOne = moveXOfOne - 1;
	// }
	// // 重绘 两点间的 的连接线
	// canvas.drawLine(moveXOfSecond, getMoveY(moveXOfSecond),
	// points.get(moveXOfTwo).x, points.get(moveXOfTwo).y, paint);
	// for (int j = moveXOfTwo; j < moveXOfOne; j++) {
	// PointF startPoint = points.get(j);
	// if (j + 1 == points.size()) {
	// // 绘制 最后一个圆点 为剪切的图片
	// canvas.drawBitmap(mLastPoint,
	// startPoint.x - mLastPoint.getWidth() / 2,
	// startPoint.y - mLastPoint.getHeight() / 2, paint);
	// break;
	// }
	// PointF endPoint = points.get(j + 1);
	// // 绘制曲线，并且覆盖剪切后的锯齿
	// canvas.drawLine(startPoint.x, startPoint.y, endPoint.x,
	// endPoint.y, paint);
	// // 为了使线条圆滑 绘一个点 每个坐标系 绘制一个圆点
	// canvas.drawPoint(startPoint.x, startPoint.y, paint);
	// }
	// canvas.drawLine(points.get(moveXOfOne).x, points.get(moveXOfOne).y,
	// moveXOfFirst, getMoveY(moveXOfFirst), paint);
	// }
	// // 第一个点 等于 第二个点
	// if (getArea(moveXOfFirst) == getArea(moveXOfSecond)) {
	// canvas.drawLine(moveXOfFirst, getMoveY(moveXOfFirst),
	// moveXOfSecond, getMoveY(moveXOfSecond), paint);
	// }
	// float allEnergy = 0.0f; // 区域内的温度总和
	// // 第一点小于第二点的情况
	// if (moveXOfFirst < moveXOfSecond) {
	// pointSpacing = Math.abs(moveXOfSecond - moveXOfFirst) / 2
	// + moveXOfFirst;
	// // 得到两点间的 能量总计
	// for (int j = moveXOfOne; j <= moveXOfTwo; j++) {
	// allEnergy += energyItems.get(j).value;
	// }
	// }
	// // 第一点大于第二点的情况
	// if (moveXOfFirst > moveXOfSecond) {
	// pointSpacing = Math.abs(moveXOfFirst - moveXOfSecond) / 2
	// + moveXOfSecond;
	// for (int j = moveXOfTwo; j <= moveXOfOne; j++) {
	// allEnergy += energyItems.get(j).value;
	// }
	// }
	// // 第两点相等的情况
	// if (moveXOfFirst == moveXOfSecond) {
	// pointSpacing = Math.abs(moveXOfFirst);
	// }
	// // 绘制 移动的 点
	// onPointMove(canvas, paint, moveXOfFirst, isDown);
	// if (eventCount >= 2) {
	// onPointMove(canvas, paint, moveXOfSecond, isDown);
	// }
	// // 绘制度数框 背景后的 横线
	// canvas.drawBitmap(mTitleLine, SPACING, mGradientHeight + SPACING_HEIGHT
	// - mTrendLine.getHeight() + 5, paint);
	// // 顶部的度数框
	// canvas.drawBitmap(mDegree, pointSpacing - mDegree.getWidth() / 2
	// + mTrendLine.getWidth() / 2, (mGradientHeight + SPACING_HEIGHT
	// - mTrendLine.getHeight() + 5) / 2, paint);
	// // 绘制 总共消耗的能耗为多少
	// String energyText = String.valueOf(allEnergy);
	// int indexOf = energyText.indexOf(".");
	// String substring = energyText.substring(0, indexOf + 2);
	// paint.setTextSize(28);
	// paint.setColor(Color.BLACK);
	// // canvas.drawText(substring + "度", pointSpacing - mDegree.getWidth()
	// // / lineNum,
	// // mGradientHeight + SPACING_HEIGHT - mTrendLine.getHeight()
	// // + mDegree.getHeight() / 3, paint);
	//
	// canvas.drawText(substring, pointSpacing - mDegree.getWidth() / lineNum,
	// mGradientHeight + SPACING_HEIGHT - mTrendLine.getHeight()
	// + mDegree.getHeight() / 3, paint);
	// }

	/**
	 * 点击绘制的 黄色的 移动圆点
	 */
	private void onPointMove(Canvas canvas, Paint paint, float moveX,
			boolean down) {

		if (canMove) {
			if (down) {
				// 点住滑动 绘制的黄色线
				if (moveX < SPACING) {
					moveX = SPACING - mTrendLine.getWidth() / 2;
				}
				if (moveX > points.get(points.size() - 1).x) {
					moveX = points.get(points.size() - 1).x
							- mTrendLine.getWidth() / 2;
				}
				canvas.drawBitmap(mTrendLine,
						moveX - mTrendLine.getWidth() / 2, mGradientHeight
								+ SPACING_HEIGHT - mTrendLine.getHeight() + 5,
						paint);
				// 绘制移动中的点
				canvas.drawBitmap(mMovePoint,
						moveX - mMovePoint.getWidth() / 2, getMoveY(moveX)
								- mMovePoint.getWidth() / 2, paint);
			}
		}
	}

	/**
	 * 判断 目前手指在屏幕中X的点 是否在 结合点上
	 * 
	 * @param moveX
	 *            手指移动中的点
	 * @return
	 */
	private EnergyItem isInPoint(float moveX) {
		EnergyItem energy = null;
		if (!isMultiNum) {

			for (int i = 0; i < points.size(); i++) {

				if (moveX == points.get(i).x) {

					energy = energyItems.get(i);
					System.out.println(energy.date + ":" + energy.value);
					break;
				}
			}
		} else {
			for (int i = 0; i < points.size(); i++) {

				if (moveX > points.get(i).x - spacingOfX
						&& moveX < points.get(i).x + spacingOfX) {
					energy = energyItems.get(i);
					// System.out.println(energy.date + ":" + energy.value);

					System.out.println("moveX:" + moveX + " points.get(i).x:"
							+ points.get(i).x);

					break;
				}
			}
		}
		return energy;
	}

	/**
	 * 获取点在哪个集合段中的位置
	 */
	private int getArea(float moveX) {
		int flag = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			PointF point_1 = points.get(i);
			PointF point_2 = points.get(i + 1);
			if (point_1.x <= moveX && point_2.x >= moveX) {
				flag = i + 1;
				break;
			}
		}
		return flag;
	}

	/**
	 * 获取点在哪个集合段中的位置
	 */
	private int getLocation(float moveX) {
		int flag = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			PointF point_1 = points.get(i);
			PointF point_2 = points.get(i + 1);
			if (point_1.x < moveX && point_2.x > moveX) {
				flag = i + 1;
				break;
			}
			if (point_1.x == moveX) {
				flag = i;
				break;
			}
			if (point_2.x == moveX) {
				flag = i + 1;
				break;
			}
		}
		return flag;
	}

	/**
	 * 获取移动线条时 Y的值，
	 */
	private float getMoveY(float x) {
		float y = mGradientHeight + SPACING_HEIGHT;
		PointF first = null;
		PointF second = null;
		for (int i = 0; i < points.size() - 1; i++) {
			PointF point_1 = points.get(i);
			PointF point_2 = points.get(i + 1);
			if (point_1.x <= x && point_2.x >= x) {
				first = point_1;
				second = point_2;
				break;
			}
		}
		// 勾股定理 ：ｙ＝（ｙ２－ｙ１）／（ｘ２－ｘ１）＊（ｘ－ｘ１）＋ｙ１
		if (first != null || second != null)
			y = Math.abs((second.y - first.y) / (second.x - first.x)
					* (x - first.x) + first.y);
		return y;
	}

	/**
	 * 创建裁剪的背景图片
	 * 
	 * @param paint
	 * @param path
	 * @param bitmap
	 * @return
	 */
	private Bitmap creatBitmap(Paint paint, Path path, Bitmap bitmap) {
		Canvas newCanvas = new Canvas(bitmap);
		newCanvas.setDrawFilter(new PaintFlagsDrawFilter(0,
				Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		newCanvas.clipPath(path, Region.Op.INTERSECT);
		newCanvas.drawBitmap(mGradientLine, 0, 0, paint);
		return bitmap;
	}

	/**
	 * 初始化 加载 view 的资源图片
	 */
	public void setImages() {
		// 背景渐变色大图
		// mGradientLine = BitmapFactory.decodeResource(getResources(),
		// R.drawable.energy_gradient_line);

		// mGradientLine = BitmapFactory.decodeResource(getResources(),
		// R.drawable.energy_gradient_line3);

		int windowW = (int) (layoutWidth - SPACING_SCALE);
		if (mGradientLine == null) {
			mGradientLine = Bitmap.createBitmap(windowW,
					DensityUtils.dip2px(mContext, layoutHeight),
					Config.ARGB_4444);
		}
		int bitMapW = mGradientLine.getWidth();
		float gradientLineScale = (float) windowW / bitMapW;
		mGradientLine = scaleBmp(mGradientLine, gradientLineScale, 0.75f); // 缩放图层
		mGradientWidth = mGradientLine.getWidth() - SPACING * 2;
		mGradientHeight = mGradientLine.getHeight();

		System.out.println("mGradientWidth:" + mGradientWidth
				+ " mGradientHeight:" + mGradientHeight);

		// 移动的黄色的线条
		mTrendLine = BitmapFactory.decodeResource(getResources(),
				R.drawable.energy_trendline);
		int height = mTrendLine.getHeight();
		float trendLineScale = (float) mGradientHeight / height;
		mTrendLine = scaleBmp(mTrendLine, 1.0f, trendLineScale); // 缩放图层
		// 最后一个灰色的点
		mLastPoint = BitmapFactory.decodeResource(getResources(),
				R.drawable.energy_trendpoint_last);
		mLastPoint = scaleBmp(mLastPoint, 0.8f, 0.8f);
		// 移动中的黄色点
		mMovePoint = BitmapFactory.decodeResource(getResources(),
				R.drawable.energy_trendpoint_move);
		mMovePoint = scaleBmp(mMovePoint, 0.8f, 0.8f);
		// 顶部的温度框
		mDegree = BitmapFactory.decodeResource(getResources(),
				R.drawable.energy_degree);
		// 顶部温度框 下的 横线
		mTitleLine = BitmapFactory.decodeResource(getResources(),
				R.drawable.energy_title_line);
		float titleLineWidth = mTitleLine.getWidth();
		float titleLineScale = (float) mGradientWidth / titleLineWidth;
		mTitleLine = scaleBmp(mTitleLine, titleLineScale, 1.0f);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		// Log.e("onMeasure", widthMeasureSpec + ":" + heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 图片缩放
	 * 
	 * @param bitmap
	 *            需要缩放的图片
	 * @param scaleX
	 *            X缩放比例
	 * @param scaleY
	 *            Y缩放比例
	 * @return
	 */
	public Bitmap scaleBmp(Bitmap bitmap, float scaleX, float scaleY) {
		Bitmap scaleBmp = null;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		scaleBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
				false);
		bitmap.recycle();
		return scaleBmp;
	}

	public void setWindowsWH() {
		System.out.println(layoutWidth + " @@@@@@ " + layoutHeight);

		if (layoutWidth < 0) {
			layoutWidth = dm.widthPixels;
		}
		if (layoutHeight < 0) {
			layoutHeight = dm.heightPixels;
		}

		System.out.println(layoutWidth + " ######## " + layoutHeight);

	}

	/**
	 * 设置数据(初始化进行)
	 */
	public void setData(ArrayList<EnergyItem> energys, int type) {
		this.energyItems = energys;
		this.type = type;

		initPoints(energys);
		energyText = energyItems.get(0).value + "";
	}

	/**
	 * 通过数据 预先存入需要绘制的 连线点
	 * 
	 * 
	 * @param powers
	 * @param date
	 */
	private void initIsMultiNum(ArrayList<EnergyItem> energys) {

		if (energys.isEmpty()) {
			return;
		}

		float fTemp = energys.get(0).value;
		boolean isAllOnlyOneNum = true;
		for (int i = 0; i < energys.size(); i++) {
			float f = energys.get(i).value;
			isAllOnlyOneNum = isAllOnlyOneNum && (f == fTemp);

		}

		isMultiNum = !isAllOnlyOneNum;

		System.out.println("isAllOnlyOneNum:" + isAllOnlyOneNum
				+ " isMultiNum:" + isMultiNum);
	}

	/**
	 * 通过数据 预先存入需要绘制的 连线点
	 * 
	 * 
	 * @param powers
	 * @param date
	 */
	private void initPoints(ArrayList<EnergyItem> energys) {

		if (energys.isEmpty()) {
			return;
		}

		initIsMultiNum(energys);

		getSpacingOfXY(energys);

		points = new ArrayList<PointF>();
		for (int i = 0; i < energys.size(); i++) {
			float f = energys.get(i).value;
			float y = 0;
			float x = 0;
			if (!isMultiNum) {

				y = ((mGradientHeight + SPACING_HEIGHT) - f * spacingOfY);
				x = (i * spacingOfX + SPACING);
				PointF point = new PointF(x, y);
				points.add(point);

			} else {
				float num = (f - (minEnergy.value - perJiange))
						* (mGradientHeight);
				y = (mGradientHeight + WEIGHT + SPACING_HEIGHT)
						- (float) MathExtend.divide(num, maxEnergy.value
								- minEnergy.value + 2 * perJiange);

				System.out.println("y:" + y + " height:"
						+ (mGradientHeight - WEIGHT - SPACING_HEIGHT));
				// float y = ((mGradientHeight + SPACING_HEIGHT) - f *
				// spacingOfY);
				x = (i * spacingOfX + SPACING);
				PointF point = new PointF(x, y);
				points.add(point);

			}

			System.out.println("initPoints# date:" + energys.get(i).date
					+ " value:" + energys.get(i).value + " point.x:" + x
					+ " point.y:" + y);
			// Log.i("initPoints", energys.get(i).date + "|point.x:" + x
			// + "|point.y:" + y);
		}
	}

	/**
	 * 获取X的间距 以及 Y的间距
	 * 
	 * @param powers
	 * @param date
	 */
	private void getSpacingOfXY(ArrayList<EnergyItem> energys) {
		maxEnergy = findMaxPowers(energys);
		minEnergy = findMinPowers(energys);
		if (isMultiNum) {
			this.perJiange = (float) (MathExtend.divide(
					(maxEnergy.value - minEnergy.value), lineNum));
			this.lineHeightNum = (float) MathExtend
					.divide(((maxEnergy.value + perJiange) - (minEnergy.value - perJiange)),
							lineNum);

			this.startNum = (minEnergy.value - perJiange);

			spacingOfX = mGradientWidth / (energys.size()) + 1;
			spacingOfY = (mGradientHeight - WEIGHT)
					/ ((maxEnergy.value - startNum) / lineNum + (maxEnergy.value - startNum));
		} else {
			maxEnergy = findMaxPowers(energys);
			Log.i("maxEnergy", maxEnergy.value + "");
			spacingOfX = mGradientWidth / (energys.size()) + 1;
			spacingOfY = (mGradientHeight - WEIGHT)
					/ ((maxEnergy.value) / 4 + maxEnergy.value);
		}

		System.out.println("d:" + perJiange + " lineHeightNum:" + lineHeightNum
				+ " maxEnergy:" + maxEnergy.value + " minEnergy:"
				+ minEnergy.value);

		System.out.println("startNum:" + startNum + " " + spacingOfX + ":"
				+ spacingOfY);
	}

	/**
	 * 找到 数据集合中 最高能量 对应的脚标
	 * 
	 * @param powers
	 * @return
	 */
	private static EnergyItem findMaxPowers(ArrayList<EnergyItem> energys) {
		EnergyItem energy = new EnergyItem();
		energy.value = 0;
		for (int i = 0; i < energys.size(); i++) {
			if (energys.get(i).value > energy.value) {
				energy = energys.get(i);
			}
		}
		return energy;
	}

	/**
	 * 找到 数据集合中 最低能量 对应的脚标
	 * 
	 * @param powers
	 * @return
	 */
	private static EnergyItem findMinPowers(ArrayList<EnergyItem> energys) {
		EnergyItem energy = new EnergyItem();
		energy.value = energys.get(0).value;
		for (int i = 0; i < energys.size(); i++) {
			if (energys.get(i).value < energy.value) {
				energy = energys.get(i);
			}
		}
		return energy;
	}

	public void setTouchEnabled(boolean touchEnabled) {
		this.touchEnabled = touchEnabled;
	}

	public boolean getTouchEnabled() {
		return this.touchEnabled;
	}

	public int getKeduchiNumColor() {
		return rulerNumColor;
	}

	public void setKeduchiNumColor(int keduchiNumColor) {
		this.rulerNumColor = keduchiNumColor;
	}

	public int getKeduchiColor() {
		return rulerColor;
	}

	public void setKeduchiColor(int keduchiColor) {
		this.rulerColor = keduchiColor;
	}

	public int getQuxianColor() {
		return kLineColor;
	}

	public void setQuxianColor(int quxianColor) {
		this.kLineColor = quxianColor;
	}

	public int getDiwenColor() {
		return shadeColor;
	}

	public void setDiwenColor(int diwenColor) {
		this.shadeColor = diwenColor;
	}

	public boolean isTouchable() {
		return touchEnabled;
	}

}
