package com.youzu.clan.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.kit.app.core.task.DoSomeThing;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.entity.ImageLibRequestResultCode;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.enums.MessageVal;
import com.youzu.clan.base.json.UploadAvatarJson;
import com.youzu.clan.base.json.model.FileInfo;
import com.youzu.clan.base.net.ClanHttp;
import com.youzu.clan.base.widget.LoadingDialogFragment;
import com.youzu.clan.common.images.PicSelectorActivity;

import java.io.File;
import java.util.List;

/**
 * Created by Zhao on 2015/9/22.
 */
public class AvatalUtils {


    public static void changeAvatal(final FragmentActivity act, Fragment fragment) {

        if (fragment != null) {
            if (ClanUtils.isToLogin(fragment, null, Activity.RESULT_OK, false)) {
                return;
            }
            boolean isAvatarChange = ClanUtils.isAvatarChange(fragment.getActivity());

            if (isAvatarChange) {
                Intent intent = new Intent(fragment.getActivity(), PicSelectorActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("remain", 1);
                intent.putExtras(bundle);
                fragment.startActivityForResult(intent, ImageLibRequestResultCode.REQUEST_SELECT_PIC);
            }
        } else {
            if (ClanUtils.isToLogin(act, null, Activity.RESULT_OK, false)) {
                return;
            }
            boolean isAvatarChange = ClanUtils.isAvatarChange(act);

            if (isAvatarChange) {
                PicSelectorActivity.selectPic(act,1,PicSelectorActivity.class);
            }
        }
    }

    public static void uploadAvatal(final FragmentActivity act, Intent data, final ImageView mPhotoImage) {
        uploadAvatal(act, data, mPhotoImage, null);
    }

    public static void uploadAvatal(final FragmentActivity act, Intent data, final ImageView mPhotoImage, final DoSomeThing doSomeThing) {
        if (act == null || data == null || mPhotoImage == null) {
            return;
        }
        Intent intent = data;
        List<ImageBean> images = (List<ImageBean>) intent.getSerializableExtra("images");
        ZogUtils.printLog(AvatalUtils.class, "images.get(0).path：" + images.get(0).path);


        File file = new File(images.get(0).path);
        FileInfo fileInfo = FileInfo.transFileInfo(act, file, null);
        ClanHttp.uploadAvatar(act, fileInfo, new JSONCallback() {
            @Override
            public void onstart(Context cxt) {
                super.onstart(cxt);
                LoadingDialogFragment.getInstance(act).show();
            }

            @Override
            public void onSuccess(Context ctx, String json) {
                UploadAvatarJson variablesJson = JsonUtils.parseObject(json, UploadAvatarJson.class);

                if (variablesJson == null
                        || variablesJson.getVariables() == null
                        || !variablesJson.getVariables().getUploadAvatar().equals(MessageVal.API_UPLOADAVATAR_SUCCESS)) {
                    onFailed(ctx, com.youzu.clan.base.common.ErrorCode.ERROR_DEFAULT, act.getString(R.string.avatar_repalce_failed));
                    return;
                }

                String avatarImgUrl = variablesJson.getVariables().getMemberAvatar();
                if (!StringUtils.isEmptyOrNullOrNullStr(avatarImgUrl)) {
                    avatarImgUrl = avatarImgUrl.replace("size=small", "size=big");
                }

                ZogUtils.printLog(AvatalUtils.class, "avatarImgUrl：" + avatarImgUrl);

                if (doSomeThing != null) {
                    doSomeThing.execute();
                }

                AppSPUtils.saveAvatarReplacedDate(act);
                LoadImageUtils.displayMineAvatar(act, mPhotoImage, avatarImgUrl);

                LoadingDialogFragment.getInstance(act).dismissAllowingStateLoss();

//                Glide.with(act)
//                        .load(AppSPUtils.getAvatartUrl(act))
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .centerCrop()
//                        .crossFade()
//                        .placeholder(R.drawable.ic_protrait_solid)
//                        .error(R.drawable.ic_protrait_solid)
//                        .into(new SimpleTarget<GlideDrawable>() {
//                            @Override
//                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                                LoadingDialogFragment.getInstance(act).dismissAllowingStateLoss();
//                            }
//
//                            @Override
//                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                super.onLoadFailed(e, errorDrawable);
//                                LoadingDialogFragment.getInstance(act).dismissAllowingStateLoss();
//                            }
//                        });
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(act, errorCode, msg);
                ToastUtils.mkLongTimeToast(act, msg);
                LoadingDialogFragment.getInstance(act).dismissAllowingStateLoss();
            }
        });
    }
}
