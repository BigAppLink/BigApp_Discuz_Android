package com.youzu.clan.base.json.article;

import com.youzu.android.framework.db.annotation.Id;

import java.io.Serializable;

/**
 * Created by tangh on 2015/9/16.
 */
public class ArticleFav implements Serializable {
    private String favid;
    private String uid;
    @Id
    private String id;
    private String idtype;
    private String spaceuid;
    private String title;
    private String description;
    private String dateline;


    public String getFavid() {
        return favid;
    }

    public void setFavid(String favid) {
        this.favid = favid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getSpaceuid() {
        return spaceuid;
    }

    public void setSpaceuid(String spaceuid) {
        this.spaceuid = spaceuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }
}
