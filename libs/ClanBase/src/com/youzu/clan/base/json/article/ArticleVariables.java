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
public class ArticleVariables extends PagedVariables {

    private Message message;
    private ArrayList<Article> data;
    private String needmore;

    public Message getMessage() {
        return message;
    }

    @JSONField(name = "Message")
    public void setMessage(Message message) {
        this.message = message;
    }

    public ArrayList<Article> getData() {
        return data;
    }

    public void setData(ArrayList<Article> data) {
        this.data = data;
    }

    public String getNeedmore() {
        return needmore;
    }

    public void setNeedmore(String needmore) {
        this.needmore = needmore;
    }

    @Override
    public List getList() {
        return data;
    }
}
