package com.youzu.clan.main.wechatstyle;

import android.app.Activity;
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
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.enums.MainActivityStyle;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.SuperViewPager;
import com.youzu.clan.base.widget.list.PinnedSectionRefreshListView;
import com.youzu.clan.base.widget.list.RefreshListView;
import com.youzu.clan.main.base.IndexPageFragment;
import com.youzu.clan.main.base.PortalPageFragment;
import com.youzu.clan.main.base.forumnav.DBForumNavUtils;
import com.youzu.clan.main.base.forumnav.ForumnavFragment;
import com.youzu.clan.message.pm.MyPMFragment;
import com.youzu.clan.thread.ThreadPublishActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private WeChatStyleFragmentAdapter mAdapter;
    private SuperViewPager mPager;
    private MainBottomTabLayout mTabLayout;

    private TextView tvDo, tvPreDo;
    private ArrayList<Fragment> fragments;

    private List<Integer> positionList = new ArrayList<Integer>();

    private int lastPosition;

    public static PlaceholderFragment placeholderFragment;

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment getInstance(TextView tv0, TextView tv1, ArrayList<Fragment> fragments) {
        if (placeholderFragment == null) {
            placeholderFragment = new PlaceholderFragment();

            placeholderFragment.tvPreDo = tv0;
            placeholderFragment.tvDo = tv1;
            placeholderFragment.fragments = fragments;
        }
        return placeholderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom_tab_ui_main, container, false);
        setViews(rootView);
        return rootView;
    }

    private void setViews(final View view) {
        mAdapter = new WeChatStyleFragmentAdapter(getFragmentManager(), fragments);
        mPager = (SuperViewPager) view.findViewById(com.kit.bottomtabui.R.id.tab_pager);
        mPager.setScrollable(AppSPUtils.isWechatScroll(getActivity()));
        mPager.setAdapter(mAdapter);
        //预加载全部
        mPager.setOffscreenPageLimit(placeholderFragment.fragments.size());
        mTabLayout = (MainBottomTabLayout) view.findViewById(com.kit.bottomtabui.R.id.main_bottom_tablayout);

        ArrayList<TabItem> tabItems = new ArrayList<TabItem>();
        int textNormalColor = getResources().getColor(R.color.gray);
        int textSelectedColor = getResources().getColor(R.color.black);
        TabItem tabItem0 = new TabItem("门户", getResources().getDrawable(R.drawable.ic_tab_home), getResources().getDrawable(R.drawable.ic_tab_home_pressed), textNormalColor, textSelectedColor,false,0);
        TabItem tabItem1 = new TabItem("论坛", getResources().getDrawable(R.drawable.ic_tab_forum), getResources().getDrawable(R.drawable.ic_tab_forum_pressed), textNormalColor, textSelectedColor,false,0);
        TabItem tabItem2 = new TabItem("发帖", getResources().getDrawable(R.drawable.trans_144_144), getResources().getDrawable(R.drawable.trans_144_144), textNormalColor, textSelectedColor, true,false,0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClanUtils.isToLogin(getActivity(), null, Activity.RESULT_OK, false)) {
                    return;
                }
                List<NavForum> forums = DBForumNavUtils.getAllNavForum(getActivity());
                if (ListUtils.isNullOrContainEmpty(forums)) {
                    ToastUtils.mkShortTimeToast(getActivity(), getString(R.string.wait_a_moment));
                } else
                    IntentUtils.gotoNextActivity(getActivity(), ThreadPublishActivity.class);
            }
        });
        TabItem tabItem3 = new TabItem("消息", getResources().getDrawable(R.drawable.ic_tab_message), getResources().getDrawable(R.drawable.ic_tab_message_pressed), textNormalColor, textSelectedColor,false,0);
        TabItem tabItem4 = new TabItem("我的", getResources().getDrawable(R.drawable.ic_tab_profile), getResources().getDrawable(R.drawable.ic_tab_profile_pressed), textNormalColor, textSelectedColor,false,0);

        tabItems.add(tabItem0);
        tabItems.add(tabItem1);
        tabItems.add(tabItem2);
        tabItems.add(tabItem3);
        tabItems.add(tabItem4);

        ArrayList<Integer> jp = new ArrayList<>();
        jp.add(2);
        mTabLayout.setJustBottonPosition(jp);

