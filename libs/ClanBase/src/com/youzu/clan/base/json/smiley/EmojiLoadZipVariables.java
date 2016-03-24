package com.youzu.clan.base.json.smiley;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Variables;

/**
 * Created by tangh on 2015/8/10.
 */
public class EmojiLoadZipVariables extends Variables {
    private String zipUrl;
    //对于拉zip包，需判断zip_flag是否为1，为1表示表情包后端有更新或者目前还未拉过zip表情包，此时需要通过zip_url拉取表情zip包
    private String zipFlag;

    public String getZipUrl() {
        return zipUrl;
    }

    @JSONField(name = "zip_url")
    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

    public String getZipFlag() {
        return zipFlag;
    }

    @JSONField(name = "zip_flag")
    public void setZipFlag(String zipFlag) {
        this.zipFlag = zipFlag;
    }
}
