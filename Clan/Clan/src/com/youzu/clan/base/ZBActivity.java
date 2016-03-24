package com.youzu.clan.base;

import com.kit.utils.ZZLogUtils;

/**
 * Created by wjwu on 2015/11/25.
 */
public class ZBActivity extends BaseActivity implements ZBCallBack {

    public void log(String msg) {
        ZZLogUtils.log(getClass().getSimpleName(), msg);
    }

    @Override
    public Object getDatas() {
        return null;
    }

}
