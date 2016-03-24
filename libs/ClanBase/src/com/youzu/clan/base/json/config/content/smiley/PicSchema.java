package com.youzu.clan.base.json.config.content.smiley;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/8/26.
 */
public class PicSchema {
    private String picName;
    private int picSize;

    public String getPicName() {
        return picName;
    }

    @JSONField(name = "pic_name")
    public void setPicName(String picName) {
        this.picName = picName;
    }

    public int getPicSize() {
        return picSize;
    }


    @JSONField(name = "pic_size")
    public void setPicSize(int picSize) {
        this.picSize = picSize;
    }
}
