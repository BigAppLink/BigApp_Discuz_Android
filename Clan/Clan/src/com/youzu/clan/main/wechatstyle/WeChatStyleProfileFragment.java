package com.youzu.clan.main.wechatstyle;

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
import android.widget.TextView;

import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.config.AppConfig;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.common.Constants;
import com.youzu.clan.base.common.ResultCode;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.json.profile.Space;
import com.youzu.clan.base.net.ClanHttp;
import com.youzu.clan.base.net.DoSignIn;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.jump.JumpWebUtils;
import com.youzu.clan.base.util.view.ViewDisplayUtils;
import com.youzu.clan.friends.FriendsActivity;
import com.youzu.clan.message.pm.MyPMActivity;
import com.youzu.clan.myfav.MyFavActivity;
import com.youzu.clan.profile.homepage.HomePageActivity;
import com.youzu.clan.profile.thread.OwnerThreadActivity;
import com.youzu.clan.setting.SettingsActivity;

public class WeChatStyleProfileFragment extends Fragment {

    @ViewInject(R.id.photo)
    private ImageView mPhotoImage;
    @ViewInject(R.id.sex)
    private ImageView mSexImage;
    @ViewInject(R.id.name)
    private TextView mUserName;
    @ViewInject(R.id.red_dot)
    private TextView mRedDotText;
    @ViewInject(value = R.id.resource)
    TextView mResourceText;
    @ViewInject(R.id.signIn)
    private ImageButton signIn;

    private ClanApplication mApplication;
    private ProfileVariables mProfileVariables;
    private SharedPreferences mSharedPreferences;

    private static WeChatStyleProfileFragment fragment;

    public static WeChatStyleProfileFragment getInstance() {
        if (fragment == null) {
            fragment = new WeChatStyleProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_wechat_style_profile, null);
        ViewUtils.inject(this, view);
        mSharedPreferences = getActivity().getSharedPreferences(Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mPreferenceListener);

        ClanUtils.initSignIn(getActivity(), signIn);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        boolean isLogined = AppSPUtils.isLogined(getActivity());

        if (isLogined) {
            updateProfile();
        } else {
            clearProfile();
        }

    }


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
        if (!AppSPUtils.isLogined(getActivity())) {
            //			toLogin();
            ClanUtils.gotoLogin(getActivity(), null, 1, false);
            return;
        }
        Intent intent = new Intent(getActivity(), clz);
        startActivity(intent);
    }

    public void jump(Intent intent) {
        if (!AppSPUtils.isLogined(getActivity())) {
//			toLogin();
            ClanUtils.gotoLogin(getActivity(), null, 1, false);
            return;
        }
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        }
    }

    /**
     * 更新用户信息
     */
    private void updateProfile() {
        ClanHttp.getProfile(getActivity(), "", new HttpCallback<ProfileJson>() {

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
        mPhotoImage.setImageResource(R.drawable.ic_profile_nologin_default);
        mSexImage.setVisibility(View.GONE);
        mUserName.setText(getString(R.string.click_to_login));
        mResourceText.setText(R.string.to_login_get_more);
        signIn.setVisibility(View.GONE);
    }

    /**
     * 更新界面
     *
     * @param t
     */
    private void updateUi(ProfileJson t) {
        if (t == null || !isAdded() || isHidden() || isDetached() || t.getVariables() == null) {
            clearProfile();

            return;
        }

        mProfileVariables = t.getVariables();
        Space space = mProfileVariables.getSpace();


        if (space == null) {
            return;
        }
        final String avatar = space.getAvatar();

        LoadImageUtils.displayMineAvatar(getActivity(), mPhotoImage, avatar);

        mResourceText.setText(ClanUtils.getLevelStr(getActivity(), space));

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
        mUserName.setText(StringUtils.get(mProfileVariables.getMemberUsername()));

        ClanUtils.initSignIn(getActivity(), signIn);

        DoSignIn.checkSignIn(getActivity(), signIn, DoSignIn.CHECK, null);

        int dotCount = AppSPUtils.getNewMessage(getActivity());
        setRedDot(dotCount);
    }


    @OnClick(R.id.user_settings)
    public void protrait(View view) {
        Intent intent = new Intent(getActivity(), HomePageActivity.class);
        intent.putExtra(Key.KEY_UID, AppSPUtils.getUid(getActivity()));

        if (mProfileVariables != null) {
            intent.putExtra(Key.KEY_PROFILE, mProfileVariables);
        }
        jump(intent);
    }


    @OnClick({R.id.photo, R.id.headerLayout})
    public void photo(View view) {
        Intent intent = new Intent(getActivity(), HomePageActivity.class);
        intent.putExtra(Key.KEY_UID, AppSPUtils.getUid(getActivity()));

        if (mProfileVariables != null) {
            intent.putExtra(Key.KEY_PROFILE, mProfileVariables);
        }
        jump(intent);
    }


    /**
     * 帖子收藏
     *
     * @param view
     */
    @OnClick(R.id.llThreadFav)
    public void llThreadFav(View view) {
        String[] name = getResources().getStringArray(R.array.my_favorites);
        Intent intent = new Intent(getActivity(), MyFavActivity.class);
        intent.putExtra("name", name[0]);
        intent.putExtra(Key.KEY_UID, AppSPUtils.getUid(getActivity()));
        jump(intent);
    }


    /**
     * 板块收藏
     *
     * @param view
     */
    @OnClick(R.id.llForumFav)
    public void llForumFav(View view) {
        String[] name = getResources().getStringArray(R.array.my_favorites);
        Intent intent = new Intent(getActivity(), MyFavActivity.class);
        intent.putExtra("name", name[1]);
        intent.putExtra(Key.KEY_UID, AppSPUtils.getUid(getActivity()));
        jump(intent);
    }

    /**
     * 我的好友
     *
     * @param view
     */
    @OnClick(R.id.myFriend)
    public void myFriend(View view) {
        Intent intent = new Intent(getActivity(), FriendsActivity.class);
        intent.putExtra(Key.KEY_UID, AppSPUtils.getUid(getActivity()));
        jump(intent);
    }

    /**
     * 我的主帖
     *
     * @param view
     */
    @OnClick(R.id.llMyPost)
    public void llMyPost(View view) {
        String[] name = getResources().getStringArray(R.array.my_thread);
        Intent intent = new Intent(getActivity(), OwnerThreadActivity.class);
        intent.putExtra("name", name[0]);
        intent.putExtra(Key.KEY_UID, AppSPUtils.getUid(getActivity()));

        jump(intent);
    }

    /**
     * 我的回帖
     *
     * @param view
     */
    @OnClick(R.id.llMyReply)
    public void llMyReply(View view) {
        String[] name = getResources().getStringArray(R.array.my_thread);
        Intent intent = new Intent(getActivity(), OwnerThreadActivity.class);
        intent.putExtra("name", name[1]);
        intent.putExtra(Key.KEY_UID, AppSPUtils.getUid(getActivity()));
        jump(intent);
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
     * 我的主页
     *
     * @param view
     */
    @OnClick(R.id.my_home_page)
    public void myHomePage(View view) {
        Intent intent = new Intent(getActivity(), HomePageActivity.class);
        intent.putExtra(Key.KEY_UID, AppSPUtils.getUid(getActivity()));

        if (mProfileVariables != null) {
            intent.putExtra(Key.KEY_PROFILE, mProfileVariables);
        }
        jump(intent);
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
        JumpWebUtils.gotoWeb(getActivity(), getString(R.string.about), AppConfig.ABOUT);
    }


}
