package com.youzu.clan.base.json;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;


public class BaseResponse implements Serializable {


    private String requestId;
    private String errorCode;
    private String errorMsg;
    private Object data;


    public String getRequestId() {
        return requestId;
    }

    @JSONField(name = "request_id")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @JSONField(name = "error_code")
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @JSONField(name = "error_msg")
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
