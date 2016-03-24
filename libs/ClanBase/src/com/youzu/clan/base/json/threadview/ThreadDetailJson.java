package com.youzu.clan.base.json.threadview;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.threadview.ThreadDetailVariables;

/**
 *
 * 主题详情
 * Created by Zhao on 15/5/5.
 */
public class ThreadDetailJson extends BaseJson {

    private ThreadDetailVariables variables;

    public ThreadDetailVariables getVariables() {
        return variables;
    }

    @JSONField(name = "Variables")
    public void setVariables(ThreadDetailVariables variables) {
        this.variables = variables;
    }
}
