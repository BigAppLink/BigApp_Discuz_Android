package com.youzu.clan.base.json.model;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/5/29.
 */
public class Icons {

    private String typeId;
    private String typeIcon;

    public String getTypeId() {
        return typeId;
    }
    @JSONField(name = "typeid")
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeIcon() {
        return typeIcon;
    }

    @JSONField(name = "typeicon")
    public void setTypeIcon(String typeIcon) {
        this.typeIcon = typeIcon;
    }
}
