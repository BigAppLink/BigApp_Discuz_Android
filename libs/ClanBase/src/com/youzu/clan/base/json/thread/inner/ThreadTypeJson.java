package com.youzu.clan.base.json.thread.inner;


import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by tangh on 2015/8/12.
 */
public class ThreadTypeJson {

    @JSONField(name = "error_code")
    private String errorCode;
    @JSONField(name = "error_msg")
    private String errorMsg;
    private ThreadType threadtypes;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ThreadType getThreadtypes() {
        return threadtypes;
    }

    public void setThreadtypes(ThreadType threadtypes) {
        this.threadtypes = threadtypes;
    }
}
