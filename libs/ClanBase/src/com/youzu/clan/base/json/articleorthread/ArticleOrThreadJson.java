package com.youzu.clan.base.json.articleorthread;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.BaseJson;

/**
 * Created by tangh on 2015/10/10.
 */
public class ArticleOrThreadJson extends BaseJson {
    private ArticleOrThreadVariables variables;

    @Override
    public ArticleOrThreadVariables getVariables() {
        return variables;
    }

    @JSONField(name = "Variables")
    public void setVariables(ArticleOrThreadVariables variables) {
        this.variables = variables;
    }
}
