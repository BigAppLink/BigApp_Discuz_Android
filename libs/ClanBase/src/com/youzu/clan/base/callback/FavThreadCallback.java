package com.youzu.clan.base.callback;

import android.support.v4.app.FragmentActivity;

import com.youzu.clan.base.json.FavThreadJson;

public  class FavThreadCallback extends ProgressCallback<FavThreadJson> {

    public FavThreadCallback(FragmentActivity activity) {
        super(activity);
    }
}
