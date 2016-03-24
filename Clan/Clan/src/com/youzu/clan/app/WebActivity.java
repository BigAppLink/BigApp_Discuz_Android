package com.youzu.clan.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;

import com.kit.app.ActivityManager;
import com.kit.app.enums.AppStatus;
import com.kit.extend.ui.web.client.DefaultWebViewClient;
import com.kit.extend.ui.web.model.WebviewGoToTop;
import com.kit.extend.ui.web.webview.LoadMoreWebView;
import com.kit.utils.ActionBarUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.config.AppConfig;
import com.youzu.clan.base.IDClan;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.CookieUtils;
import com.youzu.clan.base.util.jump.JumpWebUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;

import java.lang.reflect.Field;

import cn.sharesdk.analysis.MobclickAgent;

public class WebActivity extends com.kit.extend.ui.web.WebActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initTheme();
        ActionBarUtils.setHomeActionBar(this, R.drawable.ic_back);
    }


    public void initTheme() {
        ThemeUtils.initTheme(this);
    }


    @Override
    public boolean getExtra() {
        super.getExtra();
        contentViewName = "activity_web";

        return true;
    }

    public boolean initWidget() {
        setOverflowShowingAlways();


        return super.initWidget();
    }

    public boolean loadData() {
        return super.loadData();
    }

    public boolean initWidgetWithData() {
        return super.initWidgetWithData();
    }


    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setTitle(int resId) {
        getSupportActionBar().setTitle(resId);
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ThemeUtils.initResource(this);

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @OnClick(R.id.back)
    public void back(View view) {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().popActivity(this);
//        if(LoadingDialogFragment.getInstance(this)!=null){
//            LoadingDialogFragment.getInstance(this).dismissAllowingStateLoss();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.isShowing = AppStatus.SHOWING;

        //统计时长
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        AppConfig.isShowing = AppStatus.BACKGROUND;

        MobclickAgent.onPause(this);
    }

    @Override
    public void initWebView() {
        super.initWebView();

        webFragment.setGoToTopPosition(getGoToTopPosition());

        webFragment.setWebScrollListener(new LoadMoreWebView.WebScrollListener() {

                                             @Override
                                             public void onScroll(int dx, int dy) {
                                             }

                                             @Override
                                             public void onTop() {
                                             }

                                             @Override
                                             public void onCenter() {
                                             }

                                             @Override
                                             public void onBottom() {
                                                 ZogUtils.printLog(WebActivity.class, "bottom load more");
                                                 if (!webFragment.isRefreshing())
                                                     getData(false);
                                             }

                                         }
        );



        webFragment.getWebView().setWebViewClient(new DefaultWebViewClient(webFragment) {

            @Override
            public void loadingUrl(WebView view, String url) {
                JumpWebUtils.gotoWeb(WebActivity.this, "", url);
            }
        });


    }

    private void getData(final boolean isJumpPage) {

    }

    @Override
    public void setCookieFromCookieStore() {
        cookies = CookieUtils.getCookies(this);
        webFragment.setCookieFromCookieStore(this, content, cookies);
    }


    private int getGoToTopPosition() {
        if (AppSPUtils.getShowGoToTop(WebActivity.this) == IDClan.ID_RADIOBUTTON_SHOW_GO_TO_TOP_RIGHT) {
            return WebviewGoToTop.RIGHT;
        } else if (AppSPUtils.getShowGoToTop(WebActivity.this) == IDClan.ID_RADIOBUTTON_SHOW_GO_TO_TOP_LEFT) {
            return WebviewGoToTop.LEFT;
        } else
            return WebviewGoToTop.NONE;
    }
}