package com.youzu.clan.base.util.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kit.utils.ListUtils;
import com.kit.utils.ResourceUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.clan.base.enums.BottomButtonType;
import com.youzu.clan.base.json.homepageconfig.TitleButtonConfig;
import com.youzu.clan.base.json.homepageconfig.ViewTabConfig;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.DrawableUtils;
import com.youzu.clan.base.util.view.threadandarticle.ThreadAndArticleUtils;
import com.youzu.clan.main.bottomtab.BottomTabMainActivity;
import com.youzu.clan.main.bottomtab.MenuJumpActivity;
import com.youzu.clan.search.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zhao on 15/11/27.
 */
public class TopMenuUtils {
    public static TextView tvPreDoDisplay;
    public static TextView tvDoDisplay;

    /**
     * 设置Activity非Actionbar样式菜单项
     *
     * @param activity
     * @param viewTabConfig
     * @param ibMenu0
     * @param ibMenu1
     * @return
     */
    public static HashMap<String, Integer> setActivityTopbarMenu(final AppCompatActivity activity, final ViewTabConfig viewTabConfig, ImageButton ibMenu0, ImageButton ibMenu1) {

        ArrayList<ImageButton> ibMenu = new ArrayList<>();
        ibMenu.add(ibMenu0);
        ibMenu.add(ibMenu1);

        HashMap<String, Integer> ibMenuVisiable = new HashMap<>();


        String prefix = "ic_menu_white_";


        if (viewTabConfig != null) {
            ArrayList<TitleButtonConfig> titleButtonConfig = viewTabConfig.getTitleCfg();

            if (!ListUtils.isNullOrContainEmpty(titleButtonConfig)) {
                ZogUtils.printError(BottomTabMainActivity.class, "titleButtonConfig.size():" + titleButtonConfig.size());
                if (titleButtonConfig.size() == 1) {
                    ibMenuVisiable.put("ibMenu0Visiable", View.VISIBLE);
                    ibMenu0.setVisibility(View.VISIBLE);
                    ibMenuVisiable.put("ibMenu1Visiable", View.GONE);


                    Drawable drawable = activity.getResources().getDrawable(ResourceUtils.getDrawableId(activity, prefix + titleButtonConfig.get(0).getIconId()));
                    ibMenu0.setImageDrawable(drawable);
                } else {
                    ibMenuVisiable.put("ibMenu0Visiable", View.VISIBLE);
                    ibMenuVisiable.put("ibMenu1Visiable", View.VISIBLE);

                    ibMenu0.setVisibility(View.VISIBLE);
                    ibMenu1.setVisibility(View.VISIBLE);


                    String iconId0 = titleButtonConfig.get(0).getIconId();
                    Drawable drawable = DrawableUtils.getTopMenuDrawable(activity, iconId0);
                    if (drawable != null)
                        ibMenu0.setImageDrawable(drawable);
                    else
                        ibMenu0.setVisibility(View.GONE);

                    String iconId1 = titleButtonConfig.get(1).getIconId();
                    Drawable drawable1 = DrawableUtils.getTopMenuDrawable(activity, iconId1);
                    if (drawable1 != null)
                        ibMenu1.setImageDrawable(drawable1);
                    else
                        ibMenu1.setVisibility(View.GONE);

                }

                for (int i = 0; i < titleButtonConfig.size(); i++) {
                    final TitleButtonConfig cfg = titleButtonConfig.get(i);

                    ZogUtils.printError(MainTopUtils.class, "cfg.getTitleButtonType():" + cfg.getTitleButtonType());

                    switch (cfg.getTitleButtonType()) {
                        case BottomButtonType.HOME_PAGE:
                        case BottomButtonType.FORUM_NAV:
                            ibMenu.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    gotoNext(activity, cfg);
                                }
                            });
                            break;

                        case BottomButtonType.MESSAGE:
                        case BottomButtonType.MINE:
                            ibMenu.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!ClanUtils.isToLogin(activity, null, Activity.RESULT_OK, false)) {
                                        gotoNext(activity, cfg);
                                    }
                                }
                            });
                            break;

                        case BottomButtonType.THREAD_PUBLISH:
                            ZogUtils.printError(MainTopUtils.class, "THREAD_PUBLISH THREAD_PUBLISH THREAD_PUBLISH");
                            ibMenu.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!ClanUtils.isToLogin(activity, null, Activity.RESULT_OK, false)) {
                                        ThreadAndArticleUtils.addThread(activity);
                                    }
                                }
                            });
                            break;

                        case BottomButtonType.SEARCH:
                            ibMenu.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentUtils.gotoNextActivity(activity, SearchActivity.class);
                                }
                            });
                    }

                }

            }


        }


        return ibMenuVisiable;
    }


    private static void gotoNext(Activity activity, TitleButtonConfig cfg) {
        BundleData bundleData = new BundleData();
        bundleData.put("ButtonConfig", cfg);
        IntentUtils.gotoNextActivity(activity, MenuJumpActivity.class, bundleData);
    }


}
