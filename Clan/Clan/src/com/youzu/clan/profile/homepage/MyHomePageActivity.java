package com.youzu.clan.profile.homepage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.entity.ImageLibRequestResultCode;
import com.kit.utils.HtmlUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.common.Constants;
import com.youzu.clan.base.common.ErrorCode;
import com.youzu.clan.base.enums.MessageVal;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.UploadAvatarJson;
import com.youzu.clan.base.json.model.FileInfo;
import com.youzu.clan.base.json.profile.AdminGroup;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.json.profile.Space;
import com.youzu.clan.base.net.ClanHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;
import com.youzu.clan.common.images.PicSelectorActivity;
import com.youzu.clan.profile.thread.MyThreadActivity;

import java.io.File;
import java.util.List;

/**
 * 我的主页
 *
 * @author wangxi
 */
@ContentView(R.layout.activity_home_page)
public class MyHomePageActivity extends BaseActivity {

    TextView mTitleText;
    @ViewInject(value = R.id.name)
    TextView mNameText;
    @ViewInject(value = R.id.thread_count)
    TextView mThreadCountText;
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

    private ClanApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplication = (ClanApplication) getApplication();

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String uid = intent.getStringExtra(Key.KEY_UID);
        ClanHttp.getProfile(this, uid,
                new StringCallback(this) {
                    @Override
                    public void onSuccess(Context ctx, String t) {
                        super.onSuccess(ctx, t);
                        ProfileJson profileJson = JsonUtils.parseObject(t, ProfileJson.class);
                        if (profileJson != null) {
                            ProfileVariables variables = profileJson.getVariables();
                            setData(variables);
                        }

                    }

                    @Override
                    public void onFailed(Context cxt, int errorCode, String errorMsg) {
                        super.onFailed(MyHomePageActivity.this, errorCode, errorMsg);
                        ToastUtils.show(activity, R.string.request_failed);
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
        mThreadCountText.setText(StringUtils.get(space.getPosts()));


        mResourceText.setText(ClanUtils.getLevelStr(this, space));

        mGroupText.setText(HtmlUtils.delHTMLTag(getGroupName(space)));
        mConstellationText.setText(StringUtils.get(space.getConstellation()));
        mDateText.setText(StringUtils.get(space.getRegdate()));
        setSexDrawalbe(space);
        LoadImageUtils.displayMineAvatar(MyHomePageActivity.this, mPhotoImage, space.getAvatar());

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
     * 发帖数
     *
     * @param view
     */
    @OnClick(R.id.thread_layout)
    public void thread(View view) {
        Intent intent = new Intent(this, MyThreadActivity.class);
        startActivity(intent);
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
        boolean isAvatarChange = ClanUtils.isAvatarChange(this);
        if (isAvatarChange) {

            PicSelectorActivity.selectPic(this,1,PicSelectorActivity.class);

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unregister_account);
        builder.setPositiveButton(R.string.confirm, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClanUtils.logout(MyHomePageActivity.this, getResources().getString(R.string.logout_succeed));

            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageLibRequestResultCode.REQUEST_SELECT_PIC && resultCode == RESULT_OK) {

            LoadingDialogFragment.getInstance(MyHomePageActivity.this).show();
            Intent intent = data;
            List<ImageBean> images = (List<ImageBean>) intent.getSerializableExtra("images");
            ZogUtils.printLog(MyHomePageActivity.class, "images.get(0).path：" + images.get(0).path);


            File file = new File(images.get(0).path);
            FileInfo fileInfo = FileInfo.transFileInfo(this, file, null);
            ClanHttp.uploadAvatar(this, fileInfo, new JSONCallback() {
                @Override
                public void onstart(Context cxt) {
                    super.onstart(cxt);
                    LoadingDialogFragment.getInstance(MyHomePageActivity.this).show();
                }

                @Override
                public void onSuccess(Context ctx, String json) {
                    UploadAvatarJson variablesJson = JsonUtils.parseObject(json, UploadAvatarJson.class);

                    if (variablesJson == null
                            || variablesJson.getVariables() == null
                            || !variablesJson.getVariables().getUploadAvatar().equals(MessageVal.API_UPLOADAVATAR_SUCCESS)) {
                        onFailed(ctx, ErrorCode.ERROR_DEFAULT, getString(R.string.avatar_repalce_failed));
                        return;
                    }

                    String avatarImgUrl = variablesJson.getVariables().getMemberAvatar();

                    AppSPUtils.saveAvatarReplacedDate(MyHomePageActivity.this);

                    ZogUtils.printLog(MyHomePageActivity.class, "avatarImgUrl：" + avatarImgUrl);
                    LoadImageUtils.displayMineAvatar(MyHomePageActivity.this, mPhotoImage, avatarImgUrl);

                    Intent intent = new Intent();
                    setResult(1, intent);

                }
//                    });
//                }

                @Override
                public void onFailed(Context cxt, int errorCode, String msg) {
                    super.onFailed(MyHomePageActivity.this, errorCode, msg);
                    ToastUtils.mkLongTimeToast(MyHomePageActivity.this, msg);
                    LoadingDialogFragment.getInstance(MyHomePageActivity.this).dismissAllowingStateLoss();

                }
            });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
