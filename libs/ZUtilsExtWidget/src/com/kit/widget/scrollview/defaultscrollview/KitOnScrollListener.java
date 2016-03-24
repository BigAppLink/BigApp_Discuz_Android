package com.kit.widget.scrollview.defaultscrollview;

import com.kit.widget.floatingview.DirectionScrollListener;
import com.kit.widget.floatingview.FloatingView;

/**
 * 滚动的回调接口
 *
 * @author xiaanming
 */
public class KitOnScrollListener extends DirectionScrollListener {

    public KitOnScrollListener(FloatingView floatingView) {
        this.mFloatingView = floatingView;
    }

    /**
     * 回调方法， 返回MyScrollView滑动的Y方向距离
     *
     * @param scrollY 、
     */
    public void onScroll(int scrollY) {
    }


}
