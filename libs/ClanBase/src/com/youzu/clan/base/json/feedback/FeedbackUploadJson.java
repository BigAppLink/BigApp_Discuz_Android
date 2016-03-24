package com.youzu.clan.base.json.feedback;

/**
 * Created by tangh on 2015/8/28.
 */
public class FeedbackUploadJson extends FeedBackJson {
    private FeedBackFileInfo data;

    public FeedBackFileInfo getData() {
        return data;
    }

    public void setData(FeedBackFileInfo data) {
        this.data = data;
    }
}
