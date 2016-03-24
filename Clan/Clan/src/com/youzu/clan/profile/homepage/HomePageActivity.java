package com.youzu.clan.profile.homepage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.app.core.task.DoSomeThing;
import com.kit.imagelib.entity.ImageLibRequestResultCode;
import com.kit.utils.AppUtils;
import com.kit.utils.HtmlUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.common.Constants;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.threadview.ThreadDetailJson;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.threadview.ThreadDetailVariables;
import com.youzu.clan.base.json.threadview.Report;
import com.youzu.clan.base.json.mypm.Mypm;
import com.youzu.clan.base.json.profile.AdminGroup;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.json.profile.Space;
import com.youzu.clan.base.json.forumdisplay.Thread;
import com.youzu.clan.base.net.ClanHttp;
import com.youzu.clan.base.net.FriendHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.AvatalUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.view.ProfileUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.jump.JumpChatUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;
import com.youzu.clan.friends.ApplyFriendsActivity;
import com.youzu.clan.friends.DoFriends;
import com.youzu.clan.friends.FriendsActivity;
import com.youzu.clan.profile.thread.OwnerThreadActivity;
import com.youzu.clan.thread.ThreadReportActivity;

import java.util.ArrayList;

/**
 * 我的主页
 *
 * @author wangxi
 */
@ContentView(R.layout.activity_home_page)
public class HomePageActivity extends BaseActivity {

    @ViewInject(value = R.id.title)
    TextView mTitleText;
    @ViewInject(value = R.id.name)
    TextView mNameText;


    @ViewInject(value = R.id.ll1)
    LinearLayout ll1;

    @ViewInject(value = R.id.ll2)
    LinearLayout ll2;

    @ViewInject(value = R.id.credits)
    TextView mCredits;

    @ViewInject(value = R.id.friend_count)
    TextView mFriendCountText;

    @ViewInject(value = R.id.thread_reply_count)
    TextView mThreadReplyText;

    @ViewInject(value = R.id.thread_post_count)
    TextView mThreadPostText;
    @ViewInject(value = R.id.resource)
    TextView mResourceText;
    @ViewInject(value = R.id.group_name)
    TextView mGroupText;
    @ViewInject(value = R.id.constellation)
    TextView mConstellationText;
    @ViewInject(value = R.id.date)
    TextView mDateText;
    @ViewInject(value = R.id.photo)
    ImageView mPhotoImage;

    @ViewInject(value = R.id.sex)
    ImageView mSexImage;


    @ViewInject(value = R.id.photo_layout)
    View photoLayout;


    @ViewInject(value = R.id.top)
    View top;

    @ViewInject(value = R.id.send_msg)
    TextView mSendMsg;


    @ViewInject(value = R.id.addFriend)
    TextView mAddFriend;


    @ViewInject(value = R.id.deleteFriend)
    TextView mDeleteFriend;

    @ViewInject(value = R.id.llLogout)
    LinearLayout llLogout;

    @ViewInject(value = R.id.othersReport)
    private View othersReport;


    private String uid;
    private ProfileJson profileJson;

    private ClanApplication mApplication;
    private boolean isOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplication = (ClanApplication) getApplication();

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        uid = intent.getStringExtra(Key.KEY_UID);

