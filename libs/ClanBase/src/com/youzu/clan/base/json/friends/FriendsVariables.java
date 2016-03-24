package com.youzu.clan.base.json.friends;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.PagedVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangh on 2015/7/15.
 */
public class FriendsVariables extends PagedVariables {

    private ArrayList<Friends> list;

    @JSONField(name = "user_list")
    private ArrayList<Friends> userList;

    public void setList(ArrayList<Friends> list) {
        this.list = list;
    }

    public ArrayList<Friends> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<Friends> userList) {
        this.userList = userList;
    }

    @Override
    public List getList() {
        if(list!=null){
           return list;
        }else if(userList!=null){
            return  userList;
        }
        return list;

    }
}
