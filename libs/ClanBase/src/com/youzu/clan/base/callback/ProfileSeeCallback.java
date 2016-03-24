package com.youzu.clan.base.callback;

import android.support.v4.app.FragmentActivity;

import com.youzu.clan.base.json.ProfileJson;

public abstract class ProfileSeeCallback extends ProgressCallback<ProfileJson>{
    public ProfileSeeCallback(FragmentActivity activity) {
        super(activity);
    }
}
