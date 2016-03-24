package com.kit.extend.ui.web;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.kit.app.UIHandler;
import com.kit.extend.R;
import com.kit.extend.ui.web.client.DefaultWebViewClient;
import com.kit.extend.ui.web.client.VideoEnabledWebChromeClient;
import com.kit.extend.ui.web.model.WebviewGoToTop;
import com.kit.extend.ui.web.video.VideoPlayWebActivity;
import com.kit.extend.ui.web.view.RefreshLayout;
import com.kit.extend.ui.web.webview.LoadMoreWebView;
import com.kit.ui.BaseFragment;
import com.kit.utils.AnimUtils;
import com.kit.utils.CookieUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ResourceUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.WebViewUtils;
import com.kit.utils.ZogUtils;

import org.apache.http.cookie.Cookie;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WebFragment extends BaseFragment implements CookieKit, Handler.Callback {

    public static final int WEB_LOAD_END = 99;


    private LoadMoreWebView webView;

    private WebChromeClient webChromeClient;

    private VideoEnabledWebChromeClient videoEnabledWebChromeClient;

    private RefreshLayout refreshLayout;
    private ViewGroup videoLayout;
    private ProgressBar pb;
    private View loadingView;
    private ImageButton btnTop;


    private Timer timer;
    private TimerTask task;
//    private String cookie;

    private LoadMoreWebView.WebScrollListener webScrollListener;

    private String webTitle;

    public String title = "";
    public String content = "";
    public String contentViewName = "";
    public List<Cookie> cookies;


    private boolean isScrolling;

    /**
     * 是否显示回到顶部按钮
     */
    private int goToTopPosition = WebviewGoToTop.NONE;

    /**
     * 是否使用网页标题
     */
    private boolean isUseWebTitle = true;

    /**
     * 正在加载中的url
     */
    public String loadingUrl;

    public IInitWeb iInitWeb;

    @Override
    public boolean handleMessage(Message message) {

        switch (message.what) {

            case WEB_LOAD_END:
                ZogUtils.printLog(WebFragment.class, "WEB_LOAD_END WEB_LOAD_END");
                webView.getSettings().setBlockNetworkImage(false);
                pb.setVisibility(View.GONE);
                break;

        }
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (iInitWeb != null) {
            iInitWeb.initWebView();
            iInitWeb.loadWeb();
        }
    }

    @Override
    public View initWidget(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        Log.e("APP", "WebFragment initWidget contentViewName:" + contentViewName);
        View view = inflater.inflate(ResourceUtils.getResId(getActivity(), contentViewName, "layout"), container, false);

        webView = (LoadMoreWebView) view.findViewById(ResourceUtils.getResId(getActivity(), "webView", "id"));
        pb = (ProgressBar) view.findViewById(ResourceUtils.getResId(getActivity(), "pb", "id"));
        refreshLayout = (RefreshLayout) view.findViewById(ResourceUtils.getResId(getActivity(), "refreshLayout", "id"));
//        nonVideoLayout = findViewById(ResourceUtils.getResId(getApplication(), "nonVideoLayout", "id"));
        videoLayout = (ViewGroup) view.findViewById(ResourceUtils.getResId(getActivity(), "videoLayout", "id"));
        loadingView = getActivity().getLayoutInflater().inflate(ResourceUtils.getResId(getActivity(), "view_loading_video", "layout"), null);
        btnTop = (ImageButton) view.findViewById(ResourceUtils.getResId(getActivity(), "btnTop", "id"));

        initWebView();

        loadContent(content);

        return view;
    }


    public void initWebView() {
        //noinspection all

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                ZogUtils.printLog(WebFragment.class, "top refresh content:" + content);
                webView.reload();
                refreshLayout.setLoading(false);
            }
        });


        webView.setWebScrollListener(new LoadMoreWebView.WebScrollListener() {
            @Override
            public void onTop() {
//                ZogUtils.printLog(WebActivity.class, "onTop onTop onTop onTop");

                if (goToTopPosition == WebviewGoToTop.RIGHT) {
                    AnimUtils.hidden(getActivity(), btnTop, R.anim.anim_half_right_out);
                } else if (goToTopPosition == WebviewGoToTop.LEFT) {
                    AnimUtils.hidden(getActivity(), btnTop, R.anim.anim_half_left_out);
                }
                if (webScrollListener != null)
                    webScrollListener.onTop();
            }

            @Override
            public void onCenter() {
//                ZogUtils.printLog(WebActivity.class, "onCenter onCenter onCenter onCenter");

                if (goToTopPosition == WebviewGoToTop.RIGHT) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnTop.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    btnTop.setLayoutParams(params);
                    AnimUtils.show(getActivity(), btnTop, R.anim.anim_half_right_in);
                } else if (goToTopPosition == WebviewGoToTop.LEFT) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnTop.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    btnTop.setLayoutParams(params);
                    AnimUtils.show(getActivity(), btnTop, R.anim.anim_half_left_in);
                }
                if (webScrollListener != null)
                    webScrollListener.onCenter();
            }

            @Override
            public void onBottom() {
//                ZogUtils.printLog(WebActivity.class, "onBottom onBottom onBottom onBottom");

                if (webScrollListener != null)
                    webScrollListener.onBottom();
            }

            @Override
            public void onScroll(int dx, int dy) {
                if (Math.abs(dy) > 1) {
                    isScrolling = true;
                } else {
                    isScrolling = false;
                }
//                ZogUtils.printLog(WebActivity.class, "onScrolling onScrolling onScrolling onScrolling");
                if (webScrollListener != null)
                    webScrollListener.onScroll(dx, dy);
            }
        });

        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isScrolling)
                    webView.scrollTo(0, 0);
            }
        });

        videoEnabledWebChromeClient = new VideoEnabledWebChromeClient(refreshLayout, videoLayout, loadingView, webView) // See all available constructors...
        {
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);

                ZogUtils.printLog(WebFragment.class, progress + "");
                pb.setProgress(progress);
                if (progress >= 100) {
                    cancelTimer();
                    pb.setVisibility(View.GONE);
                    webView.getSettings().setBlockNetworkImage(false);
                } else {
                    webView.getSettings().setBlockNetworkImage(true);
                    pb.setVisibility(View.VISIBLE);
                    startTimerTask();

                }
                if (webChromeClient != null)
                    webChromeClient.onProgressChanged(webView, progress);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                if (webChromeClient != null)
                    webChromeClient.onShowCustomView(view, callback);

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                ZogUtils.printError(WebFragment.class, "title title title:" + title);
                setWebTitle(title);

                if (isUseWebTitle
                        && !StringUtils.isEmptyOrNullOrNullStr(webTitle)
                        && getActivity() != null) {
                    getActivity().setTitle(webTitle);
                }

                if (webChromeClient != null)
                    webChromeClient.onReceivedTitle(view, title);

            }
        };

