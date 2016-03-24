package com.youzu.clan.threadandarticle.model;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/11/17.
 */
public class VoteRequest {
    private String tid;
    private String[] pollAnswers;


    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String[] getPollAnswers() {
        return pollAnswers;
    }

    @JSONField(name = "pollanswers")
    public void setPollAnswers(String[] pollAnswers) {
        this.pollAnswers = pollAnswers;
    }
}
