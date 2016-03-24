package com.youzu.clan.thread.detail.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.thread.detail.ThreadDetailActivity;


/**
 * 点评帖子
 */
public class DetailMenuFragment extends BaseFragment {

    @ViewInject(R.id.ivFav)
    public ImageView ivFav;

    @ViewInject(R.id.tvFav)
    public TextView tvFav;

    @ViewInject(R.id.et)
    public EditText et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_thread_detail_menu, container, false);
        ViewUtils.inject(this, view);

        return view;
    }


    public ImageView getIvFav() {
        return ivFav;
    }

    public void setIvFav(ImageView ivFav) {
        this.ivFav = ivFav;
    }

    public TextView getTvFav() {
        return tvFav;
    }

    public void setTvFav(TextView tvFav) {
        this.tvFav = tvFav;
    }

    public EditText getEt() {
        return et;
    }

    public void setEt(EditText et) {
        this.et = et;
    }


    @OnClick(R.id.llComment)
    public void reply(View view) {
        ((ThreadDetailActivity) getActivity()).checkPost(null);
    }

    @OnClick(R.id.llFav)
    public void fav(View view) {
        ((ThreadDetailActivity) getActivity()).fav();
    }

    @OnClick(R.id.llShare)
    public void share(View view) {
        ((ThreadDetailActivity) getActivity()).doShare();
    }

    public void setFav(boolean isFavThread) {
        if (isFavThread) {
            ivFav.setImageResource(R.drawable.ic_fav_black_solid);
            tvFav.setText(R.string.fav_alright);
        } else {
            ivFav.setImageResource(R.drawable.ic_fav_black);
            tvFav.setText(R.string.fav);
        }
    }
}
