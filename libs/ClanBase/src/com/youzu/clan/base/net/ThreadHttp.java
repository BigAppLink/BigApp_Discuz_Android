package com.youzu.clan.base.net;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.kit.utils.HtmlUtils;
import com.kit.utils.SetUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.clan.base.callback.FavThreadCallback;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.CheckPostJson;
import com.youzu.clan.base.json.threadview.ThreadDetailJson;
import com.youzu.clan.base.json.forumdisplay.ThreadTypes;
import com.youzu.clan.base.json.model.FileInfo;
import com.youzu.clan.base.json.thread.inner.Post;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.DateBaseUtils;
import com.youzu.clan.base.util.EmojiUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreadHttp {

    /**
     * 拉取点评列表
     * http://192.168.180.93:8080/discuz/api/mobile/iyz_index.php?iyzmobile=1&iyzversion=4&module=viewratings&tid=468&pid=1298
     *
     * @param context
     * @param tid
     * @param callback
     */
    public static void viewRatings(Context context, String tid, String pid, final StringCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "viewratings");
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("pid", pid);
        params.addQueryStringParameter("iyzmobile", "1");

        BaseHttp.get(Url.DOMAIN, params, callback);
    }

    /**
     * 评分
     *
     * @param context
     * @param tid
     * @param callback
     */
    public static void ratePost(Context context, String tid, String pid, String reason, HashMap<String, String> commentItems, final StringCallback callback) {
        String formhash = ClanBaseUtils.getFormhash(context);

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "ratepost");
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("pid", pid);
        params.addQueryStringParameter("iyzmobile", "1");


        params.addBodyParameter("formhash", formhash);
        params.addBodyParameter("reason", reason);

        Iterator<Map.Entry<String, String>> it = commentItems.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            params.addBodyParameter("score" + entry.getKey(), entry.getValue());
        }


        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    /**
     * 评分前置检查
     *
     * @param context
     * @param tid
     * @param callback
     */
    public static void checkRate(Context context, String tid, String pid, final StringCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "rate");
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("pid", pid);
        params.addQueryStringParameter("iyzmobile", "1");

        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    /**
     * 拉取点评列表
     *
     * @param context
     * @param tid
     * @param callback
     */
    public static void commentMore(Context context, String tid, String pid, final StringCallback callback) {
        String formhash = ClanBaseUtils.getFormhash(context);

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "commentmore");
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("pid", pid);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("iyzmobile", "1");


        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    /**
     * 点评
     *
     * @param context
     * @param tid
     * @param callback
     */
    public static void commentPost(Context context, String tid, String pid, String message, HashMap<String, String> commentItems, final StringCallback callback) {
        String formhash = ClanBaseUtils.getFormhash(context);

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "commentpost");
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("pid", pid);
        params.addQueryStringParameter("iyzmobile", "1");


        params.addBodyParameter("formhash", formhash);
        params.addBodyParameter("message", message);

        Iterator<Map.Entry<String, String>> it = commentItems.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            params.addBodyParameter(entry.getKey(), entry.getValue());
        }


        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    /**
     * 点评前置检查
     *
     * @param context
     * @param tid
     * @param callback
     */
    public static void checkComment(Context context, String tid, String pid, final StringCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "comment");
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("pid", pid);
        params.addQueryStringParameter("iyzmobile", "1");

        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    /**
     * 删除
     *
     * @param context
     * @param fid
     * @param tid
     * @param callback
     */
    public static void removeThread(Context context, String fid, String tid, final StringCallback callback) {
        String formhash = ClanBaseUtils.getFormhash(context);

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "removethread");
        params.addQueryStringParameter("iyzmobile", "1");

        params.addBodyParameter("fid", fid);
        params.addBodyParameter("tid", tid);
        params.addBodyParameter("formhash", formhash);
        params.addBodyParameter("reason", "Delete by Android application :)");

        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    /**
     * 投票
     *
     * @param context
     * @param fid
     * @param tid
     * @param pollanswers
     * @param callback
     */
    public static void voteThread(Context context, String fid, String tid, String[] pollanswers, final StringCallback callback) {
        String formhash = ClanBaseUtils.getFormhash(context);

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "pollvote");
        params.addQueryStringParameter("pollsubmit", "yes");
        params.addQueryStringParameter("quickforward", "yes");
        params.addQueryStringParameter("inajax", "1");
        params.addQueryStringParameter("fid", fid);

        params.addBodyParameter("tid", tid);
        params.addBodyParameter("formhash", formhash);

        for (String s : pollanswers) {
            params.addBodyParameter("pollanswers[]", s);
        }

        params.addBodyParameter("pollsubmit", "true");

        BaseHttp.post(Url.DOMAIN, params, callback);
    }


    public static void praiseThread(Context context, String tid, final StringCallback callback) {
        String formhash = ClanBaseUtils.getFormhash(context);

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "threadrecommend2");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("do", "add");
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("hash", formhash);
        params.addQueryStringParameter("inajax", "1");
        params.addQueryStringParameter("ajaxtarget", "recommend_add_menu_content");

        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    public static void praisePost(Context context, String tid, String pid, final StringCallback callback) {
        String formhash = ClanBaseUtils.getFormhash(context);

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "postsupport");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("do", "support");
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("pid", pid);
        params.addQueryStringParameter("hash", formhash);

        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    public static void getHomeThread(Context context, String type, final JSONCallback callback) {

        ClanHttpParams params = new ClanHttpParams(context);
        params.setCacheMode(HttpCache.CACHE_AND_REFRESH);
        params.setCacheTime(AppBaseConfig.CACHE_NET_TIME);
        if (!StringUtils.isEmptyOrNullOrNullStr(type)) {
            params.addQueryStringParameter("module", "indexthread");
            params.addQueryStringParameter("iyzmobile", "1");
            params.addQueryStringParameter("view", type);
        } else {
            params.addQueryStringParameter("module", "hotthread");
        }
        BaseHttp.get(Url.DOMAIN, params, callback);
    }

    public static void getHomeThread(Context context, String url, int page, final JSONCallback callback) {

        ClanHttpParams params = new ClanHttpParams(context);
        if ("1".equals(page)) {
            params.setCacheMode(HttpCache.CACHE_AND_REFRESH);
            params.setCacheTime(AppBaseConfig.CACHE_NET_TIME);
        }
        if (!StringUtils.isEmptyOrNullOrNullStr(url)) {
            if (url.endsWith("&page=")) {
                url += page;
            } else {
                url += "&page=" + page;
            }
            BaseHttp.get(url, params, callback);
        }
    }


    public static void getDataByUrl(Context context, String url, final JSONCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.clearParams();

        if (!StringUtils.isEmptyOrNullOrNullStr(url)) {
            BaseHttp.get(url, params, callback);
        }
    }


    /**
     * 获取主题详情
     *
     * @param context
     * @param tid
     * @param page
     * @param callback
     */
    public static void getThreadDetail(final Context context, String tid, String authorid, String page, String postno, final JSONCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "viewthread");
