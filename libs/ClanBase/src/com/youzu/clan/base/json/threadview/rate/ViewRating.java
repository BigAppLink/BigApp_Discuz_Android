package com.youzu.clan.base.json.threadview.rate;

/**
 * Created by Zhao on 15/12/3.
 */
public class ViewRating {
    /**
     *"uid": "217",
     *"username": "mawentao",
     *"score": "+2",
     *"reason": "赞一个!",
     *"credit": "财富"
     */



    private String uid;
    private String username;
    private String score;
    private String reason;
    private String credit;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
