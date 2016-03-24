/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.kit.widget.listview.pulltorefreshlistviewextend;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;

public class PullToRefreshListViewFooter extends LinearLayout {

    // 未 拖拉或点击 状态
    public final static int STATE_NORMAL = 0;

    // 松开加载更多状态
    public final static int STATE_READY = 1;

    // 加载中
    public final static int STATE_LOADING = 2;

    private Context mContext;

    private RelativeLayout mContentView;
    private ProgressBar mProgressBar;
    private TextView mHintView;
    private Boolean footerProgressBarEnabled, footerTextViewEnabled;
    //Footer是否可见(暂未实现)
    private boolean isFooterVisable;
    //Footer可见的高度，原始高度
    private float footerRealHeight;
    private float visableHeight;


    // public PullToRefreshListViewFooter(Context context) {
    // super(context);
    // initView(context);
    // }

    public PullToRefreshListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public void setState(int state) {

        mProgressBar.setVisibility(View.GONE);

        // mHintView.setVisibility(View.GONE);
        if (state == STATE_READY) {
            ready();
        } else if (state == STATE_LOADING) {
            loading();
        } else {

            normal();

        }
    }

    public void setBottomMargin(int height) {
        if (height < 0)
            return;
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        return lp.bottomMargin;
    }

    public void ready() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pulltorefresh_listview_footer_hint_ready);
        if (footerProgressBarEnabled)
            mProgressBar.setVisibility(View.GONE);
    }

    /**
     * normal status
     */
    public void normal() {

        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pulltorefresh_listview_footer_hint_normal);

        if (footerProgressBarEnabled)
            mProgressBar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading() {
        // mHintView.setVisibility(View.GONE);
        if (footerProgressBarEnabled)
            mProgressBar.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pulltorefresh_listview_header_hint_loading);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    private void initView(Context context, AttributeSet attrs) {

        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.PullToRefreshListView);

        footerProgressBarEnabled = a
                .getBoolean(
                        R.styleable.PullToRefreshListView_PullToRefreshListView_FooterProgressBarEnabled,
                        false);
        footerTextViewEnabled = a
                .getBoolean(
                        R.styleable.PullToRefreshListView_PullToRefreshListView_FooterTextViewEnabled,
                        true);
        isFooterVisable = a.getBoolean(R.styleable.PullToRefreshListView_PullToRefreshListView_FooterVisiblity, true);
        visableHeight = a.getDimension(R.styleable.PullToRefreshListView_PullToRefreshListView_FooterVisibleHeight, -1);

        footerRealHeight = a.getDimension(R.styleable.PullToRefreshListView_PullToRefreshListView_FooterRealHeight, 100);
//        footerRealHeight = DensityUtils.dip2px(context, footerRealHeight);


        mContentView = (RelativeLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.pulltorefresh_listview_footer, null);

        RelativeLayout.LayoutParams lpParent;
        lpParent = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
//                LayoutParams.MATCH_PARENT, (int) realHeight);
//        mContentView.setLayoutParams(lp2);

        addView(mContentView, lpParent);

//        setVisiableHeight((int) realHeight);

        //****关键代码，用来撑起实际高度
//        mContent = mContentView
//                .findViewById(R.id.pulltorefresh_listview_footer_content);
//        setVisiableHeight((int) realHeight);
        //***


        if (visableHeight != -1)
            setVisiableHeight((int) visableHeight);

        mProgressBar = (ProgressBar) mContentView
                .findViewById(R.id.pulltorefresh_listview_footer_progressbar);

        if (footerProgressBarEnabled) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }

        mHintView = (TextView) mContentView
                .findViewById(R.id.pulltorefresh_listview_footer_hint_textview);
        if (footerTextViewEnabled) {
            mHintView.setVisibility(View.VISIBLE);
        } else {
            mHintView.setVisibility(View.GONE);
        }

    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        lp.height = height;
        mContentView.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
//        if (mContentView.getHeight() < footerRealHeight) {
//            return (int)footerRealHeight;
//        }
        return mContentView.getHeight();
    }

    public boolean isFooterVisable() {
        return isFooterVisable;
    }

    public void setFooterVisable(boolean isFooterVisable) {
        this.isFooterVisable = isFooterVisable;
    }

    public float getFooterRealHeight() {
        return footerRealHeight;
    }

    public void setFooterRealHeight(float footRealHeight) {
        this.footerRealHeight = footRealHeight;
    }

    public float getVisableHeight() {
        return visableHeight;
    }

    public void setVisableHeight(float visableHeight) {
        this.visableHeight = visableHeight;
    }
}
