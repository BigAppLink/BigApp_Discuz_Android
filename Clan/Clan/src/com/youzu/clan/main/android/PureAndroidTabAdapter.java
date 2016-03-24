package com.youzu.clan.main.android;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.enums.BottomButtonType;
import com.youzu.clan.base.json.homepageconfig.ButtonConfig;
import com.youzu.clan.base.json.homepageconfig.ViewTabConfig;
import com.youzu.clan.base.json.homepageconfig.HomepageVariables;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.main.base.ChangeableIndexPageFragment;
import com.youzu.clan.main.bottomtab.BottomTabMainActivity;
import com.youzu.clan.main.qqstyle.OnEmptyDataListener;
import com.youzu.clan.message.pm.MyPMFragment;

import java.util.ArrayList;

public class PureAndroidTabAdapter extends FragmentPagerAdapter {
    private OnEmptyDataListener listener;
    private Context context;

    public PureAndroidTabAdapter(Context context, FragmentManager fm, OnEmptyDataListener listener) {
        super(fm);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ArrayList<Fragment> fragments = new ArrayList<>();
                HomepageVariables homepageVariables = AppSPUtils.getHomePageConfigJson(context).getVariables();
                ArrayList<ButtonConfig> buttonConfigs = homepageVariables.getButtonConfigs();

                for (int i = 0; i < buttonConfigs.size(); i++) {
                    ButtonConfig bc = buttonConfigs.get(i);
                    ZogUtils.printError(BottomTabMainActivity.class, bc.getButtonName() + " bc.getButtonType():" + bc.getButtonType());
                    if (BottomButtonType.HOME_PAGE.equals(bc.getButtonType())) {
                        ViewTabConfig viewTabConfigs = bc.getViewTabConfig();
                        fragments.add(ClanUtils.getChangeableHomeFragment(viewTabConfigs));
                        break;
                    }
                }

                if (ListUtils.isNullOrContainEmpty(buttonConfigs))
                    return new ChangeableIndexPageFragment();
                return
                        fragments.get(0);
            case 1:
                return ClanUtils.getForumNav(context);
            case 2:
                return new MyPMFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
//		super.getPageTitle(position);
        String[] tabTiles = context.getResources().getStringArray(R.array.pure_android_tab);
        return tabTiles[position];

    }

}