//        videoEnabledWebChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
//
//            @Override
//            public void toggledFullscreen(boolean fullscreen) {
//                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
//
//                ZogUtils.printError(WebViewUtils.class, "toggledFullscreen:" + fullscreen);
//                if (fullscreen) {
//
//                    VideoPlayWebActivity.gotoVideo();
//                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
//                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//                    getActivity().getWindow().setAttributes(attrs);
//                    if (android.os.Build.VERSION.SDK_INT >= 14) {
//                        //noinspection all
//                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//                    }
//                } else {
//                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
//                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//                    getActivity().getWindow().setAttributes(attrs);
//                    if (android.os.Build.VERSION.SDK_INT >= 14) {
//                        //noinspection all
//                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//                    }
//                }
//            }
//
//
//        });
        webView.setWebChromeClient(videoEnabledWebChromeClient);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        //启用插件（播放视频无画面还需在manifest里面Application加入属性android:hardwareAccelerated="true"）
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        //尝试解决白板
        webView.getSettings().setDomStorageEnabled(true);

        webView.setWebViewClient(new DefaultWebViewClient(this));

        setCookieFromCookieStore(getActivity(), content, cookies);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


    }

    @Override
    public boolean getExtra() {
        super.getExtra();
        try {
            content = getArguments().getString("content");
            contentViewName = getArguments().getString("contentViewName");
        } catch (Exception e) {

        }

        if (StringUtils.isEmptyOrNullOrNullStr(contentViewName)) {
            contentViewName = "fragment_web";
        }


        return true;
    }

    public void loadContent(String cont) {
        if (StringUtils.isEmptyOrNullOrNullStr(cont)) {
//            ToastUtils.mkLongTimeToast(getActivity(),getString(ResourceUtils.getStringId(getApplication(),"empty_url")));
            return;
        }

        ZogUtils.printError(WebFragment.class, "loadContent:" + cont);

        switch (UrlScheme.ofUri(cont)) {
            case HTTP:
            case HTTPS:
                loadingUrl = cont;
                WebViewUtils.loadUrl(getActivity(), webView, loadingUrl, true);
                break;

            case WWW:
                loadingUrl = "http://" + cont;
                WebViewUtils.loadUrl(getActivity(), webView, loadingUrl, true);
                break;

            default:
                WebViewUtils.loadContent(getActivity(), webView, cont);

        }
    }


    private void callHiddenWebViewMethod(String name) {
        if (webView != null) {
            try {
                Method method = WebView.class.getMethod(name);
                method.invoke(webView);
            } catch (NoSuchMethodException e) {
                ZogUtils.printLog(WebFragment.class, "No such method: " + name + e.toString());
            } catch (IllegalAccessException e) {
                ZogUtils.printLog(WebFragment.class, "Illegal Access: " + name + e.toString());
            } catch (InvocationTargetException e) {
                ZogUtils.printLog(WebFragment.class, "Invocation Target Exception: " + name + e.toString());
            }
        }
    }


    /**
     * 为了防止说页面一直没加载完，10秒后强制加载图片，算加载完（伪完）
     */
    private void startTimerTask() {
        cancelTimer();
        //LogUtils.printLog(getClass(), "startTimerTask");
        task = new TimerTask() {
            public void run() {
                if (pb.getProgress() <= 100)
                    UIHandler.sendMessage(WebFragment.this, WEB_LOAD_END);
            }
        };
        timer = new Timer();

        timer.schedule(task, 10 * 1000);
    }

    private void cancelTimer() {
        if (task != null) {
            task.cancel(); // 将原任务从队列中移除
        }

        if (timer != null) {
            timer.cancel();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
//        webView.pauseTimers();
//        if (isFinishing()) {
//            webView.loadUrl("about:blank");
//            setContentView(new FrameLayout(getActivity()));
//        }
        callHiddenWebViewMethod("onPause");
    }


    @Override
    public void onResume() {
        super.onResume();
        callHiddenWebViewMethod("onResume");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void setCookieFromCookieStore(Context context, String url, List<Cookie> cks) {
        CookieUtils.syncCookie(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        if (!ListUtils.isNullOrContainEmpty(cks)) {
            for (int i = 0; i < cks.size(); i++) {
                Cookie cookie = cks.get(i);
                String cookieStr = cookie.getName() + "=" + cookie.getValue() + ";"
                        + "expiry=" + cookie.getExpiryDate() + ";"
                        + "domain=" + cookie.getDomain() + ";"
                        + "path=/";
//                ZogUtils.printError(WebFragment.class, "set cookie string:" + cookieStr);
                cookieManager.setCookie(url, cookieStr);//cookieStr是在HttpClient中获得的cookie

            }
        }
    }

    public void setRefreshing(boolean isRefreshing) {
        refreshLayout.setRefreshing(isRefreshing);
    }

    public boolean isRefreshing() {
        return refreshLayout.isRefreshing();
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        refreshLayout.setOnRefreshListener(onRefreshListener);
    }

    public LoadMoreWebView getWebView() {
        return webView;
    }


    public void setWebScrollListener(LoadMoreWebView.WebScrollListener webScrollListener) {
        this.webScrollListener = webScrollListener;
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        this.webChromeClient = webChromeClient;
    }


    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    /**
     * @param goToTopPosition WebviewGoToTop.RIGHT WebviewGoToTop.LEFT WebviewGoToTop.NONE
     */
    public void setGoToTopPosition(int goToTopPosition) {
        this.goToTopPosition = goToTopPosition;
    }


    public void setUseWebTitle(boolean isUseWebTitle) {
        this.isUseWebTitle = isUseWebTitle;
    }

    public void setInitWeb(IInitWeb iInitWeb) {
        this.iInitWeb = iInitWeb;
    }

    public interface IInitWeb {
        public void initWebView();

        public void loadWeb();
    }
}
