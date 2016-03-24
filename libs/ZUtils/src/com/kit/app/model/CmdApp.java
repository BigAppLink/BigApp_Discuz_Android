package com.kit.app.model;

import java.io.Serializable;

/**
 * APP内部指令
 * Created by Zhao on 14-8-29.
 */
public class CmdApp implements Serializable {

    /**
     * 从哪里发出指令
     */
    private String fromWhere;

    /**
     * 指令要到哪里去
     */
    private String toWhere;

    /**
     * 要做什么事？
     */
    private String doWhat;

    public CmdApp(String fromWhere, String toWhere, String doWhat) {
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
        this.doWhat = doWhat;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getToWhere() {
        return toWhere;
    }

    public void setToWhere(String toWhere) {
        this.toWhere = toWhere;
    }

    public String getDoWhat() {
        return doWhat;
    }

    public void setDoWhat(String doWhat) {
        this.doWhat = doWhat;
    }
}
