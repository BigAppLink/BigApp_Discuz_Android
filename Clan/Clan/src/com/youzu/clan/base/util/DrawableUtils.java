package com.youzu.clan.base.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.kit.utils.ResourceUtils;
import com.kit.utils.ZogUtils;

/**
 * Created by Zhao on 15/11/27.
 */
public class DrawableUtils extends com.kit.utils.DrawableUtils {


    public static Drawable getTopMenuDrawable(Context context, String suffix) {
        if(StringUtils.isEmptyOrNullOrNullStr(suffix)){
            return null;
        }

        String prefix = "ic_menu_white_";

        ZogUtils.printError(DrawableUtils.class, "suffix:" + suffix);
        int drawableID = ResourceUtils.getDrawableId(context, prefix + suffix);

        if (drawableID == 0) {
            prefix = "ic_tab_";
        }

        drawableID = ResourceUtils.getDrawableId(context, prefix + suffix);

        return context.getResources().getDrawable(drawableID);
    }

}
