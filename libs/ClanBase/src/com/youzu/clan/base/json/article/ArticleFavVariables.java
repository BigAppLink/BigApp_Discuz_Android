package com.youzu.clan.base.json.article;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Message;
import com.youzu.clan.base.json.model.PagedVariables;
import com.youzu.clan.base.json.model.Variables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangh on 2015/8/26.
 */
public class ArticleFavVariables extends PagedVariables {

    private Message message;
    private ArrayList<ArticleFav> list;

    public Message getMessage() {
        return message;
    }

    @JSONField(name = "Message")
    public void setMessage(Message message) {
        this.message = message;
    }

    public void setList(ArrayList<ArticleFav> list) {
        this.list = list;
    }

    @Override
    public ArrayList<ArticleFav> getList() {
        return list;
    }
}
