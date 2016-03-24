//package com.youzu.clan.main.qqstyle;
//
//import android.util.Log;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.Toast;
//
///**
//* 滚动至列表底部，读取下一页数据
//*/
//public class AutoLoadListener implements AbsListView.OnScrollListener {
//    public interface AutoLoadCallBack {
//        void execute(String url);
//    }
//    private int getLastVisiblePosition = 0,lastVisiblePositionY=0;
//    private AutoLoadCallBack  mCallback;
//    public AutoLoadListener(AutoLoadCallBack callback)
//    {
//        this.mCallback = callback;
//    }
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//            //滚动到底部
//            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                View v=(View) view.getChildAt(view.getChildCount()-1);
//                int[] location = new  int[2] ;
//                v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
//                int y=location [1];
//                Log.e("x" + location[0], "y" + location[1]);
//                if (view.getLastVisiblePosition()!=getLastVisiblePosition
//                        && lastVisiblePositionY!=y)//第一次拖至底部
//                {
//                    Toast.makeText(view.getContext(), "再次拖至底部，即可翻页", 500).show();
//                    getLastVisiblePosition=view.getLastVisiblePosition();
//                    lastVisiblePositionY=y;
//                    return;
//                }
//                else if (view.getLastVisiblePosition()==getLastVisiblePosition
//                        && lastVisiblePositionY==y)//第二次拖至底部
//                {
//                    mCallback.execute(">>>>>拖至底部");
//                }
//            }
//            //未滚动到底部，第二次拖至底部都初始化
//            getLastVisiblePosition=0;
//            lastVisiblePositionY=0;
//        }
//    }
//    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
//    }
//}