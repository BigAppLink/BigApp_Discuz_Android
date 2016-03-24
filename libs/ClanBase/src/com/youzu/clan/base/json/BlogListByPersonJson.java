package com.youzu.clan.base.json;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.blog.BlogListByPersonVariables;

/**
 * Created by wjwu on 2015/12/18.
 */
public class BlogListByPersonJson extends BaseJson {

    @JSONField(name = "Variables")
    private BlogListByPersonVariables variables;

    public BlogListByPersonVariables getVariables() {
        return variables;
    }

    public void setVariables(BlogListByPersonVariables variables) {
        this.variables = variables;
    }

}