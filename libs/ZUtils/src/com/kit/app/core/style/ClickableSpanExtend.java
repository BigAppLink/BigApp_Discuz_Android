package com.kit.app.core.style;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.kit.utils.ZogUtils;

public class ClickableSpanExtend extends ClickableSpan implements View.OnClickListener {

    //http://233.io/article/1001850.html

    private Context context;
    private String text;
    private View view;
    private String string;


    private int highlightColor;
    private int normalColor;
    private int clickBgColor;


    private SetClickableSpan setClickableSpan;

    private View.OnClickListener onClickListener;


    public ClickableSpanExtend(String text, String string, SetClickableSpan setClickableSpan, int normalColor, int highlightColor, int clickBgColor) {
        super();
        this.text = text;
        this.string = string;
        this.setClickableSpan = setClickableSpan;
        this.highlightColor = highlightColor;
        this.normalColor = normalColor;
        this.clickBgColor = clickBgColor;
    }


    @Override
    public void onClick(View view) {
        this.view = view;
        ZogUtils.printLog(ClickableSpanExtend.class, "onClick string:" + string);
        setClickableSpan.setOnClickListener(text, string);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        setClickableSpan.setStyle(text, string, ds, normalColor, highlightColor, clickBgColor);
    }


    /**
     * 监听设置器和改变ui
     */
    public interface SetClickableSpan {

        public void setStyle(String text, String string, TextPaint ds, int normalColor, int highlightColor, int clickBgColor);

        public void setOnClickListener(String text, String string);
    }
}