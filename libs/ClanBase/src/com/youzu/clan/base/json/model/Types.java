package com.youzu.clan.base.json.model;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/5/29.
 */
public class Types {

    private String typeId;
    private String typeName;

    public String getTypeId() {
        return typeId;
    }

    @JSONField(name = "typeid")
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    @JSONField(name = "typename")
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
