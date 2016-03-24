package com.kit.widget.textview.weibotextview;

import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.kit.app.core.style.ClickableSpanExtend;
import com.kit.app.core.style.ImageSpanExtend;
import com.kit.app.drawable.gif.GifDrawableExtend;
import com.kit.utils.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiboTextView extends TextView implements GifDrawableExtend.UpdateListener {
    private boolean dontConsumeNonUrlClicks = true;
    private boolean linkHit;

    private String text;

    private int normalColor;
    private int highlightTextColor;
    private int clickBgClolr;
    private int highlightBgColor;

    private SpannableStringBuilder builder;

    public LocalLinkMovementMethod localLinkMovementMethod;


    public WeiboTextView(Context context) {
        super(context);
        init();

    }

    public WeiboTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeiboTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        localLinkMovementMethod = new LocalLinkMovementMethod(this);
        //点击高亮背景颜色
        setHighlightColor(0x0000000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        linkHit = false;
        boolean res = super.onTouchEvent(event);

        if (dontConsumeNonUrlClicks)
            return linkHit;
        return res;
    }

    public void setTextViewHTML(String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        setText(strBuilder);
    }

    public static class LocalLinkMovementMethod extends LinkMovementMethod {
        static LocalLinkMovementMethod sInstance;
        //        public boolean isPressed;
        public WeiboTextView weiboTextView;


//        public static LocalLinkMovementMethod getInstance() {
//
//            if (sInstance == null)
//                sInstance = new LocalLinkMovementMethod();
//
//            return sInstance;
//        }

        public LocalLinkMovementMethod(WeiboTextView weiboTextView) {
            this.weiboTextView = weiboTextView;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            int action = event.getAction();

//            LogUtils.printLog(WeiboTextView.class, "MotionEvent:" + action);

//            if (action == MotionEvent.ACTION_MOVE) {
//                isPressed = false;
//            }
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;

                case MotionEvent.ACTION_CANCEL:
                    buffer.setSpan(new BackgroundColorSpan(0x00000000), 0, weiboTextView.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Selection.setSelection(buffer, 0, weiboTextView.length());
                    break;
            }


            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {

                    int start = buffer.getSpanStart(link[0]);
                    int end = buffer.getSpanEnd(link[0]);

//                    LogUtils.printLog(WebViewUtils.class, "start:" + start + " end:" + end);

                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                        buffer.setSpan(new BackgroundColorSpan(0x00000000), start, end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Selection.setSelection(buffer, start, end);
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        buffer.setSpan(new BackgroundColorSpan(weiboTextView.clickBgClolr), start, end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Selection.setSelection(buffer, start, end);
                    }

                    if (widget instanceof WeiboTextView) {
                        ((WeiboTextView) widget).linkHit = true;
                    }
                    return true;
                } else {
                    Selection.removeSelection(buffer);
                    Touch.onTouchEvent(widget, buffer, event);
                    return false;
                }
            }


            return Touch.onTouchEvent(widget, buffer, event);
        }
    }

    /**
     * 变换字符串中符合pattern字体颜色并展示
     *
     * @param text               所有文本
     * @param patternStrings     高亮的正则表达式
     * @param normalColor        普通文字颜色
     * @param highlightTextColor 高亮颜色
     * @param highlightBgColor   高亮文字背景颜色
     * @param isUseBackground    高亮文字是否使用背景颜色
     * @param setClickableSpan   点击时间
     */
    public void setText(Context context, String text,
                        String[] patternStrings, List emotions, int normalColor, int highlightTextColor, int clickBgClolr, int highlightBgColor,
                        boolean isUseBackground, ClickableSpanExtend.SetClickableSpan setClickableSpan, ImageSpanExtend.SetImageSpan setImageSpan,
                        boolean isUseGifAnim) {

        this.text = text;

        this.normalColor = normalColor;
        this.highlightTextColor = highlightTextColor;
        this.clickBgClolr = clickBgClolr;
        this.highlightBgColor = highlightBgColor;

        //设置一个全局的文本监听，防止点击文本没有反应。
//        builder.setSpan(new ForegroundColorSpan(normalColor), 0, text.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new ClickableSpanExtend(text,text, clickableSpan,color,normalColor), 0, text.length() -1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        if (highlightBgColor < 1) {
//            //求反色
//            highlightBgColor = 0xFF - highlightTextColor;
//        }
//        this.backgrundColor = highlightBgColor;

//        init(highlightBgColor);

        builder = new SpannableStringBuilder(text);

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

                if (isUseBackground) {

                    if (highlightBgColor < 1) {
                        //求反色
                        highlightBgColor = 0xFF - highlightTextColor;
                    }
//
                    builder.setSpan(new BackgroundColorSpan(highlightBgColor), start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);     //设置指定位置textview的背景颜色
                }


//                builder.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (setClickableSpan != null)
                    builder.setSpan(new ClickableSpanExtend(text, findText, setClickableSpan, normalColor, highlightTextColor, clickBgClolr),
                            start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        if (isUseGifAnim) {
            setImageSpan.setSpan(context, text, builder, emotions, getLineHeight(), this, isUseGifAnim);
        } else {
            setImageSpan.setSpan(context, text, builder, emotions, getLineHeight(), null, isUseGifAnim);
        }
//        if (movementMethod != null){
//            textView.setMovementMethod(LinkMovementMethod.getInstance());
        setMovementMethod(localLinkMovementMethod);
//        }


        setText(builder);


    }


    @Override
    public void update() {
        this.postInvalidate();
    }
}