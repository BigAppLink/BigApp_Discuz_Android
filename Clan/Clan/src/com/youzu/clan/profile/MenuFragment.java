package com.youzu.clan.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kit.app.core.task.DoSomeThing;
import com.kit.imagelib.entity.ImageLibRequestResultCode;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.AboutActivity;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.callback.ProfileCallback;
import com.youzu.clan.base.common.Constants;
import com.youzu.clan.base.common.ResultCode;
import com.youzu.clan.base.enums.MainActivityStyle;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.json.profile.Space;
import com.youzu.clan.base.net.ClanHttp;
import com.youzu.clan.base.net.DoSignIn;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.AvatalUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.jump.JumpProfileUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.util.view.ViewDisplayUtils;
import com.youzu.clan.friends.FriendsActivity;
import com.youzu.clan.main.qqstyle.QQStyleMainActivity;
import com.youzu.clan.message.pm.MyPMActivity;
import com.youzu.clan.myfav.MyFavActivity;
import com.youzu.clan.profile.thread.MyThreadActivity;
import com.youzu.clan.setting.SettingsActivity;

public class MenuFragment extends Fragment {

    @ViewInject(R.id.photo)
    private ImageView mPhotoImage;
    @ViewInject(R.id.sex)
    private ImageView mSexImage;
    @ViewInject(R.id.name)
    private TextView mUserName;
    @ViewInject(R.id.red_dot)
    private TextView mRedDotText;
    @ViewInject(R.id.resource)
    private TextView resource;


    @ViewInject(R.id.signIn)
    private ImageButton signIn;


    @ViewInject(R.id.scrollView)
    private ScrollView scrollView;


    @ViewInject(R.id.slidingmenumain)
    private View slidingmenumain;


    private ClanApplication mApplication;
    private ProfileVariables mProfileVariables;
    private SharedPreferences mSharedPreferences;

    private static MenuFragment fragment;

    public static MenuFragment getInstance() {
        if (fragment == null) {
            fragment = new MenuFragment();
        }
        return fragment;
    }

