package com.kit.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.MovementMethod;
import android.text.style.BackgroundColorSpan;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.app.core.style.ClickableSpanExtend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhao on 14-9-18.
 */
public class TextViewUtils {

    public static void setMargin(TextView textView, int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(left, top, right, bottom);

        textView.setLayoutParams(lp);
    }


    /**
     * 变换字符串中符合pattern字体颜色并展示
     *
     * @param textView
     * @param text
     * @param patternStrings
     * @param highlightColor
     */
    public static void setSomeTextChangeColor(TextView textView, String text,
                                              String[] patternStrings, int normalColor, int highlightColor, int highlightClickBgColor, int backgoundColor,
                                              boolean isChangeBackground, ClickableSpanExtend.SetClickableSpan clickableSpan, MovementMethod movementMethod) {


        SpannableStringBuilder builder = new SpannableStringBuilder(text);
//

        //设置一个全局的文本监听，防止点击文本没有反应。
//        builder.setSpan(new ForegroundColorSpan(normalColor), 0, text.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new ClickableSpanExtend(text,text, clickableSpan,color,normalColor), 0, text.length() -1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        for (String patternStr : patternStrings) {
            if (StringUtils.isNullOrEmpty(patternStr)) {
                return;
            }
            Pattern pattern = Pattern.compile(patternStr);
            Matcher m = pattern.matcher(text);

            int start = 0;
            int end = 0;
            String findText;

            while (m.find()) {
                start = m.start();
                end = m.end();
                findText = text.substring(start, end);
//                System.out.println(start + "");
//                System.out.println(end + "");
//                System.out.println(text.substring(start, end));

                if (isChangeBackground) {
                    if (backgoundColor < 1) {
                        //求反色
                        backgoundColor = 0xFF - highlightColor;
                    }

                    builder.setSpan(new BackgroundColorSpan(backgoundColor), start, end,
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);     //设置指定位置textview的背景颜色
                }

//                builder.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (clickableSpan != null)
                    builder.setSpan(new ClickableSpanExtend(text, findText, clickableSpan,
                                    normalColor, highlightColor, highlightClickBgColor), start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        if (movementMethod != null) {
//            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setMovementMethod(movementMethod);
        }


        textView.setText(builder);
//        textView.setClickable(false);
//        textView.setFocusable(false);

//
//        SpannableStringBuilder builder2 = new SpannableStringBuilder(text);
//
//        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
//        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
//        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Color.WHITE);
//        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.BLUE);
//        ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.GREEN);
//        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(Color.YELLOW);
//
//        builder2.setSpan(redSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder2.setSpan(whiteSpan, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        builder2.setSpan(blueSpan, 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder2.setSpan(greenSpan, 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder2.setSpan(yellowSpan, 4,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        textView.setText(builder2);
    }


}
