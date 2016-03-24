package com.kit.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.kit.app.ActivityManager;
import com.kit.app.UIHandler;
import com.kit.app.resouce.DrawableId;
import com.kit.utils.ActionBarUtils;
import com.kit.utils.ResourceUtils;

public abstract class BaseActivity extends AppCompatActivity  implements BaseV4Fragment.OnFragmentInteractionListener{

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        init();
    }

    public void initTheme() {
        ActionBarUtils.setHomeActionBar(this, ResourceUtils.getDrawableId(this, DrawableId.ic_back));
    }

    public void init() {

        this.getExtra();
        this.initWidget();
        this.loadData();
        this.initWidgetWithData();

        UIHandler.prepare();
        ActivityManager.getInstance().pushActivity(this);
    }

    protected boolean getExtra() {
        return true;
    }

    protected boolean initWidget() {
        return true;
    }


    protected boolean loadData() {
        return true;
    }

    protected boolean initWidgetWithData() {
        return true;
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


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        mContext = this;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityManager.getInstance().popActivity(this);
    }


}

