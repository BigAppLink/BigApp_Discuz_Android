package com.youzu.clan.base.json.act;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wjwu on 2015/11/25.
 */
public class ActConfig implements Serializable {
    public String fid;
    private String allowpostactivity;
    private String credit_title;
    private ArrayList<String> activitytype;
    private ArrayList<ActField> activityfield;
    private String activityextnum;
    private String activitypp;

    public String getAllowpostactivity() {
        return allowpostactivity;
    }

    public void setAllowpostactivity(String allowpostactivity) {
        this.allowpostactivity = allowpostactivity;
    }

    public String getCredit_title() {
        return credit_title;
    }

    public void setCredit_title(String credit_title) {
        this.credit_title = credit_title;
    }

    public ArrayList<String> getActivitytype() {
        return activitytype;
    }

    public void setActivitytype(ArrayList<String> activitytype) {
        this.activitytype = activitytype;
    }

    public ArrayList<ActField> getActivityfield() {
        return activityfield;
    }

    public void setActivityfield(ArrayList<ActField> activityfield) {
        this.activityfield = activityfield;
    }

    public String getActivityextnum() {
        return activityextnum;
    }

    public void setActivityextnum(String activityextnum) {
        this.activityextnum = activityextnum;
    }

    public String getActivitypp() {
        return activitypp;
    }

    public void setActivitypp(String activitypp) {
        this.activitypp = activitypp;
    }
}
