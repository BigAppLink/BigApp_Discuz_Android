//package com.youzu.clan.base.widget;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.View;
//import android.webkit.WebView;
//
///**
// * BorderScrollView
// * <ul>
// * <li>onTop and onBottom response ScrollView</li>
// * <li>you can {@link #setOnBorderListener(OnBorderListener)} to set your top and bottom response</li>
// * </ul>
// *
// * @author trinea@trinea.cn 2013-5-21
// */
//public class BorderWebView extends WebView {
//
//    private OnBorderListener onBorderListener;
//    private View             contentView;
//
//    public BorderWebView(Context context) {
//        super(context);
//    }
//
//    public BorderWebView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public BorderWebView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @Override
//    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
//        super.onScrollChanged(x, y, oldx, oldy);
//        doOnBorderListener();
//    }
//
//    public void setOnBorderListener(final OnBorderListener onBorderListener) {
//        this.onBorderListener = onBorderListener;
//        if (onBorderListener == null) {
//            return;
//        }
//
//        if (contentView == null) {
//            contentView = getChildAt(0);
//        }
//    }
//
//    /**
//     * OnBorderListener, Called when scroll to top or bottom
//     *
//     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-22
//     */
//    public static interface OnBorderListener {
//
//        /**
//         * Called when scroll to bottom
//         */
//        public void onBottom();
//
//        /**
//         * Called when scroll to top
//         */
//        public void onTop();
//    }
//
//    private void doOnBorderListener() {
//
//        //WebView的总高度
//        float webViewContentHeight=this.getContentHeight() * this.getScale();
//        //WebView的现高度
//        float webViewCurrentHeight=(this.getHeight() + this.getScrollY());
//        System.out.println("webViewContentHeight="+webViewContentHeight);
//        System.out.println("webViewCurrentHeight="+webViewCurrentHeight);
//        if ((webViewContentHeight-webViewCurrentHeight) <= 10) {
//            System.out.println("WebView滑动到了底端");
//        }
//
//        if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
//            if (onBorderListener != null) {
//                onBorderListener.onBottom();
//            }
//        } else if (getScrollY() == 0) {
//            if (onBorderListener != null) {
//                onBorderListener.onTop();
//            }
//        }
//    }
//}
