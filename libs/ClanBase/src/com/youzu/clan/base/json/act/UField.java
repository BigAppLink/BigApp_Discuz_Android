package com.youzu.clan.base.json.act;

import java.io.Serializable;

/**
 * Created by Zhao on 15/11/20.
 */
public class UField implements Serializable {
//    private String[] userfield;

    private String[] extfield;

    public String[] getExtfield() {
        return extfield;
    }

    public void setExtfield(String[] extfield) {
        this.extfield = extfield;
    }
}
