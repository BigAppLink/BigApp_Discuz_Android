package com.youzu.clan.base.json.smiley;

import com.youzu.android.framework.json.annotation.JSONField;

import java.util.ArrayList;

/**
 * Created by tangh on 2015/8/10.
 */
public class SmiliesItem {

    private String directory;
    private String name;

    private ArrayList<SmileyItem> smileyItems;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SmileyItem> getSmileyItems() {
        return smileyItems;
    }

    @JSONField(name = "smiley")
    public void setSmileyItem(ArrayList<SmileyItem> smileyItems) {
        this.smileyItems = smileyItems;
    }
}
