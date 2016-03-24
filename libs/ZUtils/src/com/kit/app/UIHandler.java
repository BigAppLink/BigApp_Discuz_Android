//#if def{sdk.debugable}
package com.kit.app;
//#else
//#=package def{sdk.package}.framework.utils;
//#endif

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import java.util.Random;

//#if def{lang} == cn

/**
 * UIHandler对Handler作了封装，在ShareSDK初始化的时候被初始化，
 * 提供全局性的GUI消息支持
 *
 * @author Alex Yu
 */
//#endif
public class UIHandler {
    private static final String[] prays = new String[]{
            "ICDilI/ilJPjgIDjgIDjgIDilI/ilJMK4pSP4pSb4pS74pSB4pSB4pSB4pSb4pS74pSTCuKUg+OAgOOA" +
                    "gOOAgOOAgOOAgOOAgOOAgOKUgwrilIPjgIDjgIDjgIDilIHjgIDjgIDjgIDilIMK4pSD44CA4pSz4pSb" +
                    "44CA4pSX4pSz44CA4pSDCuKUg+OAgOOAgOOAgOOAgOOAgOOAgOOAgOKUgwrilIPjgIDjgIDjgIDilLvj" +
                    "gIDjgIDjgIDilIMK4pSD44CA44CA44CA44CA44CA44CA44CA4pSDCuKUl+KUgeKUk+OAgOOAgOOAgOKU" +
                    "j+KUgeKUmwogICAg4pSD44CA44CA44CA4pSDICAgQ29kZSBpcyBmYXIgYXdheSBmcm9tIGJ1ZyB3aXRo" +
                    "IHRoZSBhbmltYWwgcHJvdGVjdGluZwogICAg4pSD44CA44CA44CA4pSDICAg56We5YW95L+d5L2R77yM" +
                    "5Luj56CB5pegQlVHCiAgICDilIPjgIDjgIDjgIDilJfilIHilIHilIHilJMKICAgIOKUg+OAgOOAgOOA" +
                    "gOOAgOOAgOOAgOOAgOKUo+KUkwogICAg4pSD44CA44CA44CA44CA44CA44CA44CA4pSP4pSbCiAgICDi" +
                    "lJfilJPilJPilI/ilIHilLPilJPilI/ilJsKICAgICAg4pSD4pSr4pSr44CA4pSD4pSr4pSrCiAgICAg" +
                    "IOKUl+KUu+KUm+OAgOKUl+KUu+KUmwo=",
            "44CA4pSP4pST44CA44CA44CA4pSP4pSTCuKUj+KUm+KUu+KUgeKUgeKUgeKUm+KUu+KUkwrilIPjgIDj" +
                    "gIDjgIDjgIDjgIDjgIDjgIDilIMK4pSD44CA44CA44CA4pSB44CA44CA44CA4pSDCuKUg+OAgO+8nuOA" +
                    "gOOAgOOAgO+8nOOAgOKUgwrilIPjgIDjgIDjgIDjgIDjgIDjgIDjgIDilIMK4pSDLi4u44CA4oyS44CA" +
                    "Li4u44CA4pSDCuKUg+OAgOOAgOOAgOOAgOOAgOOAgOOAgOKUgwrilJfilIHilJPjgIDjgIDjgIDilI/i" +
                    "lIHilJsK44CA44CA4pSD44CA44CA44CA4pSDICAgIENvZGUgaXMgZmFyIGF3YXkgZnJvbSBidWcgd2l0" +
                    "aCB0aGUgYW5pbWFsIHByb3RlY3RpbmcK44CA44CA4pSD44CA44CA44CA4pSDICAgIOelnuWFveS/neS9" +
                    "kSzku6PnoIHml6BidWcK44CA44CA4pSD44CA44CA44CA4pSDCuOAgOOAgOKUg+OAgOOAgOOAgOKUgwrj" +
                    "gIDjgIDilIPjgIDjgIDjgIDilIMK44CA44CA4pSD44CA44CA44CA4pSDCuOAgOOAgOKUg+OAgOOAgOOA" +
                    "gOKUl+KUgeKUgeKUgeKUkwrjgIDjgIDilIPjgIDjgIDjgIDjgIDjgIDjgIDjgIDilKPilJMK44CA44CA" +
                    "4pSD44CA44CA44CA44CA44CA44CA44CA4pSP4pSbCuOAgOOAgOKUl+KUk+KUk+KUj+KUgeKUs+KUk+KU" +
                    "j+KUmwrjgIDjgIDjgIDilIPilKvilKvjgIDilIPilKvilKsK44CA44CA44CA4pSX4pS74pSb44CA4pSX" +
                    "4pS74pSbCg==",
            "44CA44CA4pSP4pST44CA44CA44CA4pSP4pSTKyArCuOAgOKUj+KUm+KUu+KUgeKUgeKUgeKUm+KUu+KU" +
                    "kyArICsK44CA4pSD44CA44CA44CA44CA44CA44CA44CA4pSDCuOAgOKUg+OAgOOAgOOAgOKUgeOAgOOA" +
                    "gOOAgOKUgyArKyArICsgKwrjgIDilIPjgIAg4paI4paI4paI4paI4pSB4paI4paI4paI4paIIOKUgysK" +
                    "44CA4pSD44CA44CA44CA44CA44CA44CA44CA4pSDICsK44CA4pSD44CA44CA44CA4pS744CA44CA44CA" +
                    "4pSDCuOAgOKUg+OAgOOAgOOAgOOAgOOAgOOAgOOAgOKUgyArICsgCuOAgOKUl+KUgeKUk+OAgOOAgOOA" +
                    "gOKUj+KUgeKUmwrjgIDjgIDjgIDilIPjgIDjgIDjgIDilIMK44CA44CA44CA4pSD44CA44CA44CA4pSD" +
                    "ICsgKyArICsgCuOAgOOAgOOAgOKUg+OAgOOAgOOAgOKUgyAgICAgICAgICAgICAgICAgQ29kZSBpcyBm" +
                    "YXIgYXdheSBmcm9tIGJ1ZyB3aXRoIHRoZSBhbmltYWwgcHJvdGVjdGluZwrjgIDjgIDjgIDilIPjgIDj" +
                    "gIDjgIDilIMgKyAgICAg56We5YW95L+d5L2RLOS7o+eggeaXoGJ1ZwrjgIDjgIDjgIDilIPjgIDjgIDj" +
                    "gIDilIMK44CA44CA44CA4pSD44CA44CA44CA4pSD44CA44CAKwrjgIDjgIDjgIDilIPjgIAg44CA44CA" +
                    "4pSX4pSB4pSB4pSB4pSTICsgKwrjgIDjgIDjgIDilIMg44CA44CA44CA44CA44CA44CA44CA4pSj4pST" +
                    "IArjgIDjgIDjgIDilIMg44CA44CA44CA44CA44CA44CA44CA4pSP4pSbIArjgIDjgIDjgIDilJfilJPi" +
                    "lJPilI/ilIHilLPilJPilI/ilJsgKyArICsgKwrjgIDjgIDjgIDjgIDilIPilKvilKvjgIDilIPilKvi" +
                    "lKsK44CA44CA44CA44CA4pSX4pS74pSb44CA4pSX4pS74pSbKyArICsgKwo="
    };
    private static Handler handler;

