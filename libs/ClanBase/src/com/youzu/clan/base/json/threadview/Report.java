package com.youzu.clan.base.json.threadview;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tangh on 2015/8/4.
 */
public class Report implements Serializable {
    private String enable;
    private String handlekey;
    private ArrayList<String> content;

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getHandlekey() {
        return handlekey;
    }

    public void setHandlekey(String handlekey) {
        this.handlekey = handlekey;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }
}
