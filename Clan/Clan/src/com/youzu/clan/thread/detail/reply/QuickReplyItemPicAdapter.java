package com.youzu.clan.thread.detail.reply;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kit.app.core.task.DoSomeThing;
import com.kit.imagelib.entity.ImageBean;
import com.kit.utils.AppUtils;
import com.kit.widget.listview.HorizontalListView;
import com.youzu.clan.R;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.common.images.PicSelectorActivity;

import java.util.ArrayList;

public class QuickReplyItemPicAdapter extends BaseAdapter {
    public static int LIMIT = 9;

    private Fragment fragment;
    private Context context;
    private HorizontalListView horizontalListView;


    ArrayList<ImageBean> itemList = null;
    ImageBean imageBeanAdd = new ImageBean(com.kit.imagelib.R.drawable.ic_image_select_add);


    public QuickReplyItemPicAdapter(Fragment fragment, HorizontalListView horizontalListView, ArrayList<ImageBean> list) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.horizontalListView = horizontalListView;
        setData(list);
    }


    @Override
    public int getCount() {
        return itemList.size();
    }


    public ArrayList<ImageBean> getRealData() {
        ArrayList<ImageBean> tempBeans = new ArrayList<ImageBean>();
        tempBeans.addAll(itemList);
        tempBeans.remove(imageBeanAdd);
        return tempBeans;
    }

    public void setData(ArrayList<ImageBean> bs) {

        if (itemList == null) {
            itemList = new ArrayList<ImageBean>();
        }
        if (bs == null) {
            bs = new ArrayList<ImageBean>();
        }
        itemList = bs;
        itemList.remove(imageBeanAdd);
        if (getRealCount() < LIMIT) {
            Log.e("ImageSelectAdapter", "imageBeanAdd imageBeanAdd imageBeanAdd imageBeanAdd");
            itemList.add(imageBeanAdd);
        }

        AppUtils.delay(500, new DoSomeThing() {
            @Override
            public void execute(Object... object) {

                horizontalListView.setSelection(getCount()-1);
            }
        });

        notifyDataSetChanged();
    }

    public int getRealCount() {
        return getRealData().size();
    }


    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position).drawableId > 0) {
            return 1;
        } else
            return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);


        switch (type) {

            case 1:
                convertView = getViewAdd(position, convertView, parent);
                break;

            default:
                convertView = getViewSelectOne(position, convertView, parent);
                break;
        }
        return convertView;
    }

    private View getViewAdd(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_fragment_quick_reply_item_pic, null);
        }

        ImageView imageView = ViewHolder.get(convertView, R.id.pic);
        ImageView ivDel = ViewHolder.get(convertView, R.id.ivDel);
        ivDel.setVisibility(View.GONE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int remain = LIMIT - getRealCount();
                PicSelectorActivity.selectPic(fragment, remain);

            }
        });
        return convertView;
    }


    private View getViewSelectOne(final int position, View convertView, ViewGroup parent) {
        ImageBean imageBean = (ImageBean) getItem(position);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_fragment_quick_reply_item_pic, null);
        }

        ImageView imageView = ViewHolder.get(convertView, R.id.pic);
        LoadImageUtils.display(context, imageView, imageBean.path);
        ImageView ivDel = ViewHolder.get(convertView, R.id.ivDel);

        ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.remove(position);
                setData(itemList);

            }
        });
        return convertView;
    }


}