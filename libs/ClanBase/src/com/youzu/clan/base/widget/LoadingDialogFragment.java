package com.youzu.clan.base.widget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.kit.utils.ZogUtils;
import com.youzu.clan.base.R;

public class LoadingDialogFragment extends DialogFragment {
    private View v;
    private boolean mRemoved;
    private int mBackStackId;

    private static LoadingDialogFragment loadingDialogFragment;
    private static FragmentManager manager;
    private static String tag = "loading";

    public static LoadingDialogFragment getInstance(FragmentActivity activity) {
        if (loadingDialogFragment == null) {
            loadingDialogFragment = new LoadingDialogFragment();
        }

        manager = activity.getSupportFragmentManager();

        return loadingDialogFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);

        v = inflater.inflate(R.layout.fragment_dialog_progressbar_loading, container, false);
        return v;
    }


    @Deprecated
    @Override
    public void show(FragmentManager manager, String tag) {

        dismissAllowingStateLoss();

        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            ZogUtils.printLog(LoadingDialogFragment.class, e.toString());
        }
    }

    public void show() {
        if (loadingDialogFragment != null) {
            dismissAllowingStateLoss();
        }
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            ZogUtils.printLog(LoadingDialogFragment.class, e.toString());
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        if (loadingDialogFragment != null
                && loadingDialogFragment.getActivity() != null
                && !loadingDialogFragment.getActivity().isFinishing()
                && loadingDialogFragment.isAdded()
                && loadingDialogFragment.isVisible()) {
            super.dismissAllowingStateLoss();
        }

    }

    @Deprecated
    @Override
    public void dismiss() {
        super.dismiss();
    }
}