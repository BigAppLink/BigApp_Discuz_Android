package com.youzu.clan.base.json.threadview.rate;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Variables;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/12/1.
 */
public class ViewRatingVariables extends Variables {

    private ArrayList<ViewRating> viewRatings;


    public ArrayList<ViewRating> getRateList() {
        return viewRatings;
    }

    @JSONField(name = "list")
    public void setRateList(ArrayList<ViewRating> rates) {
        this.viewRatings = rates;
    }

}
