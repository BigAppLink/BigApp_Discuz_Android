package com.youzu.clan.base.json.config.content;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/9/24.
 */
public class SkeletonSetting {
    private String id;
    private String iconType;
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIconType() {
        return iconType;
    }

    @JSONField(name = "icon_type")
    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
