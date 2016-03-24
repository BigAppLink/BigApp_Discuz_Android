package com.kit.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Zhao on 14/11/21.
 *
 * 广播工具
 */
public class BroadcastUtils {

    public static Object object;


    public static void send(Context context,Intent intent,Bundle bundle){
        intent.putExtras(bundle);

        context.sendBroadcast(intent);
    }



    public static void send(Context context,Intent intent,Object obj){
        object = obj;
        context.sendBroadcast(intent);
    }


    public static Object getData() {
        return object;
    }
}
