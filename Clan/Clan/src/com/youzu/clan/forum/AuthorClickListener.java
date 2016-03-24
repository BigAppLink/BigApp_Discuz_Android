package com.youzu.clan.forum;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.youzu.clan.base.json.model.Moderator;
import com.youzu.clan.base.util.jump.JumpProfileUtils;

import java.util.ArrayList;

public class AuthorClickListener implements OnItemClickListener {

    private ArrayList<Moderator> moderators;
    private FragmentActivity activity;

    public AuthorClickListener(FragmentActivity activity,
                               ArrayList<Moderator> moderators) {
        this.activity = activity;
        this.moderators = moderators;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view,
                            int position, long id) {
        try {
            final Moderator moderator = moderators.get(position);
            final String uid = moderator.getUid();
            JumpProfileUtils.gotoProfilePage(activity, uid);

//            ClanHttp.getProfile(activity, uid,
//                    new ProgressCallback<ProfileJson>(activity) {
//                        @Override
//                        public void onSuccess(ProfileJson t) {
//                            super.onSuccess(t);
//                            ProfileVariables variables = t.getVariables();
//                            ClanUtils.toProfilePage(activity, variables);
//
//
//                        }
//
//                        @Override
//                        public void onFailed(int errorCode, String errorMsg) {
//                            onFailed(errorCode, errorMsg);
//                            ToastUtils.show(activity, R.string.request_failed);
//                        }
//                    });

        } catch (Exception e) {

        }
    }

}
