package com.youzu.clan.article;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.kit.extend.ui.web.client.DefaultWebViewClient;
import com.kit.share.SharePopupWindow;
import com.kit.utils.ActionBarUtils;
import com.kit.utils.ArrayUtils;
import com.kit.utils.MessageUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.WebActivity;
import com.youzu.clan.base.callback.FavThreadCallback;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.callback.ProgressCallback;
import com.youzu.clan.base.common.ErrorCode;
import com.youzu.clan.base.enums.MessageVal;
import com.youzu.clan.base.json.FavThreadJson;
import com.youzu.clan.base.json.article.Article;
import com.youzu.clan.base.json.article.ArticleAddFavJson;
import com.youzu.clan.base.json.article.ArticleDetailJson;
import com.youzu.clan.base.json.article.ArticleFav;
import com.youzu.clan.base.net.ArticleHttp;
import com.youzu.clan.base.net.ThreadHttp;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.ShareUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.jump.JumpWebUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;
import com.youzu.clan.myfav.MyFavUtils;
import com.youzu.clan.share.SharePlatformActionListener;
import com.youzu.clan.thread.detail.MoreActionProvider;
import com.youzu.clan.thread.detail.MoreActionProviderCallback;

import cn.sharesdk.framework.ShareSDK;


/**
 * Created by tangh on 2015/8/28.
 */
public class ArticleDetailActivity extends WebActivity implements Handler.Callback, MoreActionProviderCallback {


    private SharePopupWindow share;


    private String aid;
    private ArticleDetailJson articleJson;
    private ArticleDetailJavascriptInterface javascriptInterface;

