package com.kit.widget.listview.pulltorefreshlistviewextend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.utils.ZogUtils;
import com.kit.utils.ScrollUtils;

public class PullToRefreshListView extends ListView implements OnScrollListener {

    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private PullToRefreshListViewListener mListViewListener;

    // -- header view
    private PullToRefreshListViewHeader mHeaderView;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;

    /**
     * 头部的真实高度
     */
    private int mHeaderViewRealHeight; // header view's height
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.

    // -- footer view
    private PullToRefreshListViewFooter mFooterView;
    private RelativeLayout mFooterViewContent;


    /**
     * 底部的真实高度
     */
    private int mFooterViewRealHeight; // footer view's height

    private boolean mEnablePullLoad = true;
    private boolean mPullLoading;
    private boolean mPullLoaded;
    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400; // scroll back duration

    private static int PULL_LOAD_MORE_DELTA = 200; // 拉出什么间距的时候开始刷新
    private static int PULL_REFRESH_DELTA = 200; // 拉起什么间距的时候开始刷新

    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    // feature.


    private boolean hasHeaderMeasured, hasFooterMeasured;

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context, attrs);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context, attrs);
    }

    private void initWithContext(Context context, AttributeSet attrs) {
        ScrollUtils.disableOVER_SCROLL_NEVER(this);

        mScroller = new Scroller(context, new DecelerateInterpolator());
        // PullToRefreshListView need the scroll event, and it will dispatch the
        // event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new PullToRefreshListViewHeader(context, attrs);
        mHeaderViewContent = (RelativeLayout) mHeaderView
                .findViewById(R.id.pulltorefresh_listview_header_content);
        mHeaderTimeView = (TextView) mHeaderView
                .findViewById(R.id.pulltorefresh_listview_header_time);
        addHeaderView(mHeaderView);
        //设置加载更多为不可点击
        mHeaderView.setClickable(false);

        mHeaderViewRealHeight = (int) mHeaderView.getHeaderRealHeight();

        // init footer view
        mFooterView = new PullToRefreshListViewFooter(context, attrs);
        mFooterViewContent = (RelativeLayout) mFooterView
                .findViewById(R.id.pulltorefresh_listview_footer_content);

        mFooterViewRealHeight = (int) mFooterView.getFooterRealHeight();
//        mFooterViewRealHeight = 120;


        PULL_REFRESH_DELTA = mHeaderViewRealHeight;
        PULL_LOAD_MORE_DELTA = mFooterViewRealHeight;

        setPullRefreshEnable(mEnablePullRefresh);
        setPullLoadMoreEnable(mEnablePullRefresh);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure PullToRefreshListViewFooter is the last footer view, and
        // only add once.
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
        }
        super.setAdapter(adapter);
    }


    /**
     * 是否启用下拉刷新
     *
     * @param enable
     */
    private void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderView.hide();
            //mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mPullRefreshing = false;
            //mHeaderView.show();
            mHeaderView.hide();
            mHeaderView.setState(PullToRefreshListViewHeader.STATE_NORMAL);

            //mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 启用加载更多，会显示“下拉刷新”按钮，点击“下拉刷新”按钮加载更多
     *
     * @param enable
     */
    private void setPullLoadMoreEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
        } else {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(PullToRefreshListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * 开始刷新
     */
    private void startRefresh() {
        ZogUtils.printLog(PullToRefreshListView.class, "startRefresh!!!!!!!!!!!!!!!!");


        preRefresh();

        if (mListViewListener != null) {
            mListViewListener.onRefresh();
        }
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {

        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            mHeaderView.setState(PullToRefreshListViewHeader.STATE_NORMAL);
            resetHeaderHeight();
        }

        resetFooterHeight();

    }

    /**
     * 开始加载更多
     */
    private void startLoadMore() {
//
//        mPullLoading = true;
//        mFooterView.setState(PullToRefreshListViewFooter.STATE_LOADING);

        preLoadMore();

        if (mListViewListener != null) {
            mListViewListener.onLoadMore();
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        ZogUtils.printLog(PullToRefreshListView.class, "stopLoadMore!!!!!!!!!!!!!!!!");

        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.setState(PullToRefreshListViewFooter.STATE_NORMAL);
            resetFooterHeight();

        }

        resetHeaderHeight();
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    /**
     * 更新头部高度
     *
     * @param delta
     */
    private void updateHeaderHeight(float delta) {

//        LogUtils.printLog(PullToRefreshListView.class, "delta:" + delta);
//        if (mPullRefreshing&&mHeaderView.getVisiableHeight()>=mHeaderView.getHeaderRealHeight()) {
//            delta = 0;
//        }
        mHeaderView.setVisiableHeight((int) delta
                + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) {
            if (mHeaderView.getVisiableHeight() > PULL_REFRESH_DELTA) {// 什么时候开始刷新
                mHeaderView.setState(PullToRefreshListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(PullToRefreshListViewHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }


    /**
     * @param delta
     */
    private void updateFooterHeight(float delta) {

        mFooterView.setVisiableHeight((int) delta
                + mFooterView.getVisiableHeight());

//        int height = mFooterView.getBottomMargin() + (int) delta;
//                mFooterView.setBottomMargin(height);
        int height = mFooterView.getVisiableHeight() - mFooterViewRealHeight;

        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) { // 什么时候开始加载更多
                // more.
                mFooterView.setState(PullToRefreshListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(PullToRefreshListViewFooter.STATE_NORMAL);
            }
        }
//        setSelection(this.getCount() - 1);
    }


    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {

//        LogUtils.printLog(getClass(), "--------------------resetHeaderHeight");

        int height = mHeaderView.getVisiableHeight();
//        LogUtils.printLog(getClass(), "mHeaderView.getVisiableHeight():" + height);

        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewRealHeight) {
//            updateHeaderHeight(mHeaderViewRealHeight - height);

            // 回弹，刷新的时候不让头部隐藏掉
            mScroller.startScroll(0, height, 0, mHeaderViewRealHeight - height,
                    SCROLL_DURATION);
            return;
        }
        int finalHeight = 0;

//        LogUtils.printLog(getClass(), "header finalHeight:" + finalHeight + " mHeaderViewRealHeight:" + mHeaderViewRealHeight);
        if (mPullRefreshing) {
            finalHeight = mHeaderViewRealHeight;
        }
//        LogUtils.printLog(getClass(), "header finalHeight:" + finalHeight + " mHeaderViewRealHeight:" + mHeaderViewRealHeight);

        mScrollBack = SCROLLBACK_HEADER;

        // 复原，把下拉刷新的头部隐藏调
        mScroller.startScroll(0, height, 0, finalHeight - height,
                SCROLL_DURATION);

        invalidate();
    }

    private void resetFooterHeight() {

        int height = mFooterView.getVisiableHeight();

//        LogUtils.printLog(getClass(), "footer height:" + height
//                + " mFooterView.isFooterVisable():" + mFooterView.isFooterVisable());

        if (height == 0) {// not visible.
            if (mFooterView.isFooterVisable()) {
                updateFooterHeight(mFooterViewRealHeight);
            } else {
                updateFooterHeight(0);
            }
            return;
        }


        int finalHeight;


        if (mFooterView.isFooterVisable()) {
            finalHeight = mFooterViewRealHeight;
        } else {
            finalHeight = 0;
        }

        // is refreshing, just scroll back to show all the header.
        if (mPullLoading) {
            finalHeight = mFooterViewRealHeight;
        }

        mScrollBack = SCROLLBACK_FOOTER;

        mScroller.startScroll(0, height, 0, (finalHeight - height),
                SCROLL_DURATION);

        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                LogUtils.printLog(PullToRefreshListView.class, "ACTION_DOWN");
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
//                LogUtils.printLog(PullToRefreshListView.class, "ACTION_MOVE");
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0
                        && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1
                        && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
//                LogUtils.printLog(PullToRefreshListView.class, "ACTION default");
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
//                    LogUtils.printLog(PullToRefreshListView.class, "mHeaderView.getVisiableHeight():" + mHeaderView.getVisiableHeight());
                    if (mEnablePullRefresh
                            && (mHeaderView.getVisiableHeight() > PULL_REFRESH_DELTA) && !mPullRefreshing) {
                        // invoke refresh
                        startRefresh();
                    }
                    resetHeaderHeight();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
//                    LogUtils.printLog(PullToRefreshListView.class, "mFooterView.getVisiableHeight():" + mFooterView.getVisiableHeight());
                    if (mEnablePullLoad
                            && (mFooterView.getVisiableHeight() - mFooterViewRealHeight) > PULL_LOAD_MORE_DELTA
                            && !mPullLoading) {
                        // invoke load more.
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {

                //FIXME
                mFooterView.setVisiableHeight(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    /**
     * @param
     * @return void 返回类型
     * @Title setPullToRefreshListViewListener
     * @Description 注意setPullToRefreshListViewListener一定要在setPullLoadEnable、
     * setPullRefreshEnable之上setPullToRefreshListViewListener的onRefresh
     * 、onLoadMore才会生效
     */
    public void setPullToRefreshListViewListener(
            PullToRefreshListViewListener listener) {
        mListViewListener = listener;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }


    public void preRefresh() {
        mFooterView.setVisiableHeight(0);

        mPullRefreshing = true;
        mHeaderView.setState(PullToRefreshListViewHeader.STATE_REFRESHING);
        resetHeaderHeight();
    }

    public void preLoadMore() {
        mHeaderView.setVisiableHeight(0);

        mPullLoading = true;
        mFooterView.setState(PullToRefreshListViewFooter.STATE_LOADING);
        resetFooterHeight();
    }


}
