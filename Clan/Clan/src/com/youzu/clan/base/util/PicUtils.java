package com.youzu.clan.base.util;

import android.content.Context;
import android.content.Intent;

import com.kit.app.core.task.DoSomeThing;
import com.kit.imagelib.entity.ImageBean;
import com.kit.utils.ListUtils;

import java.util.List;

/**
 * Created by Zhao on 15/12/7.
 */
public class PicUtils {

    public static void onSeleted(final Context act, Intent data, final DoSomeThing doSomeThing) {
        if (act == null || data == null) {
            return;
        }
        List<ImageBean> images = (List<ImageBean>) data.getSerializableExtra("images");
        if (ListUtils.isNullOrContainEmpty(images)) {
            return;
        }
        if (doSomeThing != null)
            doSomeThing.execute(images);


    }
}
