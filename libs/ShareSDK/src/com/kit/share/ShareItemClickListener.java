package com.kit.share;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.kit.share.model.ShareItem;

import java.util.ArrayList;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class ShareItemClickListener implements AdapterView.OnItemClickListener {
    private SharePopupWindow pop;
    private Platform.ShareParams shareParams;
    Context context;
    PlatformActionListener platformActionListener;
    ArrayList<ShareItem> shareItems;

    public ShareItemClickListener(SharePopupWindow pop) {
        this.pop = pop;
        this.context = pop.getContext();
        this.platformActionListener = pop.getPlatformActionListener();
        this.shareParams = pop.getShareParams();
        this.shareItems = pop.getShareItems();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        share(position);
        pop.dismiss();

    }

    /**
     * 获取平台
     *
     * @param position
     * @return
     */
    private String getPlatform(int position) {
        String platform = shareItems.get(position).getPlatformName();
        return platform;
    }


    /**
     * 分享
     *
     * @param position
     */
    private void share(int position) {
        switch (getPlatform(position)) {
            case "QQ":
                qq();
                break;
            case "QZone":
                qzone();
                break;
            case "Copy":
                copy();
                break;
            default:
                Platform plat = null;
                plat = ShareSDK.getPlatform(context, getPlatform(position));
                if (platformActionListener != null) {
                    plat.setPlatformActionListener(platformActionListener);
                }
                plat.share(shareParams);
        }

    }

    /**
     * 分享到QQ空间
     */
    private void qzone() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());

        Platform qzone = ShareSDK.getPlatform(context, "QZone");

        qzone.setPlatformActionListener(platformActionListener); // 设置分享事件回调 //
        // 执行图文分享
        qzone.share(sp);
    }

    private void qq() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());
        Platform qq = ShareSDK.getPlatform(context, "QQ");
        qq.setPlatformActionListener(platformActionListener);
        qq.share(sp);
    }


    /**
     * 分享到短信
     */
    private void copy() {
        Platform.ShareParams sp = new Platform.ShareParams();
//        ClipboardUtils.copy(context, shareParams.getUrl());
//        ToastUtils.mkShortTimeToast(context, context.getString(R.string.copy_ok));
    }

    /**
     * 分享到短信
     */
    private void shortMessage() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setAddress("");
        sp.setText(shareParams.getText() + "这是网址《" + shareParams.getUrl() + "》很给力哦！");

        Platform circle = ShareSDK.getPlatform(context, "ShortMessage");
        circle.setPlatformActionListener(platformActionListener); // 设置分享事件回调
        // 执行图文分享
        circle.share(sp);
    }

}
