package com.youzu.clan.base.json.smiley;

import com.youzu.clan.base.json.BaseJson;

/**
 * Created by tangh on 2015/8/10.
 */
public class EmojiMapJson extends BaseJson {
    private EmojiMapVariables Variables;

    @Override
    public EmojiMapVariables getVariables() {
        return Variables;
    }

    public void setVariables(EmojiMapVariables variables) {
        Variables = variables;
    }
}
