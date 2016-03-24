package com.kit.widget.simplecalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.kit.utils.ToastUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * @author yuanzhi.cjy
 * 
 */
public class CalendarView extends View {
	public static final int DEFAULT_BOARD_SIZE = 50;
	private static float CELL_TEXT_SIZE;

	private int mCellWidth;
	private int mCellHeight;
	private double heightScale = 2f / 3f;// 默认应为1.0

	public static final int CURRENT_MOUNT = 0;
	public static final int NEXT_MOUNT = 1;
	public static final int PREVIOUS_MOUNT = -1;
	private static final String[] weekTitle = { "周日", "周一", "周二", "周三", "周四",
			"周五", "周六" };

	private Calendar mRightNow = null;
	private Cell mToday = null;
	private Cell mTouchedCell = null;

	private ArrayList<Cell> mMarkedCells = null;

	private Cell[][] mCells = new Cell[6][7];

	private OnMonthChangeListener monthChangeListener;
	MonthDisplayHelper mHelper;

	private Paint mBackgroundColor;
	private Paint mBackgroundColorToday;
	private Paint mBackgroundColorTouched;
	private Paint mWeekTitle;
	private Paint mLinePaint;
	private Paint mLinePaint2;

	private Context context;

	public CalendarView(Context context) {
		this(context, null);
		this.context = context;
	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initCalendarView();

	}

	private void initCalendarView() {
		mRightNow = Calendar.getInstance();
		mHelper = new MonthDisplayHelper(mRightNow.get(Calendar.YEAR),
				mRightNow.get(Calendar.MONTH), mRightNow.getFirstDayOfWeek());

		mBackgroundColor = new Paint();
		mBackgroundColorToday = new Paint();
		mBackgroundColorTouched = new Paint();
		mWeekTitle = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		mLinePaint = new Paint();
		mLinePaint2 = new Paint();

		mBackgroundColor.setColor(Color.WHITE);
		mBackgroundColorToday.setColor(Color.RED);
		mBackgroundColorToday.setAlpha(100);
		mBackgroundColorTouched.setColor(Color.BLUE);
		mBackgroundColorTouched.setAlpha(100);
		mWeekTitle.setColor(Color.rgb(135, 135, 135));
		mLinePaint.setColor(Color.BLACK);
		mLinePaint2.setColor(Color.rgb(90, 90, 90));
	}

	private void initCells() {
		class _calendar {
			public int year;
			public int month;
			public int day;
			public int whichMonth; // -1 为上月 1为下月 0为此月

			public _calendar(int y, int m, int d, int b) {
				year = y;
				month = m;
				day = d;
				whichMonth = b;
			}

			public _calendar(int y, int m, int d) { // 上个月 默认为
				this(y, m, d, PREVIOUS_MOUNT);
			}
		}
		;
		_calendar tmp[][] = new _calendar[6][7];

		for (int i = 0; i < tmp.length; i++) {
			int n[] = mHelper.getDigitsForRow(i);
			for (int d = 0; d < n.length; d++) {
				if (mHelper.isWithinCurrentMonth(i, d))
					tmp[i][d] = new _calendar(mHelper.getYear(),
							mHelper.getMonth() + 1, n[d], CURRENT_MOUNT);
				else if (i == 0) {
					tmp[i][d] = new _calendar(mHelper.getYear(),
							mHelper.getMonth(), n[d]);
				} else {
					tmp[i][d] = new _calendar(mHelper.getYear(),
							mHelper.getMonth() + 2, n[d], NEXT_MOUNT);
				}

			}
		}

		Calendar today = Calendar.getInstance();
		int thisDay = 0;
		mToday = null;
		if (mHelper.getYear() == today.get(Calendar.YEAR)
				&& mHelper.getMonth() == today.get(Calendar.MONTH)) {
			thisDay = today.get(Calendar.DAY_OF_MONTH);
		}
		// build cells
		Rect Bound = new Rect(getPaddingLeft(), mCellHeight + getPaddingTop(),
				mCellWidth + getPaddingLeft(), 2 * mCellHeight
						+ getPaddingTop());
		for (int week = 0; week < mCells.length; week++) {
			for (int day = 0; day < mCells[week].length; day++) {
				if (tmp[week][day].whichMonth == CURRENT_MOUNT) { // 此月 开始设置cell
					if (day == 0 || day == 6)
						mCells[week][day] = new RedCell(tmp[week][day].year,
								tmp[week][day].month, tmp[week][day].day,
								new Rect(Bound), CELL_TEXT_SIZE);
					else
						mCells[week][day] = new Cell(tmp[week][day].year,
								tmp[week][day].month, tmp[week][day].day,
								new Rect(Bound), CELL_TEXT_SIZE);
				} else if (tmp[week][day].whichMonth == PREVIOUS_MOUNT) { // 上月为gray
					mCells[week][day] = new GrayCell(tmp[week][day].year,
							tmp[week][day].month, tmp[week][day].day, new Rect(
									Bound), CELL_TEXT_SIZE);
				} else { // 下月为LTGray
					mCells[week][day] = new LTGrayCell(tmp[week][day].year,
							tmp[week][day].month, tmp[week][day].day, new Rect(
									Bound), CELL_TEXT_SIZE);
				}

				Bound.offset(mCellWidth, 0); // move to next column

				// get today
				if (tmp[week][day].day == thisDay
						&& tmp[week][day].whichMonth == 0) {
					mToday = mCells[week][day];
				}
			}
			Bound.offset(0, mCellHeight); // move to next row and first column
			Bound.left = getPaddingLeft();
			Bound.right = getPaddingLeft() + mCellWidth;
		}
	}

