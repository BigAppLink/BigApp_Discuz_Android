/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.youzu.clan.base.util;

import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.kit.utils.HtmlUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.kit.share.ShareContentCustomizeDemo;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.PlatformListFakeActivity;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * ShareSDK 官网地址 ： http://www.mob.com </br>
 * 1、ShareSDK接口演示页面</br>
 * 包括演示使用快捷分享完成图文分享、</br>
 * 无页面直接分享、授权、关注和不同平台的分享等等功能。</br>
 * <p/>
 * 2、如果要咨询客服，请加企业QQ 4006852216 </br>
 * 3、咨询客服时，请把问题描述清楚，最好附带错误信息截图 </br>
 * 4、一般问题，集成文档中都有，请先看看集成文档；减少客服压力，多谢合作  ^_^
 * 5、由于客服压力巨大，每个月难免有那么几天，请见谅
 */
public class ShareSDKUtils {


    private static String content;
    // 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）


    public static void share(ActionBarActivity activity, String title, String text, String comment
            , String titleUrl, String siteName, String siteUrl, String imagePath, String imageUrl, final String weiboAt) {
        ShareSDK.initSDK(activity);
        final OnekeyShare oks = new OnekeyShare();

        title = HtmlUtils.delHTMLTag(title);
        text = HtmlUtils.delHTMLTag(text);

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(titleUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if (imagePath != null && imagePath.length() > 0) {
            oks.setImagePath(imagePath);//确保SDcard下面存在此张图片
        } else
            oks.setImageUrl(imageUrl);

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(titleUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(comment);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(siteName);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(siteUrl);


        oks.setOnShareButtonClickListener(new PlatformListFakeActivity.OnShareButtonClickListener() {
            @Override
            public void onClick(View v, List<Object> checkPlatforms) {

                ZogUtils.printLog(ShareSDKUtils.class, "v:" + v + " checkPlatforms:" + checkPlatforms + " " + checkPlatforms.size());
                if (checkPlatforms != null && !checkPlatforms.isEmpty() && (checkPlatforms.get(0) instanceof SinaWeibo)) {
//                    String shareContent = oks.getTitle() + " " + oks.getTitleUrl() + ((weiboAt != null && weiboAt.length() > 0) ? " @" + weiboAt : "");

                    String shareContent = oks.getTitle();
                    oks.setText(shareContent);

                    oks.setImageUrl(null);


                }


            }
        });
        ShareContentCustomizeDemo  shareContentCustomizeDemo =  new ShareContentCustomizeDemo();
        shareContentCustomizeDemo.setSinaWeiboAt(weiboAt);
        oks.setShareContentCustomizeCallback(shareContentCustomizeDemo);

        // 启动分享GUI
        oks.show(activity);

    }

    /**
     * ShareSDK集成方法有两种</br>
     * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
     * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
     * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
     * 或者看网络集成文档 http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
     * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
     * <p/>
     * <p/>
     * 平台配置信息有三种方式：
     * 1、在我们后台配置各个微博平台的key
     * 2、在代码中配置各个微博平台的key，http://mob.com/androidDoc/cn/sharesdk/framework/ShareSDK.html
     * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
     */
    public static void showShare(ActionBarActivity activity, boolean silent, String platform, boolean captureView) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(activity.getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.png");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(activity.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(activity);

    }


}
