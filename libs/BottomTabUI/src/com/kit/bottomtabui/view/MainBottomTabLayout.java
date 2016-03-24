package com.kit.bottomtabui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.bottomtabui.R;
import com.kit.bottomtabui.model.TabItem;
import com.nineoldandroids.animation.ArgbEvaluator;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainBottomTabLayout extends LinearLayout {

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private ArgbEvaluator mColorEvaluator;

    //viewpager上一个position
    private int mLastPosition;
    private int mSelectedPosition;
    private float mSelectionOffset;
    private ArrayList<TabItem> tabItems = new ArrayList<TabItem>();

    private View[] mIconLayouts;

    private OnTabItemSelectedClickListener OnTabItemSelectedClickListener;
    //    private OnClickListener onTabClickListener;
    private OnTabClickListener.OnItemClickListener onItemClickListener;


    /**
     * key为position value为第几个
     */
    private ArrayList<Integer> justBottonPosition = new ArrayList<Integer>();


    /**
     * 真实的BottomUILayout的position
     */
    private ArrayList<String> tabItemsNoJustButtonPosition = new ArrayList<String>();

    /**
     * 真实的ViewPager的position
     */
    private LinkedList<Integer> viewPagerAllPosition = new LinkedList<Integer>();

    /**
     * 字体颜色
     */
    int mThisNormalColor = getResources().getColor(R.color.main_bottom_tab_textcolor_normal);
    int mTHisSelectedColor = getResources().getColor(R.color.main_bottom_tab_textcolor_selected);
    int mNextNormalColor = getResources().getColor(R.color.main_bottom_tab_textcolor_normal);
    int mNextSelectedColor = getResources().getColor(R.color.main_bottom_tab_textcolor_selected);


    public MainBottomTabLayout(Context context) {
        super(context);
        init();
    }

    public MainBottomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        mColorEvaluator = new ArgbEvaluator();
    }


    public void bind(ArrayList<TabItem> tabItems, ViewPager viewPager
            , OnTabClickListener.OnItemClickListener onItemClickListener, OnTabItemSelectedClickListener OnTabItemSelectedClickListener) {
        this.tabItems = tabItems;
        this.onItemClickListener = onItemClickListener;
        this.OnTabItemSelectedClickListener = OnTabItemSelectedClickListener;
        setViewPager(viewPager);
    }


    private void setViewPager(ViewPager viewPager) {
        removeAllViews();
        mViewPager = viewPager;
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            initTabLayout();
            viewPager.setCurrentItem(0);

//            for (int i : justBottonPosition) {
//                tabItemsNoJustButtonPosition.remove(i);
//                viewPagerAllPosition.add(i, i);
//            }
            for (int i = 0; i < justBottonPosition.size(); i++) {
                int j = justBottonPosition.get(i);
                tabItemsNoJustButtonPosition.remove(j - i);
                viewPagerAllPosition.add(j, j);
            }
        }
    }

    /**
     * 初始化底栏item
     */
    private void initTabLayout() {


        int count = tabItems.size();
        mIconLayouts = new View[count];
        for (int i = 0; i < count; i++) {


            tabItemsNoJustButtonPosition.add(i + "");
            viewPagerAllPosition.add(i);


            TabItem tabItem = tabItems.get(i);


//            if(tabItem.isJustButton()){
//                addView(tabItem.getJustButtonView());
//                continue;
//            }

            final View tabView = LayoutInflater.from(getContext()).inflate(R.layout.layout_mainbottom_tab, this, false);
            mIconLayouts[i] = tabView;

            BottomItemView iconView = (BottomItemView) tabView.findViewById(R.id.main_bottom_tab_icon);
            iconView.setNormalIcon(tabItem.getNormalDrawable());
            iconView.setSelectedIcon(tabItem.getSelectedDrawable());
            iconView.setText(tabItem.getTitle());
            iconView.setSelectedColor(tabItem.getTextSelectedColor());
            iconView.setNormalColor(tabItem.getTextNormalColor());
            iconView.setText2IconHeight(0);

            Log.e("APP", "tabItem.isPadding():" + tabItem.getTitle() + " " + tabItem.isPadding());

            if (tabItem.isPadding()) {
                iconView.setPadding(tabItem.getPadding(), tabItem.getPadding(), tabItem.getPadding(), tabItem.getPadding());
            }
            //textNormalColor
//            iconView.init(tabItem.getNormalDrawable(), tabItem.getSelectedDrawable());

            if (tabItem.getBackgroundDrawable() != null) {
                tabView.setBackgroundDrawable(tabItem.getBackgroundDrawable());
            }
            TextView textView = (TextView) tabView.findViewById(R.id.main_bottom_tab_text);
            textView.setText(tabItem.getTitle());
            int textNormalColor = getResources().getColor(R.color.main_bottom_tab_textcolor_normal);
            int textSelectedColor = getResources().getColor(R.color.main_bottom_tab_textcolor_selected);

            textNormalColor = tabItem.getTextNormalColor();
            textSelectedColor = tabItem.getTextSelectedColor();


            textView.setTextColor(textNormalColor);

            if (tabView == null) {
                throw new IllegalStateException("tabView is null.");
            }

            LayoutParams lp = (LayoutParams) tabView.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.weight = 1;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;

            OnTabClickListener onTabClickListener = new OnTabClickListener(this, mViewPager);

            onTabClickListener.setOnItemClickListener(new OnTabClickListener.OnItemClickListener() {
                @Override
                public boolean onItemClick(View v, int position) {

                    Log.e("APP", "onItemClick position:" + position);
                    boolean isGoOn = onItemClickListener.onItemClick(v, position);
                    if (isGoOn) {
                        setSelected(position);
                    }
                    return isGoOn;
                }
            });

            tabView.setOnClickListener(onTabClickListener);

            addView(tabView);

            if (i == mViewPager.getCurrentItem()) {
//                iconView.transformPage(0);
                setSelected(i);
            }
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            onViewPagerPageChanged(position, positionOffset);

//            if (mViewPagerPageChangeListener != null) {
//                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
//            }


        }

        @Override
        public void onPageSelected(int position) {
//            setMagicTextColor(position);
//            for (int i = 0; i < tabItems.size(); i++) {
//                int realPositionInBottomUILayout = getRealPositionInBottomUILayout(position);
//
//                ((TabIconView) mIconLayouts[i].findViewById(R.id.main_bottom_tab_icon))
//                        .transformPage(realPositionInBottomUILayout == i ? 0 : 1);
//                ((TextView) mIconLayouts[i].findViewById(R.id.main_bottom_tab_text))
//                        .setTextColor(realPositionInBottomUILayout == i ? mTHisSelectedColor : mThisNormalColor);
//            }

            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                onViewPagerPageChanged(position, 0f);
            }

//            for (int i = 0, size = getChildCount(); i < size; i++) {
//                getChildAt(i).setSelected(position == i);
//            }


            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }


    /**
     * Viewpager滚动的时候调用
     *
     * @param position
     * @param positionOffset
     */
    private void onViewPagerPageChanged(int position, float positionOffset) {

        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        if (positionOffset == 0f && mLastPosition != mSelectedPosition) {
            mLastPosition = mSelectedPosition;
        }

        Log.e("APP", " =========positionOffset:" + positionOffset);

        if (positionOffset > 0) {//滚动
            int leftPosition = getRealPositionInBottomUILayout(position);
            BottomItemView left = (BottomItemView) mIconLayouts[leftPosition].findViewById(R.id.main_bottom_tab_icon);

            int rightPosition = getRealPositionInBottomUILayout(position + 1);
            BottomItemView right = (BottomItemView) mIconLayouts[rightPosition].findViewById(R.id.main_bottom_tab_icon);

            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);

            Log.e("APP", " position:" + position + "leftPosition:" + leftPosition + " rightPosition:" + rightPosition);

        }
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int childCount = getChildCount();
        if (childCount > 0) {

            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1)) {

                View selectedTab = getChildAt(mSelectedPosition);
                View nextTab = getChildAt(mSelectedPosition + 1);

                View selectedIconView = ((LinearLayout) selectedTab).getChildAt(0);
                View nextIconView = ((LinearLayout) nextTab).getChildAt(0);

                View selectedTextView = ((LinearLayout) selectedTab).getChildAt(1);
                View nextTextView = ((LinearLayout) nextTab).getChildAt(1);

                //draw icon alpha
                if (selectedIconView instanceof TabIconView && nextIconView instanceof TabIconView) {
                    ((TabIconView) selectedIconView).transformPage(mSelectionOffset);
                    ((TabIconView) nextIconView).transformPage(1 - mSelectionOffset);
                }

                //draw text color
                Integer selectedColor = (Integer) mColorEvaluator.evaluate(mSelectionOffset,
                        mTHisSelectedColor,
                        mThisNormalColor);


                Integer nextColor = (Integer) mColorEvaluator.evaluate(1 - mSelectionOffset,
                        mTHisSelectedColor,
                        mThisNormalColor);

                if (selectedTextView instanceof TextView && nextTextView instanceof TextView) {
                    ((TextView) selectedTextView).setTextColor(selectedColor);
                    ((TextView) nextTextView).setTextColor(nextColor);
                }
            }
        }
    }


    /**
     * 设置选中了第几个
     */
    private void setSelected(int bottomTabPosition) {
        resetAllTabs();
        BottomItemView icon = (BottomItemView) mIconLayouts[bottomTabPosition].findViewById(R.id.main_bottom_tab_icon);
        icon.setIconAlpha(1.0f);


    }


    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }


    public View getTabView(int position) {
        return mIconLayouts[position];
    }


    public ArrayList<Integer> getJustBottonPosition() {
        return justBottonPosition;
    }


    public int getCount() {
        return tabItems.size();
    }


    public ArrayList<TabItem> getTabItems() {
        return tabItems;
    }

    public ArrayList<String> getTabItemsNoJustButtonPosition() {
        return tabItemsNoJustButtonPosition;
    }


    public com.kit.bottomtabui.view.OnTabItemSelectedClickListener getOnTabItemSelectedClickListener() {
        return OnTabItemSelectedClickListener;
    }


    /**
     * 按钮点击的时候，找到真实的viewpager的位置
     *
     * @param i 点击的第几个
     * @return
     */
    public int getRealPositionInViewPager(int i) {
        return viewPagerAllPosition.get(i);
    }

    /**
     * viewPager滚动时候，找到真实的bottomTabLayout位置
     *
     * @param position
     * @return
     */
    public int getRealPositionInBottomUILayout(int position) {
        if (tabItemsNoJustButtonPosition.get(position) != null && !TextUtils.isEmpty(tabItemsNoJustButtonPosition.get(position))) {
            int realPosition = Integer.parseInt(tabItemsNoJustButtonPosition.get(position));
            return realPosition;
        }
        return position;
    }

    /**
     * 设置 isJustBotton 按钮的 Position
     *
     * @param justBottonPosition
     */
    public void setJustBottonPosition(ArrayList<Integer> justBottonPosition) {
        this.justBottonPosition = justBottonPosition;
    }


    /**
     * 得到viewpager上次的位置
     *
     * @return
     */
    public int getLastPosition() {
        return mLastPosition;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    /**
     * 设置通知
     *
     * @param position
     * @param text
     * @return
     */
    public View setNotifyText(int position, String text) {
        TextView textView = (TextView) getTabView(position).findViewById(R.id.main_bottom_tab_notify_text);


        if (text != null && !TextUtils.isEmpty(text)) {
            textView.setVisibility(VISIBLE);
            textView.setText(text);

            Log.e("APP", "setNotifyText:" + text);

        } else {
            textView.setVisibility(GONE);
        }
        invalidate();
        return mIconLayouts[position];
    }


    /**
     * 采集奇幻文字颜色
     *
     * @param position
     */
    private void setMagicTextColor(int position) {
        mThisNormalColor = tabItems.get(position).getTextNormalColor();
        mTHisSelectedColor = tabItems.get(position).getTextSelectedColor();

        if (position + 1 < getChildCount()) {
            mNextNormalColor = tabItems.get(position + 1).getTextNormalColor();
            mNextSelectedColor = tabItems.get(position + 1).getTextSelectedColor();
        } else {
            if (position - 1 < 0) {
                return;
            }
            mNextNormalColor = tabItems.get(position - 1).getTextNormalColor();
            mNextSelectedColor = tabItems.get(position - 1).getTextSelectedColor();
        }
    }


    /**
     * 重置tab状态（颜色、选中）
     */
    private void resetAllTabs() {
        for (int i = 0; i < mIconLayouts.length; i++) {
            View v = mIconLayouts[i];
            if (v != null) {
                BottomItemView b = (BottomItemView) v.findViewById(R.id.main_bottom_tab_icon);
                b.setIconAlpha(0);
            }
        }
    }

}


