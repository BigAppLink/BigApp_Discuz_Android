package com.youzu.clan.base.json.homepageconfig;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by tangh on 2015/9/22.
 */
public class ThreadConfig implements Serializable{
    private String module;
    private String title;
    private String dataLink;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataLink() {
        return dataLink;
    }

    @JSONField(name = "data_link")
    public void setDataLink(String dataLink) {
        this.dataLink = dataLink;
    }
}