        photoLayout.setBackgroundColor(ThemeUtils.getThemeColor(this));
        top.setBackgroundColor(ThemeUtils.getThemeColor(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        AppUtils.delay(100, new DoSomeThing() {
            @Override
            public void execute(Object... objects) {
                ClanHttp.getProfile(HomePageActivity.this, uid, new JSONCallback() {
                    @Override
                    public void onstart(Context cxt) {
                        LoadingDialogFragment.getInstance(HomePageActivity.this).show();
                    }

                    @Override
                    public void onSuccess(Context ctx, String t) {
                        LoadingDialogFragment.getInstance(HomePageActivity.this).dismissAllowingStateLoss();

                        profileJson = JsonUtils.parseObject(t, ProfileJson.class);
                        if (profileJson != null) {
                            ProfileVariables variables = profileJson.getVariables();
                            setData(variables);
                        }

                    }

                    @Override
                    public void onFailed(Context cxt, int errorCode, String errorMsg) {
                        LoadingDialogFragment.getInstance(HomePageActivity.this).dismissAllowingStateLoss();
                        ToastUtils.show(HomePageActivity.this, R.string.request_failed);
                    }
                });
            }
        });

    }

    /**
     * 设置个人信息
     *
     * @param variables
     */
    public void setData(ProfileVariables variables) {
        if (variables == null) {
            return;
        }

        Space space = variables.getSpace();
        if (space == null) {
            return;
        }
        mNameText.setText(StringUtils.get(space.getUsername()));
        mFriendCountText.setText(StringUtils.get(space.getFriends()));


        mThreadReplyText.setText(StringUtils.get(space.getPosts()));
//        mThreadReplyText.setText(StringUtils.get(ClanUtils.parseInt(space.getPosts())
//                - ClanUtils.parseInt(space.getThreads()) + ""));
        mThreadPostText.setText(StringUtils.get(space.getThreads()));

        mCredits.setText(StringUtils.get(space.getCredits()));

        ArrayList<LinearLayout> lls = new ArrayList<>();
        lls.add(ll1);
        lls.add(ll2);

        ClanUtils.setLevel(space, lls);


        mGroupText.setText(HtmlUtils.delHTMLTag(ProfileUtils.getGroupName(space)));
        mConstellationText.setText(StringUtils.get(space.getConstellation()));
        mDateText.setText(StringUtils.get(space.getRegdate()));
        setSexDrawalbe(space);
        LoadImageUtils.displayMineAvatar(HomePageActivity.this, mPhotoImage, space.getAvatar());

        ZogUtils.printError(HomePageActivity.class, "Owner uid:" + AppSPUtils.getUid(this) + " # look uid:" + uid);
        if (!ClanUtils.isOwner(HomePageActivity.this, uid)) {//别人的主页
            mSendMsg.setVisibility(View.VISIBLE);
            ZogUtils.printError(HomePageActivity.class, "space.isMyFriend():" + space.isMyFriend());
            if (!StringUtils.isEmptyOrNullOrNullStr(space.isMyFriend()) && space.isMyFriend().equals("1")) {
                mDeleteFriend.setVisibility(View.VISIBLE);
                mAddFriend.setVisibility(View.GONE);
            } else {
                mAddFriend.setVisibility(View.VISIBLE);
                mDeleteFriend.setVisibility(View.GONE);
            }
            llLogout.setVisibility(View.GONE);
            othersReport.setVisibility(View.VISIBLE);
            mTitleText.setText(getString(R.string.other_page));
        } else {//自己的主页
            ZogUtils.printError(HomePageActivity.class, "I am in in");
            mSendMsg.setVisibility(View.GONE);
            mAddFriend.setVisibility(View.GONE);
        }

    }

    /**
     * 设置性别
     *
     * @param space
     */
    private void setSexDrawalbe(Space space) {
        String gender = space.getGender();// 性别 男1，女2
        if (Constants.SEX_MAN.equals(gender)) {
            mSexImage.setVisibility(View.VISIBLE);
            mSexImage.setImageResource(R.drawable.ic_man);
        } else if (Constants.SEX_WOMAN.equals(gender)) {
            mSexImage.setVisibility(View.VISIBLE);
            mSexImage.setImageResource(R.drawable.ic_woman);
        } else {
            mSexImage.setVisibility(View.GONE);
        }
    }

    private String getGroupName(Space space) {
        String groupName = "";
        AdminGroup group = space.getGroup();
        if (group != null) {
            groupName = group.getGrouptitle();
        }
        return StringUtils.get(groupName);
    }


    /**
     * 加好友
     *
     * @param view
     */
    @OnClick(R.id.addFriend)
    public void addFriend(View view) {

        if (!StringUtils.isEmptyOrNullOrNullStr(uid))
            DoFriends.checkFriend(HomePageActivity.this, DoFriends.CHECK_FOR_AGREED_FRIEND, uid, new DoSomeThing() {
                @Override
                public void execute(Object... objects) {
                    boolean isCan = (boolean) objects[0];
                    if (isCan) {
                        String status = objects[1].toString();

                        ZogUtils.printError(HomePageActivity.class, "status:" + status);
                        if ("2".equals(status)) {
                            DoFriends.agreedOrRefuseFriend(HomePageActivity.this, uid, DoFriends.AGREED_FRIEND, new InjectDo<BaseJson>() {
                                        @Override
                                        public boolean doSuccess(BaseJson baseJson) {
                                            mAddFriend.setVisibility(View.GONE);
                                            mDeleteFriend.setVisibility(View.VISIBLE);
                                            return true;
                                        }

                                        @Override
                                        public boolean doFail(BaseJson baseJson, String tag) {
                                            return true;
                                        }
                                    }
                            );
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(Key.KEY_UID, uid);
                            IntentUtils.gotoNextActivity(HomePageActivity.this, ApplyFriendsActivity.class, bundle);
                        }
                    }
                }
            });
        else {
            ToastUtils.mkLongTimeToast(HomePageActivity.this, getString(R.string.wait_a_moment));
        }
    }

    /**
     * 加好友
     *
     * @param view
     */
    @OnClick(R.id.deleteFriend)
    public void deleteFriend(View view) {
        FriendHttp.removeFriend(this, uid, new StringCallback(this) {
            @Override
            public void onSuccess(Context ctx, String s) {
                super.onSuccess(ctx, s);
                ClanUtils.dealMsg(HomePageActivity.this, s, "do_success", R.string.delete_success
                        , R.string.delete_failed, this, false, false, new InjectDo<BaseJson>() {
                    @Override
                    public boolean doSuccess(BaseJson baseJson) {
                        ZogUtils.printError(HomePageActivity.class, "doSuccess doSuccess doSuccess");
                        mDeleteFriend.setVisibility(View.GONE);
                        mAddFriend.setVisibility(View.VISIBLE);
                        return true;
                    }

                    @Override
                    public boolean doFail(BaseJson baseJson, String tag) {
                        return true;
                    }
                });
            }


        });

    }


    /**
     * 好友数
     *
     * @param view
     */
    @OnClick(R.id.rlFriend)
    public void friends(View view) {
        //TODO
        //非好友是否需要跳转待定

        Intent intent = new Intent(HomePageActivity.this, FriendsActivity.class);
        intent.putExtra(Key.KEY_UID, uid);
        startActivity(intent);

//        BundleData bundleData = new BundleData();
//        bundleData.put(Key.KEY_UID, uid);
//        IntentUtils.gotoNextActivity(HomePageActivity.this, FriendsActivity.class, bundleData);
    }

    /**
     * 回帖数
     *
     * @param view
     */
    @OnClick(R.id.rlThreadReply)
    public void threadReply(View view) {
//        if (mApplication.getUid().equals(uid)) {
//            Intent intent = new Intent(this, MyThreadActivity.class);
//            intent.putExtra(Key.KEY_UID, uid);
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(this, OthersThreadActivity.class);
//            intent.putExtra(Key.KEY_UID, uid);
//            startActivity(intent);
//        }


        String[] name = getResources().getStringArray(R.array.my_thread);
        Intent intent = new Intent(this, OwnerThreadActivity.class);
        intent.putExtra("name", name[1]);
        intent.putExtra(Key.KEY_UID, uid);
        startActivity(intent);

    }


    /**
     * 发帖数
     *
     * @param view
     */
    @OnClick(R.id.rlThreadPost)
    public void threadPost(View view) {
//        if (mApplication.getUid().equals(uid)) {
//            Intent intent = new Intent(this, MyThreadActivity.class);
//            intent.putExtra(Key.KEY_UID, uid);
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(this, OthersThreadActivity.class);
//            intent.putExtra(Key.KEY_UID, uid);
//            startActivity(intent);
//        }

        String[] name = getResources().getStringArray(R.array.my_thread);
        Intent intent = new Intent(this, OwnerThreadActivity.class);
        intent.putExtra("name", name[0]);
        intent.putExtra(Key.KEY_UID, uid);
        startActivity(intent);
    }

    /**
     * 发消息
     *
     * @param view
     */
    @OnClick(R.id.send_msg)
    public void sendMessage(View view) {

        if (ClanUtils.isToLogin(HomePageActivity.this, null, Activity.RESULT_OK, false)) {
            return;
        }

        if (profileJson == null) {
            return;
        }
        ProfileVariables profileVariables = profileJson.getVariables();

        Mypm mypm = new Mypm();
        Space space = profileVariables.getSpace();
        if (space == null) {
            return;
        }
        mypm.setMsgfromidAvatar(AppSPUtils.getAvatartUrl(this));
        mypm.setMsgtoidAvatar(space.getAvatar());
        mypm.setTousername(space.getUsername());
        mypm.setTouid(space.getUid());

        JumpChatUtils.gotoChat(this, mypm);
    }

    /**
     * 退出登录
     *
     * @param view
     */
    @OnClick(R.id.logout)
    public void logout(View view) {
        showDialog(1);
    }

    @OnClick(R.id.back)
    public void back(View view) {
        finish();
    }

    @OnClick(R.id.photo)
    public void photo(View view) {
        if (ClanUtils.isOwner(HomePageActivity.this, uid)) {
            AvatalUtils.changeAvatal(this, null);
        }
    }

    @OnClick(R.id.othersReport)
    public void report(View view) {
        if (ClanUtils.isToLogin(this, null, Activity.RESULT_OK, false)) {
            return;
        }

        ThreadDetailJson threadDetailJson = new ThreadDetailJson();
        ThreadDetailVariables variables = new ThreadDetailVariables();
        threadDetailJson.setVariables(variables);
        Thread thread = new Thread();
        variables.setThread(thread);

        Report report = null;
        //兼容旧的版本
        if (report == null || report.getContent() == null || report.getContent().size() == 0 || StringUtils.isEmptyOrNullOrNullStr(report.getContent().get(0))) {
            //ToastUtils.mkShortTimeToast(this, getResources().getString(R.string.wait_a_moment));
            ZogUtils.printError(HomePageActivity.class, "用户举报内容获取失败");
            report = new Report();
            ArrayList<String> content = new ArrayList<>();
            String[] contents = getResources().getStringArray(R.array.defaultMessageReport);
            for (int i = 0; i < contents.length; i++) {
                content.add(contents[i]);
            }
            report.setContent(content);
            threadDetailJson.getVariables().setReport(report);
        }
        BundleData b = new BundleData();
        b.put(com.youzu.clan.app.constant.Key.KEY_REPORT, threadDetailJson);
        b.put(com.youzu.clan.app.constant.Key.KEY_REPORT_TYPE, false);
        IntentUtils.gotoNextActivity(this, ThreadReportActivity.class, b);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unregister_account);
        builder.setPositiveButton(R.string.confirm, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClanUtils.logout(HomePageActivity.this, getResources().getString(R.string.logout_succeed));
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ZogUtils.printError(HomePageActivity.class, "onActivityResult onActivityResult onActivityResult");
        if (requestCode == ImageLibRequestResultCode.REQUEST_SELECT_PIC && resultCode == RESULT_OK) {
            AvatalUtils.uploadAvatal(this, data, mPhotoImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
