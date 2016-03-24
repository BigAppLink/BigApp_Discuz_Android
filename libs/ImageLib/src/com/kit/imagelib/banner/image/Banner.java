package com.kit.imagelib.banner.image;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 基本功能：图片滚动
 * 创建： 王杰
 */
public class Banner extends ViewPager {
    Context mActivity; // 上下文环境
    List<View> mListViews; // 图片列表
    private int mScrollTime = 0;
    private Timer timer;
    private int oldIndex = 0;
    private int curIndex = 0;



    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 广告滚动
     *
     * @param mainActivity     显示广告的主界面
     * @param imgList          图片列表, 不能为null
     * @param scrollTime       滚动间隔 ,0为不滚动
     * @param ovalLayout       圆点容器,可为LinearLayout类型
     * @param ovalLayoutId     ovalLayout为空, 圆点layout XMl
     * @param ovalLayoutItemId ovalLayout为空,圆点layout XMl 圆点XMl下View ID
     * @param focusedId        ovalLayout为空, 圆点layout XMl 选中时的动画
     * @param normalId         ovalLayout为空, 圆点layout XMl 正常时背景
     * @param isAutoPlay       自动轮播启用开关
     * @param dotAvailable     圆点是否可用
     */
    public void start(Context mainActivity, List<View> imgList,
                      int scrollTime, LinearLayout ovalLayout, int ovalLayoutId,
                      int ovalLayoutItemId, int focusedId, int normalId, boolean isAutoPlay, boolean dotAvailable) {
        mActivity = mainActivity;
        mListViews = imgList;
        mScrollTime = scrollTime;
        dotAvailable(ovalLayout, ovalLayoutId, ovalLayoutItemId, focusedId, normalId, dotAvailable);
        this.setAdapter(new MyPagerAdapter());// 设置适配器
        isAutoPlay(imgList, scrollTime, isAutoPlay);
        if (mListViews.size() > 1) {
            this.setCurrentItem((Integer.MAX_VALUE / 2)
                    - (Integer.MAX_VALUE / 2) % mListViews.size());// 设置选中为中�?图片为和�?张一�?
        }
    }

    /**
     * 圆点是否可用
     *
     * @param ovalLayout
     * @param ovalLayoutId
     * @param ovalLayoutItemId
     * @param focusedId
     * @param normalId
     * @param dotAvailable
     */
    private void dotAvailable(LinearLayout ovalLayout, int ovalLayoutId, int ovalLayoutItemId, int focusedId, int normalId, boolean dotAvailable) {
        if (dotAvailable) {
            // 设置圆点
            setOvalLayout(ovalLayout, ovalLayoutId, ovalLayoutItemId, focusedId,
                    normalId);
        }

    }

    /**
     * 是否自动滚动
     *
     * @param imgList
     * @param scrollTime
     * @param isAutoPlay
     */
    private void isAutoPlay(List<View> imgList, int scrollTime, boolean isAutoPlay) {
        if (isAutoPlay) {
            if (scrollTime != 0 && imgList.size() > 1) {
                // 设置滑动动画时间  ,如果用默认动画时间可不用 ,反射实现
                new FixedSpeedScroll(mActivity).setDuration(this, 700);

                startTimer();
                // 触摸时停止滚动
                this.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            startTimer();
                        } else {
                            stopTimer();
                        }
                        return false;
                    }
                });
            }
        }
    }

    // 设置圆点
    private void setOvalLayout(final LinearLayout ovalLayout, int ovalLayoutId,
                               final int ovalLayoutItemId, final int focusedId, final int normalId) {
        if (ovalLayout != null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            for (int i = 0; i < mListViews.size(); i++) {
                ovalLayout.addView(inflater.inflate(ovalLayoutId, null));
            }
            //ovalLayout.setGravity(Gravity.RIGHT);
            //设置圆点位置
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(0, 0, 0, 0);
//            lp.gravity = Gravity.RIGHT;
//            lp.rightMargin = 40;
//            ovalLayout.setLayoutParams(lp);
            //选中第一�?
            ovalLayout.getChildAt(0).findViewById(ovalLayoutItemId)
                    .setBackgroundResource(focusedId);
            this.setOnPageChangeListener(new OnPageChangeListener() {
                public void onPageSelected(int i) {
                    curIndex = i % mListViews.size();
                    //取消圆点选中
                    ovalLayout.getChildAt(oldIndex).findViewById(ovalLayoutItemId)
                            .setBackgroundResource(normalId);
                    //圆点选中
                    ovalLayout.getChildAt(curIndex).findViewById(ovalLayoutItemId)
                            .setBackgroundResource(focusedId);
                    oldIndex = curIndex;
                }

                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                public void onPageScrollStateChanged(int arg0) {
                }
            });
        }
    }


    /**
     * 取得当明选中下标
     *
     * @return
     */
    public int getCurrentIndex() {
        return curIndex;
    }


    /**
     * 停止滚动
     */
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 开始滚动
     */
    public void startTimer() {
        if (timer != null) {
            stopTimer();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                ((AppCompatActivity) mActivity).runOnUiThread(new Runnable() {
                    public void run() {
                        Banner.this.setCurrentItem(Banner.this
                                .getCurrentItem() + 1);
                    }
                });
            }
        }, mScrollTime, mScrollTime);
    }

    // 适配�?//循环设置
    private class MyPagerAdapter extends PagerAdapter {
        public void finishUpdate(View arg0) {
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public int getCount() {
            if (mListViews.size() == 1) {// �?��图片时不用流�?
                return mListViews.size();
            }
            return Integer.MAX_VALUE;
        }

        public Object instantiateItem(View v, int i) {
            if (((ViewPager) v).getChildCount() == mListViews.size()) {
                ((ViewPager) v)
                        .removeView(mListViews.get(i % mListViews.size()));
            }
            try {
                ((ViewPager) v).addView(mListViews.get(i % mListViews.size()), 0);
            }catch (Exception e){
                ((ViewPager) v).removeAllViews();
            }
            return mListViews.get(i % mListViews.size());
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        public Parcelable saveState() {
            return null;
        }

        public void startUpdate(View arg0) {
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
        }
    }
}
