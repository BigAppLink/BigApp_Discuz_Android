package com.youzu.clan.base.json.config.content.smiley;

import com.youzu.android.framework.json.annotation.JSONField;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/8/26.
 */
public class ZipInfo {

    private ArrayList<PicSchema> picSchema;
    private String picDirectory;


    public ArrayList<PicSchema> getPicSchema() {
        return picSchema;
    }

    @JSONField(name = "pic_schema")
    public void setPicSchema(ArrayList<PicSchema> picSchema) {
        this.picSchema = picSchema;
    }

    public String getPicDirectory() {
        return picDirectory;
    }

    @JSONField(name = "pic_directory")
    public void setPicDirectory(String picDirectory) {
        this.picDirectory = picDirectory;
    }
}