//        params.addQueryStringParameter("version", "4");
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("page", page);
        if (!StringUtils.isEmptyOrNullOrNullStr(authorid)) {
            params.addQueryStringParameter("authorid", authorid);
        }

        if (!StringUtils.isEmptyOrNullOrNullStr(postno)) {
            params.addQueryStringParameter("postno", postno);
        }


        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    /**
     * 收藏主题
     *
     * @param context
     * @param id
     * @param callback
     */
    public static void favThread(final Context context, String id, final FavThreadCallback callback) {


        String formhash = ClanBaseUtils.getFormhash(context);
        ZogUtils.printLog(ThreadHttp.class, "formhash:" + formhash);

        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "favthread");
//        params.addQueryStringParameter("version", "1");
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("inajax", "1");
//favoritesubmit=true&favoritesubmit_btn=true&formhash=62f0cd16&handlekey=k_favorite
        params.addBodyParameter("favoritesubmit", "true");
        params.addBodyParameter("favoritesubmit_btn", "true");
        params.addBodyParameter("handlekey", "k_favorite");

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(context)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(context));

        BaseHttp.post(Url.DOMAIN, params, callback);

    }


    /**
     * 删除收藏主题
     *
     * @param context
     * @param ids
     * @param callback
     */
    public static void delFavThread(final Context context, String[] ids, final FavThreadCallback callback) {


        String formhash = ClanBaseUtils.getFormhash(context);
        ZogUtils.printLog(ThreadHttp.class, "formhash:" + formhash);

        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "delfav");
