package com.youzu.clan.base.json.upload;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/7/2.
 */
public class Ret {
    public String aId;
    public String image;
    public String relative_url;
    public String abs_url;

    public String getAId() {
        return aId;
    }

    @JSONField(name = "aId")
    public void setAId(String aId) {
        this.aId = aId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRelative_url() {
        return relative_url;
    }

    public void setRelative_url(String relative_url) {
        this.relative_url = relative_url;
    }

    public String getAbs_url() {
        return abs_url;
    }

    public void setAbs_url(String abs_url) {
        this.abs_url = abs_url;
    }
}

