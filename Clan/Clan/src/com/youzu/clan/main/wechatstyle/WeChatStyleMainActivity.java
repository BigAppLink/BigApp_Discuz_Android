package com.youzu.clan.main.wechatstyle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.LinearLayout;

import com.kit.utils.DialogUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.config.BuildType;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.callback.ProfileCallback;
import com.youzu.clan.base.callback.ProgressCallback;
import com.youzu.clan.base.common.Action;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.config.content.SearchSettings;
import com.youzu.clan.base.json.model.Message;
import com.youzu.clan.base.json.mypm.Mypm;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.json.profile.Space;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.ServiceUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.view.MainTopUtils;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;
import com.youzu.clan.base.widget.list.OnDataSetChangedObserver;
import com.youzu.clan.base.widget.list.OnEditListener;
import com.youzu.clan.base.widget.list.OnMutipleChoiceListener;
import com.youzu.clan.base.widget.list.RefreshListView;
import com.youzu.clan.main.base.activity.BaseEditableMainActivity;
import com.youzu.clan.message.MessageFragment;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/6/24.
 */
@ContentView(R.layout.activity_main_bottom_tab)
public class WeChatStyleMainActivity extends BaseEditableMainActivity implements OnEditListener {
    public static final int MESSAGE_POSITION = 3;

    private ProfileVariables mProfileVariables;


    @ViewInject(R.id.top)
    private LinearLayout mTop;

    private static ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private BaseRefreshAdapter mAdapter;
    private RefreshListView mListView;

    private int position;

    private PlaceholderFragment placeholderFragment;

    private ClanApplication mApplication;
    private long exitTime;

    private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(Key.KEY_NEW_MESSAGE)) {

                int messageCount = sharedPreferences.getInt(key, 0);
                ZogUtils.printError(WeChatStyleMainActivity.class, "messageCount:" + messageCount);

                if (messageCount > 0) {
                    placeholderFragment.getTabLayout().setNotifyText(MESSAGE_POSITION, messageCount + "");
                    ((MessageFragment) fragments.get(2)).getListView().refresh();
                } else {
                    placeholderFragment.getTabLayout().setNotifyText(MESSAGE_POSITION, "");
                }
            } else if (key.equals(Key.KEY_AVATAR)) {
                final String avatar = sharedPreferences.getString(Key.KEY_AVATAR, "");
//                displayMineAvatar(avatar);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (ClanApplication) getApplication();

        MainTopUtils.setActivityTopbar(this, mTop, R.layout.top_main, getString(R.string.forum_name), null);


        fragments.clear();
        fragments.add(ClanUtils.getIndexFragment(this));
        fragments.add(ClanUtils.getForumNav(this));
//        fragments.add(new Fragment());

        fragments.add(new MessageFragment());
        //   fragments.add(WeChatStyleProfileFragment.getInstance());

        MineProfileFragment mineProfileFragment = new MineProfileFragment();
//        mineProfileFragment.setIbSignIn(MainTopUtils.ibSignIn);
        fragments.add(mineProfileFragment);

        placeholderFragment = PlaceholderFragment.getInstance(MainTopUtils.tvPreDo, MainTopUtils.tvDo, fragments);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(com.kit.bottomtabui.R.id.main_container, placeholderFragment)
                    .commit();
        }


