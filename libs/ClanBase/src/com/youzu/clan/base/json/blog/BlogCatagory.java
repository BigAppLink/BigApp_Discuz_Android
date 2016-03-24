package com.youzu.clan.base.json.blog;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wjwu on 2015/12/18.
 * 系统分类信息，在“我的日志”、“随便看看”中该字段存在
 */
public class BlogCatagory implements Serializable {
    private String id;
    private String name;
    private String link;
//    private ArrayList<String> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
