package com.youzu.clan.base.json.config.content;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wjwu on 2015/12/16.
 */
public class SettingRewrite implements Serializable {
    private String type;
    private String regex;
    private ArrayList<String> insteads;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public ArrayList<String> getInsteads() {
        return insteads;
    }

    public void setInsteads(ArrayList<String> insteads) {
        this.insteads = insteads;
    }
}
