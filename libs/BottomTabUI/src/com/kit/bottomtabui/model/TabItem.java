package com.kit.bottomtabui.model;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.kit.bottomtabui.view.OnTabClickListener;
import com.kit.bottomtabui.view.OnTabItemSelectedClickListener;

/**
 * Created by Zhao on 15/6/24.
 */
public class TabItem {

    /**
     * 是否仅仅是个button
     */
    private boolean isJustButton;

    /**
     * isJustButton为true的时候起作用
     */
    private View justButtonView;


    /**
     * isJustButton为true的时候起作用
     */
    private View.OnClickListener justButtonClickListener;

    private boolean isPadding;

    private int padding;

    private String title;
    private Drawable normalDrawable;
    private Drawable selectedDrawable;
    private int textNormalColor;
    private int textSelectedColor;
    private Drawable backgroundDrawable;
    private OnTabClickListener onTabClickListener;
    private OnTabItemSelectedClickListener OnTabItemSelectedClickListener;


    public TabItem() {
    }


    public TabItem(String title, Drawable normalDrawable, Drawable selectedDrawable) {
        this.title = title;
        this.normalDrawable = normalDrawable;
        this.selectedDrawable = selectedDrawable;
    }

    public TabItem(String title, Drawable normalDrawable, Drawable selectedDrawable, boolean isPadding, int padding) {
        this(title, normalDrawable, selectedDrawable);

        this.isPadding = isPadding;
        this.padding = padding;
    }

    public TabItem(String title, Drawable normalDrawable, Drawable selectedDrawable, int textNormalColor, int textSelectedColor, boolean isPadding, int padding) {
        this(title, normalDrawable, selectedDrawable, null, textNormalColor, textSelectedColor, isPadding, padding);
    }

    public TabItem(String title, Drawable normalDrawable, Drawable selectedDrawable, Drawable backgroundDrawable, int textNormalColor, int textSelectedColor, boolean isPadding, int padding) {
        this.title = title;
        this.normalDrawable = normalDrawable;
        this.selectedDrawable = selectedDrawable;
        this.textNormalColor = textNormalColor;
        this.textSelectedColor = textSelectedColor;
        this.backgroundDrawable = backgroundDrawable;

        this.isPadding = isPadding;
        this.padding = padding;
    }

    public TabItem(String title, Drawable normalDrawable, Drawable selectedDrawable, int textNormalColor, int textSelectedColor, boolean isJustButton, boolean isPadding, int padding, View.OnClickListener justButtonClickListener) {
        this.title = title;
        this.normalDrawable = normalDrawable;
        this.selectedDrawable = selectedDrawable;
        this.textNormalColor = textNormalColor;
        this.textSelectedColor = textSelectedColor;
        this.isJustButton = isJustButton;
        this.justButtonClickListener = justButtonClickListener;

        this.isPadding = isPadding;
        this.padding = padding;
    }


    public View getJustButtonView() {
        return justButtonView;
    }

    public void setJustButtonView(View justButtonView) {
        this.justButtonView = justButtonView;
    }

    public boolean isJustButton() {
        return isJustButton;
    }

    public void setIsJustButton(boolean isJustButton) {
        this.isJustButton = isJustButton;
    }


    public View.OnClickListener getJustButtonClickListener() {
        return justButtonClickListener;
    }

    public void setJustButtonClickListener(View.OnClickListener justButtonClickListener) {
        this.justButtonClickListener = justButtonClickListener;
    }

    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public void setBackgroundDrawable(Drawable backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getNormalDrawable() {
        return normalDrawable;
    }

    public void setNormalDrawable(Drawable normalDrawable) {
        this.normalDrawable = normalDrawable;
    }

    public Drawable getSelectedDrawable() {
        return selectedDrawable;
    }

    public void setSelectedDrawable(Drawable selectedDrawable) {
        this.selectedDrawable = selectedDrawable;
    }


    public int getTextNormalColor() {
        return textNormalColor;
    }

    public void setTextNormalColor(int textNormalColor) {
        this.textNormalColor = textNormalColor;
    }

    public int getTextSelectedColor() {
        return textSelectedColor;
    }

    public void setTextSelectedColor(int textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
    }


    public boolean isPadding() {
        return isPadding;
    }

    public void setPadding(boolean isPadding) {
        this.isPadding = isPadding;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
}
