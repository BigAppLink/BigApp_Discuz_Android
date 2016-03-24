package com.youzu.clan.base.json.thread.inner;

import com.youzu.clan.base.json.model.Icons;
import com.youzu.clan.base.json.model.Types;

import java.util.ArrayList;

/**
 * Created by tangh on 2015/8/12.
 */
public class ThreadType {
    private boolean required;
    private boolean listable;
    private String prefix;

    private ArrayList<Types> types;
    private ArrayList<Icons> icons;


    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isListable() {
        return listable;
    }

    public void setListable(boolean listable) {
        this.listable = listable;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ArrayList<Types> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<Types> types) {
        this.types = types;
    }

    public ArrayList<Icons> getIcons() {
        return icons;
    }

    public void setIcons(ArrayList<Icons> icons) {
        this.icons = icons;
    }
}
