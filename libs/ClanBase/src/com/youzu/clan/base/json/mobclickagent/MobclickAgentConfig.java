package com.youzu.clan.base.json.mobclickagent;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/6/2.
 */
public class MobclickAgentConfig {


    private int policy;
    private int duration;
    private String apiPath;


    public int getPolicy() {
        return policy;
    }

    public void setPolicy(int policy) {
        this.policy = policy;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getApiPath() {
        return apiPath;
    }

    @JSONField(name = "api_path")
    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }
}