//        params.addQueryStringParameter("version", "4");
        params.addQueryStringParameter("iyzmobile", "1");

        //formhash=c3a70aeb&delfavorite=true&favorite[]=85&favorite[]=8

        params.addBodyParameter("delfavorite", "true");
        for (String id : ids) {
            params.addBodyParameter("favorite[]", id);
        }

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(context)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(context));

        BaseHttp.post(Url.DOMAIN, params, callback);

    }


    /**
     * 发帖前置检查
     *
     * @param activity
     * @param fid              版块id，发新帖，回帖都需要这个字段
     * @param threadTypes      版块下面的分类， 发新帖需要这个字段
     * @param threadDetailJson 回复楼主需要这一个字段,当该字段为null时，标识是发新帖
     */
    public static void checkPost(final FragmentActivity activity, final String fid, final ThreadTypes threadTypes, final ThreadDetailJson threadDetailJson, HttpCallback callback) {
        checkPost(activity, fid, callback);
    }


    /**
     * 发帖前置检查
     *
     * @param activity
     * @param fid      版块id，发新帖，回帖都需要这个字段
     */
    public static void checkPost(final FragmentActivity activity, final String fid, HttpCallback callback) {


        final ClanHttpParams params = new ClanHttpParams(activity);
        params.addQueryStringParameter("module", "checkpost");
        params.addQueryStringParameter("fid", fid);

        BaseHttp.post(Url.DOMAIN, params, callback);
    }


    /**
     * 发帖前置检查
     *
     * @param activity
     * @param fid      版块id，发新帖，回帖都需要这个字段
     */
    public static CheckPostJson checkPostSync(final FragmentActivity activity, final String fid) {


        final ClanHttpParams params = new ClanHttpParams(activity);
        params.addQueryStringParameter("module", "checkpost");
        params.addQueryStringParameter("fid", fid);

        return BaseHttp.postSync(Url.DOMAIN, params, CheckPostJson.class);
    }


    /**
     * 文件上传
     *
     * @param activity
     * @param fid
     */
    public static void uploadFile(final FragmentActivity activity, String uid, String fid,
                                  String uploadhash, FileInfo fileInfo, final JSONCallback callback) {

        final ClanHttpParams params = new ClanHttpParams(activity);
        params.setTimeout(60 * 1000);

        params.addQueryStringParameter("module", "forumupload");
        params.addQueryStringParameter("fid", fid);
        params.addQueryStringParameter("iyzmobile", "1");

        params.addBodyParameter("hash", uploadhash);
        params.addBodyParameter("uid", uid);


        ZogUtils.printLog(ThreadHttp.class, "fileInfo.getFileType:" + fileInfo.getFileType());
        params.addBodyParameter("Filename", fileInfo.getFileName());
        params.addBodyParameter("Filetype", fileInfo.getFileType());
        params.addBodyParameter("Filedata", fileInfo.getFile());

        BaseHttp.post(Url.DOMAIN, params, callback);
//        BaseHttp.post(Url.DOMAIN, params, new ProgressCallback<Object>(activity) {
//            @Override
//            public void onSuccess(Object o) {
//                super.onSuccess(o);
//                callback.onSuccess(o);
//                FileUtils.saveFile("sdcard/upload.txt", JsonUtils.toJSON(o).toString());
//            }
//        });
    }


    /**
     * 回复主题
     *
     * @param activity
     * @ fid：用户在哪个板块回复帖子或主题
     * @ tid：用户回复的是哪个主题
     * @ attachnew[109][description]：本次回复内容携带一个附件，附件ID为109
     * @ noticetrimstr：如果不是回复顶楼，那么需要携带noticetrimstr参数，该参数需要客户端将被回复楼层的信息摘要提取出来，放在[quote] [/quote]中，其中格式和底色建议如示例所示，不做强硬要求。
     * @ eppid/repost：用户目前回复的是哪篇帖子，如果是回复主题（即顶楼）的话，把这两个参数删除即可；
     */
    public static void sendThreadReply(FragmentActivity activity,
                                       String message, ThreadDetailJson threadDetailJson, Post post,
                                       LinkedHashSet<String> attaches, HttpCallback callback) {
        ZogUtils.printError(ThreadHttp.class, "sendThreadReply sendThreadReply sendThreadReply");

        ZogUtils.printError(ThreadHttp.class, "sendThreadReply:::::" + message);

        ClanHttpParams params = new ClanHttpParams(activity);
        params.setTimeout(60 * 1000);

        params.addQueryStringParameter("fid", threadDetailJson.getVariables().getFid());
        params.addQueryStringParameter("tid", threadDetailJson.getVariables().getThread().getTid());
        params.addQueryStringParameter("module", "sendreply");
        params.addQueryStringParameter("replysubmit", "yes");

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(activity)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(activity));

        params.addBodyParameter("wysiwyg", "1");

        String upMessage;

        String append = "";
        if (!SetUtils.isNullOrContainEmpty(attaches)) {
            for (String s : attaches) {
                append = append + "\r\n[attachimg]" + s + "[/attachimg]";
            }
        }

        upMessage = message + append;

        params.addBodyParameter("message", upMessage);

        ZogUtils.printError(ThreadHttp.class, "upMessage:" + upMessage);

        if (post != null) {//回复主题帖子，不需要这两个参数
            params.addBodyParameter("reppid", post.getPid());
            params.addBodyParameter("reppost", post.getPid());

            String quoteStr = post.getMessage();
            ZogUtils.printError(ThreadHttp.class, "sendThreadReply 2222222222222");
            try {
                Pattern pattern = Pattern.compile("<div +class=\"quote\">.*?</div>");
                Matcher matcher = pattern.matcher(quoteStr);
                while (matcher.find()) {
                    ZogUtils.printError(ThreadHttp.class, "matcher.group(0):" + matcher.group(0));
                    quoteStr = quoteStr.replace(matcher.group(0), "");
                }

            } catch (Exception e) {
                Log.e("APP", e.getMessage());
            }

            ZogUtils.printError(ThreadHttp.class, "sendThreadReply 33333333333333");

            try {

                Pattern pattern2 = Pattern.compile("<div +class=\"reply_wrap\">.*?</div>");
                Matcher matcher2 = pattern2.matcher(quoteStr);
                while (matcher2.find()) {
                    ZogUtils.printError(ThreadHttp.class, "matcher.group(0):" + matcher2.group(0));
                    quoteStr = quoteStr.replace(matcher2.group(0), "");
                }
            } catch (Exception e) {
                Log.e("APP", e.getMessage());
            }

//            quoteStr = quoteStr.substring((quoteStr.lastIndexOf("</div>" + 5) == -1 ? 0 : quoteStr.lastIndexOf("</div>" + 5)), quoteStr.length());

            ZogUtils.printError(ThreadHttp.class, "quoteStr:" + quoteStr);
            try {
                quoteStr = EmojiUtils.getQuoteStr(quoteStr);
            } catch (Exception e) {
                ZogUtils.showException(e);
            }

            ZogUtils.printError(ThreadHttp.class, "sendThreadReply 44444444444444");

            String delHtmlStr = quoteStr;
            try {
                delHtmlStr = HtmlUtils.delHTMLTag(quoteStr);
            } catch (Exception e) {
                ZogUtils.showException(e);
            }
            ZogUtils.printError(ThreadHttp.class, "sendThreadReply 55555555555555");

            try {
                delHtmlStr = EmojiUtils.tag2Face(delHtmlStr);
            } catch (Exception e) {
                ZogUtils.showException(e);
            }

            Log.e("APP", "delHtmlStr:" + delHtmlStr);

            String postQuoteStr = delHtmlStr;
            try {
                postQuoteStr = (delHtmlStr.length() > AppBaseConfig.NUM_QUOTE_MESSAGE
                        ? delHtmlStr.substring(0, AppBaseConfig.NUM_QUOTE_MESSAGE) : delHtmlStr);
            } catch (Exception e) {
                ZogUtils.showException(e);
            }
            ZogUtils.printError(ThreadHttp.class, "sendThreadReply 6666666666666");

            try {
                postQuoteStr = EmojiUtils.face2Tag(postQuoteStr);
            } catch (Exception e) {
                ZogUtils.showException(e);
            }

            Log.e("APP", "postQuoteStr:" + postQuoteStr);

            params.addBodyParameter("noticetrimstr", "[quote][color=#999999]"
                    + post.getAuthor()
                    + " 发表于 "
                    + DateBaseUtils.getDate4Discuz(post.getDateline(), DateBaseUtils.YEAR_MONTH_DAY_HOUR_MIN_SEC)
                    + "[/color][color=#999999]\r\n"
                    + postQuoteStr
                    + " [/color][/quote]");

        }
        params.addBodyParameter("usesig", "1");
        setAttaches(params, attaches);
        BaseHttp.post(Url.DOMAIN, params, callback);
    }


    /**
     * 回复主题
     *
     * @param activity
     * @param fid：用户在哪个板块回复帖子或主题
     * @param subject：主题标题
     * @param attaches：本次回复内容携带一个附件，附件ID为109
     * @param message：主题内容
     */
    public static void newThread(FragmentActivity activity, String fid, String typeid, String subject,
                                 String message, Object attaches, HttpCallback callback) {

        ZogUtils.printError(ThreadHttp.class, "newThread:::::" + message);

        final ClanHttpParams params = new ClanHttpParams(activity);
        params.setTimeout(60 * 1000);

        params.addQueryStringParameter("fid", fid);
        params.addQueryStringParameter("module", "newthread");
        params.addQueryStringParameter("topicsubmit", "yes");

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(activity)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(activity));


        params.addBodyParameter("wysiwyg", "1");
        params.addBodyParameter("allownoticeauthor", "1");
        if (!TextUtils.isEmpty(typeid)) {
            params.addBodyParameter("typeid", typeid);
        }
        params.addBodyParameter("subject", subject);
        params.addBodyParameter("message", message);

        setAttaches(params, attaches);

        BaseHttp.post(Url.DOMAIN, params, callback);
    }


    private static ClanHttpParams setAttaches(ClanHttpParams params, Object attaches) {
        if (attaches == null) {
            return params;
        }

        if (attaches instanceof LinkedHashMap
                && attaches != null) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) attaches;
            if (map != null && !map.isEmpty()) {
                ZogUtils.printLog(ThreadHttp.class, "map.size():" + map.size());

                for (String key : map.keySet()) {
                    params.addBodyParameter("attachnew[" + key + "][description]", map.get(key));
                }
            }
        } else if (attaches instanceof Set) {
            Set<String> list = (Set<String>) attaches;
            if (list != null
                    && !list.isEmpty()) {
                ZogUtils.printLog(ThreadHttp.class, "list.size():" + list.size());

                for (String key : list) {
                    params.addBodyParameter("attachnew[" + key + "][description]", "");
                }
            }
        }

        return params;
    }

    public static void threadType(Context context, String fid, HttpCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("fid", fid);
        params.addQueryStringParameter("module", "thrdtype");
        params.addQueryStringParameter("iyzmobile", "1");
        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    /**
     * 购买帖子前置
     */
    public static void buyThreadPre(Context context, String tid, String pid, final StringCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "threadpay");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("tid", tid);
        params.addQueryStringParameter("pid", pid);
        params.addQueryStringParameter("handlekey", "pay");
        params.addQueryStringParameter("ajaxtarget", "fwin_content_pay");
        BaseHttp.get(Url.DOMAIN, params, callback);
    }
    /**
     * 购买帖子
     */
    public static void buyThread(Context context, String tid, String pid, final StringCallback callback) {
        String formhash = ClanBaseUtils.getFormhash(context);
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "threadpay");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addBodyParameter("formhash", formhash);
        params.addBodyParameter("referer", "forum.php");
        params.addBodyParameter("tid", tid);
        params.addBodyParameter("handlekey", "pay");
        params.addBodyParameter("paysubmit", "true");
        BaseHttp.post(Url.DOMAIN, params, callback);
    }
}
