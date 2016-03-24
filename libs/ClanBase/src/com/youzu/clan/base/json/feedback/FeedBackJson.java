package com.youzu.clan.base.json.feedback;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by tangh on 2015/8/27.
 */
public class FeedBackJson {

    private String requestId;
    private int errorCode;
    private String errorMsg;

    public String getRequestId() {
        return requestId;
    }

    @JSONField(name = "request_id")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @JSONField(name = "error_code")
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @JSONField(name = "error_msg")
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
