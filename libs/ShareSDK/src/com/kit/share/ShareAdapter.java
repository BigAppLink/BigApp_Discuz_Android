/*
* @Title:  ShareAdapter.java
* @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
* @data:  2014-7-21 下午2:30:32
* @version:  V1.0
*/

package com.kit.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.share.model.ShareItem;

import java.util.ArrayList;

import cn.sharesdk.demo.R;

/**
 * TODO< 分享弹出框Adapter >
 *
 * @data: 2014-7-21 下午2:30:32
 * @version: V1.0
 */

public class ShareAdapter extends BaseAdapter {
    ArrayList<ShareItem> shareItems;

//    private static String[] shareNames = new String[]{"微信好友", "朋友圈", "新浪微博", "QQ", "QQ空间", "复制链接"};
//    private int[] shareIcons = new int[]{R.drawable.sns_weixin_icon, R.drawable.sns_qqfriends_icon, R.drawable.sns_sina_icon, R.drawable.sns_weixin_timeline_icon,
//            R.drawable.sns_qzone_icon, R.drawable.sns_copy_icon};

    private LayoutInflater inflater;
    private Context context;


    public ShareAdapter(Context context,ArrayList<ShareItem> shareItems) {
        this.context = context;
        this.shareItems = shareItems;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return shareItems.size();
    }

    @Override
    public ShareItem getItem(int position) {
        return shareItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.share_item, null);
        }

        ShareItem shareItem = getItem(position);

        ImageView shareIcon = (ImageView) convertView.findViewById(R.id.share_icon);
        TextView shareTitle = (TextView) convertView.findViewById(R.id.share_title);

        shareIcon.setImageDrawable(shareItem.getResDrawable());
        shareTitle.setText(shareItem.getName());
        return convertView;
    }
}
