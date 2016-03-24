package com.keyboard.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.keyboard.adpater.EmoticonsAdapter;
import com.keyboard.bean.EmoticonBean;
import com.keyboard.bean.EmoticonSetBean;
import com.keyboard.utils.EmoticonsController;
import com.keyboard.utils.Utils;
import com.keyboard.view.I.IEmoticonsKeyboard;
import com.keyboard.view.I.IView;

import java.util.ArrayList;
import java.util.List;

public class EmoticonsPageView extends ViewPager implements IEmoticonsKeyboard, IView {

    private Context mContext;
    private int mHeight = 0;
    private int mMaxEmoticonSetPageCount = 0;
    public int mOldPagePosition = -1;

    private List<EmoticonSetBean> mEmoticonSetBeanList;
    private EmoticonsViewPagerAdapter mEmoticonsViewPagerAdapter;
    private ArrayList<View> mEmoticonPageViews = new ArrayList<View>();

    public EmoticonsPageView(Context context) {
        this(context, null);
    }

    public EmoticonsPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        if (mHeight <= 0) {
            return;
        }
        EmoticonsPageView.this.post(new Runnable() {
            @Override
            public void run() {
                updateView();
            }
        });
    }

    private void updateView() {
        if (mEmoticonSetBeanList == null) {
            return;
        }

        if (mEmoticonsViewPagerAdapter == null) {
            mEmoticonsViewPagerAdapter = new EmoticonsViewPagerAdapter();
            setAdapter(mEmoticonsViewPagerAdapter);
            setOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (mOldPagePosition < 0) {
                        mOldPagePosition = 0;
                    }
                    int end = 0;
                    int pagerPosition = 0;
                    for (EmoticonSetBean emoticonSetBean : mEmoticonSetBeanList) {

                        int size = getPageCount(emoticonSetBean);

//                        Log.e("APP", "size size size:" + size);

                        if (end + size > position) {
                            if (mOnEmoticonsPageViewListener != null) {
                                mOnEmoticonsPageViewListener.emoticonsPageViewCountChanged(size, position);
                            }
                            // 上一页
                            if (mOldPagePosition - end >= size) {
                                if (position - end >= 0) {
                                    if (mOnEmoticonsPageViewListener != null) {
                                        mOnEmoticonsPageViewListener.playTo(position - end);
                                    }
                                }
                                if (mIViewListeners != null && !mIViewListeners.isEmpty()) {
                                    for (IView listener : mIViewListeners) {
                                        listener.onPageChangeTo(pagerPosition);
                                    }
                                }
                                break;
                            }
                            // 下一页
                            if (mOldPagePosition - end < 0) {
                                if (mOnEmoticonsPageViewListener != null) {
                                    mOnEmoticonsPageViewListener.playTo(0);
                                }
                                if (mIViewListeners != null && !mIViewListeners.isEmpty()) {
                                    for (IView listener : mIViewListeners) {
                                        listener.onPageChangeTo(pagerPosition);
                                    }
                                }
                                break;
                            }
                            // 本页切换
                            if (mOnEmoticonsPageViewListener != null) {

                                int oldP = mOldPagePosition - end;
                                int newP = position - end;

                                Log.e("APP", "old:" + oldP + " new:" + newP + " end:" + end);

                                mOnEmoticonsPageViewListener.playBy(oldP, newP);
                            }
                            break;
                        }
                        pagerPosition++;
                        end += size;
                    }
                    mOldPagePosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }

        int screenWidth = Utils.getDisplayWidthPixels(mContext);
        int maxPagerHeight = mHeight;

        mEmoticonPageViews.clear();
        mEmoticonsViewPagerAdapter.notifyDataSetChanged();

        for (EmoticonSetBean bean : mEmoticonSetBeanList) {
            List<EmoticonBean> emoticonList = bean.getEmoticonList();
            if (emoticonList != null) {
                int emoticonSetSum = emoticonList.size();
                int row = bean.getRow();
                int line = bean.getLine();

                int del = bean.isShowDelBtn() ? 1 : 0;
                int everyPageMaxSum = row * line - del;
                int pageCount = getPageCount(bean);

                mMaxEmoticonSetPageCount = Math.max(mMaxEmoticonSetPageCount, pageCount);

                int start = 0;
                int end = everyPageMaxSum > emoticonSetSum ? emoticonSetSum : everyPageMaxSum;

                RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                gridParams.addRule(ResizeLayout.CENTER_VERTICAL);

                int itemHeight = Math.min((screenWidth - (bean.getRow() - 1) * Utils.dip2px(mContext, bean.getHorizontalSpacing())) / bean.getRow(), (maxPagerHeight - (bean.getLine() - 1) * Utils.dip2px(mContext, bean.getVerticalSpacing())) / bean.getLine());


//                // 计算行距
//                if (bean.getHeight() > 0) {
//                    int verticalspacing = Utils.dip2px(mContext, bean.getVerticalSpacing());
//                    itemHeight = Math.min(itemHeight,Utils.dip2px(mContext, bean.getHeight()));
//                    while (verticalspacing > 0) {
//                        int userdefHeigth = (bean.getLine() - 1) * verticalspacing + Utils.dip2px(mContext, bean.getHeight()) * (bean.getLine());
//                        if (userdefHeigth <= maxPagerHeight) {
//                            bean.setVerticalSpacing(Utils.px2dip(mContext, verticalspacing));
//                            break;
//                        }
//                        bean.setVerticalSpacing(Utils.px2dip(mContext, verticalspacing));
//                        verticalspacing = (int)Math.ceil((float)verticalspacing / 2);
//                    }
//                }

                for (int i = 0; i < pageCount; i++) {
                    RelativeLayout rl = new RelativeLayout(mContext);
                    GridView gridView = new GridView(mContext);
//                    gridView.setMotionEventSplittingEnabled(false);
                    gridView.setNumColumns(bean.getRow());
                    gridView.setBackgroundColor(Color.TRANSPARENT);
                    gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                    gridView.setCacheColorHint(Color.TRANSPARENT);
                    gridView.setHorizontalSpacing(Utils.dip2px(mContext, bean.getHorizontalSpacing()));
                    gridView.setVerticalSpacing(Utils.dip2px(mContext, bean.getVerticalSpacing()));
                    gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                    gridView.setGravity(Gravity.CENTER);
                    gridView.setVerticalScrollBarEnabled(false);

                    List<EmoticonBean> list = new ArrayList<EmoticonBean>();
                    for (int j = start; j < end; j++) {
                        list.add(emoticonList.get(j));
                    }

                    // 删除按钮
                    if (bean.isShowDelBtn()) {
                        int count = bean.getLine() * bean.getRow();
                        while (list.size() < count - 1) {
                            list.add(null);
                        }
                        list.add(new EmoticonBean(EmoticonBean.FACE_TYPE_DEL, "drawable://icon_del", null));
                    } else {
                        int count = bean.getLine() * bean.getRow();
                        while (list.size() < count) {
                            list.add(null);
                        }
                    }

                    EmoticonsAdapter adapter = new EmoticonsAdapter(mContext, list);
                    adapter.setHeight(itemHeight, Utils.dip2px(mContext, bean.getItemPadding()));
                    gridView.setAdapter(adapter);
                    rl.addView(gridView, gridParams);
                    mEmoticonPageViews.add(rl);
                    adapter.setOnItemListener(this);

                    start = everyPageMaxSum + i * everyPageMaxSum;
                    end = everyPageMaxSum + (i + 1) * everyPageMaxSum;
                    if (end >= emoticonSetSum) {
                        end = emoticonSetSum;
                    }
                }
            }
        }
        mEmoticonsViewPagerAdapter.notifyDataSetChanged();


        /**
         * 设置默认的Indicator个数
         */
        setEmoticonPosition(0);
    }

    public void setPageSelect(int position) {
        if (getAdapter() != null && position >= 0 && position < mEmoticonSetBeanList.size()) {
            int count = 0;
            for (int i = 0; i < position; i++) {
                count += getPageCount(mEmoticonSetBeanList.get(i));
            }
            setCurrentItem(count);
            setEmoticonPosition(position);
        }
    }

    public int getPageCount(EmoticonSetBean emoticonSetBean) {
        int pageCount = 0;
        if (emoticonSetBean != null && emoticonSetBean.getEmoticonList() != null
                && !emoticonSetBean.getEmoticonList().isEmpty()) {
            int del = emoticonSetBean.isShowDelBtn() ? 1 : 0;
            int everyPageMaxSum = emoticonSetBean.getRow() * emoticonSetBean.getLine() - del;
//            Log.e("APP", "emoticonSetBean.getEmoticonList().size():" + emoticonSetBean.getEmoticonList().size());

            pageCount = (int) Math.ceil((double) emoticonSetBean.getEmoticonList().size() / everyPageMaxSum);
        }
        return pageCount;
    }


    /**
     * 设置Indicator个数
     *
     * @param position 第几个库
     */
    public void setEmoticonPosition(int position) {
        /**
         * 设置默认的Indicator个数
         */
        if (mOnEmoticonsPageViewListener != null && mEmoticonSetBeanList != null && !mEmoticonSetBeanList.isEmpty()) {
            mOnEmoticonsPageViewListener.emoticonsPageViewInitFinish(getPageCount(mEmoticonSetBeanList.get(position)), 0);
        }
    }

    @Override
    public void setController(EmoticonsController controller) {
        mEmoticonSetBeanList = controller.getEmoticonSetBeanList();
    }

    private class EmoticonsViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mEmoticonPageViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mEmoticonPageViews.get(arg1));
            return mEmoticonPageViews.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }


    @Override
    public void onItemClick(EmoticonBean bean) {
        if (mIViewListeners != null && !mIViewListeners.isEmpty()) {
            for (IView listener : mIViewListeners) {
                listener.onItemClick(bean);
            }
        }
    }

    @Override
    public void onItemDisplay(EmoticonBean bean) {

    }

    @Override
    public void onPageChangeTo(int position) {

    }

    private List<IView> mIViewListeners;

    public void addIViewListener(IView listener) {
        if (mIViewListeners == null) {
            mIViewListeners = new ArrayList<IView>();
        }
        mIViewListeners.add(listener);
    }

    public void setIViewListener(IView listener) {
        addIViewListener(listener);
    }

    private OnEmoticonsPageViewListener mOnEmoticonsPageViewListener;

    public void setOnIndicatorListener(OnEmoticonsPageViewListener listener) {
        mOnEmoticonsPageViewListener = listener;
    }

    public interface OnEmoticonsPageViewListener {

        /**
         *
         * @param count 本页总数
         * @param position 本页所在位置
         */
        void emoticonsPageViewInitFinish(int count, int position);

        void emoticonsPageViewCountChanged(int count, int position);

        void playTo(int position);

        void playBy(int oldPosition, int newPosition);
    }
}