    private OnSharedPreferenceChangeListener mPreferenceListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (!key.equals(Key.KEY_NEW_MESSAGE)) {
                return;
            }
            final int messageCount = sharedPreferences.getInt(key, 0);
            mRedDotText.setVisibility(messageCount > 0 ? View.VISIBLE : View.GONE);
            mRedDotText.setText(String.valueOf(messageCount));
        }
    };

    private void setRedDot(int count) {
        mRedDotText.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        mRedDotText.setText(String.valueOf(count));
        ViewDisplayUtils.setBadgeCount(getActivity(), count);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApplication = (ClanApplication) getActivity().getApplication();
        View view = inflater.inflate(R.layout.fragment_menu, null);
        ViewUtils.inject(this, view);
        mSharedPreferences = getActivity().getSharedPreferences(Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mPreferenceListener);

        if (AppSPUtils.getConfig(getActivity()).getAppStyle() == MainActivityStyle.QQ) {
            scrollView.setBackgroundColor(getActivity().getResources().getColor(R.color.transparent));
        }

        slidingmenumain.setBackgroundColor(ThemeUtils.getThemeColor(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isLogined = AppSPUtils.isLogined(getActivity());

        if (isLogined) {
            updateProfile();
            DoSignIn.checkSignIn(getActivity(), signIn, DoSignIn.CHECK, null);
        } else {
            clearProfile();
        }
        int dotCount = AppSPUtils.getNewMessage(getActivity());
        setRedDot(dotCount);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mPreferenceListener);
    }


//	/**
//	 * 跳转到登录界面
//	 */
//	public void toLogin() {
//		Intent intent = new Intent(getActivity(), LoginActivity.class);
//		startActivityForResult(intent, 1);
//	}


    public void jump(Class clz) {
        if (ClanUtils.isToLogin(this, null, 1, false)) {
            return;
        }
        Intent intent = new Intent(getActivity(), clz);
        intent.putExtra(Key.KEY_UID, AppSPUtils.getUid(getActivity()));
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ZogUtils.printError(MenuFragment.class, "resultCode::::::" + resultCode);

        switch (resultCode) {
            case ResultCode.RESULT_CODE_EXIT: {
                clearProfile();
            }
            break;
            case ResultCode.RESULT_CODE_LOGIN: {
                if (data != null && data.getBooleanExtra(Key.KEY_LOGINED, false)) {
                    ProfileJson json = (ProfileJson) data.getSerializableExtra(Key.KEY_LOGIN_RESULT);
                    if (json == null) {
                        updateProfile();
                    } else {
                        updateUi(json);
                    }
                }
            }
            break;
            case Activity.RESULT_OK:
                if (requestCode == ImageLibRequestResultCode.REQUEST_SELECT_PIC) {
                    AvatalUtils.uploadAvatal(getActivity(), data, mPhotoImage, new DoSomeThing() {
                        @Override
                        public void execute(Object... object) {
                            if (getActivity() instanceof QQStyleMainActivity) {
                                ((QQStyleMainActivity) getActivity()).refreshAvatar();
                            }


                        }
                    });
                }
                break;
        }


    }

    /**
     * 更新用户信息
     */
    private void updateProfile() {
        ClanHttp.getProfile(getActivity(), "", new ProfileCallback() {

            @Override
            public void onSuccess(Context ctx, ProfileJson t) {
                updateUi(t);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
            }
        });
    }

    private void clearProfile() {
        mProfileVariables = null;
        resource.setVisibility(View.GONE);
        mSexImage.setVisibility(View.GONE);
        mUserName.setVisibility(View.VISIBLE);
        mUserName.setText(getString(R.string.click_to_login));
        mPhotoImage.setImageResource(R.drawable.ic_protrait_default);
    }

    /**
     * 更新界面
     *
     * @param t
     */
    private void updateUi(ProfileJson t) {
        ClanUtils.initSignIn(getActivity(), signIn);

        if (t == null || !isAdded() || isHidden() || isDetached()) {
            return;
        }
        ProfileVariables variables = t.getVariables();
        if (variables == null) {
            clearProfile();
            return;
        }

        mProfileVariables = variables;
        Space space = variables.getSpace();
        if (space == null) {
            return;
        }
        final String avatar = space.getAvatar();

        LoadImageUtils.displayMineAvatar(getActivity(), mPhotoImage, avatar);

        final String gender = space.getGender();
        if (Constants.SEX_MAN.equals(gender)) {
            mSexImage.setVisibility(View.VISIBLE);
            mSexImage.setImageResource(R.drawable.ic_man);
        } else if (Constants.SEX_WOMAN.equals(gender)) {
            mSexImage.setVisibility(View.VISIBLE);
            mSexImage.setImageResource(R.drawable.ic_woman);
        } else {
            mSexImage.setVisibility(View.GONE);
        }
        resource.setVisibility(View.VISIBLE);
        mUserName.setText(StringUtils.get(variables.getMemberUsername()));
        resource.setText(ClanUtils.getLevelStr(getActivity(), space));
    }

    /**
     * 我的主页
     *
     * @param view
     */
    @OnClick(R.id.photo)
    public void photo(View view) {
        AvatalUtils.changeAvatal(getActivity(), this);
    }


    /**
     * 签到
     *
     * @param view
     */
    @OnClick(R.id.signIn)
    private void signIn(View view) {
        if (ClanUtils.isToLogin(getActivity(), null, Activity.RESULT_OK, false)) {
            return;
        }
        DoSignIn.checkSignIn(getActivity(), signIn, DoSignIn.SIGN_IN, new DoSomeThing() {
            @Override
            public void execute(Object... object) {
                updateProfile();
            }
        });
    }

    /**
     * 我的收藏
     *
     * @param view
     */
    @OnClick(R.id.my_favories)
    public void myFavories(View view) {
        jump(MyFavActivity.class);
    }

    /**
     * 站内消息
     *
     * @param view
     */
    @OnClick(R.id.my_message)
    public void myMessage(View view) {
        jump(MyPMActivity.class);
    }


    /**
     * 站内消息
     *
     * @param view
     */
    @OnClick(R.id.myFriend)
    public void myFriend(View view) {
        jump(FriendsActivity.class);
    }

    /**
     * 我的帖子
     *
     * @param view
     */
    @OnClick(R.id.my_post)
    public void myThread(View view) {
        jump(MyThreadActivity.class);
    }

    /**
     * 我的主页
     *
     * @param view
     */
    @OnClick(R.id.my_home_page)
    public void myHomePage(View view) {
        JumpProfileUtils.gotoProfilePage(getActivity(), AppSPUtils.getUid(getActivity()));
    }


    /**
     * 设置
     *
     * @param view
     */
    @OnClick(R.id.settings)
    public void settings(View view) {
        BundleData bundleData = new BundleData();
        bundleData.put("ProfileVariables", mProfileVariables);
        IntentUtils.gotoNextActivity(getActivity(), SettingsActivity.class, bundleData);
    }


    /**
     * 关于
     *
     * @param view
     */
    @OnClick(R.id.about)
    public void about(View view) {
        IntentUtils.gotoNextActivity(getActivity(), AboutActivity.class);
    }


}
