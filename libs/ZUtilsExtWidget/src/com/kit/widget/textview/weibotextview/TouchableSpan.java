package com.kit.widget.textview.weibotextview;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class TouchableSpan extends ClickableSpan {
    private boolean mIsPressed;
    private int mPressedBackgroundColor;
    private int mNormalTextColor;
    private int mPressedTextColor;

    public TouchableSpan(int normalTextColor, int pressedTextColor, int pressedBackgroundColor) {
        mNormalTextColor = normalTextColor;
        mPressedTextColor = pressedTextColor;
        mPressedBackgroundColor = pressedBackgroundColor;
    }

    public void setPressed(boolean isSelected) {
        mIsPressed = isSelected;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
        ds.bgColor = mIsPressed ? mPressedBackgroundColor : 0x00eeeeee;
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        //todo
    }

}