//        PollingUtils.startCheckNewMessage(this, mPreferenceListener);

        AppSPUtils.setSPListener(this, mPreferenceListener);


    }

    public void showDialog() {
        if (getString(R.string.build_type).equals(BuildType.TEST)) {
            DialogUtils.showDialog(WeChatStyleMainActivity.this, getString(R.string.tips), getString(R.string.notice_test_build), getString(R.string.confirm), getString(R.string.cancel), true, true, null, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServiceUtils.startClanService(this, Action.ACTION_CHECK_NEW_MESSAGE);
    }

    @Override
    protected void onDestroy() {

        ZogUtils.printError(WeChatStyleMainActivity.class, "onDestroy onDestroy onDestroy onDestroy onDestroy onDestroy ");
        super.onDestroy();
        AppSPUtils.unsetSPListener(this, mPreferenceListener);
//        PollingUtils.stopCheckNewMessage(this, mPreferenceListener);
    }

    private boolean getData() {
        Intent intent = getIntent();
        if (intent == null) {
            return false;
        }

        mProfileVariables = (ProfileVariables) intent.getSerializableExtra(Key.KEY_PROFILE);
        if (mProfileVariables == null) {
            return false;
        }
        return true;
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(com.kit.bottomtabui.R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == com.kit.bottomtabui.R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    /**
     * 更新用户信息
     */
    private void updateProfile() {
        if (mProfileVariables == null) {
            ClanHttp.getProfile(this, "", new ProfileCallback() {

                @Override
                public void onSuccess(Context ctx, ProfileJson t) {
                    updateUi(t);
                }

                @Override
                public void onFailed(Context cxt, int errorCode, String errorMsg) {
                }
            });
        }
    }

    /**
     * 更新界面
     *
     * @param t
     */
    private void updateUi(ProfileJson t) {
        if (t == null) {
            return;
        }
        ProfileVariables variables = t.getVariables();
        if (variables == null) {
            return;
        }
        mProfileVariables = variables;
        Space space = variables.getSpace();
        if (space == null) {
            return;
        }
        final String avatar = space.getAvatar();

//        PicassoUtils.displayMineAvatar(this, mPhotoImage, space.getAvatar());
        AppSPUtils.saveAvatarUrl(this, avatar);
        if (!StringUtils.isEmptyOrNullOrNullStr(variables.getMemberUsername()))
            AppSPUtils.saveUsername(this, variables.getMemberUsername());

    }

    /**
     * 设置主页顶部的按钮
     *
     * @param isUse    是否启用
     * @param activity
     * @param position
     */
    public void setTopbar(boolean isUse, final WeChatStyleMainActivity activity, final int position) {

        if (!isUse) {
            activity.setEditable(false);
            return;
        }
        int ibEditVisible = View.GONE, tvPreDoVisible = View.GONE, tvDoVisible = View.GONE, ibSearchVisible = View.GONE, ibSignInVisible = View.GONE;
        switch (position) {
            case 0:
                MainTopUtils.tvTitle.setText(getResources().getString(R.string.forum_name));
                SearchSettings searchSettings = AppSPUtils.getContentConfig(this).getSearchSetting();
                if (!(searchSettings != null && !"1".equals(searchSettings.getEnable())))
                    ibSearchVisible = View.VISIBLE;
                break;
            case 1:
                MainTopUtils.tvTitle.setText("论坛");
                break;
            case 2:
                MainTopUtils.tvTitle.setText("消息");
                mListView = ((MessageFragment) fragments.get(position)).getListView();
                mAdapter = (BaseRefreshAdapter) ((MessageFragment) fragments.get(position)).getListView().getAdapter();

                //每次切换到这里 都要刷新
                if (AppSPUtils.isLogined(this))
                    mListView.refresh();

                tvPreDoVisible = View.VISIBLE;
                MainTopUtils.tvPreDo.setText(getResources().getString(R.string.edit));
                ZogUtils.printError(WeChatStyleMainActivity.class, "mListView.getRefreshableView().getAdapter().getCount():"
                        + ((BaseRefreshAdapter) mListView.getAdapter()).getData().size());
                if (((BaseRefreshAdapter) mListView.getAdapter()).getData().size() > 0) {
                    MainTopUtils.tvPreDo.setTextColor(getResources().getColor(R.color.white));
                } else {
                    MainTopUtils.tvPreDo.setTextColor(getResources().getColor(R.color.color_white_50));
                }

                MainTopUtils.tvPreDo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.setEditable(!mListView.isEditable());

                        mAdapter.setOnDataSetChangedObserver(new OnDataSetChangedObserver() {
                            @Override
                            public void onChanged() {
                                Log.e("APP", "onChanged onChanged onChanged!!!");
                                activity.onEditableChanged(mListView);
                            }
                        });
                    }
                });

                MainTopUtils.tvDo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDelete();
                    }
                });
                ServiceUtils.startClanService(this, Action.ACTION_CHECK_NEW_MESSAGE);

                break;

            case 3:
                MainTopUtils.tvTitle.setText("");

                break;
        }
        actionBarViewVisible(ibEditVisible, tvPreDoVisible, tvDoVisible, ibSearchVisible, ibSignInVisible);
    }

    private void actionBarViewVisible(int ibEditVisible, int tvPreDoVisible, int tvDoVisible, int ibSearchVisible, int ibSignInVisible) {
        MainTopUtils.ibEdit.setVisibility(ibEditVisible);
        MainTopUtils.tvPreDo.setVisibility(tvPreDoVisible);
        MainTopUtils.tvDo.setVisibility(tvDoVisible);
        MainTopUtils.ibMenu0.setVisibility(ibSearchVisible);
        MainTopUtils.ibSignIn.setVisibility(ibSignInVisible);
    }

    @Override
    public void onDelete() {
        if (mListView.getCheckedItemCount() < 1) {
            return;
        }
        ClanHttpParams params = new ClanHttpParams(this);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "deletepl");

        int headerCount = mListView.getRefreshableView().getHeaderViewsCount();
        SparseBooleanArray array = mListView.getChoicePostions();
        int count = mAdapter.getCount();
        StringBuffer sb = new StringBuffer();
        for (int i = headerCount; i < count + headerCount; i++) {
            if (array.get(i)) {
                Mypm mypm = (Mypm) mAdapter.getItem(i - headerCount);
                if (mypm != null && !TextUtils.isEmpty(mypm.getTouid())) {
                    sb.append(mypm.getTouid()).append("_");
                }
            }
        }
        params.addBodyParameter("deletepm_deluid", sb.toString());
        params.addBodyParameter("deletepmsubmit_btn", "true");
        params.addBodyParameter("deletesubmit", "true");

        if (!com.kit.utils.StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(this)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(this));

        BaseHttp.post(Url.DOMAIN, params, new ProgressCallback<BaseJson>(this) {
            @Override
            public void onSuccess(Context ctx, BaseJson t) {
                super.onSuccess(ctx, t);
                if (t != null && t.getMessage() != null) {
                    Message message = t.getMessage();
                    if ("delete_pm_success".equals(message.getMessageval())) {
                        ToastUtils.show(getApplicationContext(), R.string.delete_success);
                        mListView.deleteChoices();
                        return;
                    }
                }
                onFailed(ctx, -1, "");
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(cxt, errorCode, msg);
                ToastUtils.show(getApplicationContext(), R.string.delete_failed);
            }
        });
    }

    @Override
    public void onEditableChanged(RefreshListView listView) {

        Log.e("APP", "onEditableChanged!!!");

        if (listView == null) {
            Log.e("APP", "listView is null!!!");
            return;
        }
        boolean isEmpty = listView.isEmpty();
        if (MainTopUtils.tvPreDo != null) {
            String title = getString(listView.isEditable() && !isEmpty ? R.string.cancel : R.string.edit);
            MainTopUtils.tvPreDo.setText(title);
        }

        if (MainTopUtils.tvDo != null) {
            MainTopUtils.tvDo.setVisibility((listView.isEditable() && !isEmpty) ? View.VISIBLE : View.GONE);
            String title = getString(R.string.delete_with_num, listView.getCheckedItemCount());
            MainTopUtils.tvDo.setText(title);
        }

    }


    @Override
    public void onChoiceChanged(RefreshListView listView, int checkedCount) {
        String title = getString(R.string.delete_with_num, checkedCount);
        MainTopUtils.tvDo.setText(title);
    }

    @Override
    public void setEditable(boolean isEditable) {
        super.setEditable(isEditable);
        RefreshListView listView = getListView();
        if (listView != null) {
            listView.setOnMutipleChoiceListener(new OnMutipleChoiceListener() {
                @Override
                public void onChoiceChanged(RefreshListView listView, int checkedCount) {
                    WeChatStyleMainActivity.this.onChoiceChanged(listView, checkedCount);
                }

                @Override
                public void onEditableChanged(RefreshListView listView) {
                    WeChatStyleMainActivity.this.onEditableChanged(listView);
                }
            });
            listView.setEditable(isEditable);
        }

        if (isEditable) {
        }
    }

    @Override
    public RefreshListView getListView() {
        MessageFragment myPMFragment = null;
        if (position == 2) {
            myPMFragment = (MessageFragment) fragments.get(position);
            mListView = myPMFragment.getListView();
        }
        return mListView;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - exitTime > 2000) {
            ToastUtils.show(this, R.string.click_to_exit);
            exitTime = currentTime;
            return;
        }
        this.finish();
    }

}
