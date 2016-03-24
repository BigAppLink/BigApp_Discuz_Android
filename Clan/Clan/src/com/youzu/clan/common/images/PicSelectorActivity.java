package com.youzu.clan.common.images;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.kit.app.ActivityManager;
import com.kit.imagelib.entity.ImageLibRequestResultCode;
import com.kit.imagelib.photoselector.PicSelectActivity;
import com.kit.utils.ZogUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;

import cn.sharesdk.analysis.MobclickAgent;


public class PicSelectorActivity extends PicSelectActivity {


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        initTheme();
        ActivityManager.getInstance().pushActivity(this);
        buttombanner.setBackgroundColor(ThemeUtils.getThemeColor(this));
    }


    public void initTheme() {
        ThemeUtils.initTheme(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public static void selectPic(Activity activity, int remain) {
        Intent intent = new Intent(activity, PicSelectorActivity.class);
//        Intent intent = new Intent(activity, clazz);

        Bundle bundle = new Bundle();
        bundle.putInt("remain", remain);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, ImageLibRequestResultCode.REQUEST_SELECT_PIC);
    }


    public static void selectPic(Fragment fragment, int remain) {
        Intent intent = new Intent();
        intent.setClass(fragment.getActivity(),PicSelectorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("remain", remain);
        intent.putExtras(bundle);
        ZogUtils.printError(PicSelectorActivity.class, "ImageLibRequestResultCode.REQUEST_SELECT_PIC:" + ImageLibRequestResultCode.REQUEST_SELECT_PIC);

        fragment.startActivityForResult(intent, ImageLibRequestResultCode.REQUEST_SELECT_PIC);
    }


}
