package com.kit.widget.listview;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.kit.extend.widget.R;
import com.kit.utils.ZogUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

/**
 * 
 * @ClassName ReboundListView
 * @Description 下拉时具有弹性的ListView
 * @author Zhao laozhao1005@gmail.com
 * @date 2014-5-31 下午1:56:43
 * 
 */
public class ReboundListView1 extends ListView implements OnScrollListener {
	private static final int PULL_DOWN_BACK_ACTION = 0x01;
	private static final int PULL_UP_BACK_ACTION = 0x02;

	private static final float PULL_FACTOR = 0.1F;// 下拉因子,实现下拉时的延迟效果
	private static final float PULL_BACK_REDUCE_STEP = 0.1f;// 回弹时每次减少的高度
	private static final int PULL_BACK_TASK_PERIOD = 50000;// 回弹时递减HeadView高度的频率,
															// 注意以纳秒为单位
	private boolean isRecordPullDown;
	private boolean isRecordPullUp;
	private int startPullDownY;// 记录刚开始下拉时的触摸位置的Y坐标
	private int startPullUpY;// 记录刚开始上拉时的触摸位置的Y坐标
	private int firstItemIndex;// 第一个可见条目的索引
	private int lastItemIndex;// 最后一个可见条目的索引
	private View mHeadView;// 用于实现下拉弹性效果的HeadView
	private View mTailView;// 用于实现上拉弹性效果的TailView
	private int currentScrollState;
	private ScheduledExecutorService schedulor;// 实现回弹效果的调度器
	// private ScheduledExecutorService schedulor_pull_up;// 实现往下回弹效果的调度器

	/**
	 * 实现回弹效果的handler,用于递减HeadView的高度并请求重绘
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case PULL_DOWN_BACK_ACTION:
				AbsListView.LayoutParams headerParams = (LayoutParams) mHeadView
						.getLayoutParams();
				// 递减高度
				headerParams.height -= PULL_BACK_REDUCE_STEP;
				mHeadView.setLayoutParams(headerParams);
				// 重绘
				mHeadView.invalidate();
				// 停止回弹时递减headView高度的任务
				if (headerParams.height <= 0) {
					schedulor.shutdownNow();
				}

				break;
			case PULL_UP_BACK_ACTION:
				AbsListView.LayoutParams footerParams = (LayoutParams) mTailView
						.getLayoutParams();
				// 递减高度
				footerParams.height -= PULL_BACK_REDUCE_STEP;
				mTailView.setLayoutParams(footerParams);
				// 重绘
				mTailView.invalidate();
				// 停止回弹时递减headView高度的任务
				if (footerParams.height <= 0) {
					schedulor.shutdownNow();
				}

				break;
			}
		}
	};

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public ReboundListView1(Context context, boolean isHeadViewNeed,
			boolean isTailViewNeed) {
		super(context);
		init(isHeadViewNeed, isTailViewNeed);
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param attr
	 */
	public ReboundListView1(Context context, AttributeSet attr) {
		super(context, attr);
		init(true, true);
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("deprecation")
	private void init(boolean isHeadViewNeed, boolean isTailViewNeed) {
		ZogUtils.printLog(getClass(), "isHeadViewNeed=" + isHeadViewNeed);
		ZogUtils.printLog(getClass(), "isTailViewNeed=" + isTailViewNeed);
		if (isHeadViewNeed) {
			// 监听滚动状态
			setOnScrollListener(this);
			// 创建PullListView的HeadView
			mHeadView = new View(this.getContext());
			// 默认白色背景,可以改变颜色, 也可以设置背景图片
			mHeadView.setBackgroundColor(getResources().getColor(
					R.color.transparent));
			// 默认高度为0
			mHeadView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.FILL_PARENT, 0));
			this.addHeaderView(mHeadView);
		}

