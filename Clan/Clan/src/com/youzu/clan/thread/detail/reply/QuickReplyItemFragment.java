package com.youzu.clan.thread.detail.reply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kit.app.core.task.DoSomeThing;
import com.kit.imagelib.entity.ImageBean;
import com.kit.utils.FragmentUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.util.PicUtils;
import com.youzu.clan.common.images.PicSelectorActivity;
import com.youzu.clan.thread.detail.ThreadDetailActivity;

import java.util.ArrayList;


/**
 * 点评帖子
 */
public class QuickReplyItemFragment extends BaseFragment {

    @ViewInject(R.id.llPic)
    public LinearLayout llPic;

    QuickReplyItemPicFragment quickReplyItemPicFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_quick_reply_item, container, false);
        ViewUtils.inject(this, view);

        return view;
    }


    @OnClick(R.id.llPic)
    public void pic(View view) {
        PicSelectorActivity.selectPic(this, 9);
    }

    @OnClick(R.id.llFav)
    public void fav(View view) {
        ((ThreadDetailActivity) getActivity()).fav();
    }

    @OnClick(R.id.llShare)
    public void share(View view) {
        ((ThreadDetailActivity) getActivity()).doShare();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ZogUtils.printError(QuickReplyItemFragment.class, "onActivityResult resultCode = " + resultCode + ", requestCode = " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            PicUtils.onSeleted(getActivity(), data, new DoSomeThing() {
                @Override
                public void execute(Object... object) {

                    ArrayList<ImageBean> images = (ArrayList<ImageBean>) object[0];
                    if (ListUtils.isNullOrContainEmpty(images)) {
                        return;
                    }

                    ((QuickReplyFragment) getParentFragment()).imageBeans = images;

                    if (quickReplyItemPicFragment == null) {
                        quickReplyItemPicFragment = new QuickReplyItemPicFragment();

                        BundleData bundleData = new BundleData();
                        bundleData.put(Key.CLAN_DATA, images);
                        FragmentUtils.replace(getChildFragmentManager(), R.id.replaceInner, quickReplyItemPicFragment, bundleData);
                    } else {
                        quickReplyItemPicFragment.setImages(images);
                    }
                }
            });
        }
    }
}
