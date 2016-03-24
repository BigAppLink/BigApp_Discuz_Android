package com.keyboard.utils;

import android.graphics.drawable.Drawable;

import com.keyboard.XhsEmoticonsKeyBoardBar;
import com.keyboard.bean.EmoticonSetBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 此类控制board的显示 和 数据的共享
 */
public class EmoticonsController {

    private Drawable faceButtonClosedDrawable;
    private Drawable faceButtonOpenedDrawable;

    private Drawable sendButtonDisabledDrawable;
    private Drawable sendButtonDrawable;


    List<EmoticonSetBean> mEmoticonSetBeanList = new ArrayList<EmoticonSetBean>();
    /**
     * 是否启用Face图标
     */
    private boolean isUseFace = true;

    private XhsEmoticonsKeyBoardBar xhsEmoticonsKeyBoardBar;


    /**
     * 设置Send按钮的按下图片
     *
     * @param disabled
     * @param btnDrawable
     */
    public void sendButtonDrawable(Drawable disabled, Drawable btnDrawable) {
        this.sendButtonDisabledDrawable = disabled;
        this.sendButtonDrawable = btnDrawable;
    }

    public Drawable getSendButtonDisabledDrawable() {
        return sendButtonDisabledDrawable;
    }

    public Drawable getSendButtonDrawable() {
        return sendButtonDrawable;
    }


    /**
     * 设置Face按钮的按下图片
     *
     * @param closed
     * @param opened
     */
    public void faceButtonDrawable(Drawable closed, Drawable opened) {
        this.faceButtonClosedDrawable = closed;
        this.faceButtonOpenedDrawable = opened;
    }

    public Drawable getFaceButtonClosedDrawable() {
        return faceButtonClosedDrawable;
    }

    public Drawable getFaceButtonOpenedDrawable() {
        return faceButtonOpenedDrawable;
    }


    public boolean isUseFace() {
        return isUseFace;
    }

    public void setIsUseFace(boolean isUseFace) {
        this.isUseFace = isUseFace;
    }

    public List<EmoticonSetBean> getEmoticonSetBeanList() {
        return mEmoticonSetBeanList;
    }

    public EmoticonsController setEmoticonSetBeanList(List<EmoticonSetBean> emoticonSetBeanList) {
        this.mEmoticonSetBeanList = emoticonSetBeanList;
        return this;
    }

    public XhsEmoticonsKeyBoardBar getXhsEmoticonsKeyBoardBar() {
        return xhsEmoticonsKeyBoardBar;
    }

    public void setXhsEmoticonsKeyBoardBar(XhsEmoticonsKeyBoardBar xhsEmoticonsKeyBoardBar) {
        this.xhsEmoticonsKeyBoardBar = xhsEmoticonsKeyBoardBar;
    }

}
