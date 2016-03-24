package com.youzu.clan.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.app.enums.AppStatus;
import com.kit.utils.DeviceUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.config.AppConfig;
import com.youzu.clan.base.util.theme.ThemeUtils;

import java.lang.reflect.Field;

import cn.sharesdk.analysis.MobclickAgent;

public class BaseActivity extends com.kit.ui.BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initTheme() {
        super.initTheme();
        ThemeUtils.initTheme(this);
    }

    public void init() {
        ViewUtils.inject(this);
        super.init();
    }


    protected boolean getExtra() {
        return true;
    }

    protected boolean initWidget() {
        setOverflowShowingAlways();
        return true;
    }


    @Override
    protected void onStart() {
//        try {
            super.onStart();
//        } catch (Exception e) {
//            ZogUtils.showException(e);
//            AppUtils.restartApp(this);
//     //       IntentUtils.gotoSingleNextActivity(this, GuideActivity.class, true);
//        }

    }

    protected boolean loadData() {
        return true;
    }

    protected boolean initWidgetWithData() {
        return true;
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.back)
    public void back(View view) {
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.isShowing = AppStatus.SHOWING;
        //统计时长
        MobclickAgent.onResume(this);

//        if ((DateUtils.getCurrDateLong() - AppSPUtils.getExitTime(this)) >  60 * 1000) {
//            IntentUtils.gotoSingleNextActivity(this, GuideActivity.class, true);
//        }

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        AppConfig.isShowing = AppStatus.BACKGROUND;
    }


    private void addStatus() {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DeviceUtils.getStatusBarHeight(this));
        textView.setBackgroundColor(Color.parseColor("#3F9FE0"));
        textView.setLayoutParams(lParams);
        // 获得根视图并把TextView加进去。
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.addView(textView);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        AppSPUtils.saveExitTime(this, DateUtils.getCurrDateLong());
    }


    @Override
    protected void onRestoreInstanceState(Bundle state) {
        ZogUtils.printError(BaseActivity.class, "onRestoreInstanceState: state = " + state);
        try {
            super.onRestoreInstanceState(state);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ZogUtils.printError(BaseActivity.class, "onSaveInstanceState");
    }

}
