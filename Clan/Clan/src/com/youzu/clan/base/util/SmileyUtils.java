package com.youzu.clan.base.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import com.keyboard.XhsEmoticonsKeyBoardBar;
import com.keyboard.bean.EmoticonBean;
import com.keyboard.bean.EmoticonSetBean;
import com.keyboard.db.EmojiDb;
import com.keyboard.utils.EmoticonsController;
import com.keyboard.utils.Utils;
import com.keyboard.utils.imageloader.ImageBase;
import com.keyboard.utils.imageloader.ImageLoader;
import com.keyboard.view.VerticalImageSpan;
import com.kit.config.AppConfig;
import com.kit.utils.FileUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.base.json.smiley.SmileyItem;
import com.youzu.clan.base.json.smiley.SmiliesItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhao on 15/8/12.
 */
public class SmileyUtils {


    /**
     * 初始化表情数据库
     *
     * @param context
     */
    public static void initEmoticonsDB(final Context context, final ArrayList<SmiliesItem> smilies) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                EmojiDb.clear(context);
//                ZogUtils.printError(SmileyUtils.class, "initEmoticonsDB smilies.size():" + smilies.size());

                ArrayList<ArrayList<EmoticonBean>> emojiLibrary = parseEmojiLibrary(context, smilies, EmoticonBean.FACE_TYPE_NOMAL, ImageBase.Scheme.FILE);

                for (int i = 0; i < smilies.size(); i++) {

                    SmiliesItem sm = smilies.get(i);
                    String name = getName(sm);
//                    name = sm.getDirectory();
                    ZogUtils.printError(SmileyUtils.class, "smiley name:" + name);

                    /**
                     * bar上面的标识图标
                     */
                    String simpleFileName = getSmileyFileName(context, ImageBase.Scheme.FILE, sm.getDirectory(), sm.getSmileyItems().get(0));

                    EmoticonSetBean emojiEmoticonSetBean = new EmoticonSetBean(name, 3, 7);
                    emojiEmoticonSetBean.setIconUri(simpleFileName);
                    emojiEmoticonSetBean.setDescription(sm.getName());
                    emojiEmoticonSetBean.setIconName(sm.getSmileyItems().get(0).getUrl());
                    emojiEmoticonSetBean.setItemPadding(20);
                    emojiEmoticonSetBean.setVerticalSpacing(10);
                    emojiEmoticonSetBean.setShowDelBtn(true);

//                    ZogUtils.printError(SmileyUtils.class, "emojiLibrary.get(i).size():" + emojiLibrary.get(i).size());

                    emojiEmoticonSetBean.setEmoticonList(emojiLibrary.get(i));
                    EmojiDb.addEmojiSet(context, emojiEmoticonSetBean);
                }