		if (isTailViewNeed) {
			// 监听滚动状态
			setOnScrollListener(this);
			// 创建PullListView的HeadView
			mTailView = new View(this.getContext());
			// 默认白色背景,可以改变颜色, 也可以设置背景图片
			mTailView.setBackgroundColor(getResources().getColor(
					R.color.transparent));
			// 默认高度为0
			mTailView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.FILL_PARENT, 0));
			this.addFooterView(mTailView);
		}
	}

	/**
	 * 覆盖onTouchEvent方法,实现下拉回弹效果
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (!isRecordPullDown && !isRecordPullUp) {
				// it's not in pull down state or pull up state, break
				ZogUtils.printLog(getClass(),
                        "ACTION_UP it's not in pull down state or pull up state, break");
				break;
			}
			if (isPullDownState()) {
				ZogUtils.printLog(getClass(), "isRecordPullDown="
                        + isRecordPullDown);
				// 以一定的频率递减HeadView的高度,实现平滑回弹
				schedulor = Executors.newScheduledThreadPool(1);
				schedulor.scheduleAtFixedRate(new Runnable() {

					@Override
					public void run() {
						mHandler.sendEmptyMessage(PULL_DOWN_BACK_ACTION);

					}
				}, 0, PULL_BACK_TASK_PERIOD, TimeUnit.NANOSECONDS);

				setPullDownState(!isRecordPullDown);
			} else if (isPullUpState()) {
				ZogUtils.printLog(getClass(), "isRecordPullUp="
                        + isRecordPullUp);
				// 以一定的频率递减HeadView的高度,实现平滑回弹
				schedulor = Executors.newScheduledThreadPool(1);
				schedulor.scheduleAtFixedRate(new Runnable() {

					@Override
					public void run() {
						mHandler.sendEmptyMessage(PULL_UP_BACK_ACTION);

					}
				}, 0, PULL_BACK_TASK_PERIOD, TimeUnit.NANOSECONDS);

				setPullUpState(!isRecordPullUp);
			}

			break;

		case MotionEvent.ACTION_MOVE:
			ZogUtils.printLog(getClass(), "firstItemIndex=" + firstItemIndex);
			if (!isRecordPullDown && firstItemIndex == 0) {
				ZogUtils.printLog(getClass(), "firstItemIndex="
                        + firstItemIndex + " set isRecordPullDown=true");
				startPullDownY = (int) event.getY();
				setPullType(PULL_DOWN_BACK_ACTION);
			} else if (!isRecordPullUp && lastItemIndex == getCount()) {
				ZogUtils.printLog(getClass(), "lastItemIndex == getCount()"
                        + " set isRecordPullUp=true");
				startPullUpY = (int) event.getY();
				setPullType(PULL_UP_BACK_ACTION);
			}

			if (!isRecordPullDown && !isRecordPullUp) {
				// it's not in pull down state or pull up state, break
				ZogUtils.printLog(getClass(),
                        "ACTION_MOVE it's not in pull down state or pull up state, break");
				break;
			}

			if (isRecordPullDown) {
				int tempY = (int) event.getY();
				int moveY = tempY - startPullDownY;
				if (moveY < 0) {
					setPullDownState(false);
					break;
				}

				ZogUtils.printLog(getClass(), "tempY=" + tempY);
				ZogUtils.printLog(getClass(), "startPullDownY="
                        + startPullDownY);
				ZogUtils.printLog(getClass(), "moveY=" + moveY);
				mHeadView.setLayoutParams(new AbsListView.LayoutParams(
						LayoutParams.FILL_PARENT, (int) (moveY * PULL_FACTOR)));
				mHeadView.invalidate();
			} else if (isRecordPullUp) {
				int tempY = (int) event.getY();
				int moveY = startPullUpY - tempY;
				if (moveY < 0) {
					setPullUpState(false);
					break;
				}

				ZogUtils.printLog(getClass(), "tempY=" + tempY);
				ZogUtils.printLog(getClass(), "startPullUpY=" + startPullUpY);
				ZogUtils.printLog(getClass(), "moveY=" + moveY);
				mTailView.setLayoutParams(new AbsListView.LayoutParams(
						LayoutParams.FILL_PARENT, (int) (moveY * PULL_FACTOR)));
				mTailView.invalidate();
			}

			break;
		}
		return super.onTouchEvent(event);
	}

	private synchronized void setPullDownState(boolean state) {
		isRecordPullDown = state;
	}

	public boolean isPullDownState() {
		return isRecordPullDown;
	}

	private synchronized void setPullUpState(boolean state) {
		isRecordPullUp = state;
	}

	public boolean isPullUpState() {
		return isRecordPullUp;
	}

	/**
	 * set pull type
	 * 
	 * @param action
	 *            pull-down:PULL_DOWN_BACK_ACTION pull-up:PULL_UP_BACK_ACTION
	 */
	private synchronized void setPullType(final int action) {
		switch (action) {
		case PULL_DOWN_BACK_ACTION:
			isRecordPullDown = true;
			isRecordPullUp = false;
			break;
		case PULL_UP_BACK_ACTION:
			isRecordPullDown = false;
			isRecordPullUp = true;
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisiableItem,
			int visibleItemCount, int totalItemCount) {
		this.firstItemIndex = firstVisiableItem;
		this.lastItemIndex = firstVisiableItem + visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		currentScrollState = scrollState;
		ZogUtils.printLog(getClass(), "scrollState: "
                + getScrollStateString(currentScrollState));
	}

	private String getScrollStateString(int flag) {
		String str = "";
		switch (flag) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			str = "SCROLL_STATE_IDLE";
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			str = "SCROLL_STATE_TOUCH_SCROLL";
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			str = "SCROLL_STATE_FLING";
			break;
		default:
			str = "wrong state";
		}

		return str;
	}
}