package com.youzu.clan.base.json.forumnav;

import com.youzu.android.framework.db.annotation.Id;
import com.youzu.clan.base.json.forum.BaseForum;
import com.youzu.clan.base.json.model.Moderator;

import java.util.ArrayList;

public class NavForum extends BaseForum {
    private static final long serialVersionUID = 5106707072439985487L;

    @Id
    private String fid;
    private String type;
    private String fup;
    private String status;
    private String rank;
    private String oldrank;
    private String viewperm;
    private ArrayList<Moderator> moderators;
    private ArrayList<NavForum> forums;
    private ArrayList<NavForum> subs;
    private String description;
    private boolean hasParent = true;
    public int grade=3;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFup() {
        return fup;
    }

    public void setFup(String fup) {
        this.fup = fup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getOldrank() {
        return oldrank;
    }

    public void setOldrank(String oldrank) {
        this.oldrank = oldrank;
    }

    public String getViewperm() {
        return viewperm;
    }

    public void setViewperm(String viewperm) {
        this.viewperm = viewperm;
    }

    public ArrayList<Moderator> getModerators() {
        return moderators;
    }

    public void setModerators(ArrayList<Moderator> moderators) {
        this.moderators = moderators;
    }

    public ArrayList<NavForum> getForums() {
        return forums;
    }

    public void setForums(ArrayList<NavForum> forums) {
        this.forums = forums;
    }

    public ArrayList<NavForum> getSubs() {
        return subs;
    }

    public void setSubs(ArrayList<NavForum> subs) {
        this.subs = subs;
    }

    public boolean hasParent() {
        return hasParent;
    }

    public void setHasParent(boolean hasParent) {
        this.hasParent = hasParent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getId() {
        return fid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }



}
