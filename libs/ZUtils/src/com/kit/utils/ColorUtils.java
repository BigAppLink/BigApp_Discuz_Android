package com.kit.utils;


import android.graphics.Color;

public class ColorUtils {

    public static String toHex(int a, int r, int g, int b) {
        return toBrowserHexValue(a) + toBrowserHexValue(r)
                + toBrowserHexValue(g) + toBrowserHexValue(b);
    }

    public static int toColor(int r, int g, int b) {

        return Color.rgb(r, g, b);

    }

    public static String toBrowserHexValue(int number) {
        StringBuilder builder = new StringBuilder(
                Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }

    /**
     * 转化成浏览器中常用的，形如 #000000 这样的字符串
     *
     * @param color
     * @return
     */
    public static String toBrowserColor(int color) {
        int red = getRed(color);
        int green = getGreen(color);
        int blue = getBlue(color);

        String redStr = ((red + "").length() < 2 ? red + "0" : red + "");
        String greenStr = ((green + "").length() < 2 ? green + "0" : green + "");
        String blueStr = ((blue + "").length() < 2 ? blue + "0" : blue + "");

        return "#" + redStr + blueStr + greenStr;
    }

    /**
     * 转化成浏览器中常用的，形如 #000000 这样的字符串
     *
     * @param color
     * @return
     */
    public static int getRed(int color) {
        int red = (color & 0xff0000) >> 16;
        return red;
    }

    /**
     * 转化成浏览器中常用的，形如 #000000 这样的字符串
     *
     * @param color
     * @return
     */
    public static int getGreen(int color) {
        int green = (color & 0x00ff00) >> 8;
        return green;
    }

    /**
     * 转化成浏览器中常用的，形如 #000000 这样的字符串
     *
     * @param color
     * @return
     */
    public static int getBlue(int color) {
        int blue = (color & 0x0000ff);
        return blue;
    }


    /**
     * 将纯色转化为透明色
     * @param color
     * @param alpha
     * @return
     */
    public static int toAlpha(int color, int alpha) {
        int r = getRed(color);
        int g = getGreen(color);
        int b = getBlue(color);
        return Color.argb(alpha, r, g, b);
    }


}