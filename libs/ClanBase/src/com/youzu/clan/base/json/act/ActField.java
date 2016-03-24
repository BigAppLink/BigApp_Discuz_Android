package com.youzu.clan.base.json.act;

import java.io.Serializable;

/**
 * Created by wjwu on 2015/11/25.
 */
public class ActField  implements Serializable {
    private String fieldid;
    private String fieldtext;
    public boolean isSelected = false;

    public String getFieldid() {
        return fieldid;
    }

    public void setFieldid(String fieldid) {
        this.fieldid = fieldid;
    }

    public String getFieldtext() {
        return fieldtext;
    }

    public void setFieldtext(String fieldtext) {
        this.fieldtext = fieldtext;
    }
}