    public boolean isFavThread = false;
    private MenuItem ivFav;


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WEB_INITOK_START_LOAD:
                    webFragment.setRefreshing(true);
                    refreshData();
                    break;
                case WEB_LOAD_FAIL:
                case WEB_LOAD_SUCCESS:
                    webFragment.setRefreshing(false);
                    break;

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this, true);
    }


    @Override
    public boolean initWidget() {

        super.initWidget();
        ViewUtils.inject(this);


        return true;
    }

    @Override
    public void loadWeb() {
        super.loadWeb();
        MessageUtils.sendMessage(handler, WEB_INITOK_START_LOAD);

    }

    @Override
    public boolean getExtra() {
        Bundle bundle = getIntent().getExtras();
        aid = bundle.getString("aid");
        contentViewName = "activity_article_detail";

        return true;
    }

    @Override
    public void initWebView() {
        super.initWebView();
        webFragment.setUseWebTitle(false);

        webFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ZogUtils.printLog(ArticleDetailActivity.class, "top refresh");

                refreshData();
            }
        });


        webFragment.getWebView().setBackgroundColor(getResources().getColor(R.color.background));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            webFragment.getWebView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        webFragment.getWebView().getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        webFragment.getWebView().setWebViewClient(new DefaultWebViewClient(webFragment) {

            @Override
            public void onPageFinished(WebView view, String url) {
                //开始
                super.onPageFinished(view, url);

            }


            @Override
            public void loadingUrl(WebView view, String url) {
                JumpWebUtils.gotoWeb(ArticleDetailActivity.this, "", url);

            }
        });
        javascriptInterface = new ArticleDetailJavascriptInterface(this, handler);

    }


    private void refreshData() {
        getData(false);
    }

    private void getData(final boolean isJumpPage) {
        webFragment.setRefreshing(true);
        ArticleHttp.getArticleDetail(this, aid, new JSONCallback() {

            @Override
            public void onSuccess(Context ctx, String str) {
                super.onSuccess(ctx, str);
                MessageUtils.sendMessage(handler, WEB_LOAD_SUCCESS);
                try {
                    articleJson = ClanUtils.parseObject(str, ArticleDetailJson.class);
                } catch (Exception e) {
                    ZogUtils.showException(e);
                }
                if (articleJson == null || articleJson.getVariables() == null) {
                    onFailed(ArticleDetailActivity.this, ErrorCode.ERROR_DEFAULT, getString(R.string.load_failed));
                    return;
                }
                if (articleJson.getMessage() != null) {
                    onFailed(ArticleDetailActivity.this, ErrorCode.ERROR_DEFAULT, articleJson.getMessage().getMessagestr());
                    return;
                }
                refreshFavStatus(null);
                setWeb();
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(ArticleDetailActivity.this, errorCode, msg);
                MessageUtils.sendMessage(handler, WEB_LOAD_FAIL);
            }
        });
    }

    /**
     * 获取数据之后，初始化网页
     */
    public void setWeb() {
        ZogUtils.printError(ArticleDetailActivity.class, "hotThreadDetailJson.getVariables():" + articleJson.getVariables());
        webFragment.getWebView().getSettings().setJavaScriptEnabled(true);
        javascriptInterface.setArticleJson(articleJson);
        webFragment.getWebView().addJavascriptInterface(javascriptInterface, "android");
        webFragment.getWebView().loadUrl("file:///android_asset/www/portal_detail.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_thread_detail, menu);

        MenuItem author = menu.findItem(R.id.action_author);
        author.setVisible(false);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ThemeUtils.initResource(this);

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

//            case R.id.action_fav:
//                if (isFavThread) {
//                    delFav();
//                } else {
//                    addFav();
//                }
//                return true;
//            case R.id.action_share:
//                doShare();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }


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
        }
        if (share != null) {
            share.dismiss();
        }
        return false;
    }


    @Override
    public boolean isDoNext(String doWhat) {
        return true;
    }

    /**
     * 分享
     */
    @Override
    public void doShare() {
        if (articleJson == null || articleJson.getVariables() == null || articleJson.getVariables().getData() == null)
            return;

        Article article = articleJson.getVariables().getData();

        String title = article.getTitle();
        String text = article.getSummary();
        String titleUrl = article.getShareUrl();


        if (StringUtils.isEmptyOrNullOrNullStr(title)) {
            title = getString(R.string.share);
        }

        if (StringUtils.isEmptyOrNullOrNullStr(text)) {
            text = getString(R.string.share);
        }
        String imageUrl;
        String[] images = javascriptInterface.getImages();
        if (!ArrayUtils.isNullOrContainEmpty(images)) {
            imageUrl = images[0];
        } else {
            imageUrl = getString(R.string.icon_net_url);
        }
        SharePlatformActionListener sharePlatformActionListener = new SharePlatformActionListener(this, this);

        share = ShareUtils.showShare(this, findViewById(R.id.main), title, text, imageUrl, titleUrl, sharePlatformActionListener);
    }

    @Override
    public void addFav() {
        if (ClanUtils.isToLogin(this, null, Activity.RESULT_OK, false)) {
            return;
        }

        ArticleHttp.addArticleFavs(this, aid, new ProgressCallback<ArticleAddFavJson>(this) {

            @Override
            public void onSuccess(Context ctx, ArticleAddFavJson articleFavJson) {
                super.onSuccess(ctx, articleFavJson);
                String messageval = null;
                try {
                    messageval = articleFavJson.getMessage().getMessageval();
                } catch (Exception e) {
                    ZogUtils.showException(e);
                }

                if (StringUtils.isEmptyOrNullOrNullStr(messageval)) {
                    ToastUtils.mkShortTimeToast(ArticleDetailActivity.this, getString(R.string.fav_fail));
                    return;
                }

                if (messageval.equals(MessageVal.FAVORITE_DO_SUCCESS)) {
                    refreshFavStatus(articleFavJson.getVariables().getFavid());
                } else if (messageval.equals(MessageVal.TO_LOGIN)) {
                    ClanUtils.gotoLogin(ArticleDetailActivity.this, null, Activity.RESULT_OK, false);
                    ivFav.setIcon(R.drawable.ic_fav_black);

                } else if (messageval.equals(MessageVal.FAVORITE_REPEAT)) {
                    isFavThread = true;
                    invalidateOptionsMenu();
                    ClanUtils.loadMyFav(ArticleDetailActivity.this);
                    ToastUtils.mkShortTimeToast(ArticleDetailActivity.this, articleFavJson.getMessage().getMessagestr());

                } else {
                    ToastUtils.mkShortTimeToast(ArticleDetailActivity.this, articleFavJson.getMessage().getMessagestr());
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(ArticleDetailActivity.this, errorCode, msg);
                ToastUtils.show(getApplicationContext(), msg);
            }
        });
    }

    @Override
    public void delFav() {
        if (ClanUtils.isToLogin(this, null, Activity.RESULT_OK, false)) {
            return;
        }

        final String favid = getFavid();
        if (StringUtils.isEmptyOrNullOrNullStr(favid)) {
            return;
        }
        String[] ids = {favid};
        //帖子的取消收藏和文章的取消收藏是一个接口；
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
                    ToastUtils.mkShortTimeToast(ArticleDetailActivity.this, getString(R.string.fav_fail));
                    return;
                }
                if (messageval.equals(MessageVal.FAVORITE_DELETE_SUCCEED)) {
                    MyFavUtils.deleteArticle(ArticleDetailActivity.this, articleJson.getVariables().getData().getAid());
                    refreshFavStatus(null);
                    ToastUtils.mkShortTimeToast(ArticleDetailActivity.this, getString(R.string.fav_del_success));
                } else if (messageval.equals(MessageVal.TO_LOGIN)) {
                    ClanUtils.gotoLogin(ArticleDetailActivity.this, null, Activity.RESULT_OK, false);
                } else {
                    ToastUtils.mkShortTimeToast(ArticleDetailActivity.this, favThreadJson.getMessage().getMessagestr());
                }
            }


            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(ArticleDetailActivity.this, errorCode, msg);
                ToastUtils.show(getApplicationContext(), msg);
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
        return false;
    }

    @Override
    public boolean isHaveReport() {
        return false;
    }

    @Override
    public boolean isHaveDelete() {
        return false;
    }
    @Override
    public boolean isHaveJumpPost() {
        return false;
    }

    @Override
    public void doJumpPost() {

    }



    @Override
    public void delete() {

    }

    @Override
    public boolean isFav() {
        return isFavThread;
    }


    @Override
    public String getShareUrl() {
        if (articleJson != null && articleJson.getVariables() != null && articleJson.getVariables().getData() != null) {
            Article article = articleJson.getVariables().getData();
            return article.getShareUrl();
        }
        return null;
    }

    @Override
    public void doJumpPage() {

    }

    @Override
    public void report() {
    }

    private String getFavid() {
        String favid = null;
        if (articleJson != null && articleJson.getVariables() != null) {
            Article article = articleJson.getVariables().getData();
            if (article == null)
                return null;

            ArticleFav favArticle = MyFavUtils.getFavArticleById(this, article.getAid());

            if (favArticle != null) {
                favid = favArticle.getFavid();
                ZogUtils.printLog(ArticleDetailActivity.class, "favid" + favid);
            }
        }
        return favid;
    }

    /**
     * 刷新主题的收藏状态
     *
     * @param favid 如果传值为null，为单纯的刷新
     *              如果传值有值，则为保存主题到本地收藏缓存
     */
    private void refreshFavStatus(String favid) {
        if (favid == null) {
            favid = getFavid();
        } else {
            articleJson.getVariables().getData().setFavid(favid);
            MyFavUtils.saveOrUpdateArticle(ArticleDetailActivity.this, articleJson.getVariables().getData());
            ToastUtils.mkShortTimeToast(ArticleDetailActivity.this, getString(R.string.fav_success));

        }
        if (!StringUtils.isEmptyOrNullOrNullStr(favid)) {
            isFavThread = true;
        } else {
            isFavThread = false;
        }
        ZogUtils.printError(ArticleDetailActivity.class, "favid=" + favid);
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ZogUtils.printError(ArticleDetailActivity.class, "onBackPressed onBackPressed");
        if (share != null) {
            share.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ZogUtils.printError(ArticleDetailActivity.class, "onPause onPause");
        LoadingDialogFragment.getInstance(this).dismissAllowingStateLoss();

    }


}
