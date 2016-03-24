package com.youzu.clan.base.json.config.content;

import com.youzu.clan.base.json.BaseResponse;
import com.youzu.clan.base.json.config.content.ContentConfig;

/**
 * Created by Zhao on 15/6/16.
 */
public class ContentConfigJson extends BaseResponse {


    private ContentConfig config;

    public ContentConfig getConfig() {
        return config;
    }

    public void setConfig(ContentConfig config) {
        this.config = config;
    }
}
