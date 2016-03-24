package com.youzu.clan.base.net;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.kit.utils.StringUtils;
import com.kit.utils.ZZLogUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.act.ActPlayer;
import com.youzu.clan.base.json.act.ActPublishInfo;
import com.youzu.clan.base.json.act.JoinField;
import com.youzu.clan.base.json.act.SpecialActivity;
import com.youzu.clan.base.util.ClanBaseUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class ActHttp {

    public static void sendActPublish(FragmentActivity activity,
                                      ActPublishInfo info,
                                      Object attaches, HttpCallback callback) {
        ZZLogUtils.log("ActHttp", "sendActPublish:::::" + JsonUtils.toJSONString(info));
        ClanHttpParams params = new ClanHttpParams(activity);
        params.setTimeout(60 * 1000);
        params.addQueryStringParameter("fid", info.fid);
        params.addQueryStringParameter("module", "newthread");
        params.addQueryStringParameter("topicsubmit", "yes");
        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(activity))) {
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(activity));
        }
        params.addBodyParameter("wysiwyg", "1");
        params.addBodyParameter("special", "4");
        params.addBodyParameter("allownoticeauthor", "1");
        params.addBodyParameter("usesig", "1");
        params.addBodyParameter("subject", info.subject);
        params.addBodyParameter("activitytime", "0");
        params.addBodyParameter("starttimefrom[0]", info.starttimefrom);

        params.addBodyParameter("activityplace", info.activitycity);
        params.addBodyParameter("activitycity", info.activitycity);
        params.addBodyParameter("activityclass", info.activityclass);
        params.addBodyParameter("message", info.message);
        if (!TextUtils.isEmpty(info.extfield)) {
            params.addBodyParameter("extfield", info.extfield);
        }
        if (!TextUtils.isEmpty(info.activityaid)) {
            params.addBodyParameter("activityaid", info.activityaid);
            if (!TextUtils.isEmpty(info.activityaid_url)) {
                params.addBodyParameter("activityaid_url", info.activityaid_url);
            }
        }
        setUserFileds(params, info.userfields);
        setAttaches(params, attaches);
        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    public static void sendActApplyReceipt(FragmentActivity activity, String operation,
                                           String tid, String reason, ArrayList<ActPlayer> applyidarray,
                                           HttpCallback callback) {
        ZZLogUtils.log("ActHttp", "sendActApplyReceipt::::: operation = " + operation + ", tid = " + tid + ", reason = " + reason + ", applyidarray.size = " + applyidarray.size());
        ClanHttpParams params = new ClanHttpParams(activity);
        params.setTimeout(60 * 1000);
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("module", "activityapplylist");
        params.addQueryStringParameter("applylistsubmit", "yes");
        params.addQueryStringParameter("iyzmobile", "1");
        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(activity))) {
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(activity));
        }
        params.addBodyParameter("operation", operation);
        params.addBodyParameter("handlekey", "activity");
        if (!TextUtils.isEmpty(reason)) {
            params.addBodyParameter("reason", reason);
        }
        setApplyidarray(params, applyidarray);
        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    public static void cancelApply(FragmentActivity activity,
                                   String fid, String tid, String pid, String message,
                                   HttpCallback callback) {
        ZZLogUtils.log("ActHttp", "cancelApply::::" + ", fid = " + fid + ", tid = " + tid + ", pid = " + pid + ", message = " + message);
        ClanHttpParams params = new ClanHttpParams(activity);
        params.setTimeout(60 * 1000);
        params.addQueryStringParameter("module", "activityclient");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("fid", fid);
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("pid", pid);
        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(activity))) {
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(activity));
        }
        if (!TextUtils.isEmpty(message)) {
            params.addBodyParameter("message", message);
        }
        params.addBodyParameter("activitycancel", "true");
        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    public static void sendActApply(FragmentActivity activity,
                                    String fid, String tid, String pid, SpecialActivity act, String message,
                                    HttpCallback callback) {
        ZZLogUtils.log("ActHttp", "sendActApply::::" + ", fid = " + fid + ", tid = " + tid + ", pid = " + pid + ", joinFileds = " + JsonUtils.toJSONString(act.getJoinFields()));
        ClanHttpParams params = new ClanHttpParams(activity);
        params.setTimeout(60 * 1000);
        params.addQueryStringParameter("module", "activityclient");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("fid", fid);
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("pid", pid);
        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(activity))) {
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(activity));
        }
        params.addBodyParameter("handlekey", "activityapplies");
        params.addBodyParameter("message", message);
        params.addBodyParameter("activitysubmit", "true");
        if ("积分".equals(act.getCredit_title())) {
            params.addBodyParameter("payment", "-1");
        } else {
            params.addBodyParameter("payment", "1");
            params.addBodyParameter("payvalue", "0");//TODO
        }
        if (act.getJoinFields() != null && act.getJoinFields().size() > 0) {
            for (JoinField field : act.getJoinFields()) {
                if ("text".equals(field.getFormType())
                        || ("select".equals(field.getFormType()))
                        || ("datepicker".equals(field.getFormType()))
                        || ("textarea".equals(field.getFormType()))
                        || ("radio".equals(field.getFormType()))
                        || ("select".equals(field.getFormType()))) {
                    params.addBodyParameter(field.getFieldId(), field.getDefaultValue());
                    continue;
                }
                if ("list".equals(field.getFormType()) || "checkbox".equals(field.getFormType())) {
                    setMultiFileds(params, field.getFieldId(), field.selected_multi);
                } else if ("file".equals(field.getFormType())) {
                    params.addBodyParameter(field.getFieldId(), field.abs_url);
                }
            }
        }
        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    private static ClanHttpParams setMultiFileds(ClanHttpParams params, String fieldId, String[] selected_multi) {
        if (selected_multi == null || selected_multi.length < 1) {
            return params;
        }
        for (String filed : selected_multi) {
            if (!TextUtils.isEmpty(filed)) {
                params.addBodyParameter(fieldId + "[]", filed);
            }
        }
        return params;
    }

    private static ClanHttpParams setUserFileds(ClanHttpParams params, ArrayList<String> userfields) {
        if (userfields == null || userfields.size() < 1) {
            return params;
        }
        for (String filed : userfields) {
            if (!TextUtils.isEmpty(filed)) {
                params.addBodyParameter("userfield[]", filed);
            }
        }
        return params;
    }

    private static ClanHttpParams setApplyidarray(ClanHttpParams params, ArrayList<ActPlayer> applyidarray) {
        if (applyidarray == null || applyidarray.size() < 1) {
            return params;
        }
        for (ActPlayer child : applyidarray) {
            if (child != null) {
                params.addBodyParameter("applyidarray[]", child.getApplyid());
            }
        }
        return params;
    }


    private static ClanHttpParams setAttaches(ClanHttpParams params, Object attaches) {
        if (attaches == null) {
            return params;
        }

        if (attaches instanceof LinkedHashMap
                && attaches != null) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) attaches;
            if (map != null && !map.isEmpty()) {
                ZogUtils.printLog(ActHttp.class, "map.size():" + map.size());

                for (String key : map.keySet()) {
                    params.addBodyParameter("attachnew[" + key + "][description]", map.get(key));
                }
            }
        } else if (attaches instanceof Set) {
            Set<String> list = (Set<String>) attaches;
            if (list != null
                    && !list.isEmpty()) {
                ZogUtils.printLog(ActHttp.class, "list.size():" + list.size());

                for (String key : list) {
                    params.addBodyParameter("attachnew[" + key + "][description]", "");
                }
            }
        }

        return params;
    }
}
