/**
 * ****************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *****************************************************************************
 */
package com.youzu.clan.base.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import com.kit.utils.MessageUtils;
import com.kit.utils.ZogUtils;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class PullToRefreshWebView extends com.handmark.pulltorefresh.library.PullToRefreshWebView {
    private int scrollYNum;
    private int contentHeight;


    private OnContentChangedListener onContentChangedListener;
    LinkedList<Integer> contentHeightList = new LinkedList<Integer>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            contentHeight = getRefreshableView().getContentHeight();
            contentHeightList.add(contentHeight);

        }
    };

    public PullToRefreshWebView(Context context) {
        super(context);
    }

    public PullToRefreshWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshWebView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshWebView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        invalidate();

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getScrollYNum() {
        return scrollYNum;
    }


    public void startCheckContentChange() {
        final Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                ZogUtils.printLog(PullToRefreshWebView.class, "-------检查Content有无变化--------" + contentHeight);
                MessageUtils.sendMessage(handler, 0);
                if(contentHeightList.size()>100){
                    timer.cancel();
                }
                if (contentHeightList.size() > 1) {
                    int last = contentHeightList.get(contentHeightList.size() - 1);
                    int before_last = contentHeightList.get(contentHeightList.size() - 2);

                    if (last != before_last) {
                        onContentChangedListener.onContentChanged();
                        timer.cancel();
                        ZogUtils.printLog(PullToRefreshWebView.class, "!!!!!!!!!!Content有变化!!!!!!!!!!!");
                        contentHeightList.clear();

                    }

                }
            }
        }, 0, 50);// delay=20毫秒 后执行该任务
    }


    public void setOnContentChangedListener(OnContentChangedListener onContentChangedListener) {
        this.onContentChangedListener = onContentChangedListener;
    }

    public interface OnContentChangedListener {
        public void onContentChanged();
    }


}
