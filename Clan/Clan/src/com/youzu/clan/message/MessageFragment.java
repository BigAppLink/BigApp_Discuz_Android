package com.youzu.clan.message;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kit.utils.intentutils.IntentUtils;
import com.kit.widget.slidingtab.SlidingTabLayout;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.base.EditableFatherFragment;
import com.youzu.clan.base.EditableFragment;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.SimplePagerAdapter;
import com.youzu.clan.base.widget.list.RefreshListView;
import com.youzu.clan.friends.NewFriendsActivity;
import com.youzu.clan.message.pm.MyPMFragment;
import com.youzu.clan.message.pm.NotifyFragment;

public class MessageFragment extends EditableFatherFragment {

    int positionInViewpager = 0;

    private ViewPager mViewPager;
    private EditableFragment[] mFragments = new EditableFragment[2];


    public MessageFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_changable_portal_page, container, false);
//        RefreshListView listView = (RefreshListView) view.findViewById(R.id.list);
        ViewUtils.inject(this, view);

        AppSPUtils.saveNewMessage(getActivity(), 0);


        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        String[] tabs = getResources().getStringArray(R.array.my_message);
        MyPMFragment myPMFragment = new MyPMFragment();
        myPMFragment.setObserver(mObserver);
        mFragments[0] = myPMFragment;


        NotifyFragment notifyFragment = new NotifyFragment();
        notifyFragment.setObserver(mObserver);
        mFragments[1] = notifyFragment;


        mViewPager.setAdapter(new SimplePagerAdapter(getChildFragmentManager(), tabs, mFragments));
        SlidingTabLayout indicator = (SlidingTabLayout) view.findViewById(R.id.slidingIndicator);
        indicator.setEquipotent(true);
        indicator.setSelectedIndicatorColors(ThemeUtils.getThemeColor(getActivity()));
        indicator.setDividerColors(0);
        indicator.getTabStrip().setBottomBorderHeight(1);
        indicator.setViewPager(mViewPager);
        // indicator.setBottomBorderColor(getResources().getColor(R.color.divider_line));
        setCurr(indicator);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                positionInViewpager = arg0;
                setEditable(false);
//                initMainMenu(getActivity());

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        return view;


    }

    private void setCurr(SlidingTabLayout indicator) {
        String[] name = getResources().getStringArray(R.array.my_favorites);

        String getName = null;
        try {
            getName = (String) getActivity().getIntent().getExtras().get("name");
        } catch (Exception e) {
        }

        if (getName == null || getName.equals(name[0])) {
            indicator.setCurrentItem(0);
        } else {
            indicator.setCurrentItem(1);
        }
    }

    public RefreshListView getListView(int index) {
        return mFragments[index].getListView();
    }

    @Override
    public RefreshListView getListView() {
        return mFragments[positionInViewpager].getListView();
    }

    @OnClick(R.id.newFriends)
    protected void newFriends(View view) {
        IntentUtils.gotoNextActivity(getActivity(), NewFriendsActivity.class);
    }

    @Override
    public void setEditable(boolean isEditable) {
        if (positionInViewpager == 0)
            ((MyPMFragment) mFragments[positionInViewpager]).setEditable(isEditable);
        else
            return;

    }


    public boolean getEditable() {
        if (positionInViewpager == 0)
            return ((MyPMFragment) mFragments[positionInViewpager]).getEditable();
        else
            return false;

    }


    @Override
    public void onDelete() {

    }

    public void refresh() {
        if (positionInViewpager == 0)
            ((MyPMFragment) mFragments[positionInViewpager]).refresh();
    }

    public void initMainMenu() {
        if (!isAdded()) {
            return;
        }
//        Fragment showingFragment = null;
//        boolean isShow = false;
//
//        try {
//            showingFragment = BottomTabMainActivity.getFragments().get(BottomTabMainActivity.NOW_POSITION_IN_VIEWPAGER);
//        } catch (Exception e) {
//        }
//
//        if (showingFragment != null && showingFragment instanceof MessageFragment) {
//            isShow = true;
//        }
//
//        if (activity instanceof MenuJumpActivity) {
//            isShow = true;
//        }
//
//        if (isShow) {
//            tvPreDo.setText(getActivity().getResources().getString(R.string.edit));
//            tvPreDo.setVisibility(View.VISIBLE);
//        }else
//            tvPreDo.setVisibility(View.GONE);


        tvPreDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageFragment.this.setEditable(!MessageFragment.this.getEditable());
            }
        });


        if (mFragments != null && mFragments[positionInViewpager] != null) {
            if (positionInViewpager == 0)
                ((MyPMFragment) mFragments[positionInViewpager]).initMainMenu();
            else
                return;
        }

    }
}