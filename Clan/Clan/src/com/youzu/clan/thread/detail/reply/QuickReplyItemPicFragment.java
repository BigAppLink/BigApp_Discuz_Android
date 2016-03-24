package com.youzu.clan.thread.detail.reply;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.kit.imagelib.entity.ImageBean;
import com.kit.utils.FragmentUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.widget.listview.HorizontalListView;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * 选择完图片展示的横向滚动视图
 */
public class QuickReplyItemPicFragment extends BaseFragment {

    @ViewInject(R.id.horizontalListView)
    public HorizontalListView horizontalListView;
    QuickReplyItemPicAdapter quickReplyItemPicAdapter;
    ArrayList<ImageBean> images;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_item_pic, container, false);
        ViewUtils.inject(this, view);

        BundleData bundleData = FragmentUtils.getData(this);
        Type type = new TypeToken<ArrayList<ImageBean>>() {
        }.getType();
        images = bundleData.getArrayList(Key.CLAN_DATA, type);
        quickReplyItemPicAdapter = new QuickReplyItemPicAdapter(this,horizontalListView, images);
        horizontalListView.setAdapter(quickReplyItemPicAdapter);

        return view;
    }


    public void setImages(ArrayList<ImageBean> images) {
        this.images = images;
        setAdapter();
    }

    private void setAdapter() {
        if (ListUtils.isNullOrContainEmpty(images))
            return;
        ZogUtils.printError(QuickReplyItemPicFragment.class, "images.size():" + images.size());

        if (quickReplyItemPicAdapter == null) {
            quickReplyItemPicAdapter = new QuickReplyItemPicAdapter(this,horizontalListView, images);
            horizontalListView.setAdapter(quickReplyItemPicAdapter);
        } else {

            ArrayList<ImageBean> realData = quickReplyItemPicAdapter.getRealData();
            realData.addAll(images);
            quickReplyItemPicAdapter.setData(realData);

        }

    }




    public int getSelectedCount() {
        if (quickReplyItemPicAdapter != null) {
            quickReplyItemPicAdapter.getRealCount();
        }
        return 0;
    }


    public int getImagesRealCount() {
        if (quickReplyItemPicAdapter != null) {
            quickReplyItemPicAdapter.getRealCount();
        }
        return 0;
    }
}
