package com.youzu.clan.main.base.listener;

import android.content.Context;
import android.view.View;

import com.kit.imagelib.banner.image.Banner;
import com.kit.imagelib.banner.image.OnBannerItemClickListener;
import com.youzu.clan.base.json.homeconfig.HomeConfigItem;

import java.util.List;

/**
 * Created by Zhao on 15/7/1.
 */
public class OnBannerItemOnClickLisetener extends OnBannerItemClickListener {

    public OnBannerItemOnClickLisetener(Context context, Banner banner, List data) {
        super(context, banner, data);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        DoHomeCofigOnClick.doClick(context, (HomeConfigItem) data.get(banner.getCurrentIndex()));
    }
}
