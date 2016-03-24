package com.youzu.clan.base.json.signin;

import com.youzu.clan.base.json.BaseJson;

/**
 * Created by tangh on 2015/8/11.
 */
public class SignInJson extends BaseJson {
 private SignInVariables2 Variables;

    @Override
    public SignInVariables2 getVariables() {
        return Variables;
    }

    public void setVariables(SignInVariables2 variables) {
        Variables = variables;
    }
}
