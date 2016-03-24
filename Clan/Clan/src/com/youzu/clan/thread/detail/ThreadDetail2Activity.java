//package com.youzu.clan.thread;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.view.MenuItemCompat;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.webkit.WebView;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.kit.extend.ui.web.client.DefaultWebViewClient;
//import com.kit.extend.ui.web.webview.LoadMoreWebView;
//import com.kit.share.SharePopupWindow;
//import com.kit.utils.ActionBarUtils;
//import com.kit.utils.ArrayUtils;
//import com.kit.utils.HtmlUtils;
//import com.kit.utils.KeyboardUtils;
//import com.kit.utils.MessageUtils;
//import com.kit.utils.ZogUtils;
//import com.kit.utils.intentutils.BundleData;
//import com.kit.utils.intentutils.IntentUtils;
//import com.youzu.android.framework.ViewUtils;
//import com.youzu.android.framework.view.annotation.ViewInject;
//import com.youzu.android.framework.view.annotation.event.OnClick;
//import com.youzu.clan.R;
//import com.youzu.clan.app.WebActivity;
//import com.youzu.clan.app.constant.Key;
//import com.youzu.clan.base.callback.FavThreadCallback;
//import com.youzu.clan.base.callback.JSONCallback;
//import com.youzu.clan.base.common.ErrorCode;
//import com.youzu.clan.base.enums.MessageVal;
//import com.youzu.clan.base.json.FavThreadJson;
//import com.youzu.clan.base.json.HotThreadDetailJson;
//import com.youzu.clan.base.json.hotthread.HotThreadDetailVariables;
//import com.youzu.clan.base.json.hotthread.Report;
//import com.youzu.clan.base.json.viewthread.Post;
//import com.youzu.clan.base.net.DoCheckPost;
//import com.youzu.clan.base.net.ThreadHttp;
//import com.youzu.clan.base.util.AppUSPUtils;
//import com.youzu.clan.base.util.ClanUtils;
//import com.youzu.clan.base.util.ShareUtils;
//import com.youzu.clan.base.util.StringUtils;
//import com.youzu.clan.base.util.ToastUtils;
//import com.youzu.clan.base.util.jump.JumpWebUtils;
//import com.youzu.clan.base.util.theme.ThemeUtils;
//import com.youzu.clan.base.widget.LoadingDialogFragment;
//import com.youzu.clan.myfav.MyFavUtils;
//import com.youzu.clan.share.SharePlatformActionListener;
//
//import java.util.ArrayList;
//
//import cn.sharesdk.framework.ShareSDK;
//
//
//public class ThreadDetail2Activity extends WebActivity implements View.OnClickListener, Handler.Callback, MoreActionProviderCallback {
//
//    public static final int REQUEST_CODE = 0x111;
//
//    @ViewInject(R.id.toolbar)
//    private Toolbar toolbar;
//
//    @ViewInject(R.id.tvPage)
//    private TextView tvPage;
//
//    @ViewInject(R.id.tvPage2)
//    public TextView tvPage2;
//
//    @ViewInject(R.id.rlJumpPage)
//    public View rlJumpPage;
//
//    @ViewInject(R.id.llReply)
//    public View llReply;
//
//    @ViewInject(R.id.ivFav)
//    public ImageView ivFav;
//
//    @ViewInject(R.id.tvFav)
//    public TextView tvFav;
//
//    @ViewInject(R.id.et)
//    public EditText et;
//
//    @ViewInject(R.id.btnCancel)
//    public ImageButton btnCancel;
//
//    @ViewInject(R.id.btnOk)
//    public ImageButton btnOk;
//
//    public LoadMoreWebView webView2;
//
//    private SharePopupWindow share;
//
//    private HotThreadDetailJson hotThreadDetailJson;
//
//    public int page = 0;
//    private int totalPage = 1;
//    private String tid;
//    //    private String favid;
//    private String authorid;
//    private ThreadDetailJavascriptInterface javascriptInterface;
////    private LoadingDialogFragment loadingDialogFragment;
//
//    private boolean isLookAuthor = false;
//    public boolean isFavThread = false;
//
//    public boolean isJumpPage;
//
//
//    public Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            ZogUtils.printError(ThreadDetail2Activity.class, "handler" + handler);
//
//            switch (msg.what) {
//                case WEB_INITOK_START_LOAD:
//                    ZogUtils.printError(ThreadDetail2Activity.class, "webFragment" + webFragment + " webFragment.refreshLayout:" + webFragment.refreshLayout);
//                    webFragment.refreshLayout.setRefreshing(true);
//                    refreshData();
//                    break;
//                case WEB_LOAD_FAIL:
//                case WEB_LOAD_SUCCESS:
//                    webFragment.refreshLayout.setRefreshing(false);
//                    break;
//
//            }
//        }
//    };
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ShareSDK.initSDK(this, true);
//    }
//
//    @Override
//    public boolean initWidget() {
//        super.initWidget();
//        ViewUtils.inject(this);
//
//        return true;
//    }
//
//    @Override
//    public void loadWeb() {
//        super.loadWeb();
//        MessageUtils.sendMessage(handler, WEB_INITOK_START_LOAD);
//    }
//
//    @Override
//    public boolean getExtra() {
//        super.getExtra();
//        Bundle bundle = getIntent().getExtras();
//        tid = bundle.getString("tid");
//        contentViewName = "activity_thread_detail";
//
////        tid = "25";
//        return true;
//    }
//
//    @Override
//    public void initWebView() {
//        super.initWebView();
//
//        webFragment.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                ZogUtils.printLog(ThreadDetail2Activity.class, "top refresh");
//
//                refreshData();
//            }
//        });
//
//        webView2 = webFragment.webView;
//
//
//        webView2.setBackgroundColor(
//
//                getResources()
//
//                        .
//
//                                getColor(R.color.background)
//
//        );
//
//        webView2.setWebViewClient(new
//
//                                          DefaultWebViewClient(webFragment.webView,webFragment) {
//
//                                              @Override
//                                              public void onPageFinished(WebView view, String url) {
//                                                  //开始
//                                                  super.onPageFinished(view, url);
//                                                  if (isJumpPage) {
//                                                      webView2.loadUrl("javascript:jump()");
//                                                  }
////*********************************输出渲染完成后的html页面
////                webView2.loadUrl("javascript:window.android.showSource(document.body.innerHTML);");
//
//                                              }
//
//                                              @Override
//                                              public void loadingUrl(WebView view, String url) {
//                                                  JumpWebUtils.gotoWeb(ThreadDetail2Activity.this, "", url);
//                                              }
//
//
//                                          }
//
//        );
//        javascriptInterface = new
//
//                ThreadDetailJavascriptInterface(this, handler);
//
//
//    }
//
//    private void refreshData() {
//        page = 0;
//        getThreadDetailData(false);
//    }
//
//    private void getThreadDetailData(final boolean isJumpPage) {
//        webFragment.refreshLayout.setRefreshing(true);
//
//        this.isJumpPage = isJumpPage;
//        if (page < totalPage) {
//            page++;
//
//            ThreadHttp.getThreadDetail(this, tid, authorid, page + "", new JSONCallback() {
//
//                @Override
//                public void onstart(Context cxt) {
//                    super.onstart(cxt);
//
//                }
//
//                @Override
//                public void onSuccess(Context ctx, String str) {
//                    super.onSuccess(ctx, str);
//                    MessageUtils.sendMessage(handler, WEB_LOAD_SUCCESS);
//
//
//                    if (!StringUtils.isEmptyOrNullOrNullStr(str)) {
//                        String resize = AppUSPUtils.getPicSizeStr(ThreadDetail2Activity.this);
//                        str = ClanUtils.resizePicSize(ThreadDetail2Activity.this, str, resize);
//                    }
//
//                    ZogUtils.printError(ThreadDetail2Activity.class, "hotThreadDetailJson:::" + str);
//                    try {
//                        hotThreadDetailJson = ClanUtils.parseObject(str, HotThreadDetailJson.class);
//                    } catch (Exception e) {
//                        ZogUtils.showException(e);
//                    }
//                    invalidateOptionsMenu();
//                    if (hotThreadDetailJson != null && hotThreadDetailJson.getMessage() != null) {
//                        onFailed(ThreadDetail2Activity.this, ErrorCode.ERROR_DEFAULT, hotThreadDetailJson.getMessage().getMessagestr());
//                        return;
//                    }
//
//                    if (hotThreadDetailJson == null
//                            || hotThreadDetailJson.getVariables() == null
//                            || hotThreadDetailJson.getVariables().getTotalpage() == null) {
//                        onFailed(ThreadDetail2Activity.this, ErrorCode.ERROR_DEFAULT, getString(R.string.load_failed));
//                        return;
//                    }
//
//                    if (page == 1) {
//                        refreshFavStatus(null);
//                    }
//
//                    setWeb(page, isJumpPage);
//                }
//
//                @Override
//                public void onFailed(Context cxt, int errorCode, String msg) {
//                    super.onFailed(ThreadDetail2Activity.this, errorCode, msg);
//                    MessageUtils.sendMessage(handler, WEB_LOAD_FAIL);
////                    ToastUtils.show(getApplicationContext(), msg);
//                    page--;
//                }
//            });
//        } else {
//            webFragment.refreshLayout.setLoading(false);
//        }
//    }
//
//
//    private String getFavid() {
//        String favid = null;
//        if (hotThreadDetailJson != null && hotThreadDetailJson.getVariables() != null) {
//            com.youzu.clan.base.json.viewthread.Thread thread = hotThreadDetailJson.getVariables().getThread();
//            if (thread == null)
//                return null;
//
//            com.youzu.clan.base.json.favthread.Thread favThread = MyFavUtils.getFavThreadById(this, thread.getTid());
//
//            if (favThread != null) {
//                favid = favThread.getFavid();
//                ZogUtils.printLog(ThreadDetail2Activity.class, "favid" + favid);
//            }
//        }
//        return favid;
//    }
//
//
//    /**
//     * 刷新主题的收藏状态
//     *
//     * @param favid 如果传值为null，为单纯的刷新
//     *              如果传值有值，则为保存主题到本地收藏缓存
//     */
//    private void refreshFavStatus(String favid) {
//        if (favid == null) {
//            favid = getFavid();
//        } else {
//            MyFavUtils.saveOrUpdateThread(ThreadDetail2Activity.this
//                    , hotThreadDetailJson.getVariables().getThread()
//                    , favid);
//            ToastUtils.mkShortTimeToast(ThreadDetail2Activity.this, getString(R.string.fav_success));
//
//        }
//        if (!StringUtils.isEmptyOrNullOrNullStr(favid)) {
//            isFavThread = true;
//            ivFav.setImageResource(R.drawable.ic_fav_black_solid);
//            tvFav.setText(R.string.fav_alright);
//        } else {
//            isFavThread = false;
//            ivFav.setImageResource(R.drawable.ic_fav_black);
//            tvFav.setText(R.string.fav);
//        }
//
//
//        invalidateOptionsMenu();
//
//    }
//
//
//    /**
//     * 获取数据之后，初始化网页
//     */
//    public void setWeb(int page, boolean isJumpPage) {
//        ZogUtils.printError(ThreadDetail2Activity.class, "hotThreadDetailJson.getVariables():" + hotThreadDetailJson.getVariables());
//        ZogUtils.printError(ThreadDetail2Activity.class, "hotThreadDetailJson.getVariables().getTotalpage():" + hotThreadDetailJson.getVariables().getTotalpage());
//
//
//        totalPage = Integer.parseInt(hotThreadDetailJson.getVariables().getTotalpage());
//
//        String pageNumber = String.format(getResources().getString(R.string.page_number),
//                page + "/" + totalPage);
//        tvPage.setText(pageNumber);
//
//        tvPage2.setText(page + "/" + totalPage);
//
//        webView2.getSettings().setJavaScriptEnabled(true);
//
//        hotThreadDetailJson.getVariables().setJumpPage(isJumpPage);
//
//
//        if (page <= 1 || isJumpPage) {
//
//            hotThreadDetailJson.getVariables().setPage(page);
//
//            javascriptInterface.setData(hotThreadDetailJson);
//
//            webView2.addJavascriptInterface(javascriptInterface, "android");
//
//            webView2.loadUrl("file:///android_asset/www/bbs.html");
//
//        } else {
////            webView.reload();
//            HotThreadDetailJson oldHotThreadDetailJson = javascriptInterface.getThreadDetailData();
//            oldHotThreadDetailJson.getVariables().getPostlist().addAll(hotThreadDetailJson.getVariables().getPostlist());
//
//
//            oldHotThreadDetailJson.getVariables().setPage(page);
//
////            javascriptInterface.setData(oldHotThreadDetailJson);
//            hotThreadDetailJson.getVariables().setPage(page);
//            javascriptInterface.setData(hotThreadDetailJson);
//            webView2.addJavascriptInterface(javascriptInterface, "android");
//
//            ZogUtils.printLog(ThreadDetail2Activity.class, "page:" + page);
//
//            webView2.loadUrl("javascript:setCommentsNext('" + (hotThreadDetailJson.getVariables().getPage() - 1) + "')");
//
//        }
//    }
//
//    /**
//     * 只看楼主
//     */
//    private void lookAuthor() {
//        isLookAuthor = !isLookAuthor;
////        getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
//        invalidateOptionsMenu();
//        authorid = null;
//        if (isLookAuthor
//                && hotThreadDetailJson != null
//                && hotThreadDetailJson.getVariables() != null
//                && hotThreadDetailJson.getVariables().getThread() != null
//                && hotThreadDetailJson.getVariables().getThread().getAuthorid() != null) {
//            authorid = hotThreadDetailJson.getVariables().getThread().getAuthorid();
//        }
//        page = 0;
//        getThreadDetailData(false);
//
//    }
//
//    /**
//     * 跳页
//     *
//     * @param pg
//     */
//    private void jumpPage(int pg) {
//        KeyboardUtils.hiddenKeyboard(this, et);
//
//        ZogUtils.printLog(ThreadDetail2Activity.class, "pg:" + pg);
//
//        invalidateOptionsMenu();
//
//        rlJumpPage.setVisibility(View.GONE);
//
//        page = pg - 1;
//
//        //跳页清空历史数据，避免中间有楼层差
////        hotThreadDetailJson.getVariables().getPostlist().clear();
//        getThreadDetailData(true);
//
//    }
//
//
//    /**
//     * 分享
//     */
//    public void doShare() {
//        if (javascriptInterface.getRealData() == null || javascriptInterface.getRealData().getVariables() == null)
//            return;
//
//        HotThreadDetailVariables hotThreadDetailVariables = javascriptInterface.getRealData().getVariables();
//        com.youzu.clan.base.json.viewthread.Thread thread = hotThreadDetailVariables.getThread();
//
//        String title = thread.getSubject();
//        String text = thread.getMessage();
//        String titleUrl = thread.getShareUrl();
//
//        title = HtmlUtils.delHTMLTag(title);
//        text = HtmlUtils.delHTMLTag(text);
//
//        if (StringUtils.isEmptyOrNullOrNullStr(title)) {
//            title = getString(R.string.share);
//        }
//
//        if (StringUtils.isEmptyOrNullOrNullStr(text)) {
//            text = getString(R.string.share);
//        }
//
////        ZogUtils.printError(ThreadDetailActivity.class, "title:::" + title + "\ntext:::" + text);
//
////        ShareSDKUtils.share(this, title, text, "", titleUrl, "", titleUrl, null, getString(R.string.icon_net_url), getString(R.string.sina_weibo_at));
//        String imageUrl;
//        String[] images = javascriptInterface.getImages();
//
//        if (!ArrayUtils.isNullOrContainEmpty(images)) {
//            imageUrl = images[0];
////            imageUrl = (imageUrl.contains(Url.BASE) ? imageUrl : Url.REAL_BASE + imageUrl);
//        } else {
//            imageUrl = getString(R.string.icon_net_url);
//        }
//        SharePlatformActionListener sharePlatformActionListener = new SharePlatformActionListener(this, this);
//
//        share = ShareUtils.showShare(this, findViewById(R.id.main), title, text, imageUrl, titleUrl, sharePlatformActionListener);
//    }
//
//    public void addFav() {
//        if (ClanUtils.isToLogin(this, null, Activity.RESULT_OK, false)) {
//            return;
//        }
//
//
//        ThreadHttp.favThread(this, tid, new FavThreadCallback(this) {
//
//            @Override
//            public void onSuccess(Context ctx, FavThreadJson favThreadJson) {
//                super.onSuccess(ctx, favThreadJson);
//                String messageval = null;
//                try {
//                    messageval = favThreadJson.getMessage().getMessageval();
//                } catch (Exception e) {
//                }
//
//                if (StringUtils.isEmptyOrNullOrNullStr(messageval)) {
//                    ToastUtils.mkShortTimeToast(ThreadDetail2Activity.this, getString(R.string.fav_fail));
//
//                    return;
//                }
//                if (messageval.equals(MessageVal.FAVORITE_DO_SUCCESS)) {
//                    refreshFavStatus(favThreadJson.getVariables().getFavid());
//                } else if (messageval.equals(MessageVal.TO_LOGIN)) {
//                    ClanUtils.gotoLogin(ThreadDetail2Activity.this, null, Activity.RESULT_OK, false);
//                    ivFav.setImageResource(R.drawable.ic_fav_black);
//
////                    IntentUtils.gotoSingleNextActivity(ThreadDetailActivity.this, LoginActivity.class, false);
//                } else if (messageval.equals(MessageVal.FAVORITE_REPEAT)) {
//                    isFavThread = true;
//                    invalidateOptionsMenu();
//                    ClanUtils.loadMyFav(ThreadDetail2Activity.this);
//
//                    ToastUtils.mkShortTimeToast(ThreadDetail2Activity.this, favThreadJson.getMessage().getMessagestr());
//                } else {
//                    ToastUtils.mkShortTimeToast(ThreadDetail2Activity.this, favThreadJson.getMessage().getMessagestr());
//                }
//            }
//
//
//            @Override
//            public void onFailed(Context cxt, int errorCode, String msg) {
//                super.onFailed(ThreadDetail2Activity.this, errorCode, msg);
//                ToastUtils.show(getApplicationContext(), msg);
//                page--;
//            }
//        });
//    }
//
//    /**
//     * 举报
//     */
//    public void report() {
//        if (ClanUtils.isToLogin(this, null, Activity.RESULT_OK, false)) {
//            return;
//        }
//
//        if (hotThreadDetailJson == null || hotThreadDetailJson.getVariables() == null) {
//            ToastUtils.mkShortTimeToast(this, getResources().getString(R.string.wait_a_moment));
//            return;
//        }
//        Report report = hotThreadDetailJson.getVariables().getReport();
//        //兼容旧的版本
//        if (report == null || report.getContent() == null || report.getContent().size() == 0 || StringUtils.isEmptyOrNullOrNullStr(report.getContent().get(0))) {
//            //ToastUtils.mkShortTimeToast(this, getResources().getString(R.string.wait_a_moment));
//            ZogUtils.printError(ThreadDetail2Activity.class, "举报内容获取失败");
//            report = new Report();
//            ArrayList<String> content = new ArrayList<>();
//            String[] contents = getResources().getStringArray(R.array.defaultReport);
//            for (int i = 0; i < contents.length; i++) {
//                content.add(contents[i]);
//            }
//            report.setContent(content);
//            hotThreadDetailJson.getVariables().setReport(report);
//        }
//        BundleData b = new BundleData();
//        b.put(Key.KEY_REPORT, hotThreadDetailJson);
//        b.put(Key.KEY_REPORT_TYPE, true);
//        IntentUtils.gotoNextActivity(this, ThreadReportActivity.class, b);
//        // IntentUtils.gotoNextActivity(this,ThreadReportActivity.class);
//    }
//
//    public void delFav() {
//        if (ClanUtils.isToLogin(this, null, Activity.RESULT_OK, false)) {
//            return;
//        }
//
//        final String favid = getFavid();
//        if (StringUtils.isEmptyOrNullOrNullStr(favid)) {
//            return;
//        }
//        String[] ids = {favid};
//        ThreadHttp.delFavThread(this, ids, new FavThreadCallback(this) {
//
//            @Override
//            public void onSuccess(Context ctx, FavThreadJson favThreadJson) {
//                super.onSuccess(ctx, favThreadJson);
//                String messageval = null;
//                try {
//                    messageval = favThreadJson.getMessage().getMessageval();
//                } catch (Exception e) {
//                }
//
//                if (StringUtils.isEmptyOrNullOrNullStr(messageval)) {
//                    ToastUtils.mkShortTimeToast(ThreadDetail2Activity.this, getString(R.string.fav_del_fail));
//                    return;
//                }
//                if (messageval.equals(MessageVal.FAVORITE_DELETE_SUCCEED)) {
//                    isFavThread = false;
//                    invalidateOptionsMenu();
//                    MyFavUtils.deleteThread(ThreadDetail2Activity.this
//                            , hotThreadDetailJson.getVariables().getThread().getTid()
//                    );
//                    refreshFavStatus(null);
//                    ToastUtils.mkShortTimeToast(ThreadDetail2Activity.this, getString(R.string.fav_del_success));
//                } else if (messageval.equals(MessageVal.TO_LOGIN)) {
//                    ClanUtils.gotoLogin(ThreadDetail2Activity.this, null, Activity.RESULT_OK, false);
//
////                    IntentUtils.gotoSingleNextActivity(ThreadDetailActivity.this, LoginActivity.class, false);
//                } else {
//                    ToastUtils.mkShortTimeToast(ThreadDetail2Activity.this, favThreadJson.getMessage().getMessagestr());
//                }
//            }
//
//
//            @Override
//            public void onFailed(Context cxt, int errorCode, String msg) {
//                super.onFailed(ThreadDetail2Activity.this, errorCode, msg);
//                ToastUtils.show(getApplicationContext(), msg);
////                page--;
//            }
//        });
//    }
//
//    @Override
//    public void doJumpPage() {
//        ZogUtils.printLog(MoreActionProvider.class, "doJumpPage activity.rlJumpPage:" + this.rlJumpPage);
//        rlJumpPage.setVisibility(View.VISIBLE);
//        KeyboardUtils.showKeyboard(this, et);
//    }
//
//    @Override
//    public boolean isHaveShare() {
//        return getString(R.string.is_use_share).equals("1");
//    }
//
//    @Override
//    public boolean isHaveFav() {
//        return true;
//    }
//
//    @Override
//    public boolean isHaveJumpPage() {
//        return true;
//    }
//
//    @Override
//    public boolean isHaveReport() {
//        return true;
//    }
//
//    @Override
//    public boolean isFav() {
//        return isFavThread;
//    }
//
//    @Override
//    public String getShareUrl() {
//        if (hotThreadDetailJson != null && hotThreadDetailJson.getVariables() != null && hotThreadDetailJson.getVariables().getThread() != null) {
//            com.youzu.clan.base.json.viewthread.Thread thread = hotThreadDetailJson.getVariables().getThread();
//            return thread.getShareUrl();
//        }
//        return null;
//    }
//
//    /**
//     * 检查权限
//     *
//     * @param post 为null时，为回复楼主；为其他大于1的数字时候，为回复跟帖
//     */
//    public void checkPost(Post post) {
//        if (ClanUtils.isToLogin(this, null, Activity.RESULT_OK, false)) {
//            return;
//        }
//        hotThreadDetailJson = javascriptInterface.getRealData();
//
//        if (hotThreadDetailJson == null) {
//            ToastUtils.mkLongTimeToast(this, getString(R.string.wait_a_moment));
//            return;
//        }
////        if (post != null)
////            ZogUtils.printError(ThreadDetailActivity.class, JsonUtils.toJSON(post).toString());
//
//        DoCheckPost.checkPost(this, hotThreadDetailJson.getVariables().getFid(), null, hotThreadDetailJson, post);
//
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_thread_detail, menu);
//
//        MenuItem item = menu.findItem(R.id.action_more);
//        MoreActionProvider provider = (MoreActionProvider) MenuItemCompat.getActionProvider(item);
//        provider.setCallback(this, this);
//        return true;
//    }
//
//    @Override
//    public boolean onMenuOpened(int featureId, Menu menu) {
//        ActionBarUtils.setOverflowIconVisible(featureId, menu);
//        return super.onMenuOpened(featureId, menu);
//    }
//
//    /* Called whenever we call invalidateOptionsMenu() */
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        if (isLookAuthor) {
//            menu.findItem(R.id.action_author).setIcon(R.drawable.ic_long_all);
//        } else {
//            menu.findItem(R.id.action_author).setIcon(R.drawable.ic_author_white);
//        }
//
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        ThemeUtils.initResource(this);
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                this.finish();
//                return true;
//
//            case R.id.action_author:
//                lookAuthor();
//                return true;
//
////            case R.id.action_more:
////                // Get the ActionProvider for later usage
////                provider = (MoreActionProvider) menu.findItem(R.id.menu_share)
////                        .getActionProvider();
////                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @OnClick({R.id.btnOk, R.id.btnCancel, R.id.llReply, R.id.tvPage, R.id.llComment, R.id.llFav, R.id.llShare})
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnOk:
//                String etStr = et.getText().toString();
//                if (StringUtils.isEmptyOrNullOrNullStr(etStr)) {
//                    ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_page_number_1));
//
//                } else {
//                    //验证页码
//                    int pg = Integer.parseInt(etStr);
//
//                    if (pg < 1) {
//                        ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_page_number_2));
//                    } else if (pg > totalPage) {
//                        ToastUtils.mkShortTimeToast(this, getString(R.string.verify_error_page_number_3));
//                    } else {//通过验证
//                        jumpPage(pg);
//
//                    }
//                }
//                break;
//
//            case R.id.btnCancel:
//                rlJumpPage.setVisibility(View.GONE);
//                KeyboardUtils.hiddenKeyboard(this, et);
//                break;
//
//            case R.id.tvPage:
//                rlJumpPage.setVisibility(View.VISIBLE);
//                KeyboardUtils.showKeyboard(this, et);
//                break;
//
//            case R.id.llComment:
//            case R.id.llReply:
//                checkPost(null);
//                break;
//            case R.id.llFav:
//                if (isFavThread) {
//                    delFav();
//                } else {
//                    addFav();
//                }
//                break;
//
//            case R.id.llShare:
//                doShare();
//                break;
//
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //回复之后刷新评论
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
////            Intent intent = data;
////            List<ImageBean> images = (List<ImageBean>) intent
////                    .getSerializableExtra("images");
//
//            page = javascriptInterface.getPage();
//
//            ZogUtils.printLog(ThreadDetail2Activity.class, "page:" + page);
//            if ((page == 1 && page == totalPage) || (isJumpPage && page == totalPage)) {
//                refreshData();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
////        if (loadingDialogFragment != null) {
////        LoadingDialogFragment.getInstance(this).dismissAllowingStateLoss();
////        }
//        if (share != null) {
//            share.dismiss();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ShareSDK.stopSDK(this);
//    }
//
//
//    @Override
//    public boolean handleMessage(Message msg) {
//        int what = msg.what;
//        LoadingDialogFragment.getInstance(this).dismissAllowingStateLoss();
//
//        switch (what) {
//            case SharePlatformActionListener.ON_CANCEL:
//                break;
//            case SharePlatformActionListener.ON_ERROR:
//                Toast.makeText(this
//                        , (msg.obj != null ? msg.obj.toString() : getString(R.string.share_failed))
//                        , Toast.LENGTH_SHORT).show();
//                break;
//            case SharePlatformActionListener.ON_COMPLETE:
//                Toast.makeText(this
//                        , (msg.obj != null ? msg.obj.toString() : getString(R.string.share_completed))
//                        , Toast.LENGTH_SHORT).show();
//
//                break;
//        }
//        if (share != null) {
//            share.dismiss();
//        }
//        return false;
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        ZogUtils.printError(ThreadDetail2Activity.class, "onBackPressed onBackPressed");
//        if (share != null) {
//            share.dismiss();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        ZogUtils.printError(ThreadDetail2Activity.class, "onPause onPause");
//        LoadingDialogFragment.getInstance(this).dismissAllowingStateLoss();
//    }
//}
