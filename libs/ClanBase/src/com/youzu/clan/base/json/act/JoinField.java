package com.youzu.clan.base.json.act;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.FileInfo;

import java.io.Serializable;

/**
 * Created by Zhao on 15/11/20.
 */
public class JoinField implements Serializable {

//"fieldid": "realname",
//"title": "真实姓名",
//"formtype": "text",
//"default": "",
//"size": "0",
//"choices": [],
//"validate": ""
    public boolean isNotRequired = false;

    private String fieldId;
    private String title;
    private String formType;
    private String defaultValue;
    private String size;
    private String[] choices;
    private String validate;
    private String description;

    public String[] selected_multi;
    public String abs_url;

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

    public String getFormType() {
        return formType;
    }

    @JSONField(name = "formtype")
    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @JSONField(name = "default")
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
