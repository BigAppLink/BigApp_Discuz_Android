package com.kit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

/**
 * Created by Zhao on 15/10/22.
 */
public class BrowserUtils {

    public static void gotoBrowser(Activity context, String url) {
        Uri uri = Uri.parse(url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        //下面这句会让用户自行选择浏览器打开
        browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        context.startActivity(browserIntent);
    }

    /**
     * 值为true时，使用传统方法让用户选择。<br/>
     * 值为false时，程序自动为用户选定浏览器浏览。<br/>
     * 目前支持且优先级从高到低为：<br/>
     * 1.UC浏览器<br/>
     * 2.Opera浏览器<br/>
     * 3.QQ浏览器<br/>
     * 4.Dolphin Browser(不支持WAP)<br/>
     * 5.Skyfire Browser(不支持WAP)<br/>
     * 6.Steel Browser(不支持WAP)<br/>
     * 7.系统浏览器<br/>
     * 验证是否支持直接启动访问指定页面的方法为：<br/>
     * 执行下面的<code>doDefault()</code>时会出现如下选择框，<br/>
     * 选择浏览器，如果能够正常访问，可以指定该浏览器访问指定网页；<br/>
     * 如果该浏览器启动后没有跳转到指定网页，则不支持。<br/>
     * 实践中，Go浏览器与天天浏览器并不支持。<br/>
     * <img src="../../../../choice.png" mce_src="choice.png"/> <br/>
     * 正确显示图片请在不改变包名的前提下将choice.png放于工程目录下，与src、res同级。<br/>
     */

    public static void choiceBrowserToVisitUrl(Context context, String url) {
        boolean existUC = false, existOpera = false, existQQ = false, existDolphin = false, existSkyfire = false, existSteel = false, existGoogle = false;
        String ucPath = "", operaPath = "", qqPath = "", dolphinPath = "", skyfirePath = "", steelPath = "", googlePath = "";
        PackageManager packageMgr = context.getPackageManager();
        List<PackageInfo> list = packageMgr.getInstalledPackages(0);
        for (int i = 0; i < list.size(); i++) {
            PackageInfo info = list.get(i);
            String temp = info.packageName;
            if (temp.equals("com.uc.browser")) {
                // 存在UC
                ucPath = temp;
                existUC = true;
            } else if (temp.equals("com.tencent.mtt")) {
                // 存在QQ
                qqPath = temp;
                existQQ = true;
            } else if (temp.equals("com.opera.mini.android")) {
                // 存在Opera
                operaPath = temp;
                existOpera = true;
            } else if (temp.equals("mobi.mgeek.TunnyBrowser")) {
                dolphinPath = temp;
                existDolphin = true;
            } else if (temp.equals("com.skyfire.browser")) {
                skyfirePath = temp;
                existSkyfire = true;
            } else if (temp.equals("com.kolbysoft.steel")) {
                steelPath = temp;
                existSteel = true;
            } else if (temp.equals("com.android.browser")) {
                // 存在GoogleBroser
                googlePath = temp;
                existGoogle = true;
            }
        }
        if (existUC) {
            gotoUrl(context, ucPath, url, packageMgr);
        } else if (existOpera) {
            gotoUrl(context, operaPath, url, packageMgr);
        } else if (existQQ) {
            gotoUrl(context, qqPath, url, packageMgr);
        } else if (existDolphin) {
            gotoUrl(context, dolphinPath, url, packageMgr);
        } else if (existSkyfire) {
            gotoUrl(context, skyfirePath, url, packageMgr);
        } else if (existSteel) {
            gotoUrl(context, steelPath, url, packageMgr);
        } else if (existGoogle) {
            gotoUrl(context, googlePath, url, packageMgr);
        } else {
            doDefault(context, url);
        }
    }

    private static void gotoUrl(Context context, String packageName, String url,
                                PackageManager packageMgr) {
        try {
            Intent intent;
            intent = packageMgr.getLaunchIntentForPackage(packageName);
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            // 在1.5及以前版本会要求catch(android.content.pm.PackageManager.NameNotFoundException)异常，该异常在1.5以后版本已取消。
            e.printStackTrace();
        }
    }

    private static void doDefault(Context context, String visitUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(visitUrl));
//        加上下面这一句会让选择浏览器打开
//        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        context.startActivity(intent);
    }

    /**
     * 直接启动UC，用于验证测试。
     */
    public static void showUCBrowser(Context context, String visitUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.uc.browser", "com.uc.browser.ActivityUpdate");
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
    }

    /**
     * 直接启动QQ，用于验证测试。
     */
    public static void showQQBrowser(Context context, String visitUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.tencent.mtt", "com.tencent.mtt.MainActivity");
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
    }

    /**
     * 直接启动Opera，用于验证测试。
     */
    public static void showOperaBrowser(Context context, String visitUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.opera.mini.android",
                "com.opera.mini.android.Browser");
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
    }

    /**
     * 直接启动Dolphin Browser，用于验证测试。
     */
    public static void showDolphinBrowser(Context context, String visitUrl) {
        // 方法一：
        // Intent intent = new Intent();
        // intent.setClassName("mobi.mgeek.TunnyBrowser",
        // "mobi.mgeek.TunnyBrowser.BrowserActivity");
        // intent.setAction(Intent.ACTION_VIEW);
        // intent.addCategory(Intent.CATEGORY_DEFAULT);
        // intent.setData(Uri.parse(visitUrl));
        // startActivity(intent);
        // 方法二：
        gotoUrl(context, "mobi.mgeek.TunnyBrowser", visitUrl, ((Activity) context).getPackageManager());
    }

    /**
     * 直接启动Skyfire Browser，用于验证测试。
     */
    public static void showSkyfireBrowser(Context context, String visitUrl) {
        // 方法一：
        Intent intent = new Intent();
        intent.setClassName("com.skyfire.browser",
                "com.skyfire.browser.core.Main");
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
        // 方法二：
        // gotoUrl("com.skyfire.browser", visitUrl, getPackageManager());
    }

    /**
     * 直接启动Steel Browser，用于验证测试。
     */
    public static void showSteelBrowser(Context context, String visitUrl) {
        // 方法一：
        // Intent intent = new Intent();
        // intent.setClassName("com.kolbysoft.steel",
        // "com.kolbysoft.steel.Steel");
        // intent.setAction(Intent.ACTION_VIEW);
        // intent.addCategory(Intent.CATEGORY_DEFAULT);
        // intent.setData(Uri.parse(visitUrl));
        // startActivity(intent);
        // 方法二：
        gotoUrl(context, "com.kolbysoft.steel", visitUrl, context.getPackageManager());
    }
}
