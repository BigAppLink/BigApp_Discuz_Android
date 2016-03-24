package com.youzu.clan.base.json;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.act.ActPlayerVariables;
import com.youzu.clan.base.json.forumnav.ForumnavVariables;

public class ActPlayerJson extends BaseJson {

    private static final long serialVersionUID = 894045894026317236L;
    @JSONField(name = "Variables")
    private ActPlayerVariables variables;

    @Override
    public ActPlayerVariables getVariables() {
        return variables;
    }

    public void setVariables(ActPlayerVariables variables) {
        this.variables = variables;
    }
}
