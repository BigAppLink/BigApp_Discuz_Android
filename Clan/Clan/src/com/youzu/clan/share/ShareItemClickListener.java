package com.youzu.clan.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

import com.kit.share.SharePopupWindow;
import com.kit.share.model.ShareItem;
import com.kit.utils.AppUtils;
import com.kit.utils.ClipboardUtils;
import com.kit.utils.FileUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.bitmap.BitmapUtils;
import com.kit.utils.DrawableUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.util.DateUtils;
import com.youzu.clan.base.util.ImageUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;

import java.util.ArrayList;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class ShareItemClickListener implements AdapterView.OnItemClickListener {
    private SharePopupWindow pop;
    Context context;
    PlatformActionListener platformActionListener;
    ArrayList<ShareItem> shareItems;

    Platform.ShareParams shareParams;
    String defaultSharePath = ImageUtils.getDefaultCacheDir() + "/ic_launcher.png";
    String defaultShareNet2LocalPath = ImageUtils.getDefaultCacheDir() + "/temp_" + DateUtils.getCurrDateLong() + ".jpg";
    String path = defaultSharePath;
    Bitmap bitmap;

    /**
     * 是否使用网络图片
     */
    boolean isUseNetBitmap = false;

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
        initSharePath(pop, position);
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


    private void initSharePath(SharePopupWindow pop, final int position) {
        ZogUtils.printError(ShareItemClickListener.class, "pre doShare:" + JsonUtils.toJSON(shareParams).toString());


        final String platform = getPlatform(position);

        if (platform.equals("Copy")) {
            share(platform);
            return;
        }else{
            LoadingDialogFragment.getInstance((FragmentActivity) context).show();
        }

        shareParams = pop.getShareParams();
        if (FileUtils.isExists(path)) {
            path = DrawableUtils.saveDrawable(context, R.drawable.ic_launcher, path);
            shareParams.setImagePath(path);
        }

        if (!StringUtils.isEmptyOrNullOrNullStr(shareParams.getImageUrl())) {
            ImageLoader.getInstance().loadImage(shareParams.getImageUrl(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    share(platform);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bmp) {
                    bitmap = bmp;
                    if (bitmap != null) {
                        try {
                            BitmapUtils.saveImage(defaultShareNet2LocalPath, bitmap, 20 * 1024);
                            path = defaultShareNet2LocalPath;
                        } catch (Exception e) {
                        }
                    }
                    shareParams.setImagePath(path);
                    shareParams.setImageUrl(s);
                    share(platform);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    share(platform);
                }
            });

        } else {
            shareParams.setImageUrl(context.getString(R.string.icon_net_url));
            share(platform);

        }
    }


    /**
     * 分享
     *
     * @param platform
     */
    private void share(String platform) {

        ZogUtils.printError(ShareItemClickListener.class, "path:" + path);


        switch (platform) {

            case "QQ":
//                if (!AppUtils.isAvilible(context, "com.tencent.mobileqq")) {
//                    ToastUtils.mkShortTimeToast(context, context.getString(R.string.no_qq_no_share));
//                    return;
//                }

                qq();
                break;
            case "QZone":
//                if (!AppUtils.isAvilible(context, "com.tencent.mobileqq")) {
//                    ToastUtils.mkShortTimeToast(context, context.getString(R.string.no_qq_no_share));
//                    return;
//                }

                qzone();
                break;
            case "SinaWeibo":
                String title = shareParams.getTitle();
                title = title.substring(0, (title.length() > 120 ? 120 : title.length()));
                String content = title + " " + shareParams.getUrl();
                shareParams.setText(content);
                shareParams.setUrl(null);
                shareParams.setTitleUrl(null);

                shareParams.setImagePath(null);
                shareParams.setImageUrl(null);

                doShare(platform);
                break;

            case "Wechat":
                if (!AppUtils.isAvilible(context, "com.tencent.mm")) {
                    ToastUtils.mkShortTimeToast(context, context.getString(R.string.no_wechat_no_share));
                    return;
                }
                doShare("Wechat");
                break;

            case "WechatMoments":
                if (!AppUtils.isAvilible(context, "com.tencent.mm")) {
                    ToastUtils.mkShortTimeToast(context, context.getString(R.string.no_wechat_no_share));
                    return;
                }

                doShare("WechatMoments");
                break;

            case "Copy":
                copy();
                break;
            default:
                doShare(platform);
                break;

        }

    }

    private void doShare(String platformName) {

//        shareParams.setImageUrl("http://mobfile.youzu.com/show/Uploads_image/2/e/a/d/eadf1eda9529216516c1af5a5dd3fa5f_1024_1024.png");
//        shareParams.setText("bdjdhdhdhhdjdjdj");
//        shareParams.setTitle("hdjdbdj");
//        shareParams.setUrl("http://192.168.180.23:8080/luyi3.2gbk/forum.php?mod=viewthread&tid=1");

//        ZogUtils.printError(ShareItemClickListener.class, "title:::" + shareParams.getTitle() + "\ntext:::" + shareParams.getText());

        ZogUtils.printError(ShareItemClickListener.class, "do doShare:" + JsonUtils.toJSON(shareParams).toString());

        Platform plat = ShareSDK.getPlatform(context, platformName);
        plat.SSOSetting(true);
        if (platformActionListener != null) {
            plat.setPlatformActionListener(platformActionListener);
        }
        plat.share(shareParams);


//        plat.setPlatformActionListener(platformActionListener);
//        ShareCore shareCore = new ShareCore();
////        shareCore.setShareContentCustomizeCallback(customizeCallback);
//        shareCore.share(plat, data);
    }

    /**
     * 分享到QQ空间
     */
    private void qzone() {
        ZogUtils.printError(ShareItemClickListener.class, "qzone do doShare:" + JsonUtils.toJSON(shareParams).toString());


        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setImagePath(path);

        sp.setComment("我对此分享内容的评论");
        sp.setSite(context.getString(R.string.app_name));
        sp.setSiteUrl(shareParams.getUrl());

        Platform qzone = ShareSDK.getPlatform(context, "QZone");

        qzone.setPlatformActionListener(platformActionListener); // 设置分享事件回调 //
        // 执行图文分享
        qzone.share(sp);
    }

    private void qq() {
        ZogUtils.printError(ShareItemClickListener.class, "qq do doShare:" + JsonUtils.toJSON(shareParams).toString());

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
        sp.setText(shareParams.getText());
        sp.setImagePath(shareParams.getImagePath());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());
        Platform qq = ShareSDK.getPlatform(context, "QQ");
        qq.setPlatformActionListener(platformActionListener);

        ZogUtils.printError(ShareItemClickListener.class, "qq doShare:" + JsonUtils.toJSON(sp).toString());

        qq.share(sp);
    }


    /**
     * 分享到短信
     */
    private void copy() {
//        Platform.ShareParams sp = new Platform.ShareParams();
        ClipboardUtils.copy(context, shareParams.getUrl());
        ToastUtils.mkShortTimeToast(context, context.getString(R.string.copy_ok));
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
