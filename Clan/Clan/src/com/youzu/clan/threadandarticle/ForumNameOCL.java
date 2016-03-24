package com.youzu.clan.threadandarticle;

import android.content.Context;
import android.view.View;

import com.youzu.clan.base.util.jump.JumpForumUtils;

public class ForumNameOCL implements View.OnClickListener {
    Context context;
    String forumName;
    String fid;

    public ForumNameOCL(Context context,String forumName, String fid) {
        this.context = context;
        this.forumName = forumName;
        this.fid = fid;
    }

    @Override
    public void onClick(View v) {
        JumpForumUtils.gotoForum(context,forumName,fid);

    }
}