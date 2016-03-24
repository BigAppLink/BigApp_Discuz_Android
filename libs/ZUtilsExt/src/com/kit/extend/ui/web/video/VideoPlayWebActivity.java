package com.kit.extend.ui.web.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.kit.extend.ui.web.WebActivity;
import com.kit.utils.DensityUtils;
import com.kit.utils.MessageUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;


/**
 * 视频全屏播放Activity
 * <p/>
 * 注意！此Activity必须为Fullscreen,并设置为横屏
 */
public class VideoPlayWebActivity extends WebActivity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WEB_INITOK_START_LOAD:
                    webFragment.setRefreshing(true);
                    refreshData();
                    break;
                case WEB_LOAD_SUCCESS:
                case WEB_LOAD_FAIL:
                    webFragment.setRefreshing(false);
                    break;

            }
        }
    };

    private VideoJavascriptInterface javascriptInterface;

    private String videoPlayUrl;
    private String videoUrl;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {

        super.onCreate(savedInstanceState, persistentState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏


        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if (((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }


    }


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


//<iframe iframe="" id="iyouzu_youku_0" height="200" width="" src="http://player.youku.com/embed/XMTMyNTI3MDYxNg==" frameborder="0" allowfullscreen=""></iframe>


    @Override
    public boolean getExtra() {
        super.getExtra();
        BundleData bundleData = IntentUtils.getData(getIntent());
        try {
            videoUrl = bundleData.getObject("videoUrl", String.class);
            content = videoUrl;
            videoPlayUrl = bundleData.getObject("videoPlayUrl", String.class);
//            content = "<body><div style=\"margin: 0px auto;\"> <center><iframe style=\"margin: 0px auto;\"  height=\"900px\" width=\"1000px\" src=\"" + content + "\" border=\"0\"  frameborder=\"0\" allowfullscreen=\"true\"></iframe></center></div></body>";
            title = bundleData.getObject("title", String.class);
            contentViewName = bundleData.getObject("contentViewName", String.class);
        } catch (Exception e) {
        }

        if (StringUtils.isEmptyOrNullOrNullStr(contentViewName)) {
            contentViewName = "activity_video_web";
        }


        return true;
    }

    @Override
    public boolean initWidget() {


        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setVideoPlayUrl(videoPlayUrl);
        videoInfo.setVideoUrl(videoPlayUrl);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        videoInfo.setVideoWidth(DensityUtils.px2dip(this, display.getWidth()));
        videoInfo.setVideoHeight(DensityUtils.px2dip(this, display.getHeight()));

        javascriptInterface = new VideoJavascriptInterface(this, videoInfo, handler);


        return super.initWidget();
    }


    /**
     * 初始化webview
     */
    public void initWebView() {

        webFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ZogUtils.printLog(VideoPlayWebActivity.class, "top refresh");
                refreshData();
            }
        });

        webFragment.getWebView().setBackgroundColor(0xFFFFFF);

        webFragment.getWebView().getSettings().setUseWideViewPort(true);
        webFragment.getWebView().getSettings().setLoadWithOverviewMode(true);

        webFragment.getWebView().setVerticalScrollBarEnabled(false);
        webFragment.getWebView().setHorizontalScrollBarEnabled(false);
        webFragment.getWebView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });


        webFragment.getWebView().addJavascriptInterface(javascriptInterface, "android");

    }

    @Override
    public void loadWeb() {
        super.loadWeb();
        MessageUtils.sendMessage(handler, WEB_INITOK_START_LOAD);

    }

    private void refreshData() {
        ZogUtils.printError(VideoPlayWebActivity.class,"videoPlayUrl:"+videoPlayUrl+" videoUrl:"+videoUrl);

        if (!StringUtils.isEmptyOrNullOrNullStr(videoPlayUrl)) {
            webFragment.getWebView().loadUrl("file:///android_asset/www/video.html");
            MessageUtils.sendMessage(handler, WEB_LOAD_SUCCESS);
        } else {
            webFragment.loadContent(videoUrl);
            MessageUtils.sendMessage(handler, WEB_LOAD_SUCCESS);
        }
    }

    /**
     * @param context
     * @param clazz   要跳转到的Activity，如果没有子类继承本类，可传本类
     * @param title
     * @param url     视频页面的地址
     */
    public static void gotoVideo(Context context, Class clazz, String title, String url) {
        BundleData bundleData = new BundleData();
        bundleData.put("title", title);
        bundleData.put("videoUrl", url);

        IntentUtils.gotoNextActivity(context, clazz, bundleData);
    }


    /**
     * 直接全屏播放视频
     *
     * @param context
     * @param clazz   要跳转到的Activity，如果没有子类继承本类，可传本类
     * @param title
     * @param url     视频的真实地址
     */
    public static void play(Context context, Class clazz, String title, String url) {
        BundleData bundleData = new BundleData();
        bundleData.put("title", title);
        bundleData.put("videoPlayUrl", url);

        IntentUtils.gotoNextActivity(context, clazz, bundleData);
    }
}
