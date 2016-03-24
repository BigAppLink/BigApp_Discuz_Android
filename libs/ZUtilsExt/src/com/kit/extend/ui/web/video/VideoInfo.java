package com.kit.extend.ui.web.video;

/**
 * Created by Zhao on 15/10/13.
 */
public class VideoInfo {

    /**
     * 视频页面url
     */
    private String videoUrl;

    /**
     * 视频的直接播放url
     */
    private String videoPlayUrl;

    private int videoWidth;
    private int videoHeight;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    public String getVideoPlayUrl() {
        return videoPlayUrl;
    }

    public void setVideoPlayUrl(String videoPlayUrl) {
        this.videoPlayUrl = videoPlayUrl;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }
}
