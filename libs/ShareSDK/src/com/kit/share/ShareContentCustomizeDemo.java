package com.kit.share;

import android.content.Context;
import android.util.Log;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * 快捷分享项目现在添加为不同的平台添加不同分享内容的方法。
 */
public class ShareContentCustomizeDemo implements ShareContentCustomizeCallback {
    private Context context;
    private String weiboAt;

    public void setContext(Context context) {
        this.context = context;
    }


    public void setSinaWeiboAt(String weiboAt) {
        this.weiboAt = weiboAt;
    }

    public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
        if (platform instanceof CustomPlatform) {
            return;
        }
//		int id = ShareSDK.platformNameToId(platform.getName());
//		if (MainActivity.TEST_TEXT != null && MainActivity.TEST_TEXT.containsKey(id)) {
//			String text = MainActivity.TEST_TEXT.get(id);
//			paramsToShare.setText(text);
//		} else if ("Twitter".equals(platform.getName())) {
//			// 改写twitter分享内容中的text字段，否则会超长，
//			// 因为twitter会将图片地址当作文本的一部分去计算长度
//
//			String text = platform.getContext().getString(R.string.share_content_short);
//			paramsToShare.setText(text);
//		}

        Log.e("APP", "platform.getName():" + platform.getName());

        int id = ShareSDK.platformNameToId(platform.getName());
        if (SinaWeibo.NAME.equals(platform.getName())) {
            // 改写twitter分享内容中的text字段，否则会超长，
            // 因为twitter会将图片地址当作文本的一部分去计算长度

            String title = paramsToShare.getTitle();
            String titleUrl = paramsToShare.getTitleUrl();

//            if(title!=null&&title.length())


            String text = title + " " + titleUrl + ((weiboAt != null && weiboAt.length() > 0) ? " @" + weiboAt : "");
            paramsToShare.setText(text);
            paramsToShare.setImageUrl(null);
        }
    }

}
