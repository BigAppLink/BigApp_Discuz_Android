package com.youzu.clan.base.json.article;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Message;
import com.youzu.clan.base.json.model.Variables;

import java.util.ArrayList;

/**
 * Created by tangh on 2015/8/26.
 */
public class ArticleDetailVariables extends Variables {

    private Message message;
    private Article data;

    public Message getMessage() {
        return message;
    }

    @JSONField(name = "Message")
    public void setMessage(Message message) {
        this.message = message;
    }

    public Article getData() {
        return data;
    }

    public void setData(Article data) {
        this.data = data;
    }
}
