package com.youzu.clan.base.json.threadview.comment;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Variables;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/12/1.
 */
public class CommentCheckVariables extends Variables {

    private ArrayList<CommentField> commentFields;
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<CommentField> getCommentFields() {
        return commentFields;
    }

    @JSONField(name = "comment_fields")
    public void setCommentFields(ArrayList<CommentField> commentFields) {
        this.commentFields = commentFields;
    }



}