//        mTabLayout.getTabView(2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        mTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int back = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                setCurr(position);

                //没有登录,回滚viewpager
                if ((fragments.get(position) instanceof MyPMFragment)
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

                int size = placeholderFragment.fragments.size();
                if (positionList.size() >= size)
                    positionList = ListUtils.subList(positionList, positionList.size() - size, size);


                if (fragments.get(position) instanceof MyPMFragment) {
                    getTabLayout().setNotifyText(WeChatStyleMainActivity.MESSAGE_POSITION, "");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        OnTabItemSelectedClickListener OnTabItemSelectedClickListener = new OnTabItemSelectedClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                int curr = mPager.getCurrentItem();
                ZogUtils.printError(PlaceholderFragment.class, "mPager.getCurrentItem():" + curr);

                switch (curr) {

                    case 0:
                        if (MainActivityStyle.TAB_BOTTOM == AppSPUtils.getConfig(getActivity()).getAppStyle()) {
                            Fragment fragment = fragments.get(curr);

                            if (fragment instanceof PortalPageFragment) {
                                PortalPageFragment f = (PortalPageFragment) fragment;
                                ZogUtils.printError(PlaceholderFragment.class, "PortalPageFragment f:" + f + " f.getListView():" + f.getListView());
                                f.getListView().setRefreshing(true);

                                if (f.getListView() instanceof PinnedSectionRefreshListView) {
                                    ((PinnedSectionRefreshListView) f.getListView()).getRefreshableView().smoothScrollToPosition(0);
                                    ((PinnedSectionRefreshListView) f.getListView()).refresh();
                                } else {
                                    ((RefreshListView) f.getListView()).getRefreshableView().smoothScrollToPosition(0);
                                    ((RefreshListView) f.getListView()).refresh();
                                }
                            } else if (fragment instanceof IndexPageFragment) {
                                IndexPageFragment f = (IndexPageFragment) fragment;
                                ZogUtils.printError(PlaceholderFragment.class, "IndexPageFragment f:" + f + " f.getListView():" + f.getListView());
                                f.getListView().setRefreshing(true);
                                f.getListView().getRefreshableView().smoothScrollToPosition(0);
                                f.getListView().refresh();
                            }

                        } else {
                            ((IndexPageFragment) fragments.get(curr)).getListView().setRefreshing(true);
                            ((IndexPageFragment) fragments.get(curr)).getListView().getRefreshableView().smoothScrollToPosition(0);
                            ((IndexPageFragment) fragments.get(curr)).getListView().refresh();
                        }
                        break;

                    case 1:
                        if (fragments.get(curr) instanceof ForumnavFragment) {
                            ((ForumnavFragment) fragments.get(curr)).getListView().setRefreshing(true);
                            ((ForumnavFragment) fragments.get(curr)).getListView().getRefreshableView().smoothScrollToPosition(0);
                            ((ForumnavFragment) fragments.get(curr)).getListView().refresh();
                        }
                        break;
                }
            }
        };
//        mTabLayout.setOnTabSelectedClickListener(onTabSelectedClickListener);
//        OnTabClick onTabClickListener = new OnTabClick(mTabLayout, mPager, null, OnTabItemSelectedClickListener);

        OnTabClickListener.OnItemClickListener onItemClickListener = new OnTabClickListener.OnItemClickListener() {
            @Override
            public boolean onItemClick(View v, int position) {


                if (position == 3 && ClanUtils.isToLogin( getActivity(), null, Activity.RESULT_OK, false)) {
                    return false;
                }

                if (position ==2 && mTabLayout.getTabItems().get(position).isJustButton()) {
                    mTabLayout.getTabItems().get(position).getJustButtonClickListener().onClick(v);
                    return false;
                }


                return true;
            }
        };
        mTabLayout.bind(tabItems, mPager, onItemClickListener, OnTabItemSelectedClickListener);

        setCurr(0);
    }


    private void setCurr(int position) {
        final WeChatStyleMainActivity activity = ((WeChatStyleMainActivity) getActivity());
        activity.setPosition(position);
        activity.setTopbar(true, activity, position);
    }


    public MainBottomTabLayout getTabLayout() {
        return mTabLayout;
    }
}