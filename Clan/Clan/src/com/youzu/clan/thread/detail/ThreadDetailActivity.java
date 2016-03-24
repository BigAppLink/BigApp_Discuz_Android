package com.youzu.clan.thread.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeUtil;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.kit.extend.ui.web.client.DefaultWebViewClient;
import com.kit.share.SharePopupWindow;
import com.kit.utils.ActionBarUtils;
import com.kit.utils.FragmentUtils;
import com.kit.utils.KeyboardUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.WebActivity;
import com.youzu.clan.app.config.AppConfig;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.callback.FavThreadCallback;
import com.youzu.clan.base.common.ResultCode;
import com.youzu.clan.base.enums.MessageVal;
import com.youzu.clan.base.enums.ReplyButtonType;
import com.youzu.clan.base.json.FavThreadJson;
import com.youzu.clan.base.json.forumdisplay.Thread;
import com.youzu.clan.base.json.thread.inner.Post;
import com.youzu.clan.base.json.threadview.Report;
import com.youzu.clan.base.json.threadview.ThreadDetailJson;
import com.youzu.clan.base.json.threadview.ThreadDetailVariables;
import com.youzu.clan.base.net.DoCheckPost;
import com.youzu.clan.base.net.ThreadHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.jump.JumpWebUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;
import com.youzu.clan.myfav.MyFavUtils;
import com.youzu.clan.share.SharePlatformActionListener;
import com.youzu.clan.thread.ThreadReplyActivity;
import com.youzu.clan.thread.ThreadReportActivity;
import com.youzu.clan.thread.detail.menu.DetailMenuFragment;
import com.youzu.clan.thread.detail.reply.QuickReplyFragment;
import com.youzu.clan.threadandarticle.ZhaoHandler;
import com.youzu.clan.threadandarticle.model.BigAppH5;
import com.youzu.clan.threadandarticle.model.ShareData;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;


public class ThreadDetailActivity extends WebActivity implements View.OnClickListener, Handler.Callback, MoreActionProviderCallback {

    public static final int REQUEST_CODE = 0x111;

    private int finishTime = 0;

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.tvPage)
    private TextView tvPage;

    @ViewInject(R.id.tvPage2)
    public TextView tvPage2;

    @ViewInject(R.id.rlJumpPage)
    public View rlJumpPage;


    //    @ViewInject(R.id.ivFav)
//    public ImageView ivFav;
//
//    @ViewInject(R.id.tvFav)
//    public TextView tvFav;
//
    @ViewInject(R.id.et)
    public EditText et;

    @ViewInject(R.id.btnCancel)
    public ImageButton btnCancel;

    @ViewInject(R.id.btnOk)
    public ImageButton btnOk;


    private SharePopupWindow share;

    DetailMenuFragment detailMenuFragment;
    QuickReplyFragment quickReplyFragment;

    private int jumpType = 0;


    public int page = 1;
    private int postno = 1;
    private int totalPage = 1;
    private int maxPosition = 1;
    public String tid;
    //    private String favid;
    private String authorid;
    private ThreadDetailJavascriptInterface javascriptInterface;
