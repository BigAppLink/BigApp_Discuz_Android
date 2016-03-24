package com.kit.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * @author Zhao laozhao1005@gmail.com
 * @ClassName ListViewUtils
 * @Description 重新设置listview的高度
 * @date 2014-5-29 下午3:45:58
 */
public class ListViewUtils {

    /**
     * @param
     * @return void 返回类型
     * @Title setListViewHeightBasedOnChildren
     * @Description 根据children高度和多少，重新设置listview的高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView,
                                                        int attHeight) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + attHeight;
        listView.setLayoutParams(params);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = getListViewHeightBasedOnChildren(listView);
        listView.setLayoutParams(params);

        System.out.println("params.height:" + params.height);

        LinearLayout linearLayout = (LinearLayout) listView.getParent();

        // android.widget.LinearLayout.LayoutParams params2 = new
        // android.widget.LinearLayout.LayoutParams(
        // LayoutParams.MATCH_PARENT, params.height);
        //
        // params2.setMargins(10, 0, 10, 10);

        linearLayout.setLayoutParams(params);
    }

    /**
     * @param
     * @return int 返回类型
     * @Title getListViewHeightBasedOnChildren
     * @Description 得到children的高度
     */
    public static int getListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            int itemHeight = listItem.getMeasuredHeight();
            System.out.println("itemHeight itemHeight:" + itemHeight);
            totalHeight += itemHeight;

        }

        int height = (int) (totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 0.5);

        return height;
    }

    /**
     * 通过listview的元素的高度来显示listview
     *
     * @param listView
     * @param attHeight
     */
    public static void setListViewParentHeightBasedOnChildren(
            ListView listView, int attHeight) {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (int) (getListViewHeightBasedOnChildren(listView)
                + attHeight + 0.5);
        listView.setLayoutParams(params);

        System.out.println("params.height:" + params.height);

        LinearLayout linearLayout = (LinearLayout) listView.getParent();

        // android.widget.LinearLayout.LayoutParams params2 = new
        // android.widget.LinearLayout.LayoutParams(
        // LayoutParams.MATCH_PARENT, params.height);
        //
        // params2.setMargins(10, 0, 10, 10);

        linearLayout.setLayoutParams(params);

    }

    /**
     * @param
     * @return void 返回类型
     * @Title setListViewParentHeight
     * @Description 重新设置listview父控件的高度
     */
    public static void setListViewParentHeight(ListView listView, int height) {

        LinearLayout linearLayout = (LinearLayout) listView.getParent();
        android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT, height);
        linearLayout.setLayoutParams(params);

    }


    /**
     * 回滚到头部
     *
     * @param listview
     */
    public static void toHead(ListView listview) {
        listview.requestFocusFromTouch();
        listview.setSelection(0);
    }
}
