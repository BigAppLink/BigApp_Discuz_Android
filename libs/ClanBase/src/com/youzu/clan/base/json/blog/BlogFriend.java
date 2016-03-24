package com.youzu.clan.base.json.blog;

import java.io.Serializable;

/**
 * Created by wjwu on 2015/12/18.
 */
public class BlogFriend implements Serializable {
    private String friend;
    private String friendname;//全站用户可见

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }
}
