package com.youzu.clan.main.bottomtab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.kit.app.UIHandler;
import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.AppUtils;
import com.kit.utils.FragmentUtils;
import com.kit.utils.GsonUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.common.Action;
import com.youzu.clan.base.enums.BottomButtonType;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.homepageconfig.TitleButtonConfig;
import com.youzu.clan.base.json.homepageconfig.TitleButtonConfigJson;
import com.youzu.clan.base.json.homepageconfig.ViewTabConfig;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.json.profile.Space;
import com.youzu.clan.base.net.ThreadHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.ServiceUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.view.MenuJumpTopUtils;
import com.youzu.clan.base.util.view.ViewDisplayUtils;
import com.youzu.clan.base.widget.list.OnLoadListener;
import com.youzu.clan.main.wechatstyle.MineProfileFragment;
import com.youzu.clan.message.MessageFragment;

import java.util.HashMap;

/**
 * Created by Zhao on 15/6/24.
 */
@ContentView(R.layout.activity_main_bottom_tab)
public class MenuJumpActivity extends BaseActivity implements Handler.Callback, OnLoadListener {

    private static final int FRAGMENT_INIT_COMPLITE = 100 + 1;

    @ViewInject(R.id.top)
    private LinearLayout mTop;

    TitleButtonConfig bc;

    ViewTabConfig viewTabConfig;


    private static Fragment fragment = new Fragment();

    HashMap<String, View> views;

    private long exitTime;

    private ProfileVariables mProfileVariables;

//    private Skeleton skeleton;


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case FRAGMENT_INIT_COMPLITE:
                FragmentUtils.replace(getSupportFragmentManager(), com.kit.bottomtabui.R.id.main_container, fragment);
                AppUtils.delay(1500, new DoSomeThing() {
                    @Override
                    public void execute(Object... object) {
                        if (fragment instanceof MessageFragment)
                            ((MessageFragment) fragment).initMainMenu();
//                        else if (fragment instanceof MineProfileFragment)
//                            ((MineProfileFragment) fragment).initMainMenu();
                    }
                });
                break;

        }
        return false;
    }

    @Override
    public void onSuccess(boolean hasMore) {
        UIHandler.sendMessage(MenuJumpActivity.this, FRAGMENT_INIT_COMPLITE);
        MenuJumpTopUtils.setActivityTopbarMenu(this, viewTabConfig, MenuJumpTopUtils.ibMenu0, MenuJumpTopUtils.ibMenu1);
    }

    @Override
    public void onFailed() {
        UIHandler.sendMessage(MenuJumpActivity.this, FRAGMENT_INIT_COMPLITE);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(Key.KEY_NEW_MESSAGE)) {

                int messageCount = sharedPreferences.getInt(key, 0);
                ZogUtils.printError(MenuJumpActivity.class, "messageCount:" + messageCount);

                if (messageCount > 0) {
                    setRedDot(messageCount);

                    if (fragment instanceof MessageFragment) {
                        ((MessageFragment) fragment).refresh();
                    }
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

        MenuJumpTopUtils.setActivityTopbar(this, mTop, R.layout.top_menu_jump, "", null);


        setTitle(bc.getButtonName());
//        ibSearch = (ImageButton) views.get(8);


        ZogUtils.printError(MenuJumpActivity.class, bc.getButtonName() + " bc.getTitleButtonType():" + bc.getTitleButtonType());

        switch (bc.getTitleButtonType()) {
            case BottomButtonType.HOME_PAGE:
                loadPage(this, true);
                break;

            case BottomButtonType.FORUM_NAV:
                fragment = ClanUtils.getForumNav(this);
                UIHandler.sendMessage(MenuJumpActivity.this, FRAGMENT_INIT_COMPLITE);
                break;

            case BottomButtonType.THREAD_PUBLISH:
//                    fragments.add(ClanUtils.getIndexFragment(this));
                break;

            case BottomButtonType.MESSAGE:
                fragment = new MessageFragment();
                UIHandler.sendMessage(MenuJumpActivity.this, FRAGMENT_INIT_COMPLITE);


                break;

            case BottomButtonType.MINE:
                fragment = new MineProfileFragment();
//                ((MineProfileFragment) fragment).setIbSignIn(MenuJumpTopUtils.ibSignIn);
                UIHandler.sendMessage(MenuJumpActivity.this, FRAGMENT_INIT_COMPLITE);
                break;
        }


//        PollingUtils.startCheckNewMessage(this, mPreferenceListener);

        AppSPUtils.setSPListener(this, mPreferenceListener);


    }

    private void loadPage(final OnLoadListener listener, final boolean isRefresh) {
        ZogUtils.printError(MenuJumpActivity.class, "bc.getViewLink()1:" + bc.getViewLink());


        if (bc == null || StringUtils.isEmptyOrNullOrNullStr(bc.getViewLink())) {
            listener.onSuccess(false);
            return;
        }

        ZogUtils.printError(MenuJumpActivity.class, "bc.getViewLink()2:" + bc.getViewLink());

        ThreadHttp.getDataByUrl(this, bc.getViewLink(), new JSONCallback() {
            @Override
            public void onSuccess(Context ctx, String jsonStr) {
                super.onSuccess(ctx, jsonStr);
                ZogUtils.printError(MenuJumpActivity.class, "jsonStr:" + jsonStr);
                TitleButtonConfigJson titleButtonConfigJson = JsonUtils.parseObject(jsonStr, TitleButtonConfigJson.class);

                ZogUtils.printError(MenuJumpActivity.class, "titleButtonConfigJson:" + GsonUtils.toJson(titleButtonConfigJson));

                viewTabConfig = titleButtonConfigJson.getVariables().getViewTabConfig();
                fragment = ClanUtils.getChangeableHomeFragment(viewTabConfig);

                listener.onSuccess(false);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(cxt, errorCode, errorMsg);
                listener.onFailed();
            }
        });
    }


    @Override
    protected boolean getExtra() {
        BundleData bundleData = IntentUtils.getData(getIntent());
        bc = bundleData.getObject("ButtonConfig", TitleButtonConfig.class);
        return super.getExtra();
    }

    private void setRedDot(int count) {
        ViewDisplayUtils.setBadgeCount(this, count);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ZogUtils.printError(MenuJumpActivity.class, "onResume");
        ServiceUtils.startClanService(this, Action.ACTION_CHECK_NEW_MESSAGE);

        MenuJumpTopUtils.setActivityTopbar(this, mTop, R.layout.top_menu_jump, "", null);

    }

    @Override
    protected void onDestroy() {

        ZogUtils.printError(MenuJumpActivity.class, "onDestroy onDestroy onDestroy onDestroy onDestroy onDestroy ");
        super.onDestroy();
        AppSPUtils.unsetSPListener(this, mPreferenceListener);
//        PollingUtils.stopCheckNewMessage(this, mPreferenceListener);
    }


    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        MenuJumpTopUtils.tvTitle.setText(title);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        setTitle(title.toString());
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

        AppSPUtils.saveAvatarUrl(this, avatar);
        if (!StringUtils.isEmptyOrNullOrNullStr(variables.getMemberUsername()))
            AppSPUtils.saveUsername(this, variables.getMemberUsername());

    }


    @Override
    public void onBackPressed() {
        this.finish();
    }


}
