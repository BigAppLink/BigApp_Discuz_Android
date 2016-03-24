package com.youzu.clan.base.callback;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.youzu.clan.base.widget.LoadingDialogFragment;

public class ProgressCallback<T> extends HttpCallback<T> {

    private String loadingTag = "loading";
    private boolean showLoading = true;
    private LoadingDialogFragment loadingFragment;
    protected FragmentActivity activity;

    public ProgressCallback(FragmentActivity activity) {
        this.activity = activity;
        loadingFragment = LoadingDialogFragment.getInstance(activity);
    }

    @Override
    public void onstart(Context cxt) {
        super.onstart(cxt);
        if (showLoading && !activity.isFinishing() && !loadingFragment.isAdded()) {
            try {
                loadingFragment.show();
            }catch (Exception e){
                loadingFragment.dismissAllowingStateLoss();
            }
        }
    }

    @Override
    public void onFailed(Context cxt,int errorCode, String msg) {
        super.onFailed(activity,errorCode, msg);

        if (showLoading && !activity.isFinishing() && loadingFragment.isAdded()) {
//            loadingFragment.dismiss();
            loadingFragment.dismissAllowingStateLoss();
        }
    }


    @Override
    public void onSuccess(Context ctx,T t) {
        super.onSuccess(ctx,t);
        if (showLoading && !activity.isFinishing() && loadingFragment.isAdded()) {
//            loadingFragment.dismiss();
            loadingFragment.dismissAllowingStateLoss();
        }
    }


    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }

}
