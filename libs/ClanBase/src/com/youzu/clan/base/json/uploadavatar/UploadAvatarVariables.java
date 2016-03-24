package com.youzu.clan.base.json.uploadavatar;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Variables;

public class UploadAvatarVariables extends Variables {

    private String uploadAvatar;

    public String getUploadAvatar() {
        return uploadAvatar;
    }

    @JSONField(name = "uploadavatar")
    public void setUploadAvatar(String uploadAvatar) {
        this.uploadAvatar = uploadAvatar;
    }
}