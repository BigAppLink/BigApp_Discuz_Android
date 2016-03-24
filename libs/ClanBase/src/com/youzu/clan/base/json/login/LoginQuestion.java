package com.youzu.clan.base.json.login;

import java.io.Serializable;

/**
 * Created by tangh on 2015/7/14.
 */
public class LoginQuestion implements Serializable{
    private String questionid;
    private String question;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }
}
