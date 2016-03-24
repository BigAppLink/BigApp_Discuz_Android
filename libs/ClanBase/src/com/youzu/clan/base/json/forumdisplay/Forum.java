package com.youzu.clan.base.json.forumdisplay;

import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.db.annotation.Id;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.json.forum.BaseForum;
import com.youzu.clan.base.json.model.Moderator;

import java.io.Serializable;
import java.util.ArrayList;

public class Forum extends BaseForum implements Serializable {
    private static final long serialVersionUID = 2303949609341129560L;

    @Id
    private String fid;
    private String fup;
    private String autoclose;
    private String password;
    private ArrayList<Moderator> moderators;
    private String allowspecialonly;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFup() {
        return fup;
    }

    public void setFup(String fup) {
        this.fup = fup;
    }



    public String getAutoclose() {
        return autoclose;
    }

    public void setAutoclose(String autoclose) {
        this.autoclose = autoclose;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Moderator> getModerators() {
        return moderators;
    }

    public void setModerators(ArrayList<Moderator> moderators) {
        this.moderators = moderators;
    }


    public String getId() {
        return fid;
    }

    public void setId(String id) {
        this.fid = id;
    }

    public String getAllowspecialonly() {
        return allowspecialonly;
    }

    public void setAllowspecialonly(String allowspecialonly) {
        this.allowspecialonly = allowspecialonly;
    }

    public NavForum toNavForum() {
        String str = JsonUtils.toJSON(this).toString();

        NavForum f = JsonUtils.parseObject(str, NavForum.class);
        return f;
    }

}
