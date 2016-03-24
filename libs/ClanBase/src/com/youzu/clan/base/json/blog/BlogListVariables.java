package com.youzu.clan.base.json.blog;

import com.youzu.clan.base.json.model.PagedVariables;

import java.util.ArrayList;
import java.util.List;

public class BlogListVariables extends PagedVariables {
    private static final long serialVersionUID = -1168535909216278892L;
    private ArrayList<BlogInfo> list;
    private ArrayList<BlogCatagory> category;

    public void setList(ArrayList<BlogInfo> list) {
        this.list = list;
    }

    public ArrayList<BlogCatagory> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<BlogCatagory> category) {
        this.category = category;
    }

    @Override
    public List getList() {
        return list;
    }
}
