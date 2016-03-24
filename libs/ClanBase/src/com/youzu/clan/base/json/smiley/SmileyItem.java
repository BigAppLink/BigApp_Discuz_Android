package com.youzu.clan.base.json.smiley;

import com.youzu.android.framework.db.annotation.Id;

/**
 * Created by tangh on 2015/8/10.
 */
public class SmileyItem {

    @Id
    private String id;

    private String code;

    private String url;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
