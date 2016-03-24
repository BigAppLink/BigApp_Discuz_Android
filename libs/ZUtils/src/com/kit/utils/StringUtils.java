package com.kit.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String trim(String str, String trim) {
        if (isEmptyOrNullOrNullStr(str)) {
            return str;
        }

        if (str.indexOf(trim) == 0) {
            str = str.substring(trim.length(), str.length());
        }

        if (str.lastIndexOf(trim) == str.length() - 1) {
            str = str.substring(0, str.length() - trim.length());
        }
        return str;
    }

    /**
     * 判断一个字符串是否都为数字
     */
    public static boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }

    /**
     * 取出字符串中的数字
     */
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 取出字符串中的非数字
     */
    public static String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 根据正则表达式判断是否包含
     *
     * @param text
     * @param patternStr
     * @return
     */
    public static boolean isMatch(String text, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher m = pattern.matcher(text);
        if (m.find())
            return true;


        return false;

    }


    /**
     * 根据正则表达式判断是否包含
     *
     * @param text
     * @param patternStrings
     * @return
     */
    public static boolean isMatch(String text, String[] patternStrings) {
        for (String patternStr : patternStrings) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher m = pattern.matcher(text);
            if (m.find())
                return true;
        }
        return false;
    }

    /**
     * 查看字符串中包含多少个in
     *
     * @param str
     * @param in
     * @return
     */
    public static int getCountInString(String str, String in) {

        String[] strArray = str.trim().split(in);
        int count = strArray.length - 1;
        if (count < 1) {
            count = 0;
        }
        return count;

    }

    // FIXME 现在不能用
    public static Integer[] getIntArray4String(String str) {
        ArrayList<Integer> intlist = new ArrayList<Integer>();

        String s = "\\d+.\\d+";
        Pattern pattern = Pattern.compile(s);
        Matcher ma = pattern.matcher(str);

        while (ma.find()) {
            System.out.println(ma.group());

        }

        Integer[] intArray = (Integer[]) intlist.toArray();
        return intArray;

    }

    public static String getString(String str, String defaultStr) {

        if (str != null && !TextUtils.isEmpty(str)) {
            return str;

        }
        return defaultStr;

    }


    /**
     * 去除中文
     *
     * @param str
     * @return
     */
    public static String trimChinese(String str) {


        String reg = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(str);
        String repickStr = mat.replaceAll("");
        System.out.println("去中文后:" + repickStr);

        return repickStr;
    }

    /**
     * 去除空格，包含字符串中的空格
     */
    public static String trimBlank(String str) {
        str = str.replace(" ", "");
        return str;
    }


    /**
     * 去除标点符号
     */
    public static String trimPunct(String str) {

        String tempStr = "";
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 97 && chars[i] <= 122) || (chars[i] >= 65 && chars[i] <= 90)) {
//                LogUtils.printLog(StringUtils.class, chars[i] + "");
                tempStr += chars[i];
            }

        }

        return tempStr;
    }


    /**
     * @param
     * @return boolean 返回类型
     * @Title isStringContains
     * @Description str是否包含containsStr
     */
    public static boolean isStartWith(String str, String[] containsStr) {

        boolean isStartWith = false;

        for (String strItem : containsStr) {

            Pattern p = Pattern.compile(strItem + "(.*?)");
            Matcher m = p.matcher(str);

            while (m.find()) {
                String first = m.group(0);
                String second = str.replaceAll(first, "");

                ZogUtils.printLog(StringUtils.class, first
                                + " "
                                + second
                );

                isStartWith = true;
                return isStartWith;
            }

        }

        return isStartWith;

    }


    /**
     * @param
     * @return boolean 返回类型
     * @Title isStringContains
     * @Description str是否包含containsStr
     */
    public static boolean isStringContains(String str, String[] containsStr) {

        boolean isContains = false;

        for (String strItem : containsStr) {

            // LogUtils.printLog(StringUtils.class,
            // "contains:" + str.contains(strItem) + " strItem:" + strItem);

            if (!StringUtils.isNullOrEmpty(str) && str.contains(strItem)) {
                isContains = true;
                return isContains;
            }

        }

        return isContains;

    }

    /**
     * @param
     * @return boolean 返回类型
     * @Title isEmpty
     * @Description 字符串是否为空字符串
     */
    public static boolean isEmpty(String str) {

        if (TextUtils.isEmpty(str)) {
            return true;

        }
        return false;

    }

//    public static String toDecimal3(double d) {
//        if (d == 0) {
//            return "0.000";
//        }
//        DecimalFormat f = new DecimalFormat(",###0.000");
//        String s = f.format(d);
//
//
//        return s;
//    }

    /**
     * 把double转化为精度为scale的字符串
     *
     * @param d
     * @param scale 精度
     * @return
     */
    public static String toDecimal(double d, int scale) {

        String format = ",###0.";
        if (scale > 0) {
            for (int i = 0; i < scale; i++) {
                format = format + "0";
            }
        } else {
            return (Integer.parseInt(d + "") + "");
        }
        DecimalFormat f = new DecimalFormat(format);
        String s = f.format(d);
        return s;
    }


    //    /**
