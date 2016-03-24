package com.youzu.clan.thread.deal.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

import com.kit.utils.FragmentUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseActivity;


/**
 *  参加活动
 */
@ContentView(R.layout.activity_fragment_replace)
public class ActApplyActivityCopy extends BaseActivity implements Handler.Callback {


    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;

        switch (what) {

        }
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActApplyFragment actApplyFragment = new ActApplyFragment();
        FragmentUtils.replace(getSupportFragmentManager(),R.id.repalce,actApplyFragment);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_commit:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
