package com.kit.widget.slidingtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;

public class SlidingTabStrip extends LinearLayout {

    private static final int DEFAULT_BOTTOM_BORDER_COLOR = 0xFFD4D4D4;
    private static final int DEFAULT_BOTTOM_BORDER_HEIGHT = 1;

    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF33B5E5;
    private static final int DEFAULT_UNSELECTED_INDICATOR_COLOR = 0xFFA9A9A9;
    private static final int DEFAULT_SELECTED_INDICATOR_HEIGHT = 4;

    private static final int DEFAULT_DIVIDER_COLOR = 0xFFD4D4D4;
    private static final float DEFAULT_DIVIDER_HEIGHT = 0.5f;
    private static final int DEFAULT_DIVIDER_WIDTH = 1;

    private static final int DEFAULT_SELECTED_TEXT_COLOR = 0;
    private static final int DEFAULT_UNSELECTED_TEXT_COLOR = 0xFFA9A9A9;
    private static final int DEFAULT_TEXT_SIZE_SP = 15;

    private ViewPager mViewPager;
    /**
     * 每个tab的左右padding的距离
     */
    private static final int TAB_VIEW_PADDING_DIPS = 5;
    /**
     * 是否等分水平条，true等分，false，水平排放。
     */
    private boolean isEquipotent=false;
    /**
     * 选中的字体颜色
     */
    private int mSelectedTextColor;
    /**
     * 不被选中的字体颜色
     */
    private int mUnselectedTextColor;
    /**
     * 字体大小
     */
    private int mTextSize;

    /**
     * 和底部分割线有关的东西
     */
    private final Paint mBottomBorderPaint;
    private int mBottomBorderColor;
    private int mBottomBorderHeight;

    /**
     * 和底部选中有关的东西
     */
    private final Paint mSelectedIndicatorPaint;
    private int mSelectedIndicatorColor;
    private int mUnselectedIndicatorColor;
    private int mSelectedIndicatorHeight;

    /**
     * 和竖直分割线有关的
     */
    private final Paint mDividerPaint;
    private int mDividerColor;
    private float mDividerHeight;
    private int mDividerWidth;



    private int mSelectedPosition;
    private float mSelectionOffset;

    private SlidingTabLayout.TabColorizer mCustomTabColorizer;
    private final SimpleTabColorizer mDefaultTabColorizer;

    SlidingTabStrip(Context context) {
        this(context, null);
    }

    SlidingTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        final float density = getResources().getDisplayMetrics().density;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingTab);
        mBottomBorderColor=a.getColor(R.styleable.SlidingTab_bottomBorderColor,DEFAULT_BOTTOM_BORDER_COLOR);
        mBottomBorderHeight = (int)a.getDimension(R.styleable.SlidingTab_bottomBorderHeight,DEFAULT_BOTTOM_BORDER_HEIGHT*density);

        mSelectedIndicatorColor=a.getColor(R.styleable.SlidingTab_selectedIndicatorColor, DEFAULT_SELECTED_INDICATOR_COLOR);
        mUnselectedIndicatorColor=a.getColor(R.styleable.SlidingTab_unselectedIndicatorColor, DEFAULT_UNSELECTED_INDICATOR_COLOR);
        mSelectedIndicatorHeight=(int)a.getDimension(R.styleable.SlidingTab_selectedIndicatorHeight,DEFAULT_SELECTED_INDICATOR_HEIGHT*density);

        mDividerColor=a.getColor(R.styleable.SlidingTab_tabDividerColor, DEFAULT_DIVIDER_COLOR);
        mDividerHeight=a.getDimension(R.styleable.SlidingTab_tabDividerHeight, DEFAULT_DIVIDER_HEIGHT);
        mDividerWidth=(int)a.getDimension(R.styleable.SlidingTab_tabDividerWidth,DEFAULT_DIVIDER_WIDTH);

        mSelectedTextColor=a.getColor(R.styleable.SlidingTab_tabSeletedTextColor, DEFAULT_SELECTED_TEXT_COLOR);
        mUnselectedTextColor=a.getColor(R.styleable.SlidingTab_tabUnseletedTextColor, DEFAULT_UNSELECTED_TEXT_COLOR);
        mTextSize=(int)a.getDimension(R.styleable.SlidingTab_tabTextSize, DEFAULT_TEXT_SIZE_SP);

        isEquipotent=a.getBoolean(R.styleable.SlidingTab_isEquipotent,false);

        mDefaultTabColorizer = new SimpleTabColorizer();
        mDefaultTabColorizer.setIndicatorColors(mSelectedIndicatorColor);
        mDefaultTabColorizer.setDividerColors(mDividerColor);

        mBottomBorderPaint = new Paint();
        mBottomBorderPaint.setColor(mBottomBorderColor);

        mSelectedIndicatorPaint = new Paint();

        mDividerPaint = new Paint();
        mDividerPaint.setStrokeWidth(mDividerWidth);
    }



    void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight();
        final int childCount = getChildCount();
        final int dividerHeightPx = (int) (Math.min(Math.max(0f, mDividerHeight), 1f) * height);
        final SlidingTabLayout.TabColorizer tabColorizer = mCustomTabColorizer != null
                ? mCustomTabColorizer
                : mDefaultTabColorizer;
        // Thick colored underline below the current selection
        if (childCount > 0) {
            View selectedTitle = getChildAt(mSelectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
            int color = tabColorizer.getIndicatorColor(mSelectedPosition);

            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1)) {
                int nextColor = tabColorizer.getIndicatorColor(mSelectedPosition + 1);
                if (color != nextColor) {
                    color = blendColors(nextColor, color, mSelectionOffset);
                }

                // Draw the selection partway between the tabs
                View nextTitle = getChildAt(mSelectedPosition + 1);
                left = (int) (mSelectionOffset * nextTitle.getLeft() +(1.0f - mSelectionOffset) * left);
                right = (int) (mSelectionOffset * nextTitle.getRight() +(1.0f - mSelectionOffset) * right);
            }

            mSelectedIndicatorPaint.setColor(color);

            canvas.drawRect(left, height - mSelectedIndicatorHeight, right, height, mSelectedIndicatorPaint);
        }

        // Thin underline along the entire bottom edge
        canvas.drawRect(0, height - mBottomBorderHeight, getWidth(), height, mBottomBorderPaint);

        // Vertical separators between the titles
        int separatorTop = (height - dividerHeightPx) / 2;
        for (int i = 0; i < childCount - 1; i++) {
            View child = getChildAt(i);

            mDividerPaint.setColor(tabColorizer.getDividerColor(i));
            canvas.drawLine(child.getRight(), separatorTop, child.getRight(), separatorTop + dividerHeightPx, mDividerPaint);
        }
    }

    /**
     * Set the alpha value of the {@code color} to be the given {@code alpha} value.
     */
    private static int setColorAlpha(int color, byte alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio of which to blend. 1.0 will return {@code color1}, 0.5 will give an even blend,
     *              0.0 will return {@code color2}.
     */
    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    private static class SimpleTabColorizer implements SlidingTabLayout.TabColorizer {
        private int[] mIndicatorColors;
        private int[] mDividerColors;

        @Override
        public final int getIndicatorColor(int position) {
            return mIndicatorColors[position % mIndicatorColors.length];
        }

        @Override
        public final int getDividerColor(int position) {
            return mDividerColors[position % mDividerColors.length];
        }

        void setIndicatorColors(int... colors) {
            mIndicatorColors = colors;
        }

        void setDividerColors(int... colors) {
            mDividerColors = colors;
        }
    }

    /**
     * 当切换tab业时，替换文字的颜色；
     */
    public void changeCurrentTextColor(){
        final int tabStripChildCount = getChildCount();
        if (tabStripChildCount == 0 || mSelectedPosition < 0 || mSelectedPosition >= tabStripChildCount) {
            return;
        }
        for(int i=0;i<tabStripChildCount;i++){
            View selectedChild = getChildAt(i);
            if(selectedChild instanceof TextView){
                TextView textView=(TextView)selectedChild;
                if(i==mSelectedPosition){
                    textView.setTextColor(mSelectedTextColor);
                }else{
                    textView.setTextColor(mUnselectedTextColor);
                }
            }
        }
    }

    /**
     * 是否等分界面
     * @param isEquipotent
     */
    public void setEquipotent(boolean isEquipotent) {
        this.isEquipotent=isEquipotent;
        ViewGroup.LayoutParams params =getLayoutParams();
        if (isEquipotent) {
            params.width = getContext().getResources().getDisplayMetrics().widthPixels;
        } else {
            params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        }
        notifyDataSetChanged();
    }
    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            populateTabStrip();
        }
    }

    public void setCustomTabColorizer(SlidingTabLayout.TabColorizer customTabColorizer) {
        mCustomTabColorizer = customTabColorizer;
        if(mSelectedTextColor==0&&mCustomTabColorizer!=null){
            mSelectedTextColor=mCustomTabColorizer.getIndicatorColor(0);
        }
        invalidate();
    }
    /**
     * 设置当前选中的导航条的颜色
     * @param colors
     */
    public void setSelectedIndicatorColors(int... colors) {
        // Make sure that the custom colorizer is removed
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setIndicatorColors(colors);
        if(mSelectedTextColor==0&&colors!=null&&colors.length>0){
            mSelectedTextColor=colors[0];
        }
        invalidate();
    }
    /**
     * 竖直分割线的颜色
     * @param colors
     */
    public void setDividerColors(int... colors) {
        // Make sure that the custom colorizer is removed
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setDividerColors(colors);
        invalidate();
    }

    /**
     * 下边分割线的颜色
     * @param color
     */
    public void setBottomBorderColor(int color) {
        if (color == 0)
            return;

        mBottomBorderColor = color;
        mBottomBorderPaint.setColor(mBottomBorderColor);
        invalidate();
    }

    public int getUnselectedTextColor() {
        return mUnselectedTextColor;
    }
    /**
     * 未选中字体的颜色
     * @param mUnselectedTextColor
     */
    public void setUnselectedTextColor(int mUnselectedTextColor) {
        this.mUnselectedTextColor = mUnselectedTextColor;
    }

    public int getSelectedTextColor() {
        return mSelectedTextColor;
    }
    /**
     * 选中字体的颜色
     * @param mSelectedTextColor
     */
    public void setSelectedTextColor(int mSelectedTextColor) {
        this.mSelectedTextColor = mSelectedTextColor;
    }

    public int getTextSize() {
        return mTextSize;
    }
    /**
     * 字体的大小
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    public int getBottomBorderHeight() {
        return mBottomBorderHeight;
    }
    /**
     * 底部分割线的高度
     * @param mBottomBorderHeight
     */
    public void setBottomBorderHeight(int mBottomBorderHeight) {
        this.mBottomBorderHeight = mBottomBorderHeight;
    }

    public int getSelectedIndicatorHeight() {
        return mSelectedIndicatorHeight;
    }

    /**
     * 底部导航条的高度
     * @param mSelectedIndicatorHeight
     */
    public void setSelectedIndicatorHeight(int mSelectedIndicatorHeight) {
        this.mSelectedIndicatorHeight = mSelectedIndicatorHeight;
    }

    public int getDividerWidth() {
        return mDividerWidth;
    }
    /**
     * 竖直分割线的宽度
     * @param mDividerWidth
     */
    public void setDividerWidth(int mDividerWidth) {
        this.mDividerWidth = mDividerWidth;
    }

    public float getDividerHeight() {
        return mDividerHeight;
    }

    public void setDividerHeight(float mDividerHeight) {
        this.mDividerHeight = mDividerHeight;
    }

    public void notifyDataSetChanged() {
        removeAllViews();

        if (mViewPager != null) {
            populateTabStrip();
            this.invalidate();
        }
    }

    /**
     * 根据viewpage生成对应的textview，并添加到父空间中
     */
    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;
            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(isEquipotent?0: FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
            if(isEquipotent){
                params.weight = 1;
            }
            addView(tabView, params);
        }
    }
    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     *
     */
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        // textView.setTypeface(Typeface.DEFAULT_BOLD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(true);
        }

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding * 2, padding, padding * 2, padding);

        return textView;
    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < getChildCount(); i++) {
                if (v == getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }
}
