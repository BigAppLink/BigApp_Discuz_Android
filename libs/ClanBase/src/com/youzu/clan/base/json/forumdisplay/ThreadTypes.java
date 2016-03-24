package com.youzu.clan.base.json.forumdisplay;

import com.youzu.clan.base.json.model.Icons;
import com.youzu.clan.base.json.model.Types;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhao on 15/5/29.
 */
public class ThreadTypes {

    private String required;
    private String listable;
    private String prefix;
    private List<Types> types;
    private List<Icons> icons;
    private Map<String,String>  moderators;


    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getListable() {
        return listable;
    }

    public void setListable(String listable) {
        this.listable = listable;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<Types> getTypes() {
        return types;
    }

    public void setTypes(List<Types> types) {
        this.types = types;
    }

    public List<Icons> getIcons() {
        return icons;
    }

    public void setIcons(List<Icons> icons) {
        this.icons = icons;
    }

    public Map<String, String> getModerators() {
        return moderators;
    }

    public void setModerators(Map<String, String> moderators) {
        this.moderators = moderators;
    }
}
