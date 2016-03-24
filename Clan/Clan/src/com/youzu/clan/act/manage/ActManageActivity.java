package com.youzu.clan.act.manage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kit.utils.ActionBarUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.clan.R;
import com.youzu.clan.base.ZBActivity;
import com.youzu.clan.base.json.act.SpecialActivity;
import com.youzu.clan.thread.detail.ThreadDetailActivity;


@ContentView(R.layout.activity_act_manage)
public class ActManageActivity extends ZBActivity {

    /***
     * 1参加活动，2管理活动
     */
    private int fragment_type = 0;
    private FragmentActApply mFragmentActApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean initWidget() {
        super.initWidget();
        ActionBarUtils.setHomeActionBar(this, R.drawable.ic_back);
        setTitle(R.string.z_act_manage_title);
        Intent data = getIntent();
        if (data == null) {
            return true;
        }
        fragment_type = data.getIntExtra("type", 0);
        if (fragment_type == 1) {//参加活动
            setTitle(R.string.z_act_apply_title);
            if (mMenuItem != null) {
                mMenuItem.setVisible(true);
            }
            mFragmentActApply = FragmentActApply.newInstance(data.getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_act_manage, mFragmentActApply, FragmentActApply.class.getSimpleName()).commit();
        } else if (fragment_type == 2) {//管理活动
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_act_manage, FragmentActManage.newInstance(data.getExtras()), FragmentActManage.class.getSimpleName()).commit();
        }
        return true;
    }

    @Override
    protected boolean initWidgetWithData() {
        return super.initWidgetWithData();
    }

    MenuItem mMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_thread_reply, menu);
        mMenuItem = menu.findItem(R.id.action_send);
        mMenuItem.setTitle(R.string.z_act_top_btn_commit);
        if (fragment_type == 1) {
            mMenuItem.setVisible(true);
        } else {
            mMenuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_send:
                if (mFragmentActApply != null) {
                    mFragmentActApply.onCommit();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFragmentActApply != null) {
            mFragmentActApply.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static void gotoActManage(Context context, String fid, String tid, String pid) {
        Intent intent = new Intent(context, ActManageActivity.class);
        intent.putExtra("fid", fid);
        intent.putExtra("type", 2);
        intent.putExtra("tid", tid);
        intent.putExtra("pid", pid);
        context.startActivity(intent);
    }

    public static void gotoActApply(Activity context, SpecialActivity specialAct, String pid, String fid) {
        if (specialAct == null) {
            return;
        }
        Intent intent = new Intent(context, ActManageActivity.class);
        intent.putExtra("SpecialActivity", specialAct);
        intent.putExtra("fid", fid);
        intent.putExtra("type", 1);
        intent.putExtra("tid", specialAct.getTid());
        intent.putExtra("pid", pid);
        context.startActivityForResult(intent, ThreadDetailActivity.REQUEST_CODE);
    }

}
