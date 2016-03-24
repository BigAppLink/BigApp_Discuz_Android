package com.youzu.clan.base.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.youzu.clan.R;

public class EmptyView extends LinearLayout {

    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private boolean emptyViewEnable = true;

    private OnClickListener mErrorClickListener;

    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            showLoading();
            if (mErrorClickListener != null) {
                mErrorClickListener.onClick(mErrorView);
            }
        }
    };

    public EmptyView(Context context) {
        this(context, R.layout.include_empty_view);
    }

    public EmptyView(Context context, int layout) {
        super(context);
        final View emptyView = LayoutInflater.from(context).inflate(layout, null);
        final View loadingView = LayoutInflater.from(context).inflate(R.layout.include_loading_view, null);
        final View errorView = LayoutInflater.from(context).inflate(R.layout.include_error_view, null);
        errorView.setOnClickListener(listener);

        addView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(errorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(loadingView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mEmptyView = emptyView;
        mErrorView = errorView;
        mLoadingView = loadingView;
        showLoading();
    }

    public void setOnErrorClickListener(OnClickListener listener) {
        mErrorClickListener = listener;
    }

    public void showEmpty() {
        if (emptyViewEnable) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mErrorView.setVisibility(View.GONE);
        mLoadingView.setVisibility(GONE);
    }

    public void showError() {
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }

    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
    }


    public boolean isEmptyViewEnable() {
        return emptyViewEnable;
    }

    public void setEmptyViewEnable(boolean emptyViewEnable) {
        this.emptyViewEnable = emptyViewEnable;
    }
}
