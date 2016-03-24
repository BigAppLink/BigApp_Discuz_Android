package com.kit.share.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Zhao on 15/7/16.
 */
public class ShareItem {
    private String platformName;

    private Drawable resDrawable;
    private String name;

    public ShareItem() {
    }


    public ShareItem(String platformName, Drawable resDrawable, String name) {
        this.platformName = platformName;
        this.resDrawable = resDrawable;
        this.name = name;
    }


    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getResDrawable() {
        return resDrawable;
    }

    public void setResDrawable(Drawable resDrawable) {
        this.resDrawable = resDrawable;
    }
}
