package com.youzu.clan.base.json.upload;

import com.youzu.clan.base.json.model.Variables;

public class UploadVariables extends Variables {
    private static final long serialVersionUID = 1715402302348076579L;
    private String code;
    private String message;
    private Ret ret;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Ret getRet() {
        return ret;
    }

    public void setRet(Ret ret) {
        this.ret = ret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
