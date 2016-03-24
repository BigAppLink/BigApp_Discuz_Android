package com.youzu.clan.base.json.homepageconfig;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tangh on 2015/9/22.
 */
public class Recommend implements Serializable {
    private String type;
    private ArrayList<ThreadConfig> threadConfigs;


    private ArrayList<Object> datas=new ArrayList<>();
    private int forumFilterSelectIndex;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ThreadConfig> getThreadConfigs() {
        return threadConfigs;
    }

    @JSONField(name = "thread_config")
    public void setThreadConfigs(ArrayList<ThreadConfig> threadConfigs) {
        this.threadConfigs = threadConfigs;
    }

    public ArrayList<Object> getDatas() {
        return datas;
    }

    public void addDatas(ArrayList datas) {
        if(datas==null){
            return;
        }
        this.datas.addAll(datas);
    }

    public int getForumFilterSelectIndex() {
        return forumFilterSelectIndex;
    }

    public void setForumFilterSelectIndex(int forumFilterSelectIndex) {
        this.forumFilterSelectIndex = forumFilterSelectIndex;
    }
}
