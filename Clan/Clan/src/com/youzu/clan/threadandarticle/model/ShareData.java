package com.youzu.clan.threadandarticle.model;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/11/13.
 */
public class ShareData {
//    share_image: 分享图片,
//    share_url: 页面地址,
//    share_subject: 页面主题,
//    share_abstract: 页面描述



    private  String shareImage;
    private  String shareUrl;
    private  String shareSubject;
    private  String shareAbstract;

    public String getShareImage() {
        return shareImage;
    }

    @JSONField(name = "share_image")
    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    @JSONField(name = "share_url")
    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareSubject() {
        return shareSubject;
    }

    @JSONField(name = "share_subject")
    public void setShareSubject(String shareSubject) {
        this.shareSubject = shareSubject;
    }

    public String getShareAbstract() {
        return shareAbstract;
    }

    @JSONField(name = "share_abstract")
    public void setShareAbstract(String shareAbstract) {
        this.shareAbstract = shareAbstract;
    }
}