                Utils.setIsInitDb(context, true);

            }
        }).start();


    }

    public static SpannableStringBuilder replaceSmileyCode2Smiley(Context context, String input) {
        if (TextUtils.isEmpty(input)) {
            return new SpannableStringBuilder("");
        }
        Pattern pattern = Pattern.compile("ddddd", Pattern.UNICODE_CASE
                | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        SpannableStringBuilder ssb = new SpannableStringBuilder(input);
        while (matcher.find()) {
            String found = matcher.group();
            EmoticonBean bean = EmojiDb.getEmojiByUnicode(context, found);
            if (bean != null) {
                Drawable drawable = ImageLoader.getInstance(context).getDrawable(bean.getIconUri());
                if (drawable != null) {
                    int itemHeight = drawable.getIntrinsicHeight();
                    int itemWidth = drawable.getIntrinsicWidth();

                    drawable.setBounds(0, 0, itemHeight, itemWidth);
                    VerticalImageSpan imageSpan = new VerticalImageSpan(drawable);
                    ssb.setSpan(imageSpan, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }

        }
        return ssb;
    }


    private static ArrayList<EmoticonBean> parseEmoji(Context context, ArrayList<SmileyItem> smileyItems, String dir, long eventType, ImageBase.Scheme scheme) {
        ArrayList<EmoticonBean> emojis = new ArrayList<EmoticonBean>();
        for (int i = 0; i < smileyItems.size(); i++) {
            SmileyItem smileyItem = smileyItems.get(i);
            String fileName = getSmileyFileName(context, scheme, dir, smileyItem);

            ZogUtils.printError(SmileyUtils.class, "fileName=" + fileName);

            EmoticonBean bean = new EmoticonBean(eventType, fileName, smileyItem.getCode());
            bean.setShortName(smileyItem.getId());
            emojis.add(bean);
            //file:///data/data/com.youzu.clan/files/emoji/default/sad.gif
            //file:///data/data/com.youzu.clan/files/emoji/coolmonkey/11.gif
        }
        return emojis;
    }

    private static ArrayList<ArrayList<EmoticonBean>> parseEmojiLibrary(Context context, ArrayList<SmiliesItem> smiliesItems, long eventType, ImageBase.Scheme scheme) {
        ArrayList<ArrayList<EmoticonBean>> emojiLibrary = new ArrayList<ArrayList<EmoticonBean>>();

        for (SmiliesItem smiliesItem : smiliesItems) {
            ArrayList<EmoticonBean> emojis = new ArrayList<EmoticonBean>();

            String dir = smiliesItem.getDirectory();
            for (SmileyItem smileyItem : smiliesItem.getSmileyItems()) {
                String fileName = getSmileyFileName(context, scheme, dir, smileyItem);
                ZogUtils.printError(SmileyUtils.class, "fileName=" + fileName);
                EmoticonBean bean = new EmoticonBean(eventType, fileName, smileyItem.getCode());
                bean.setShortName(smileyItem.getUrl());

//                "file://" + SmileyUtils.getUnZipAlrightSmileyFilePath(context) +
                bean.setIconUri(fileName);
                bean.setGroupName(dir);

                emojis.add(bean);
            }
            emojiLibrary.add(emojis);
        }
        return emojiLibrary;
    }

    /**
     * @param context
     * @param xhsEmoticonsKeyBoardBar 只有当使用的是表情键盘的时候才要传递这个参数，popwindow类型的不传
     * @return
     */
    public static EmoticonsController getController(Context context, XhsEmoticonsKeyBoardBar xhsEmoticonsKeyBoardBar) {
        List<EmoticonSetBean> mEmoticonSetBeanList = new ArrayList<>();
        List<EmoticonSetBean> dbEmoticonSetBeanList = EmojiDb.getAllEmojiSet(context);

        EmoticonsController controller = new EmoticonsController();

        if (!ListUtils.isNullOrContainEmpty(dbEmoticonSetBeanList)) {
            for (EmoticonSetBean emoticonSetBean : dbEmoticonSetBeanList) {

                ZogUtils.printError(SmileyUtils.class, "emoticonSetBean.getName():" + emoticonSetBean.getName());

                List<EmoticonSetBean> smiley = EmojiDb.getEmojiLibraryByGroup(context, emoticonSetBean.getName(), emoticonSetBean.getName());
                mEmoticonSetBeanList.addAll(smiley);
            }
        }
        controller.setEmoticonSetBeanList(mEmoticonSetBeanList);
        if (xhsEmoticonsKeyBoardBar != null) {
            controller.setXhsEmoticonsKeyBoardBar(xhsEmoticonsKeyBoardBar);
        }


        return controller;
    }


    public static String getUnZipSmileyFilePath(final Context context) {
        return AppConfig.CACHE_DATA_DIR;
    }

    public static String getSmileyZipFilePath(final Context context) {
        return AppConfig.CACHE_DATA_DIR + "zip/smiley.zip";
    }


    /**
     * 解压缩之后的目录路径
     *
     * @param context
     * @return
     */
    public static String getUnZipAlrightSmileyFilePath(final Context context) {
        return AppConfig.CACHE_DATA_DIR + "smiley/";
    }

    public static String getSmileyFileName(Context context, ImageBase.Scheme scheme, String dir, SmileyItem smileyItem) {
        return scheme.toUri(getUnZipAlrightSmileyFilePath(context) + dir + "/" + smileyItem.getUrl());
    }

    public static void distory(Context context) {
        EmojiDb.clear(context);
        FileUtils.deleteFile(getSmileyZipFilePath(context));
        FileUtils.deleteFile(getUnZipAlrightSmileyFilePath(context));
    }

    public static String getName(SmiliesItem smiliesItem) {
        String name = smiliesItem.getName();
        if (StringUtils.isEmptyOrNullOrNullStr(name)) {
            name = smiliesItem.getDirectory();
        }
        name = smiliesItem.getDirectory();

        return name;
    }
}
