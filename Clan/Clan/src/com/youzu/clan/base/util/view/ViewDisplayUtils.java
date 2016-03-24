package com.youzu.clan.base.util.view;

import android.content.Context;

import com.kit.utils.BadgeUtils;
import com.youzu.clan.base.util.AppUSPUtils;

/**
 * Created by Zhao on 15/11/9.
 */
public class ViewDisplayUtils {


    /**
     * 显示角标
     * 针对 Samsung / xiaomi / sony 手机有效
     *
     */
    public static void setBadgeCount(Context context, int count) {
        if (AppUSPUtils.isUShowBadge(context)) {
            BadgeUtils.setBadgeCount(context, count);
        }
    }

}
