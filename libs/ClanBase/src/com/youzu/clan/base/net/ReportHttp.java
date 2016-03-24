package com.youzu.clan.base.net;

import android.support.v4.app.FragmentActivity;

import com.kit.utils.StringUtils;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.threadview.Report;
import com.youzu.clan.base.util.ClanBaseUtils;

/**
 * Created by tangh on 2015/8/31.
 */
public class ReportHttp {

    public static void report(FragmentActivity activity, String uid, String tid, String fid, Report report, int position, HttpCallback<String> callback) {

        ClanHttpParams params = new ClanHttpParams(activity);
        params.addQueryStringParameter("module", "report");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("inajax", "1");
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("uid", uid);
        params.addBodyParameter("report_select", report.getContent().get(position));
        params.addBodyParameter("message", report.getContent().get(position));

        params.addBodyParameter("referer", "forum.php");
        params.addBodyParameter("reportsubmit", "true");
        params.addBodyParameter("rtype", "thread");//表示就举报的类型，是帖子或者回复等
        params.addBodyParameter("rid", tid);//目前等于帖子的id。

        params.addBodyParameter("fid", fid);
        params.addBodyParameter("handlekey", report.getHandlekey());


        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(activity)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(activity));

        BaseHttp.post(Url.DOMAIN, params, callback);
    }
}
