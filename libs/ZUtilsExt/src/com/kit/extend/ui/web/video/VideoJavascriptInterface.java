package com.kit.extend.ui.web.video;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.kit.utils.GsonUtils;
import com.kit.utils.ZogUtils;

public class VideoJavascriptInterface {

    private Context context;
    private VideoInfo videoInfo;
    private Handler handler;

    public VideoJavascriptInterface(Context context, VideoInfo videoInfo, Handler handler) {
        this.context = context;
        this.videoInfo = videoInfo;
        this.handler = handler;
    }

    @JavascriptInterface
    public String getVideoInfo() {
        ZogUtils.printError(VideoJavascriptInterface.class, "getBBS getBBS getBBS getBBS getBBS");



        Object o = GsonUtils.toJson(videoInfo);
        ZogUtils.printError(VideoJavascriptInterface.class, "BBS BBS BBS:" + o);


        return o.toString();

    }



    @JavascriptInterface
    public void showSource(String html) {
        ZogUtils.printError(VideoJavascriptInterface.class, "HTML::::" + html);
    }


}