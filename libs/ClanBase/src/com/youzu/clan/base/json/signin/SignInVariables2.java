package com.youzu.clan.base.json.signin;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Variables;

/**
 * Created by tangh on 2015/8/11.
 */
public class SignInVariables2 extends Variables{

    private String status;

    private String checkinEnabled;
    private String checked;
    private String message;

    private String days;
    private String credit;
    private String bonus_days;
    private String bonus_plus;
    private String title;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckinEnabled() {
        return checkinEnabled;
    }

    @JSONField(name = "checkin_enabled")
    public void setCheckinEnabled(String checkinEnabled) {
        this.checkinEnabled = checkinEnabled;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getBonus_days() {
        return bonus_days;
    }

    public void setBonus_days(String bonus_days) {
        this.bonus_days = bonus_days;
    }

    public String getBonus_plus() {
        return bonus_plus;
    }

    public void setBonus_plus(String bonus_plus) {
        this.bonus_plus = bonus_plus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
