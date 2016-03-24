package com.youzu.clan.base.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.kit.share.ShareModel;
import com.kit.share.SharePopupWindow;
import com.kit.share.model.ShareItem;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.share.ShareItemClickListener;
import com.youzu.clan.thread.detail.ThreadDetailActivity;

import java.util.ArrayList;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Zhao on 15/8/18.
 */
public class ShareUtils {


    public static SharePopupWindow showShare(Context context, View showAt, String title, String text, String imageurl, String url, PlatformActionListener platformActionListener) {
        ZogUtils.printError(ThreadDetailActivity.class, "showShare imageurl:" + imageurl);

        ArrayList<ShareItem> items = new ArrayList<>();
        if (ClanUtils.isUseShareSDKPlatformName(context, Wechat.NAME)) {
            ShareItem shareItem0 = new ShareItem(Wechat.NAME, context.getResources().getDrawable(R.drawable.sns_weixin_icon), "微信好友");
            ShareItem shareItem1 = new ShareItem(WechatMoments.NAME, context.getResources().getDrawable(R.drawable.sns_weixin_timeline_icon), "朋友圈");
            items.add(shareItem0);
            items.add(shareItem1);
        }

        if (ClanUtils.isUseShareSDKPlatformName(context, SinaWeibo.NAME)) {
            ShareItem shareItem2 = new ShareItem(SinaWeibo.NAME, context.getResources().getDrawable(R.drawable.sns_sina_icon), "新浪微博");
            items.add(shareItem2);

        }

        if (ClanUtils.isUseShareSDKPlatformName(context, "QQ")) {
            ShareItem shareItem3 = new ShareItem("QQ", context.getResources().getDrawable(R.drawable.sns_qqfriends_icon), "QQ");
            ShareItem shareItem4 = new ShareItem("QZone", context.getResources().getDrawable(R.drawable.sns_qzone_icon), "QQ空间");
            items.add(shareItem3);
            items.add(shareItem4);
        }

        ShareItem shareItem5 = new ShareItem("Copy", context.getResources().getDrawable(R.drawable.sns_copy_icon), "复制链接");
        items.add(shareItem5);

        ShareModel model = new ShareModel();
        model.setImageUrl(imageurl);
        model.setText(text);
        model.setTitle(title);
        model.setUrl(url);


        SharePopupWindow share = SharePopupWindow.init(context, items, model);
        share.setPlatformActionListener(platformActionListener);
        share.setOnItemClickListener(new ShareItemClickListener(share));
        share.showAtLocation(showAt,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        return  share;
    }

}
