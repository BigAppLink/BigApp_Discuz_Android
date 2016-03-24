package com.youzu.clan.base.json.article;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.BaseJson;

/**
 * Created by tangh on 2015/8/26.
 */
public class ArticleDetailJson extends BaseJson {
    private ArticleDetailVariables variables;

    @Override
    public ArticleDetailVariables getVariables() {
        return variables;
    }

    @JSONField(name = "Variables")
    public void setVariables(ArticleDetailVariables variables) {
        this.variables = variables;
    }
}
