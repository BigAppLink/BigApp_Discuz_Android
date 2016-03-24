package com.youzu.clan.base.json.article;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Message;
import com.youzu.clan.base.json.model.Variables;

import java.util.ArrayList;

/**
 * Created by tangh on 2015/8/26.
 */
public class ArticleAddFavVariables extends Variables {

    private String favid;

    public String getFavid() {
        return favid;
    }

    public void setFavid(String favid) {
        this.favid = favid;
    }
}
