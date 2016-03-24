package com.youzu.clan.main.bottomtab;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kit.app.core.task.DoSomeThing;
import com.kit.bottomtabui.model.TabItem;
import com.kit.bottomtabui.view.MainBottomTabLayout;
import com.kit.bottomtabui.view.OnTabClickListener;
import com.kit.bottomtabui.view.OnTabItemSelectedClickListener;
import com.kit.utils.AppUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ResourceUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.enums.BottomButtonType;
import com.youzu.clan.base.json.homepageconfig.ButtonConfig;
import com.youzu.clan.base.json.homepageconfig.HomepageVariables;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.util.view.threadandarticle.ThreadAndArticleUtils;
import com.youzu.clan.base.widget.SuperViewPager;
import com.youzu.clan.main.wechatstyle.WeChatStyleFragmentAdapter;
import com.youzu.clan.message.MessageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TabholderFragment extends Fragment {

    private WeChatStyleFragmentAdapter mAdapter;
    private SuperViewPager mPager;
    private MainBottomTabLayout mTabLayout;

    private TextView tvDo, tvPreDo;
    private ArrayList<Fragment> fragments;

    private List<Integer> positionList = new ArrayList<Integer>();


    public static TabholderFragment tabholderFragment;


    private HomepageVariables homepageVariables;
//    private Skeleton skeleton;

    public TabholderFragment() {
    }

    public static TabholderFragment getInstance(TextView tv0, TextView tv1, ArrayList<Fragment> fragments) {
        if (tabholderFragment == null) {
            tabholderFragment = new TabholderFragment();

            tabholderFragment.tvPreDo = tv0;
            tabholderFragment.tvDo = tv1;
            tabholderFragment.fragments = fragments;
        }
        return tabholderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom_tab_main, container, false);
        setViews(rootView);
        return rootView;
    }

    private void setViews(final View view) {

        //ZogUtils.printError(TabholderFragment.class, "fragments.size():" + fragments.size());
        mAdapter = new WeChatStyleFragmentAdapter(getFragmentManager(), fragments);
        mPager = (SuperViewPager) view.findViewById(com.kit.bottomtabui.R.id.tab_pager);
        mPager.setScrollable(AppSPUtils.isWechatScroll(getActivity()));
        mPager.setAdapter(mAdapter);
        //预加载全部
        mPager.setOffscreenPageLimit(tabholderFragment.fragments.size());
        mTabLayout = (MainBottomTabLayout) view.findViewById(com.kit.bottomtabui.R.id.main_bottom_tablayout);

        homepageVariables = AppSPUtils.getHomePageConfigJson(getActivity()).getVariables();
        final ArrayList<ButtonConfig> buttonConfigs = homepageVariables.getButtonConfigs();
        //ZogUtils.printError(TabholderFragment.class, "homepageVariables.getButtonConfigs().size()1:" + homepageVariables.getButtonConfigs().size());


        ArrayList<TabItem> tabItems = new ArrayList<TabItem>();
        int textNormalColor = getResources().getColor(R.color.gray);
//        int textSelectedColor = getResources().getColor(R.color.bg_title);
        int textSelectedColor = ThemeUtils.getThemeColor(getActivity());

        ArrayList<Integer> jp = new ArrayList<>();

        for (int i = 0; i < buttonConfigs.size(); i++) {

            ButtonConfig s = buttonConfigs.get(i);

            Drawable normal = getResources().getDrawable(ResourceUtils.getDrawableId(getActivity(), "icon_add_nomal"));
            try {
                normal = getResources().getDrawable(ResourceUtils.getDrawableId(getActivity(), "ic_tab_" + s.getIconId()));
            } catch (Exception e) {
                ZogUtils.showException(e);
            }


            Drawable press = getResources().getDrawable(ResourceUtils.getDrawableId(getActivity(), "icon_add_press"));
            try {
                press = getResources().getDrawable(ResourceUtils.getDrawableId(getActivity(), "ic_tab_pressed_" + s.getIconId()));
            } catch (Exception e) {
                ZogUtils.showException(e);
            }

            ZogUtils.printError(TabholderFragment.class, "StringUtils.isEmptyOrNullOrNullStr(s.getButtonName()):" + StringUtils.isEmptyOrNullOrNullStr(s.getButtonName()));


            TabItem tabItem = new TabItem(s.getButtonName(), normal, press, textNormalColor, textSelectedColor, StringUtils.isEmptyOrNullOrNullStr(s.getButtonName()), 10);

            if (BottomButtonType.THREAD_PUBLISH.equals(s.getButtonType())) {
                //发帖按钮
                tabItem = new TabItem(s.getButtonName(), normal, press, textNormalColor, textSelectedColor, true, StringUtils.isEmptyOrNullOrNullStr(s.getButtonName()), 10, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ThreadAndArticleUtils.addThread(getActivity());
                    }
                });


                jp.add(i);
            }
            tabItems.add(tabItem);
        }


        //ZogUtils.printError(TabholderFragment.class, "tabItems.size():" + tabItems.size() + " jp.size():" + jp.size());


        mTabLayout.setJustBottonPosition(jp);

        mTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int back = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                setCurr(position);

                //没有登录,回滚viewpager
                if ((fragments.get(position) instanceof MessageFragment)
                        && ClanUtils.isToLogin(getActivity(), null, Activity.RESULT_OK, false)) {

                    if (positionList.size() > 0) {
                        back = positionList.get(positionList.size() - 1);
                    }
                    AppUtils.delay(500, new DoSomeThing() {
                        @Override
                        public void execute(Object... objects) {
                            mPager.setCurrentItem(back);
                        }
                    });
                    return;
                }

                positionList.add(position);

                int size = tabholderFragment.fragments.size();
                if (positionList.size() >= size)
                    positionList = ListUtils.subList(positionList, positionList.size() - size, size);


                if (fragments.get(position) instanceof MessageFragment) {
                    getTabLayout().setNotifyText(BottomTabMainActivity.MESSAGE_POSITION, "");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        OnTabItemSelectedClickListener OnTabItemSelectedClickListener = new OnTabItemSelectedClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                int positionInViewPager = mTabLayout.getRealPositionInViewPager(position);
                //ZogUtils.printError(TabholderFragment.class, "OnTabItemSelectedClickListener positionInViewPager:" + positionInViewPager);
                ButtonConfig buttonConfig = buttonConfigs.get(positionInViewPager);
                //ZogUtils.printError(TabholderFragment.class, "buttonConfig.getIconType():" + buttonConfig.getButtonType());
                Fragment fragment = fragments.get(positionInViewPager);
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).refresh();
                }
            }
        };

        OnTabClickListener.OnItemClickListener onItemClickListener = new OnTabClickListener.OnItemClickListener() {
            @Override
            public boolean onItemClick(View v, int position) {
                //ZogUtils.printError(TabholderFragment.class, "======onItemClickListener position:" + position + " homepageVariables.getButtonConfigs().size():" + homepageVariables.getButtonConfigs().size());

                ButtonConfig buttonConfig = homepageVariables.getButtonConfigs().get(position);

                if (BottomButtonType.MESSAGE.equals(buttonConfig.getButtonType()) && ClanUtils.isToLogin(getActivity(), null, Activity.RESULT_OK, false)) {
                    //ZogUtils.printError(TabholderFragment.class, "MESSAGE MESSAGE MESSAGE");
                    return false;
                }

                if (BottomButtonType.THREAD_PUBLISH.equals(buttonConfig.getButtonType()) || mTabLayout.getTabItems().get(position).isJustButton()) {
                    //ZogUtils.printError(TabholderFragment.class, "THREAD_PUBLISH THREAD_PUBLISH THREAD_PUBLISH");
                    mTabLayout.getTabItems().get(position).getJustButtonClickListener().onClick(v);
                    return false;
                }


                return true;
            }
        };

        mTabLayout.bind(tabItems, mPager, onItemClickListener, OnTabItemSelectedClickListener);


        setCurr(0);

    }


    public void setCurr(int position) {

        //ZogUtils.printError(TabholderFragment.class, "setCurr:::::" + position);

        if (
                (
                        BottomButtonType.MESSAGE.equals(homepageVariables.getButtonConfigs().get(0).getButtonType())
                                || BottomButtonType.THREAD_PUBLISH.equals(homepageVariables.getButtonConfigs().get(0).getButtonType())
                )
                        && ClanUtils.isToLogin(getActivity(), null, Activity.RESULT_OK, false)) {
        }

        final BottomTabMainActivity activity = ((BottomTabMainActivity) getActivity());
        activity.setTopbar(true, position);
    }


    public MainBottomTabLayout getTabLayout() {
        return mTabLayout;
    }


    public int getCurrInViewPager(){
       return mPager.getCurrentItem();
    }


//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putInt("position", mTabLayout.getSelectedPosition());
//
//        ZogUtils.printError(TabholderFragment.class, "mTabLayout.getSelectedPosition():" + mTabLayout.getSelectedPosition());
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onViewStateRestored(Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState != null) {
//            int position = savedInstanceState.getInt("position", 0);
//            ZogUtils.printError(TabholderFragment.class, "position:" + position);
//
//            setCurr(position);
//        }
//    }
}