    public static void prepare() {
        if (handler == null) {
            handler = new Handler(new Callback() {
                public boolean handleMessage(Message msg) {
                    UIHandler.handleMessage(msg);
                    return false;
                }
            });
            printPray();
        }
    }

    private static void printPray() {
        try {
            Random rnd = new Random();
            String pray = prays[Math.abs(rnd.nextInt()) % 3];
            byte[] base64 = Base64.decode(pray, Base64.NO_WRAP);
            Log.d("UIHandler printPray == ", "\n" + new String(base64, "utf-8"));
        } catch (Throwable t) {
            Log.w("UIHandler printPray == ", t.getMessage());
        }
    }

    private static void handleMessage(Message msg) {
        Object[] objs = (Object[]) msg.obj;
        Message inner = (Message) objs[0];
        Callback callback = (Callback) objs[1];
        if (callback != null) {
            callback.handleMessage(inner);
        }
    }


    private static Message getMessage(int msgWhat, Callback callback) {
        Message shell = new Message();
        Message inner = new Message();
        inner.what = msgWhat;
        shell.obj = new Object[]{inner, callback};
        return shell;
    }


    private static Message getShellMessage(Message msg, Callback callback) {
        Message shell = new Message();
        shell.obj = new Object[]{msg, callback};
        return shell;
    }


    private static Message getShellMessage(int what, Callback callback) {
        Message msg = new Message();
        msg.what = what;
        return getShellMessage(msg, callback);
    }


    public static boolean sendMessage(Callback callback, Message msg) {
        return handler.sendMessage(getShellMessage(msg, callback));
    }

    public static boolean sendMessage(Callback callback, int msgWhat, Bundle data) {
        Message msg = new Message();
        msg.what = msgWhat;
        msg.setData(data);
        return handler.sendMessage(getShellMessage(msg, callback));
    }

    /**
     * 发送消息
     *
     * @param callback
     * @return
     */
    public static boolean sendMessage(Callback callback, int arg1, int arg2, Object obj, int what, Bundle bundle) {
        Message message = Message.obtain();
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        message.what = what;
        message.setData(bundle);
        return  handler.sendMessage(getShellMessage(message,callback));
    }



    public static boolean sendMessage(Callback callback, int msgWhat) {
        return handler.sendMessage(getMessage(msgWhat, callback));
    }

    public static boolean sendMessageDelayed(Message msg, long delayMillis, Callback callback) {
        return handler.sendMessageDelayed(getShellMessage(msg, callback), delayMillis);
    }

    public static boolean sendMessageAtTime(Message msg, long uptimeMillis, Callback callback) {
        return handler.sendMessageAtTime(getShellMessage(msg, callback), uptimeMillis);
    }

    public static boolean sendMessageAtFrontOfQueue(Message msg, Callback callback) {
        return handler.sendMessageAtFrontOfQueue(getShellMessage(msg, callback));
    }

    public static boolean sendEmptyMessage(int what, Callback callback) {
        return handler.sendMessage(getShellMessage(what, callback));
    }

    public static boolean sendEmptyMessageAtTime(int what, long uptimeMillis, Callback callback) {
        return handler.sendMessageAtTime(getShellMessage(what, callback), uptimeMillis);
    }

    public static boolean sendEmptyMessageDelayed(int what, long delayMillis, Callback callback) {
        return handler.sendMessageDelayed(getShellMessage(what, callback), delayMillis);
    }

}