//     * @param
//     * @return boolean 返回类型
//     * @Description 字符串是否为null或空字符串
//     */
    public static boolean isNullOrEmpty(String str) {


        if (str == null || TextUtils.isEmpty(str)) {
            return true;

        }
        return false;

    }

    /**
     * @param
     * @return boolean 返回类型
     * @Title isEmptyOrNullOrNullStr
     * @Description 字符串是否为null或空字符或为“null”
     */
    public static boolean isEmptyOrNullOrNullStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        } else {
            str = str.trim();
            if (str.equals("null")
                    || str.equals("\"null\"")
                    || str.equals("”null“")
                    || str.equals("'null'")
                    || str.equals("’null‘")) {
                return true;
            }
        }
        return false;

    }

    public static String toDecimal(double d) {
        if (d == 0) {
            return "0.00";
        }
        DecimalFormat f = new DecimalFormat(",###0.00");
        String s = f.format(d);

        return s;
    }

    public static String getStrWith0(String s) {
        if (Integer.parseInt(s) < 10 && Integer.parseInt(s) > 0) {
            return "0" + s;
        }
        return s;
    }

    public static ArrayList<Integer> getIntFormStr(String str) {
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(str);
        String result = m.replaceAll("");
        ArrayList<Integer> digitList = new ArrayList<Integer>();
        for (int i = 0; i < result.length(); i++) {
            digitList.add(Integer.parseInt(result.substring(i, i + 1)));

        }
        return digitList;
    }

    // 过滤特殊字符
    public static String filter(String str) {
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 中文乱码,获取正确的字符
     *
     * @return
     */
    public static String getChineseString(String str) {
        String resultStr = str;

        String UTF_Str = "";
        String GB_Str = "";
        // boolean isMessyCode = false;

        boolean isISO = false;

        try {

            isISO = isCharset(resultStr, "ISO-8859-1");

            ZogUtils.printLog(StringUtils.class, resultStr + " isISO:" + isISO);
            if (isISO) {
                try {

                    if (isGBK(resultStr)) {
                        GB_Str = new String(resultStr.getBytes("ISO-8859-1"),
                                "GBK");
                        ZogUtils.printLog(StringUtils.class, "GBK：" + GB_Str);
                    } else if (isCharset(resultStr, "UTF-8")) {
                        UTF_Str = new String(resultStr.getBytes("ISO-8859-1"),
                                "UTF-8");
                        ZogUtils.printLog(StringUtils.class, "UTF-8：" + UTF_Str);
                    }
                } catch (UnsupportedEncodingException e) {
                    ZogUtils.showException(e);
                }

            }
        } catch (Exception e) {
            ZogUtils.showException(e);
        }

        return resultStr;
    }

    /**
     * 判断是否为汉字
     *
     * @param str
     * @return
     */
    public static boolean isGBK(String str) {
        char[] chars = str.toCharArray();
        boolean isGBK = false;
        for (int i = 0; i < chars.length; i++) {
            byte[] bytes = ("" + chars[i]).getBytes();
            if (bytes.length == 2) {
                int[] ints = new int[2];
                ints[0] = bytes[0] & 0xff;
                ints[1] = bytes[1] & 0xff;
                if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
                        && ints[1] <= 0xFE) {
                    isGBK = true;
                    break;
                }
            }
        }
        return isGBK;
    }

    /**
     * 判断是否为乱码
     *
     * @param str
     * @return
     */
    public static boolean isMessyCode(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
            // 从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
            // System.out.println("--- " + (int) c);
            if ((int) c == 0xfffd) {
                // 存在乱码
                // System.out.println("存在乱码 " + (int) c);
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否为双整型数字
     *
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile("-*\\d*.\\d*");
        // Pattern p = Pattern.compile("-*"+"\\d*"+"."+"\\d*");
        return p.matcher(str).matches();
    }

    /**
     * 判断字符串是否为整字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile("-*\\d*");
        return p.matcher(str).matches();
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isCharset(String str, String charsetName) {
        return Charset.forName(charsetName).newEncoder().canEncode(str);
    }


    public static String replaceUrlValueReg(String url, String name, String value) {
        if (!StringUtils.isEmptyOrNullOrNullStr(url) && !StringUtils.isEmptyOrNullOrNullStr(value)) {
//            url = url.replaceAll( name +"=[^&]*", name + "=" + value);
            url = url.replaceAll("(" + name + "=[^&]*)", name + "=" + value);
        }
        return url;
    }


}
