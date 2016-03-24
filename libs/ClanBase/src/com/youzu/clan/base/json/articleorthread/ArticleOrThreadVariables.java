package com.youzu.clan.base.json.articleorthread;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Variables;

import java.util.ArrayList;

/**
 * Created by tangh on 2015/10/10.
 */
public class ArticleOrThreadVariables extends Variables {
    private String count;
    private String page;
    private String needMore;
    private String picMode;
    private ArrayList<ArticleOrThread> data;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getNeedMore() {
        return needMore;
    }

    @JSONField(name = "need_more")
    public void setNeedMore(String needMore) {
        this.needMore = needMore;
    }

    public String getPicMode() {
        return picMode;
    }

    @JSONField(name = "pic_mode")
    public void setPicMode(String picMode) {
        this.picMode = picMode;
    }

    public ArrayList<ArticleOrThread> getData() {
        return data;
    }

    public void setData(ArrayList<ArticleOrThread> data) {
        this.data = data;
    }
}
