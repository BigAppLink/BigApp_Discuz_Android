package com.youzu.clan.base.json.login;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tangh on 2015/7/14.
 */
public class LoginQuestionData implements Serializable {
    @JSONField(name="error_code")
    private int errorCode;
    @JSONField(name="error_msg")
    private String errorMsg;
    @JSONField(name="data")
    private ArrayList<LoginQuestion> loginQuestions;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ArrayList<LoginQuestion> getLoginQuestions() {
        return loginQuestions;
    }

    public void setLoginQuestions(ArrayList<LoginQuestion> loginQuestions) {
        this.loginQuestions = loginQuestions;
    }
}
