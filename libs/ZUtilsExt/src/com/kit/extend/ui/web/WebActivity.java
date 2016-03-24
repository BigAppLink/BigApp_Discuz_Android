package com.kit.extend.ui.web;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.kit.app.core.task.DoSomeThing;
import com.kit.extend.R;
import com.kit.ui.BaseActivity;
import com.kit.utils.ActionBarUtils;
import com.kit.utils.AppUtils;
import com.kit.utils.ResourceUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;

import org.apache.http.cookie.Cookie;

import java.util.List;

public class WebActivity extends BaseActivity implements WebFragment.IInitWeb {

    public static final int WEB_INITOK_START_LOAD = 999 + 0;
    public static final int WEB_LOAD_SUCCESS = 999 + 1;
    public static final int WEB_LOAD_FAIL = 999 + 2;
    public String title = "";
    public String content = "";
    public String contentViewName = "";
    public List<Cookie> cookies;

    public WebFragment webFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);

        setCookieFromCookieStore();

    }

    @Override
    public boolean getExtra() {
        super.getExtra();
        BundleData bundleData = IntentUtils.getData(getIntent());
        try {
            content = bundleData.getObject("content", String.class);
            title = bundleData.getObject("title", String.class);
            contentViewName = bundleData.getObject("contentViewName", String.class);
        } catch (Exception e) {

        }

        if (StringUtils.isEmptyOrNullOrNullStr(contentViewName)) {
            contentViewName = "activity_web";
        }


        return true;
    }

    @Override
    public boolean initWidget() {
        setContentView(ResourceUtils.getResId(getApplication(), contentViewName, "layout"));
        ActionBarUtils.setHomeActionBar(this, R.drawable.ic_back);


        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        webFragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        webFragment.setArguments(bundle);
        fragmentTransaction.add(ResourceUtils.getViewId(this, "container"), webFragment, "web");
        fragmentTransaction.commit();


        webFragment.setInitWeb(this);

//        setCookieFromCookieStore(this, content);

        return super.initWidget();
    }


    @Override
    public boolean initWidgetWithData() {
        if (!StringUtils.isEmptyOrNullOrNullStr(title))
            setTitle(title);
        return super.initWidgetWithData();
    }


    /**
     * 初始化webview
     */
    public void initWebView() {

    }

    /**
     * webview初始化完成，可以开始加载页面
     */
    public void loadWeb() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        MenuItem item = menu.findItem(R.id.action_more);
        MoreActionWebProvider provider = (MoreActionWebProvider) MenuItemCompat.getActionProvider(item);
        provider.setActivity(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        ActionBarUtils.setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history

        if ((keyCode == KeyEvent.KEYCODE_BACK) && webFragment.getWebView().canGoBack()) {
            if (webFragment.getWebView().canGoBack()) {
                // 返回键退回
                webFragment.getWebView().goBack();
            } else {
                this.finish();
            }
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param context
     * @param clazz   要跳转到的Activity，如果没有子类继承本WebActivity类，可传本类
     * @param title
     * @param content
     */
    public static void gotoWeb(Context context, Class clazz, String title, String content) {
        ZogUtils.printError(WebActivity.class, "content:" + content);

        if (context instanceof WebActivity) {
            ((WebActivity) context).webFragment.getWebView().loadUrl(content);
        } else {
            BundleData bundleData = new BundleData();
            bundleData.put("title", title);
            bundleData.put("content", content);
//        bundleData.put("cookie", cookie);
            IntentUtils.gotoNextActivity(context, clazz, bundleData);
        }
    }


    public void setCookieFromCookieStore() {
        webFragment.setCookieFromCookieStore(this, content, cookies);
    }
}
