package com.kit.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


/**
 * Created by Zhao on 14-8-11.
 */
public class MessageUtils {

    /**
     * 发送消息
     *
     * @param handler
     * @param messageWhat Message.what
     * @return
     */
    public static void sendMessage(Handler handler, int messageWhat) {
        Message message = new Message();
        message.what = messageWhat;
        handler.sendMessage(message);
    }

    /**
     * 发送消息
     *
     * @param handler
     * @return
     */
    public static void sendMessage(Handler handler, int arg1, int arg2, Object obj, int what, Bundle bundle) {
        Message message = Message.obtain();
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        message.what = what;
        message.setData(bundle);
        handler.sendMessage(message);
    }


}
