package com.youzu.clan.base.util;

import android.text.TextUtils;
import android.util.Log;

import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhao on 15/6/12.
 */
public class EmojiUtils {

    public static HashMap<String, String> emojiTages = new HashMap<>();
    public static HashMap<String, String> emojiFace = new HashMap<>();
    public static HashMap<String, String> emojiFaceTag = new HashMap<>();

    /**
     * 将emoji表情替换成*
     *
     * @param source
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source) {
        if(StringUtils.isEmptyOrNullOrNullStr(source)){
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
        }else{
            return source;
        }
    }



    public static void init() {
        emojiTages = new HashMap<>();

        emojiTages.put("smile", ":)");
        emojiTages.put("sad", ":(");
        emojiTages.put("biggrin", ":D");
        emojiTages.put("cry", ":'(");
        emojiTages.put("huffy", ":@");
        emojiTages.put("shocked", ":o");
        emojiTages.put("tongue", ":P");
        emojiTages.put("shy", ":$");
        emojiTages.put("titter", ";P");
        emojiTages.put("sweat", ":L");
        emojiTages.put("mad", ":Q");
        emojiTages.put("lol", ":lol");
        emojiTages.put("loveliness", ":loveliness:");
        emojiTages.put("funk", ":funk:");
        emojiTages.put("curse", ":curse:");
        emojiTages.put("dizzy", ":dizzy:");
        emojiTages.put("shutup", ":shutup:");
        emojiTages.put("sleepy", ":sleepy:");
        emojiTages.put("hug", ":hug:");
        emojiTages.put("victory", ":victory:");
        emojiTages.put("time", ":time:");
        emojiTages.put("kiss", ":kiss:");
        emojiTages.put("handshake", ":handshake:");
        emojiTages.put("call", ":call:");


        emojiFace = new HashMap<>();

        emojiFace.put(":)", "😌");
        emojiFace.put(":(", "😔");
        emojiFace.put(":D", "😃");
        emojiFace.put(":'(", "😭");
        emojiFace.put(":@", "😠");
        emojiFace.put(":o", "😲");
        emojiFace.put(":P", "😜");
        emojiFace.put(":$", "😆");
        emojiFace.put(";P", "😝");
        emojiFace.put(":L", "😓");
        emojiFace.put(":Q", "😫");
        emojiFace.put(":lol", "😁");
        emojiFace.put(":loveliness:", "😊");
        emojiFace.put(":funk:", "😱");
        emojiFace.put(":curse:", "😤");
        emojiFace.put(":dizzy:", "😖");
        emojiFace.put(":shutup:", "😷");
        emojiFace.put(":sleepy:", "😪");
        emojiFace.put(":hug:", "😚");
        emojiFace.put(":victory:", "✌");
        emojiFace.put(":time:", "⏰");
        emojiFace.put(":kiss:", "👄");
        emojiFace.put(":handshake:", "👌");
        emojiFace.put(":call:", "📞");

        emojiFaceTag = new HashMap<>();

        emojiFaceTag.put("😌", ":)");
        emojiFaceTag.put("😔", ":(");
        emojiFaceTag.put("😃", ":D");
        emojiFaceTag.put("😭", ":'(");
        emojiFaceTag.put("😠", ":@");
        emojiFaceTag.put("😲", ":o");
        emojiFaceTag.put("😜", ":P");
        emojiFaceTag.put("😆", ":$");
        emojiFaceTag.put("😝", ";P");
        emojiFaceTag.put("😓", ":L");
        emojiFaceTag.put("😫", ":Q");
        emojiFaceTag.put("😁", ":lol");
        emojiFaceTag.put("😊", ":loveliness:");
        emojiFaceTag.put("😱", ":funk:");
        emojiFaceTag.put("😤", ":curse:");
        emojiFaceTag.put("😖", ":dizzy:");
        emojiFaceTag.put("😷", ":shutup:");
        emojiFaceTag.put("😪", ":sleepy:");
        emojiFaceTag.put("😚", ":hug:");
        emojiFaceTag.put("✌", ":victory:");
        emojiFaceTag.put("⏰", ":time:");
        emojiFaceTag.put("👄", ":kiss:");
        emojiFaceTag.put("👌", ":handshake:");
        emojiFaceTag.put("📞", ":call:");

    }

    public static String face2Tag(String str) {
        try {
            for (Map.Entry<String, String> entry : emojiFaceTag.entrySet()) {

                String key = entry.getKey();
                String value = entry.getValue();

                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

                str = str.replace(key, value);
            }
        } catch (Exception e) {
            Log.e("APP", e.getMessage());

        }

        Log.e("APP", "str:" + str);
        return str;

    }

    public static String tag2Face(String str) {
        try {
            for (Map.Entry<String, String> entry : emojiFace.entrySet()) {

                String key = entry.getKey();
                String value = entry.getValue();

                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

                str = str.replace(key, value);
            }
        } catch (Exception e) {
            Log.e("APP", e.getMessage());

        }

        Log.e("APP", "str:" + str);
        return str;

    }

    public static String getQuoteStr(String str) {

        if (StringUtils.isEmptyOrNullOrNullStr(str)) {
            return str;
        }

        init();

        String img = "";
        Pattern p_image;
        Matcher m_image;

        String regEx_img = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);//<img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");
        m_image = p_image.matcher(str);

        try {
            while (m_image.find()) {

//            for(int i = 0;i<m_image.groupCount();i++){

//                str = m_image.
                img = m_image.group();
                String key = null;
                if (img.lastIndexOf(".") > 0 && img.lastIndexOf("=") > 0) {
                    key = img.substring(img.lastIndexOf("=") + 1, img.lastIndexOf("."));
                }
                Log.e("APP", "img:" + img);
                Log.e("APP", "key:" + key);
                if (key == null)
                    continue;

                String imgTemped = emojiTages.get(key);
                Log.e("APP", "imgTemped:" + imgTemped);

                if (imgTemped != null && !TextUtils.isEmpty(imgTemped)) {
                    str = str.replace(img, imgTemped);
                }
            }

        } catch (Exception e) {
            ZogUtils.showException(e);
            return str;
        }

        Log.e("APP", "getQuoteStr() str:" + str);
        return str;
    }


    private String[] getImgs(String content) {

        String img = "";
        Pattern p_image;
        Matcher m_image;
        String str = "";

        String[] images = null;

        String regEx_img = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";


        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);//<img[^<>]*src=[\'\"]([0-9A-Za-z.\\/]*)[\'\"].(.*?)>");


        m_image = p_image.matcher(content);


        while (m_image.find()) {


            img = m_image.group();


//            Matcher m = Pattern.compile("src\\s*=\\s*['\"]([^'\"]+)['\"]").matcher(img);
//
//
//            while (m.find()) {
//
//
//                String tempSelected = m.group(1);
//
//
//                if ("".equals(str)) {
//
//
//                    str = tempSelected;
//
//
//                } else {
//                    String temp = tempSelected;
//                    str = str + "," + temp;
//                }
//            }
        }


        if (!"".equals(str)) {
            images = str.split(",");
        }
        return images;

    }


}