	public int getYear() {
		return mHelper.getYear();
	}

	public int getMonth() {
		return mHelper.getMonth() + 1;
	}

	public void updateThisMonth() {

		initCells();
		invalidate();

	}

	public void nextMonth() {
		mHelper.nextMonth();
		initCells();
		invalidate();
		if (monthChangeListener != null)
			monthChangeListener.onMonthChanged();
	}

	public void previousMonth() {
		mHelper.previousMonth();
		initCells();
		invalidate();
		if (monthChangeListener != null)
			monthChangeListener.onMonthChanged();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width = -1, height = -1;
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = DEFAULT_BOARD_SIZE;
			if (widthMode == MeasureSpec.AT_MOST && width > widthSize) {
				width = widthSize;
			}
		}
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = DEFAULT_BOARD_SIZE;
			if (heightMode == MeasureSpec.AT_MOST && height > heightSize) {
				height = heightSize;
			}
		}

		if (widthMode != MeasureSpec.EXACTLY) {
			width = height;
		}

		if (heightMode != MeasureSpec.EXACTLY) {
			height = width;
		}

		if (widthMode == MeasureSpec.AT_MOST && width > widthSize) {
			width = widthSize;
		}
		if (heightMode == MeasureSpec.AT_MOST && height > heightSize) {
			height = heightSize;
		}

		mCellWidth = (width - getPaddingLeft() - getPaddingRight()) / 7;
		mCellHeight = (int) (((height - getPaddingTop() - getPaddingBottom()) / 7) * heightScale);

		setMeasuredDimension(width, (int) (height * heightScale));

		System.out.println("heightScale: " + heightScale + " "
				+ "mCellHeight: " + mCellHeight + " "
				+ (int) (height * heightScale));

		CELL_TEXT_SIZE = mCellHeight * 0.5f;
		mWeekTitle.setTextSize(mCellHeight * 0.3f);
		initCells();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		System.out.println("ondraw");
		super.onDraw(canvas);
		// draw backrgound
		canvas.drawRect(getPaddingLeft(), getPaddingTop(), 7 * mCellWidth
				+ getPaddingLeft(), (7 * mCellHeight + getPaddingTop()),
				mBackgroundColor);

		Rect tempBound = new Rect(getPaddingLeft(), getPaddingTop(),
				getPaddingLeft() + mCellWidth, getPaddingTop() + mCellHeight);
		for (String str : weekTitle) {
			int dx, dy;
			dx = (int) (mWeekTitle.measureText(str) / 2);
			dy = (int) ((-mWeekTitle.ascent() + mWeekTitle.descent()) / 2);
			canvas.drawText(str, tempBound.centerX() - dx, tempBound.centerY()
					+ dy, mWeekTitle);
			tempBound.offset(mCellWidth, 0);
		}

		// draw cells
		for (Cell[] week : mCells) {
			for (Cell day : week) {
				day.draw(canvas);
			}
		}
		// draw today
		if (mToday != null) {
			Rect bound = mToday.getBound();
			canvas.drawRect(bound.left, bound.top, bound.right, bound.bottom,
					mBackgroundColorToday);
		}

		// draw touched
		if (mTouchedCell != null) {
			Rect bound = mTouchedCell.getBound();
			canvas.drawRect(bound.left, bound.top, bound.right, bound.bottom,
					mBackgroundColorTouched);

		}

		// draw marked
		if (mMarkedCells != null && !mMarkedCells.isEmpty()) {
			Rect bound = mTouchedCell.getBound();
			canvas.drawRect(bound.left, bound.top, bound.right, bound.bottom,
					mBackgroundColorTouched);
		}

		// draw vertical lines
		for (int c = 0; c <= 7; c++) {
			float x = c * mCellWidth + getPaddingLeft();
			canvas.drawLine(x - 0.5f, getPaddingTop(), x - 0.5f, 7
					* mCellHeight + getPaddingTop(), mLinePaint2);
			canvas.drawLine(x + 0.5f, getPaddingTop(), x + 0.5f, 7
					* mCellHeight + getPaddingTop(), mLinePaint);

		}
		// draw horizontal lines
		for (int r = 0; r <= 7; r++) {
			float y = r * mCellHeight + getPaddingTop();
			canvas.drawLine(getPaddingLeft(), y - 0.5f, 7 * mCellWidth
					+ getPaddingLeft(), y - 0.5f, mLinePaint);
			canvas.drawLine(getPaddingLeft(), y + 0.5f, 7 * mCellWidth
					+ getPaddingLeft(), y + 0.5f, mLinePaint2);
		}
	}

	public void setmTouchedCellAtPoint(int x, int y) {
		int lx = x - getPaddingLeft();
		int ly = y - getPaddingTop();

		int row = (int) (ly / mCellHeight);
		int col = (int) (lx / mCellWidth);

		if (col >= 0 && col < 7 && row >= 1 && row < 7) {
			mTouchedCell = mCells[row - 1][col];
		} else {
			mTouchedCell = null;
		}
		updateThisMonth();
	}

	public void setMarkedCellAtPoint(int x, int y) {
		int lx = x - getPaddingLeft();
		int ly = y - getPaddingTop();

		int row = (int) (ly / mCellHeight);
		int col = (int) (lx / mCellWidth);

		if (col >= 0 && col < 7 && row >= 1 && row < 7) {
			mTouchedCell = mCells[row - 1][col];
		} else {
			mTouchedCell = null;
		}

	}

	public void setMarkedCellWithDate(Date date) {

	}

	public Cell getCellAtPoint(int x, int y) {
		int lx = x - getPaddingLeft();
		int ly = y - getPaddingTop();

		int row = (int) (ly / mCellHeight);
		int col = (int) (lx / mCellWidth);
		Cell cell = null;
		if (col >= 0 && col < 7 && row >= 1 && row < 7) {
			cell = mCells[row - 1][col];
		} else {
			cell = null;
		}
		return cell;
	}

	private class GrayCell extends Cell {
		public GrayCell(int year, int month, int dayOfMon, Rect rect, float s) {
			super(year, month, dayOfMon, rect, s);
			mPaint.setColor(Color.GRAY);
		}
	}

	private class LTGrayCell extends Cell {
		public LTGrayCell(int year, int month, int dayOfMon, Rect rect, float s) {
			super(year, month, dayOfMon, rect, s);
			mPaint.setColor(Color.GRAY);
		}
	}

	private class RedCell extends Cell {
		public RedCell(int year, int month, int dayOfMon, Rect rect, float s) {
			super(year, month, dayOfMon, rect, s);
			mPaint.setColor(0xdddd0000);
		}

	}

	public Cell getmTouchedCell() {

		return mTouchedCell;

	}

	public void setmTouchedCell(Cell mTouchedCell) {
		this.mTouchedCell = mTouchedCell;
	}

	public void setMonthChangeListener(OnMonthChangeListener monthChangeListener) {
		this.monthChangeListener = monthChangeListener;
	}

	public interface OnMonthChangeListener {
		public void onMonthChanged();
	}
}
