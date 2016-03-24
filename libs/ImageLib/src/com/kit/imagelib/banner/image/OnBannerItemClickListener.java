package com.kit.imagelib.banner.image;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * 滚动图片点击事件，此类构造方法可直接传入对象，取对象中的数据供业务逻辑使用
 */
public class OnBannerItemClickListener implements View.OnClickListener {

    public Context context;
    public List data;
    public Banner banner;

    public OnBannerItemClickListener(Context context, Banner banner, List data) {
        this.context = context;
        this.banner = banner;
        this.data = data;
    }


    @Override
    public void onClick(View view) {
        //业务逻辑处理
    }
}