package com.youzu.clan.threadandarticle.model;

/**
 * Created by Zhao on 15/11/13.
 */
public class BigAppH5 {
    private PostData postData;
    private ShareData shareData;
    private String bigApi_h5_detailJump;
    private PostData bigApi_h5_viewOnly;
    private PostData bigApi_h5_detailReplyMain;


    public PostData getPostData() {
        return postData;
    }

    public void setPostData(PostData postData) {
        this.postData = postData;
    }

    public ShareData getShareData() {
        if (shareData == null)
            return new ShareData();
        return shareData;
    }

    public void setShareData(ShareData shareData) {
        this.shareData = shareData;
    }

    public String getBigApi_h5_detailJump() {
        return bigApi_h5_detailJump;
    }

    public void setBigApi_h5_detailJump(String bigApi_h5_detailJump) {
        this.bigApi_h5_detailJump = bigApi_h5_detailJump;
    }

    public PostData getBigApi_h5_viewOnly() {
        return bigApi_h5_viewOnly;
    }

    public void setBigApi_h5_viewOnly(PostData bigApi_h5_viewOnly) {
        this.bigApi_h5_viewOnly = bigApi_h5_viewOnly;
    }

    public PostData getBigApi_h5_detailReplyMain() {
        return bigApi_h5_detailReplyMain;
    }

    public void setBigApi_h5_detailReplyMain(PostData bigApi_h5_detailReplyMain) {
        this.bigApi_h5_detailReplyMain = bigApi_h5_detailReplyMain;
    }
}
