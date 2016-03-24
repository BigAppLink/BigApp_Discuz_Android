/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.kit.widget.listview.pulltorefreshlistviewextend;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kit.extend.widget.R;

public class PullToRefreshListViewHeader extends LinearLayout {
    private LinearLayout mContentView;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;
    private ImageView mIconImageView;

    private LinearLayout llTime;
    private int mState = STATE_NORMAL;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private final int ROTATE_ANIM_DURATION = 180;

    // 未 拖拉或点击 状态
    public final static int STATE_NORMAL = 0;

    // 松开刷新状态
    public final static int STATE_READY = 1;

    // 加载中
    public final static int STATE_REFRESHING = 2;

    private boolean headerProgressBarEnabled = false;
    private boolean headerArrowEnabled = false;
    private boolean headerIconEnabled = false;
    private boolean headerTimeEnabled = false;
    private Drawable headerIconSrc;

    private boolean headerVisiblity;

    private float headerRealHeight;

//	private int realHeaderHeight = 0;

    // public PullToRefreshListViewHeader(Context context) {
    // super(context);
    // initView(context);
    // }

    /**
     * @param context
     * @param attrs
     */
    public PullToRefreshListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public void setState(int state) {
        if (state == mState)
            return;

        if (state == STATE_REFRESHING) { // 显示进度
            if (headerArrowEnabled) {
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.INVISIBLE);
            }
            if (headerProgressBarEnabled)
                mProgressBar.setVisibility(View.VISIBLE);
        } else { // 显示箭头图片
            if (headerArrowEnabled)
                mArrowImageView.setVisibility(View.VISIBLE);
            if (headerProgressBarEnabled)
                mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                normal();
                break;
            case STATE_READY:
                ready();
                break;
            case STATE_REFRESHING:
                refreshing();
                break;
            default:
                break;
        }

        mState = state;
    }

    public void ready() {
        if (mState != STATE_READY) {
            if (headerArrowEnabled) {
                mArrowImageView.clearAnimation();
                mArrowImageView.startAnimation(mRotateUpAnim);
            }
            mHintTextView
                    .setText(R.string.pulltorefresh_listview_header_hint_ready);
        }
    }

    /**
     * normal status
     */
    public void normal() {

        if (headerArrowEnabled) {
            if (mState == STATE_READY) {
                mArrowImageView.startAnimation(mRotateDownAnim);
            }
            if (mState == STATE_REFRESHING) {
                mArrowImageView.clearAnimation();
            }
        }
        mHintTextView
                .setText(R.string.pulltorefresh_listview_header_hint_normal);

    }

    /**
     * refreshing status
     */
    public void refreshing() {
        mHintTextView
                .setText(R.string.pulltorefresh_listview_header_hint_loading);
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
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.PullToRefreshListView);

        headerProgressBarEnabled = a
                .getBoolean(
                        R.styleable.PullToRefreshListView_PullToRefreshListView_FooterProgressBarEnabled,
                        false);
        headerArrowEnabled = a
                .getBoolean(
                        R.styleable.PullToRefreshListView_PullToRefreshListView_HeaderArrowEnabled,
                        false);
        headerIconEnabled = a
                .getBoolean(
                        R.styleable.PullToRefreshListView_PullToRefreshListView_HeaderIconEnabled,
                        false);

        headerTimeEnabled = a
                .getBoolean(
                        R.styleable.PullToRefreshListView_PullToRefreshListView_HeaderTimeEnabled,
                        false);

        headerIconSrc = a
                .getDrawable(R.styleable.PullToRefreshListView_PullToRefreshListView_HeaderIconSrc);

        headerVisiblity = a
                .getBoolean(R.styleable.PullToRefreshListView_PullToRefreshListView_HeaderVisiblity, false);

        headerRealHeight = a
                .getDimension(R.styleable.PullToRefreshListView_PullToRefreshListView_HeaderRealHeight, 120);
//        headerRealHeight = DensityUtils.dip2px(context, headerRealHeight);

        mContentView = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.pulltorefresh_listview_header, null);
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, 0);
        addView(mContentView, lp);
        setGravity(Gravity.BOTTOM);

        mHintTextView = (TextView) findViewById(R.id.pulltorefresh_listview_header_hint_textview);

        mArrowImageView = (ImageView) findViewById(R.id.pulltorefresh_listview_header_arrow);
        if (headerArrowEnabled) {
            mArrowImageView.setVisibility(VISIBLE);
        } else {
            mArrowImageView.setVisibility(GONE);
        }

        mProgressBar = (ProgressBar) findViewById(R.id.pulltorefresh_listview_header_progressbar);
        if (headerProgressBarEnabled) {
            mProgressBar.setVisibility(VISIBLE);
        } else {
            mProgressBar.setVisibility(GONE);
        }

        mIconImageView = (ImageView) findViewById(R.id.pulltorefresh_listview_header_iv_icon);
        if (headerIconSrc != null) {
            mIconImageView.setVisibility(VISIBLE);
            mIconImageView.setImageDrawable(headerIconSrc);
        } else {
            mIconImageView.setVisibility(GONE);
        }

        llTime = (LinearLayout) findViewById(R.id.pulltorefresh_listview_header_lltime);
        if (headerTimeEnabled) {
            llTime.setVisibility(VISIBLE);
        } else {
            llTime.setVisibility(GONE);
        }

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

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
        return mContentView.getHeight();
    }

    public float getHeaderRealHeight() {
        return headerRealHeight;
    }

    public void setHeaderRealHeight(float headerRealHeight) {
        this.headerRealHeight = headerRealHeight;
    }
}
