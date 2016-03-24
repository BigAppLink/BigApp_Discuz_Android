package com.youzu.clan.base.json.threadview.rate;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Variables;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/12/1.
 */
public class RateCheckVariables extends Variables {

    private ArrayList<Rate> rateList;
    private String status;
    private ArrayList<String> reasons;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Rate> getRateList() {
        return rateList;
    }

    @JSONField(name = "ratelist")
    public void setRateList(ArrayList<Rate> rates) {
        this.rateList = rates;
    }

    public ArrayList<String> getReasons() {
        return reasons;
    }

    public void setReasons(ArrayList<String> reasons) {
        this.reasons = reasons;
    }
}
