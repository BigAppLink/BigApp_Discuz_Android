package com.youzu.clan.base.util.jump;

import android.content.Context;

import com.kit.utils.StringUtils;
import com.kit.utils.UrlUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.clan.app.WebActivity;
import com.youzu.clan.app.receiver.MessageUrl;
import com.youzu.clan.article.ArticleDetailActivity;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.main.base.forumnav.DBForumNavUtils;
import com.youzu.clan.thread.detail.ThreadDetailActivity;
import com.youzu.clan.video.VideoPlayWebActivity;

import java.util.Map;

/**
 * Created by Zhao on 15/9/9.
 */
public class JumpWebUtils {
    /**
     * 跳转web页面
     *
     * @param context
     */
    public static void gotoWeb(Context context, String title, String url) {
        ZogUtils.printError(JumpWebUtils.class, "Url:::::" + url
                + " AppSPUtils.getConfig(context).getApiUrlBase():"
                + AppSPUtils.getConfig(context).getApiUrlBase());

        if (StringUtils.isEmptyOrNullOrNullStr(url)) {
            return;
        }

        url = url.trim();

        if (url.contains(AppSPUtils.getConfig(context).getApiUrlBase())) {
            //本站链接
            if (url.contains("mod=viewthread&tid=")) {
                gotoThreadDetail(context, url);
            } else if (url.contains("mod=view&aid=")) {
                gotoArticleDetail(context, url);
            } else if (url.contains("mod=forumdisplay&fid=")) {
                gotoForum(context, url);
            } else if (url.contains("mod=space&uid=")) {
                gotoProfile(context, url);
            } else {
                //静态url解析
                MessageUrl messageUrl = ClanUtils.checkUrlPatten(context, url);
                if (messageUrl != null) {
                    if ("portal_article".equals(messageUrl.type)) {
                        gotoArticleDetailByAid(context, messageUrl.id);
                    } else if ("forum_forumdisplay".equals(messageUrl.type)) {
                        gotoForumByFid(context, messageUrl.id, url);
                    } else if ("forum_viewthread".equals(messageUrl.type)) {
                        gotoThreadDetailByTid(context, messageUrl.id);
                    } else if ("home_space".equals(messageUrl.type)) {
                        gotoProfileByUid(context, messageUrl.value);
                    }
                } else {
                    toWeb(context, title, url);
                }
            }


        } else if (url.startsWith("http") && url.contains("http://v.youku.com/v_show")
                && !(context instanceof VideoPlayWebActivity)) {
            //当打开的链接为url，而且是优酷视频的url时候
            JumpVideoUtils.gotoVideo(context, title, url);
        } else {
            toWeb(context, title, url);
        }

    }


    private static void toWeb(Context context, String title, String url) {
        if (context instanceof ThreadDetailActivity
                || context instanceof ArticleDetailActivity) {
            //不然就在帖子详情页或文章详情页加载网页了 有点2。。。。。。。。
            BundleData bundleData = new BundleData();
            bundleData.put("title", title);
            bundleData.put("content", url);
            IntentUtils.gotoNextActivity(context, WebActivity.class, bundleData);
        } else {
            WebActivity.gotoWeb(context, WebActivity.class, title, url);
        }
    }

    private static void gotoProfile(Context context, String url) {
        //查看帖子详情
        //http://192.168.180.93:8080/discuz/home.php?mod=space&uid=13
        ZogUtils.printError(JumpWebUtils.class, "space");
        Map<String, String> map = UrlUtils.getUrlParameters(url);
        String uid = map.get("uid");
        ZogUtils.printError(JumpWebUtils.class, "uid:::::" + uid);
        JumpProfileUtils.gotoProfilePage(context, uid);

    }

    private static void gotoThreadDetail(Context context, String url) {
        //查看帖子详情
        //http://192.168.180.93:8080/discuz/forum.php?mod=viewthread&tid=685
        ZogUtils.printError(JumpWebUtils.class, "viewthread");
        Map<String, String> map = UrlUtils.getUrlParameters(url);
        String tid = map.get("tid");
        ZogUtils.printError(JumpWebUtils.class, "tid:::::" + tid);
        JumpThreadUtils.gotoThreadDetail(context, tid);

    }

    private static void gotoArticleDetail(Context context, String url) {
        //文章详情
//          http://192.168.180.93:8080/discuz/portal.php?mod=view&aid=9
        ZogUtils.printError(JumpWebUtils.class, "view");
        Map<String, String> map = UrlUtils.getUrlParameters(url);
        String aid = map.get("aid");
        ZogUtils.printError(JumpWebUtils.class, "aid:::::" + aid);
        JumpArticleUtils.gotoArticleDetail(context, aid);
    }


    private static void gotoForum(Context context, String url) {
        //板块详情
//          http://192.168.180.93:8080/discuz/forum.php?mod=forumdisplay&fid=2
        ZogUtils.printError(JumpWebUtils.class, "forumdisplay");
        Map<String, String> map = UrlUtils.getUrlParameters(url);
        String fid = map.get("fid");
        ZogUtils.printError(JumpWebUtils.class, "fid:::::" + fid);
        NavForum navForum = DBForumNavUtils.getNavForumById(context, fid);

        if (navForum != null)
            JumpForumUtils.gotoForum(context, navForum.getName(), navForum.getFid());
        else
            WebActivity.gotoWeb(context, WebActivity.class, "", url);
    }

    private static void gotoForumByFid(Context context, String fid, String url) {
        //板块详情
//          http://192.168.180.93:8080/discuz/forum.php?mod=forumdisplay&fid=2
        ZogUtils.printError(JumpWebUtils.class, "forumdisplay");
        ZogUtils.printError(JumpWebUtils.class, "fid:::::" + fid);
        NavForum navForum = DBForumNavUtils.getNavForumById(context, fid);

        if (navForum != null)
            JumpForumUtils.gotoForum(context, navForum.getName(), navForum.getFid());
        else
            WebActivity.gotoWeb(context, WebActivity.class, "", url);
    }

    private static void gotoThreadDetailByTid(Context context, String tid) {
        //查看帖子详情
        //http://192.168.180.93:8080/discuz/forum.php?mod=viewthread&tid=685
        ZogUtils.printError(JumpWebUtils.class, "viewthread");
        ZogUtils.printError(JumpWebUtils.class, "tid:::::" + tid);
        JumpThreadUtils.gotoThreadDetail(context, tid);
    }

    private static void gotoProfileByUid(Context context, String uid) {
        //查看帖子详情
        //http://192.168.180.93:8080/discuz/home.php?mod=space&uid=13
        ZogUtils.printError(JumpWebUtils.class, "space");
        ZogUtils.printError(JumpWebUtils.class, "uid:::::" + uid);
        JumpProfileUtils.gotoProfilePage(context, uid);

    }

    private static void gotoArticleDetailByAid(Context context, String aid) {
        //文章详情
//          http://192.168.180.93:8080/discuz/portal.php?mod=view&aid=9
        ZogUtils.printError(JumpWebUtils.class, "view");
        ZogUtils.printError(JumpWebUtils.class, "aid:::::" + aid);
        JumpArticleUtils.gotoArticleDetail(context, aid);
    }


}
