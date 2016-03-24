package com.kit.share;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.kit.share.model.ShareItem;

import java.util.ArrayList;

import cn.sharesdk.demo.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * TODO<分享工具>
 *
 * @data: 2014-7-21 下午2:45:38
 * @version: V1.0
 */

public class SharePopupWindow extends PopupWindow {

    private Context context;
    private PlatformActionListener platformActionListener;
    private ShareParams shareParams;
    private GridView gridView;
    private ArrayList<ShareItem> shareItems;

    private SharePopupWindow(Context cx) {
        this.context = cx;
    }



    private void make() {
        View view = LayoutInflater.from(context).inflate(R.layout.share_layout, null);
        gridView = (GridView) view.findViewById(R.id.share_gridview);
        ShareAdapter adapter = new ShareAdapter(context, shareItems);
        gridView.setAdapter(adapter);

        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        // 取消按钮
        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });


        View pop_layout =  view.findViewById(R.id.pop_layout);

        // 取消按钮
        pop_layout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });


        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);

        // 设置SelectPicPopupWindow点击外部关闭
        this.setOutsideTouchable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
//         设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
//        gridView.setOnItemClickListener(new ShareItemClickListener(this));

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        gridView.setOnItemClickListener(onItemClickListener);
    }


    /**
     * 初始化分享参数
     *
     * @param shareModel
     */
    public static SharePopupWindow init(Context context, ArrayList<ShareItem> shareItems,  ShareModel shareModel) {
        SharePopupWindow sharePopupWindow = new SharePopupWindow(context);
        sharePopupWindow.context = context;
        sharePopupWindow.shareItems = shareItems;
        if (shareModel != null) {
            ShareParams sp = new ShareParams();
            sp.setShareType(Platform.SHARE_TEXT);
            sp.setShareType(Platform.SHARE_WEBPAGE);

            sp.setTitle(shareModel.getTitle());
            sp.setText(shareModel.getText());
            sp.setUrl(shareModel.getUrl());
            sp.setImageUrl(shareModel.getImageUrl());
            sharePopupWindow.shareParams = sp;

        }
        sharePopupWindow.make();

        return sharePopupWindow;
    }

    public Context getContext() {
        return context;
    }

    public PlatformActionListener getPlatformActionListener() {
        return platformActionListener;
    }

    public void setPlatformActionListener(
            PlatformActionListener platformActionListener) {
        this.platformActionListener = platformActionListener;
    }


    public ShareParams getShareParams() {
        return shareParams;
    }

    public ArrayList<ShareItem> getShareItems() {
        return shareItems;
    }


}
