package com.youzu.clan.base.json.forumdisplay;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.PagedVariables;
import com.youzu.clan.base.json.act.ActConfig;

import java.util.ArrayList;
import java.util.List;

public class ForumDisplayVariables extends PagedVariables {

    private static final long serialVersionUID = 1999797743080198075L;
    private Forum forum;
    private Group group;

    private String openImageMode;
    private String tpp;


    private ArrayList<Forum> subList;

    private ArrayList<Thread> forumThreadList;
    private ThreadTypes threadTypes;

    private ActConfig activity_config;

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getOpenImageMode() {
        return openImageMode;
    }

    @JSONField(name = "open_image_mode")
    public void setOpenImageMode(String openImageMode) {
        this.openImageMode = openImageMode;
    }

    public String getTpp() {
        return tpp;
    }

    public void setTpp(String tpp) {
        this.tpp = tpp;
    }


    public ThreadTypes getThreadTypes() {
        return threadTypes;
    }

    @JSONField(name = "threadtypes")
    public void setThreadTypes(ThreadTypes threadTypes) {
        this.threadTypes = threadTypes;
    }

    public ArrayList<Thread> getForumThreadList() {
        return forumThreadList;
    }

    @JSONField(name = "forum_threadlist")
    public void setForumThreadList(ArrayList<Thread> forumThreadlist) {
        this.forumThreadList = forumThreadlist;
    }

    public ArrayList<Forum> getSubList() {
        return subList;
    }

    @JSONField(name = "sublist")
    public void setSubList(ArrayList<Forum> subList) {
        this.subList = subList;
    }

    @Override
    public List getList() {
        return forumThreadList;
    }

    public ActConfig getActivity_config() {
        return activity_config;
    }

    public void setActivity_config(ActConfig activity_config) {
        this.activity_config = activity_config;
    }
}
