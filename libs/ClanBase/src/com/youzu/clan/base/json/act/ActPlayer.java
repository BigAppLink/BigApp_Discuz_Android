package com.youzu.clan.base.json.act;

import android.text.Spanned;

import java.util.ArrayList;

/**
 * Created by wjwu on 2015/11/26.
 */
public class ActPlayer {
    public boolean isChecked;
    /***
     * 0表示只有三条数据，1表示有更多处于收起状态，2表示展开状态
     */
    public int mode;

    private String applyid;
    private String tid;
    private String username;
    private String uid;
    private String message;
    private String verified;
    private String dateline;
    private String payment;
    private String ufielddata;
    private String can_select;
    public CharSequence  desc;
    public CharSequence  desc_short;

    public String getApplyid() {
        return applyid;
    }

    public void setApplyid(String applyid) {
        this.applyid = applyid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getUfielddata() {
        return ufielddata;
    }

    public void setUfielddata(String ufielddata) {
        this.ufielddata = ufielddata;
    }

    public String getCan_select() {
        return can_select;
    }

    public void setCan_select(String can_select) {
        this.can_select = can_select;
    }
}
