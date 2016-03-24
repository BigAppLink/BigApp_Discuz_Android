package com.youzu.clan.base.util.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.utils.MapUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.common.Action;
import com.youzu.clan.base.enums.BottomButtonType;
import com.youzu.clan.base.json.homepageconfig.ButtonConfig;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ServiceUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.common.WebFragment;
import com.youzu.clan.main.bottomtab.BottomTabMainActivity;
import com.youzu.clan.main.wechatstyle.MineProfileFragment;
import com.youzu.clan.message.MessageFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zhao on 15/11/24.
 */
public class MainTopUtils extends TopMenuUtils {
    public static TextView tvTitle;
    public static ImageView mAravarImage;
    public static View mRedDotView;
    public static TextView tvPreDo;
    public static TextView tvDo;
    public static ImageButton ibEdit;
    public static ImageButton ibSignIn;

    public static ImageButton ibMenu0;
    public static ImageButton ibMenu1;
    public static ImageView back;


    public static HashMap setActivityTopbar(final AppCompatActivity activity, ViewGroup container, int layoutID, String title, View.OnClickListener mAvatarClickListener) {

        HashMap<String, View> viewList = new HashMap<String, View>();

        View view = LayoutInflater.from(activity).inflate(layoutID, null);


        tvTitle = (TextView) view.findViewById(R.id.main_title);
        mAravarImage = (ImageView) view.findViewById(R.id.photo);
        mRedDotView = view.findViewById(R.id.red_dot);
        tvPreDo = (TextView) view.findViewById(R.id.tv0);
        tvDo = (TextView) view.findViewById(R.id.tv1);
        ibEdit = (ImageButton) view.findViewById(R.id.ibEdit);
        ibSignIn = (ImageButton) view.findViewById(R.id.ibSignIn);

        ibMenu0 = (ImageButton) view.findViewById(R.id.ibMenu0);
        ibMenu1 = (ImageButton) view.findViewById(R.id.ibMenu1);
        try {
            ImageView back = (ImageView) view.findViewById(R.id.back);
            viewList.put("back", back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });

        } catch (Exception e) {

        }


        viewList.put("avatar", mAravarImage);
        viewList.put("avatar", mAravarImage);
        viewList.put("reddot", mRedDotView);
        viewList.put("title", tvTitle);
        viewList.put("tvPreDo", tvPreDo);
        viewList.put("tvDo", tvDo);
        viewList.put("ibEdit", ibEdit);
        viewList.put("ibSignIn", ibSignIn);
        viewList.put("ibMenu0", ibMenu0);
        viewList.put("ibMenu1", ibMenu1);

        if (!StringUtils.isEmptyOrNullOrNullStr(title))
            tvTitle.setText(title);

        if (mAvatarClickListener != null) {
            mAravarImage.setVisibility(View.VISIBLE);
            mAravarImage.setOnClickListener(mAvatarClickListener);
        }


        if (container != null) {
            container.setBackgroundColor(ThemeUtils.getThemeColor(activity));

            if (container.getChildCount() > 0) {
                container.removeAllViews();
            }
            container.addView(view);

        }

        return viewList;
    }


    /**
     * 设置主页顶部的按钮
     *
     * @param activity
     * @param buttonConfig
     */
    public static void setMainTopbar(final AppCompatActivity activity, ButtonConfig buttonConfig, ArrayList<Fragment> fragments, int positionInViewPager) {

        if (buttonConfig == null || buttonConfig.getButtonType() == null)
            return;

        Fragment fragment = fragments.get(positionInViewPager);

        int ibEditVisible = View.GONE, tvPreDoVisible = View.GONE, tvDoVisible = View.GONE, ibSignInVisible = View.GONE;
        int ibMenu0Visiable = View.GONE, ibMenu1Visiable = View.GONE;
        tvTitle.setText(buttonConfig.getButtonName());

        resetMainTopMenu();

        switch (buttonConfig.getButtonType()) {
            case BottomButtonType.HOME_PAGE:

                String title = "";
                if (BottomTabMainActivity.NOW_POSITION_IN_VIEWPAGER == 0) {
                    title = activity.getString(R.string.forum_name);
                } else if (fragment instanceof WebFragment) {
                    title = ((WebFragment) fragment).getWebTitle();
                } else {
                    title = buttonConfig.getButtonName();
                }
                tvTitle.setText(title);

                HashMap<String, Integer> visableMenu = MainTopUtils.setActivityTopbarMenu(activity, buttonConfig.getViewTabConfig(), MainTopUtils.ibMenu0, MainTopUtils.ibMenu1);
                if (!MapUtils.isNullOrContainEmpty(visableMenu)) {
                    ibMenu0Visiable = visableMenu.get("ibMenu0Visiable");
                    ibMenu1Visiable = visableMenu.get("ibMenu1Visiable");
                }
                break;
            case BottomButtonType.FORUM_NAV:
                tvTitle.setText("论坛");
                break;
            case BottomButtonType.MESSAGE:
                tvTitle.setText("消息");
                final MessageFragment messageFragment = (MessageFragment) fragments.get(positionInViewPager);
                //每次切换到这里 都要刷新
                if (AppSPUtils.isLogined(activity))
                    messageFragment.refresh();

                messageFragment.initMainMenu();

                ServiceUtils.startClanService(activity, Action.ACTION_CHECK_NEW_MESSAGE);

                break;

            case BottomButtonType.MINE:
                tvTitle.setText("");
                final MineProfileFragment mineProfileFragment = (MineProfileFragment) fragments.get(positionInViewPager);
                mineProfileFragment.initMainMenu();

                break;
        }


        if (ibMenu0Visiable == View.VISIBLE) {
            ibMenu0.setVisibility(View.VISIBLE);
        } else
            ibMenu0.setVisibility(View.GONE);


        if (ibMenu1Visiable == View.VISIBLE) {
            ibMenu1.setVisibility(View.VISIBLE);
        } else
            ibMenu1.setVisibility(View.GONE);


    }

    private static void resetMainTopMenu() {
        ibMenu0.setVisibility(View.GONE);
        ibMenu1.setVisibility(View.GONE);

        tvPreDo.setVisibility(View.GONE);
        tvDo.setVisibility(View.GONE);

        ibEdit.setVisibility(View.GONE);
        ibSignIn.setVisibility(View.GONE);
    }
}
