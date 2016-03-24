package com.youzu.clan.act.publish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kit.utils.ActionBarUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.clan.R;
import com.youzu.clan.base.json.act.ActConfig;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.thread.ThreadPublishDialogFragment;


@ContentView(R.layout.activity_act_publish)
public class ActPublishActivity extends ActPublishBase implements View.OnClickListener {
    private ThreadPublishDialogFragment fragment;

    private View frame_act_publish_1, frame_act_publish_2;
    private View ll_step_1, ll_step_2;
    private TextView tv_step_1, tv_step_2;

    private int _themeColor = 0;
    private int _color_normal = 0;
    private GradientDrawable _selected_drawable;

    private FragmentActPublishStep1 mFragmentActPublishStep1;
    private FragmentActPublishStep2 mFragmentActPublishStep2;
    private MenuItem mMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fragment = new ThreadPublishDialogFragment();
        _themeColor = ThemeUtils.getThemeColor(this);
        _color_normal = getResources().getColor(R.color.z_txt_c_act_publish_step_n);
        _selected_drawable = new GradientDrawable();
        _selected_drawable.setStroke(2, _themeColor);
        _selected_drawable.setShape(GradientDrawable.OVAL);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean getExtra() {
        //TODO
        return true;
    }

    @Override
    public boolean initWidget() {
        super.initWidget();
        ActionBarUtils.setHomeActionBar(this, R.drawable.ic_back);
        setTitle(R.string.z_act_publish_title);
        frame_act_publish_1 = findViewById(R.id.frame_act_publish_1);
        frame_act_publish_2 = findViewById(R.id.frame_act_publish_2);
        ll_step_1 = findViewById(R.id.ll_step).findViewById(R.id.rl_item_0);
        ll_step_2 = findViewById(R.id.ll_step).findViewById(R.id.rl_item_1);
        ll_step_1.setOnClickListener(this);
        ll_step_2.setOnClickListener(this);
        tv_step_1 = (TextView) ll_step_1.findViewById(R.id.tv_name_1);
        tv_step_2 = (TextView) ll_step_2.findViewById(R.id.tv_name_2);
        onClick(ll_step_1);
        return true;
    }

    @Override
    public Object getDatas() {
        return mActConfig;
    }

    @Override
    protected boolean initWidgetWithData() {
        return super.initWidgetWithData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_thread_reply, menu);
        mMenuItem = menu.findItem(R.id.action_send);
        mMenuItem.setTitle(R.string.z_act_top_btn_next);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                if (getResources().getString(R.string.z_act_top_btn_commit).equals(item.getTitle())) {
                    if (mFragmentActPublishStep2 != null) {
                        ActInfo temp = mFragmentActPublishStep2.checkInputInfo(mActInfo);
                        if (temp != null) {
                            mActInfo = temp;
                            sendAct();
                        }
                    }
                } else {
                    onClick(ll_step_2);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setCurrentStep(int stepId) {
        if (stepId == 1) {
            if (mMenuItem != null) {
                mMenuItem.setTitle(R.string.z_act_top_btn_next);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tv_step_1.setBackground(_selected_drawable);
                tv_step_2.setBackground(null);
            } else {
                tv_step_1.setBackgroundDrawable(_selected_drawable);
                tv_step_2.setBackgroundDrawable(null);
            }
            tv_step_1.setTextColor(_themeColor);
            tv_step_2.setTextColor(_color_normal);
            if (mFragmentActPublishStep1 == null) {
                mFragmentActPublishStep1 = new FragmentActPublishStep1();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_act_publish_1, mFragmentActPublishStep1, FragmentActPublishStep1.class.getSimpleName()).commit();
            }
            frame_act_publish_1.setVisibility(View.VISIBLE);
            frame_act_publish_2.setVisibility(View.GONE);
            return;
        }
        if (mMenuItem != null) {
            mMenuItem.setTitle(R.string.z_act_top_btn_commit);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tv_step_1.setBackground(null);
            tv_step_2.setBackground(_selected_drawable);
        } else {
            tv_step_1.setBackgroundDrawable(null);
            tv_step_2.setBackgroundDrawable(_selected_drawable);
        }
        tv_step_1.setTextColor(_color_normal);
        tv_step_2.setTextColor(_themeColor);
        if (mFragmentActPublishStep2 == null) {
            mFragmentActPublishStep2 = new FragmentActPublishStep2();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_act_publish_2, mFragmentActPublishStep2, FragmentActPublishStep2.class.getSimpleName()).commit();
        }
        frame_act_publish_1.setVisibility(View.GONE);
        frame_act_publish_2.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFragmentActPublishStep2 != null) {
            mFragmentActPublishStep2.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        int vid = view.getId();
        if (vid == R.id.rl_item_0) {
            setCurrentStep(1);
        } else if (vid == R.id.rl_item_1) {
            if (mFragmentActPublishStep1 != null) {
                mActInfo = mFragmentActPublishStep1.checkInputInfo();
                if (mActInfo != null) {
                    setCurrentStep(2);
                }
            }
        }
    }

    public static void gotoActPublishActivity(Activity activity, ActConfig config, int requestCode) {
        Intent intent = new Intent(activity, ActPublishActivity.class);
        intent.putExtra("ActConfig", config);
        activity.startActivityForResult(intent, requestCode);
    }

}
