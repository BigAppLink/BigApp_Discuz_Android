package com.youzu.clan.base.json.threadview.comment;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/12/1.
 */
public class CommentField {
    private String fieldId;
    private String title;


    private String value;


    public String getFieldId() {
        return fieldId;
    }

    @JSONField(name = "fieldid")
    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
