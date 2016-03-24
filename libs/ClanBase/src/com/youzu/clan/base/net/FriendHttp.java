package com.youzu.clan.base.net;

import android.content.Context;

import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.base.callback.CheckFriendCallback;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.util.ClanBaseUtils;

/**
 * Created by Zhao on 15/7/18.
 */
public class FriendHttp {

    public static void getFriendCount(final Context context, final HttpCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "newfriend");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("only_count", "1");

//        ZogUtils.printError(FriendHttp.class, "ClanBaseUtils.getFormhash(context):"
//                + ClanBaseUtils.getFormhash(context));

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(context)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(context));

        BaseHttp.post(Url.DOMAIN, params, callback);
    }


    public static void checkFriend(final Context context, String uid,String optype, final CheckFriendCallback callback) {

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "addfriend");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("check", "1");//检查标识字段固定为1
        params.addQueryStringParameter("uid", uid);
        params.addQueryStringParameter("optype", optype);

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(context)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(context));


        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    public static void removeFriend(final Context context, String uid, final StringCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "removefriend");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("uid", uid);


        ZogUtils.printError(FriendHttp.class, "ClanBaseUtils.getFormhash(context):"
                + ClanBaseUtils.getFormhash(context));


        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(context)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(context));

        BaseHttp.post(Url.DOMAIN, params, callback);
    }
/**
* 同意添加好友，或者拒绝添加好友*/
    public static void agreedOrRefuseFriend(final Context context, String uid,String audit, final StringCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module","auditfriend");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("audit", audit);
        params.addQueryStringParameter("gid", "1");
        params.addQueryStringParameter("uid", uid);

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(context)))
            params.addQueryStringParameter("formhash", ClanBaseUtils.getFormhash(context));

        BaseHttp.post(Url.DOMAIN, params, callback);
    }


}
