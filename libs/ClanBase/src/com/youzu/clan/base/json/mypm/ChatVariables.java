package com.youzu.clan.base.json.mypm;

import com.youzu.clan.base.json.model.Variables;

/**
 * Created by Zhao on 15/10/8.
 */
public class ChatVariables extends Variables {

    private String message;
    private String pmid;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }
}
