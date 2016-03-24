package com.youzu.clan.base.json.homepageconfig;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.BaseJson;

/**
 *
 * 界面的详细配置
 * 
 */
public class TitleButtonConfigJson extends BaseJson {
    private TitleButtonConfigVariables variables;


    @Override
    public TitleButtonConfigVariables getVariables() {
        return variables;
    }

    @JSONField(name = "Variables")
    public void setVariables(TitleButtonConfigVariables variables) {
        this.variables = variables;
    }
}
