package com.kit.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {
    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符

    /**
     * @param htmlStr
     * @return 删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {

        if (htmlStr == null) {
            return null;
        }
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        if (p_script != null) {
            Matcher m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
        }


        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        if (p_style != null) {
            Matcher m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
        }

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        if (p_html != null) {
            Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
        }

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        if (p_space != null) {
            Matcher m_space = p_space.matcher(htmlStr);
            htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        }
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String getTextFromHtml(String htmlStr) {
        if (htmlStr == null)
            return null;

        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
        return htmlStr;
    }

    public static String[] getActUfielddata(String htmlStr) {
        if (htmlStr == null) {
            return null;
        }
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        if (p_html != null) {
            Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll("w~w~w"); // 过滤html标签
        }

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        if (p_space != null) {
            Matcher m_space = p_space.matcher(htmlStr);
            htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        }
        return htmlStr.trim().split("w~w~w"); // 返回文本字符串
    }

    public static void main(String[] args) {
        String str = "<div style='text-align:center;'>&nbsp;整治“四风”&nbsp;&nbsp;&nbsp;清弊除垢<br/><span style='font-size:14px;'>&nbsp;</span><span style='font-size:18px;'>公司召开党的群众路线教育实践活动动员大会</span><br/></div>";
        System.out.println(getTextFromHtml(str));
    }
}