//    private LoadingDialogFragment loadingDialogFragment;

    private boolean isLookAuthor = false;
    public boolean isFavThread = false;

    private String replyButtonType;

    private ZhaoHandler bridgeHandler;


    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        LoadingDialogFragment.getInstance(this).dismissAllowingStateLoss();

        switch (what) {
            case SharePlatformActionListener.ON_CANCEL:
                break;
            case SharePlatformActionListener.ON_ERROR:
                Toast.makeText(this
                        , (msg.obj != null ? msg.obj.toString() : getString(R.string.share_failed))
                        , Toast.LENGTH_SHORT).show();
                break;
            case SharePlatformActionListener.ON_COMPLETE:
                Toast.makeText(this
                        , (msg.obj != null ? msg.obj.toString() : getString(R.string.share_completed))
                        , Toast.LENGTH_SHORT).show();

                break;

            case WEB_INITOK_START_LOAD:
                ZogUtils.printError(ThreadDetailActivity.class, "webFragment" + webFragment);
                webFragment.setRefreshing(true);
                refreshData();
                break;
            case WEB_LOAD_FAIL:
            case WEB_LOAD_SUCCESS:
                webFragment.setRefreshing(false);
                String res = msg.getData().getString("data");
                bridgeHandler.callBack(res);
                break;

        }
        if (share != null) {
            share.dismiss();
        }
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this, true);
    }

    @Override
    public boolean initWidget() {
        super.initWidget();

        BundleData bundleData = new BundleData();

        switch (replyButtonType) {
            case ReplyButtonType.MENU_THREE:
                detailMenuFragment = new DetailMenuFragment();
                FragmentUtils.replace(getSupportFragmentManager(), R.id.replaceBottom, detailMenuFragment);
                break;
            case ReplyButtonType.CHAT:
                quickReplyFragment = new QuickReplyFragment();
                FragmentUtils.replace(getSupportFragmentManager(), R.id.replaceBottom, quickReplyFragment);
                break;
        }
        return true;
    }

    @Override
    public void loadWeb() {
        super.loadWeb();
    }

    @Override
    public boolean getExtra() {
        super.getExtra();
        Bundle bundle = getIntent().getExtras();
        tid = bundle.getString("tid");
        contentViewName = "activity_thread_detail";

        replyButtonType = AppSPUtils.getContentConfig(this).getReplyButtonType();

//        replyButtonType = ReplyButtonType.CHAT;

//        tid = "25";
        return true;
    }

    @Override
    public void initWebView() {
        super.initWebView();
        webFragment.setUseWebTitle(false);
        webFragment.
                setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ZogUtils.printLog(ThreadDetailActivity.class, "top refresh");

                        refreshData();
                    }
                });

        if (AppConfig.NET_DEBUG) {
            webFragment.getWebView().getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        webFragment.getWebView().setBackgroundColor(
                getResources().getColor(R.color.background)
        );


        webFragment.getWebView().setWebViewClient(new DefaultWebViewClient(webFragment) {

                                                      @Override
                                                      public void onPageFinished(WebView view, String url) {

//                                                     if (AppConfig.NET_DEBUG) {
//                                                         //只有线上测试环境才会重定向被加载两次
//                                                         if (finishTime > 1) {
//                                                             return;
//                                                         }
//                                                         finishTime++;
//                                                     }

                                                          ZogUtils.printError(ThreadDetailActivity.class, "onPageFinished url:::::::::::" + url);

                                                          super.onPageFinished(view, url);

                                                          BridgeUtil.webViewLoadLocalJs(view, "www/js/zhao/bigappNative.js");


                                                      }

                                                      @Override
                                                      public void loadingUrl(WebView view, String url) {
                                                          JumpWebUtils.gotoWeb(ThreadDetailActivity.this, "", url);
                                                      }


                                                      @Override
                                                      public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                                          if (failingUrl.contains("#")) {
                                                              ZogUtils.printError(ThreadDetailActivity.class, "failing url:" + failingUrl);
                                                              final int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
                                                              if (sdkVersion > Build.VERSION_CODES.GINGERBREAD) {
                                                                  String[] temp;
                                                                  temp = failingUrl.split("#");
                                                                  view.loadUrl(temp[0]); // load page without internal link

                                                              }

                                                              view.loadUrl(failingUrl);  // try again
                                                          }
//                                                          else {
//                                                              view.loadUrl("file:///android_asset/tableofcontents.html");
//                                                          }
                                                      }
                                                  }

        );


        loadHtmlInjectJS();

    }


    /**
     * 加载html并注入js
     */
    private void loadHtmlInjectJS() {
        if (AppConfig.NET_DEBUG) {
//            webFragment.getWebView().loadUrl("http://192.168.180.23:8080/gaofy/#/posts_detail?bigapp_device=android");
            webFragment.getWebView().loadUrl("http://192.168.180.23:8080/gaofy/newest/#/posts_detail?bigapp_device=android");
        } else
            webFragment.getWebView().loadUrl("file:///android_asset/www/index.html#/posts_detail?bigapp_device=android");


        webFragment.getWebView().getSettings().setJavaScriptEnabled(true);

        javascriptInterface = new ThreadDetailJavascriptInterface(this, this);
        webFragment.getWebView().addJavascriptInterface(javascriptInterface, "android");

        bridgeHandler = new ZhaoHandler(this);
        webFragment.getWebView().setDefaultHandler(bridgeHandler);
        webFragment.getWebView().registerHandler("getData", bridgeHandler);
        webFragment.getWebView().registerHandler("clickImage", bridgeHandler);
        webFragment.getWebView().registerHandler("clickAvatar", bridgeHandler);
        webFragment.getWebView().registerHandler("showToast", bridgeHandler);


    }


    public void refreshData() {
        page = 0;
        if (webFragment != null && webFragment.getWebView() != null) {
            loadHtmlInjectJS();
        }
        isLookAuthor = false;

        refreshFavStatus();
    }


    private ThreadDetailJson getThreadDetailData() {
        return bridgeHandler.getDoDetail().getThreadDetailData();
    }


    private ThreadDetailVariables getVariables() {
        if (bridgeHandler == null
                || bridgeHandler.getDoDetail() == null
                || bridgeHandler.getDoDetail().getThreadDetailData() == null
                || bridgeHandler.getDoDetail().getThreadDetailData().getVariables() == null)
            return null;
        return bridgeHandler.getDoDetail().getThreadDetailData().getVariables();

    }


    private String getFavid() {
        String favid = null;
        if (getVariables() != null) {
            Thread thread = getVariables().getThread();
            if (thread == null)
                return null;

            com.youzu.clan.base.json.favthread.Thread favThread = MyFavUtils.getFavThreadById(this, thread.getTid());

            if (favThread != null) {
                favid = favThread.getFavid();
                ZogUtils.printLog(ThreadDetailActivity.class, "favid" + favid);
            }
        }
        return favid;
    }


    /**
     * 刷新主题的收藏状态
     */
    public void refreshFavStatus() {
        String favid = getFavid();
        if (!StringUtils.isEmptyOrNullOrNullStr(favid)) {
            isFavThread = true;
        } else {
            isFavThread = false;
        }

        switch (replyButtonType) {
            case ReplyButtonType.MENU_THREE:
                detailMenuFragment.setFav(isFavThread);
                break;

        }

        invalidateOptionsMenu();
    }


    /**
     * 获取数据之后，初始化网页
     */
    public void setPage(int page) {
        ZogUtils.printError(ThreadDetailActivity.class, "getThreadDetailData().getVariables():" + getVariables());
        ZogUtils.printError(ThreadDetailActivity.class, "getThreadDetailData().getVariables().getTotalpage():" + getVariables().getTotalpage());

        this.page = page;
    }


    @Override
    public boolean isDoNext(String doWhat) {
        if (getVariables() == null) {
            ToastUtils.mkShortTimeToast(this, getString(R.string.wait_a_moment));
            return false;
        }

        switch (doWhat) {
            case "addFav":
            case "delFav":
            case "report":
            case "checkPost":


                if (ClanUtils.isToLogin(this, null, Activity.RESULT_OK, false)) {
                    return false;
                }
            default:
                return true;
        }

    }


    /**
     * 只看楼主
     */
    private void lookAuthor() {
        if (!isDoNext("lookAuthor")) {
            return;
        }
        isLookAuthor = !isLookAuthor;
//        getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
        invalidateOptionsMenu();
        authorid = null;
        if (isLookAuthor
                && getVariables() != null
                && getVariables().getThread() != null
                && getVariables().getThread().getAuthorid() != null) {
            authorid = getVariables().getThread().getAuthorid();
        }
        page = 0;

        // bridgeHandler.getDoDetail().getThreadDetailData(this, tid, authorid, "1", false);
        HashMap data = new HashMap();
        if (isLookAuthor) {
            data.put("role", 1);
        }
        webFragment.getWebView().callHandler("viewAutherOnly", JsonUtils.toJSONString(data), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                ZogUtils.printError(ThreadDetailActivity.class, "data:" + data);
            }
        });

    }


    /**
     * 跳楼
     *
     * @param postno
     */
    private void jumpPost(int postno) {
        if (!isDoNext("jumpPost")) {
            return;
        }
        this.postno = postno;
        KeyboardUtils.hiddenKeyboard(this, et);

        invalidateOptionsMenu();

        rlJumpPage.setVisibility(View.GONE);


        //跳页清空历史数据，避免中间有楼层差
//        bridgeHandler.getDoDetail().getThreadDetailData(this, tid, "", page + "", true);


        HashMap data = new HashMap();
        data.put("position", postno);
        webFragment.getWebView().callHandler("jumpAction", JsonUtils.toJSONString(data), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                ZogUtils.printError(ThreadDetailActivity.class, "data:" + data);
            }
        });

    }


    /**
     * 跳页
     *
     * @param pg
     */
    private void jumpPage(int pg) {
        if (!isDoNext("jumpPage")) {
            return;
        }

        this.page = pg;

        KeyboardUtils.hiddenKeyboard(this, et);

        ZogUtils.printLog(ThreadDetailActivity.class, "pg:" + pg);

        invalidateOptionsMenu();

        rlJumpPage.setVisibility(View.GONE);

        HashMap data = new HashMap();
        data.put("page", pg);
        webFragment.getWebView().callHandler("jumpAction", JsonUtils.toJSONString(data), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                ZogUtils.printError(ThreadDetailActivity.class, "data:" + data);
            }
        });

    }


    @Override
    public void doShare() {
        if (!isDoNext("doShare")) {
            return;
        }

        ZogUtils.printError(ThreadDetailActivity.class, "bridgeHandler:" + bridgeHandler + " bridgeHandler.getCallBackFunction():" + bridgeHandler.getCallBackFunction() + JsonUtils.toJSONString(bridgeHandler.getCallBackFunction()));
        webFragment.getWebView().callHandler("getShareInfo", "share", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                ZogUtils.printError(ThreadDetailActivity.class, "data:" + data);
                ShareData shareData = JsonUtils.parseObject(data, ShareData.class);
                bridgeHandler.getDoDetail().doShare(shareData);
            }
        });
    }

    public void addFav() {
        if (!isDoNext("addFav")) {
            return;
        }

        ThreadHttp.favThread(this, tid, new FavThreadCallback(this) {

            @Override
            public void onSuccess(Context ctx, FavThreadJson favThreadJson) {
                super.onSuccess(ctx, favThreadJson);
                String messageval = "";
                try {
                    messageval = favThreadJson.getMessage().getMessageval();
                } catch (Exception e) {
                }

                if (StringUtils.isEmptyOrNullOrNullStr(messageval)) {
                    ToastUtils.mkShortTimeToast(ThreadDetailActivity.this, getString(R.string.fav_fail));
                    return;
                }

                switch (messageval) {
                    case MessageVal.FAVORITE_DO_SUCCESS:
                        MyFavUtils.saveOrUpdateThread(ThreadDetailActivity.this
                                , getVariables().getThread()
                                , favThreadJson.getVariables().getFavid());
                        refreshFavStatus();
                        ToastUtils.mkShortTimeToast(ThreadDetailActivity.this, getString(R.string.fav_success));
                        break;
                    case MessageVal.TO_LOGIN:
                        ClanUtils.gotoLogin(ThreadDetailActivity.this, null, Activity.RESULT_OK, false);
                        refreshFavStatus();
                        break;
                    case MessageVal.FAVORITE_REPEAT:
                        isFavThread = true;
                        refreshFavStatus();
                        ClanUtils.loadMyFav(ThreadDetailActivity.this);
                        ToastUtils.mkShortTimeToast(ThreadDetailActivity.this, favThreadJson.getMessage().getMessagestr());
                        break;
                    default:
                        ToastUtils.mkShortTimeToast(ThreadDetailActivity.this, favThreadJson.getMessage().getMessagestr());
                }

            }


            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(ThreadDetailActivity.this, errorCode, msg);
                ToastUtils.show(getApplicationContext(), msg);
                page--;
            }
        });
    }

    public void delFav() {

        if (!isDoNext("delFav")) {
            return;
        }

        final String favid = getFavid();
        if (StringUtils.isEmptyOrNullOrNullStr(favid)) {
            return;
        }
        String[] ids = {favid};
        ThreadHttp.delFavThread(this, ids, new FavThreadCallback(this) {

            @Override
            public void onSuccess(Context ctx, FavThreadJson favThreadJson) {
                super.onSuccess(ctx, favThreadJson);
                String messageval = null;
                try {
                    messageval = favThreadJson.getMessage().getMessageval();
                } catch (Exception e) {
                }

                if (StringUtils.isEmptyOrNullOrNullStr(messageval)) {
                    ToastUtils.mkShortTimeToast(ThreadDetailActivity.this, getString(R.string.fav_del_fail));
                    return;
                }
                if ((MessageVal.FAVORITE_DELETE_SUCCEED).equals(messageval)) {
                    isFavThread = false;
                    invalidateOptionsMenu();
                    MyFavUtils.deleteThread(ThreadDetailActivity.this
                            , getVariables().getThread().getTid()
                    );
                    refreshFavStatus();
                    ToastUtils.mkShortTimeToast(ThreadDetailActivity.this, getString(R.string.fav_del_success));
                } else if (MessageVal.TO_LOGIN.equals(messageval)) {
                    ClanUtils.gotoLogin(ThreadDetailActivity.this, null, Activity.RESULT_OK, false);
                } else {
                    ToastUtils.mkShortTimeToast(ThreadDetailActivity.this, favThreadJson.getMessage().getMessagestr());
                }
            }


            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(ThreadDetailActivity.this, errorCode, msg);
                ToastUtils.show(getApplicationContext(), msg);
//                page--;
            }
        });
    }

    @Override
    public void delete() {
//        PostData postData = bridgeHandler.getDataFromBigAppH5(PostData.class);
        ThreadDetailVariables variables = getVariables();
        if (variables == null)
            return;
        bridgeHandler.getDoDetail().doDelete(variables.getFid(), variables.getThread().getTid());
    }


    @Override
    public void doJumpPage() {
        if (!isDoNext("doJumpPage")) {
            return;
        }

        ZogUtils.printLog(MoreActionProvider.class, "doJumpPage activity.rlJumpPage:" + this.rlJumpPage);
        int pg = page;
        if (pg < 1) {
            pg = 1;
        }

        totalPage = Integer.parseInt(getVariables().getTotalpage());

        String pageNumber = String.format(getResources().getString(R.string.page_number),
                pg + "/" + totalPage);
        tvPage.setText(pageNumber);

        tvPage2.setText(pg + "/" + totalPage);

        et.setText(pg + "");

        rlJumpPage.setVisibility(View.VISIBLE);
        KeyboardUtils.showKeyboard(this, et);
        jumpType = 0;
    }


    @Override
    public void doJumpPost() {
        if (!isDoNext("doJumpPost")) {
            return;
        }

        int pn = postno;
        if (pn < 1) {
            pn = 1;
        }

        BigAppH5 bigAppH5 = bridgeHandler.getDoDetail().getBigAppH5();

        maxPosition = bigAppH5.getPostData().getMaxposition();
        ZogUtils.printLog(MoreActionProvider.class, "activity.doJumpPost maxPosition:" + maxPosition);

        String pageNumber = String.format(getResources().getString(R.string.page_number),
                pn + "/" + maxPosition);
        tvPage.setText(pageNumber);

        tvPage2.setText(pn + "/" + maxPosition);

        et.setText(pn + "");

        rlJumpPage.setVisibility(View.VISIBLE);
        KeyboardUtils.showKeyboard(this, et);

        jumpType = 1;
    }


    /**
     * 举报
     */
    public void report() {

        if (!isDoNext("report")) {
            return;
        }


        Report report = getVariables().getReport();
        //兼容旧的版本
        if (report == null || report.getContent() == null || report.getContent().size() == 0 || StringUtils.isEmptyOrNullOrNullStr(report.getContent().get(0))) {
            //ToastUtils.mkShortTimeToast(this, getResources().getString(R.string.wait_a_moment));
            ZogUtils.printError(ThreadDetailActivity.class, "举报内容获取失败");
            report = new Report();
            ArrayList<String> content = new ArrayList<>();
            String[] contents = getResources().getStringArray(R.array.defaultReport);
            for (int i = 0; i < contents.length; i++) {
                content.add(contents[i]);
            }
            report.setContent(content);
            getVariables().setReport(report);
        }
        BundleData b = new BundleData();
        b.put(Key.KEY_REPORT, getThreadDetailData());
        b.put(Key.KEY_REPORT_TYPE, true);
        IntentUtils.gotoNextActivity(this, ThreadReportActivity.class, b);
        // IntentUtils.gotoNextActivity(this,ThreadReportActivity.class);
    }


    @Override
    public String getShareUrl() {

        if (!isDoNext("getShareUrl")) {
            return null;
        }


        if (getVariables() != null && getVariables().getThread() != null) {
            Thread thread = getVariables().getThread();
            return thread.getShareUrl();
        }
        return null;
    }

    /**
     * 检查权限
     *
     * @param post 为null时，为回复楼主；为其他大于1的数字时候，为回复跟帖
     */
    public void checkPost(final Post post) {

        if (!isDoNext("checkPost")) {
            return;
        }

//        if (post != null)
//            ZogUtils.printError(ThreadDetailActivity.class, JsonUtils.toJSON(post).toString());

        DoCheckPost.checkPostBeforeReply(this, getVariables().getFid(), new InjectDo() {
            @Override
            public boolean doSuccess(Object baseJson) {

                if (getThreadDetailData() != null) {
                    //回帖
                    BundleData bundleData = new BundleData();
                    bundleData.put("HotThreadDetailJson", getThreadDetailData());
                    bundleData.put("CheckPostJson", baseJson);
                    bundleData.put("fid", getVariables().getFid());
//                            bundleData.put("position", position);
                    if (post != null)
                        bundleData.put("post", post);

                    IntentUtils.gotoSingleNextActivity(ThreadDetailActivity.this
                            , ThreadReplyActivity.class, bundleData, ThreadDetailActivity.REQUEST_CODE);
                }
                return false;
            }

            @Override
            public boolean doFail(Object baseJson, String tag) {
                return false;
            }
        });


    }


    @Override
    public boolean isHaveShare() {
        return getString(R.string.is_use_share).equals("1");
    }

    @Override
    public boolean isHaveFav() {
        return true;
    }

    @Override
    public boolean isHaveJumpPage() {
        return true;
    }

    @Override
    public boolean isHaveJumpPost() {
        return true;
    }

    @Override
    public boolean isHaveReport() {
        return true;
    }

    @Override
    public boolean isFav() {
        return isFavThread;
    }


    @Override
    public boolean isHaveDelete() {
        if (getVariables() == null) {
            return false;
        }
        return "1".equals(getVariables().getIsmoderator());
    }


    public void fav() {
        if (isFavThread) {
            delFav();
        } else {
            addFav();
        }
    }

    public void setSharePopupWindow(SharePopupWindow share) {
        this.share = share;
    }


    public String getReplyButtonType() {
        return replyButtonType;
    }

    public DetailMenuFragment getDetailMenuFragment() {
        return detailMenuFragment;
    }

    public QuickReplyFragment getQuickReplyFragment() {
        return quickReplyFragment;
    }

    public void refreshAfterReply() {
        webFragment.getWebView().loadUrl("javascript:window.BigAppH5.bigApi_h5_detailReplyMain()");
        switch (replyButtonType) {
            case ReplyButtonType.MENU_THREE:
                break;

            case ReplyButtonType.CHAT:
                quickReplyFragment.reset();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_thread_detail, menu);

        MenuItem item = menu.findItem(R.id.action_more);
        MoreActionProvider provider = (MoreActionProvider) MenuItemCompat.getActionProvider(item);
        provider.setCallback(this, this);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        ActionBarUtils.setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (isLookAuthor) {
            menu.findItem(R.id.action_author).setIcon(R.drawable.ic_long_all);
        } else {
            menu.findItem(R.id.action_author).setIcon(R.drawable.ic_author_white);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ThemeUtils.initResource(this);


        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            case R.id.action_author:
                lookAuthor();
                return true;

//            case R.id.action_more:
//                // Get the ActionProvider for later usage
//                provider = (MoreActionProvider) menu.findItem(R.id.menu_share)
//                        .getActionProvider();
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.btnOk, R.id.btnCancel, R.id.llReply, R.id.tvPage, R.id.llComment, R.id.llFav, R.id.llShare})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                String etStr = et.getText().toString();
                if (jumpType == 0) {
                    if (StringUtils.isEmptyOrNullOrNullStr(etStr)) {
                        ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_page_number_1));

                    } else {
                        //验证页码
                        int pg = Integer.parseInt(etStr);

                        if (pg < 1) {
                            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_page_number_2));
                        } else if (pg > totalPage) {
                            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_page_number_3));
                        } else {//通过验证
                            jumpPage(pg);
                        }
                    }
                } else {
                    if (StringUtils.isEmptyOrNullOrNullStr(etStr)) {
                        ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_post_number_1));

                    } else {
                        //验证楼层
                        int postno = Integer.parseInt(etStr);

                        if (postno < 1) {
                            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_post_number_2));
                        } else if (postno > maxPosition) {
                            ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_post_number_3));
                        } else {//通过验证
                            jumpPost(postno);
                        }
                    }
                }
                break;

            case R.id.btnCancel:
                rlJumpPage.setVisibility(View.GONE);
                KeyboardUtils.hiddenKeyboard(this, et);
                break;

            case R.id.tvPage:
                rlJumpPage.setVisibility(View.VISIBLE);
                KeyboardUtils.showKeyboard(this, et);
                break;

            case R.id.llComment:
            case R.id.llReply:
                checkPost(null);
                break;
            case R.id.llFav:
                fav();
                break;

            case R.id.llShare:
                doShare();
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ZogUtils.printError(ThreadDetailActivity.class, "resultCode:" + resultCode + " requestCode:" + requestCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (replyButtonType) {
                case ReplyButtonType.MENU_THREE:
                    detailMenuFragment.onActivityResult(requestCode, resultCode, data);
                    break;

                case ReplyButtonType.CHAT:
                    quickReplyFragment.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }

        //回复之后刷新评论
        if (requestCode == REQUEST_CODE) {

            switch (resultCode) {

                case ResultCode.RESULT_CODE_REPLY_MAIN_OK:
                case ResultCode.RESULT_CODE_REPLY_POST_OK:
                    refreshAfterReply();
                    break;

                case ResultCode.RESULT_CODE_JOIN_ACTIVITY:
                    Bundle extras = data.getExtras();
                    String response = "";
                    if (extras != null) {
                        response = extras.getString("apply_result");
                    }
                    ZogUtils.printError(ThreadDetailActivity.class, "RESULT_CODE_JOIN_ACTIVITY response:" + response);
                    bridgeHandler.callBack(response);

                    break;
            }


        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (loadingDialogFragment != null) {
//        LoadingDialogFragment.getInstance(this).dismissAllowingStateLoss();
//        }
        if (share != null) {
            share.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ZogUtils.printError(ThreadDetailActivity.class, "onBackPressed onBackPressed");
        if (share != null) {
            share.dismiss();
        } else
            this.finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        ZogUtils.printError(ThreadDetailActivity.class, "onPause onPause");
        LoadingDialogFragment.getInstance(this).dismissAllowingStateLoss();
    }
}
