package com.youzu.clan.app.service;

import android.app.IntentService;
import android.content.Intent;

import com.kit.utils.AppUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.common.Action;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.FavForumJson;
import com.youzu.clan.base.json.FavThreadJson;
import com.youzu.clan.base.json.article.ArticleFav;
import com.youzu.clan.base.json.article.ArticleFavJson;
import com.youzu.clan.base.json.article.ArticleFavVariables;
import com.youzu.clan.base.json.config.AdInfo;
import com.youzu.clan.base.json.favforum.FavForumVariables;
import com.youzu.clan.base.json.favforum.Forum;
import com.youzu.clan.base.json.favthread.FavThreadVariables;
import com.youzu.clan.base.json.favthread.Thread;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.net.ArticleHttp;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.net.LoadEmojiUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.Tools;
import com.youzu.clan.main.base.forumnav.DBForumNavUtils;
import com.youzu.clan.myfav.MyFavUtils;

import java.util.ArrayList;

public class ClanService extends IntentService {


    public ClanService() {
        super("ClanService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (Action.ACTION_NAV_FORUM.equals(intent.getAction())) {
            ArrayList<NavForum> forums = new ArrayList<NavForum>();
            NavForum f = new NavForum();
            f.setFid("1");
            forums.add(f);
            DBForumNavUtils.saveOrUpdateAllForum(this, forums);

            AppUtils.sleep(100);

        } else if (Action.ACTION_MY_FAV.equals(intent.getAction())) {
            updateFavForum();
            updateFavThread();
            updateFavArticle();
        } else if (Action.ACTION_CHECK_NEW_MESSAGE.equals(intent.getAction())) {
            ClanUtils.checkNewMessage(this);
        } else if (Action.ACTION_AD_CLICK.equals(intent.getAction())) {
            AdInfo adInfo = (AdInfo) intent.getSerializableExtra(Key.KEY_AD_INFO);
            uploadAdInfo(adInfo);
        } else if (Action.ACTION_AD_SHOW.equals(intent.getAction())) {
            AdInfo adInfo = (AdInfo) intent.getSerializableExtra(Key.KEY_AD_INFO);
            uploadAdInfo(adInfo);
        } else if (Action.ACTION_DOWNLOAD_EMOJI.equals(intent.getAction())) {
            LoadEmojiUtils.loadSmileyZipUrl(this);
        }

    }

    private void uploadAdInfo(AdInfo adInfo) {
        final ClanHttpParams params = new ClanHttpParams(this);
        params.addBodyParameter("recom_id", adInfo.getRecomId());
        params.addBodyParameter("action", adInfo.getAction());
        params.addBodyParameter("forum_id", adInfo.getForumId());
        params.addBodyParameter("app_bundleid", getPackageName());
        params.addBodyParameter("app_name", Tools.getApplicationName(this));
        params.addBodyParameter("app_version", Tools.getAppVersion(this));
        params.addBodyParameter("device", Tools.getModel());
        params.addBodyParameter("device_operator", Tools.getNetOperator(this));
        params.addBodyParameter("device_os", Tools.getOs(this));
        params.addBodyParameter("device_reachable", Tools.getNetworkType(this));
        params.addBodyParameter("device_resolution", Tools.getDevicePixels(this));
        params.addBodyParameter("forum_id", adInfo.getForumId());
        params.addBodyParameter("fourm_name", adInfo.getForumName());
        params.addBodyParameter("fourm_nav_id", adInfo.getForumNavId());
        params.addBodyParameter("fourm_nav_name", adInfo.getForumNavName());
        params.addBodyParameter("idfa", Tools.getDeviceId(this));
        params.addBodyParameter("show_position", String.valueOf(adInfo.getShowPosition()));
        params.addBodyParameter("random_num", adInfo.getRandomNum());
        //设备信息：
        //device_operator 运营商
        //device_reachable 联网方式

    }


    public void checkNewMessage() {

//        ClanHttpParams params = new ClanHttpParams(this);
//        params.addQueryStringParameter("module", "checknewpm");
//        params.addQueryStringParameter("iyzmobile", "1");
//        NewMessageJson json = (NewMessageJson) BaseHttp.postSync(Url.DOMAIN, params, NewMessageJson.class);
//        if (json != null) {
//            int count = 0;
//            try {
//                count = Integer.valueOf(json.getNewpm());
//            } catch (Exception e) {
//            }
//            AppSPUtils.saveNewMessage(this, count);
//
////            Intent intent = new Intent(this, GuideActivity.class);
//
//            if (count > 0) {
//                NotifyUtils.mkNotity(this, ClanApplication.getNotificationManager()
//                        , null, null, "新消息", "你有" + count + "个好友发来新消息", R.drawable.ic_launcher, R.drawable.ic_notify, NotifyUtils.NOTIFY, R.string.app_name);
//            } else {
//                NotifyUtils.cancel(ClanApplication.getNotificationManager(), NotifyUtils.NOTIFY);
//            }
//        }
    }


    public void updateFavForum() {
        int page = 1;
        boolean hasMore = true;
        while (hasMore) {
            final ClanHttpParams params = new ClanHttpParams(this);
            params.addQueryStringParameter("module", "myfavforum");
            params.addQueryStringParameter("page", String.valueOf(page));
            FavForumJson json = null;
            try {
                json = (FavForumJson) BaseHttp.postSync(Url.DOMAIN, params, FavForumJson.class);
            } catch (Exception e) {
                ZogUtils.showException(e);
            }

            if (json == null) {
                return;
            }

            FavForumVariables variables = json.getVariables();
            if (variables == null) {
                return;
            }

            final ArrayList<Forum> forums = variables.getList();
            if (forums == null || forums.size() < 1) {
                return;
            }
            hasMore = "1".equals(variables.getNeedMore());
            if (page == 1) {
                MyFavUtils.deleteAllForum(this);
            }
            MyFavUtils.saveOrUpdateAllForum(this, forums);
            page++;
            try {
                java.lang.Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateFavThread() {
        int page = 1;
        boolean hasMore = true;
        while (hasMore) {
            final ClanHttpParams params = new ClanHttpParams(this);
            params.addQueryStringParameter("module", "myfavthread");
            params.addQueryStringParameter("page", String.valueOf(page));
            FavThreadJson json = null;
            try {
                json = (FavThreadJson) BaseHttp.postSync(Url.DOMAIN, params, FavThreadJson.class);
            } catch (Exception e) {
                ZogUtils.showException(e);
            }
            if (json == null) {
                return;
            }

            FavThreadVariables variables = json.getVariables();
            if (variables == null) {
                return;
            }

            final ArrayList<Thread> forums = variables.getList();
            if (forums == null || forums.size() < 1) {
                return;
            }
            hasMore = "1".equals(variables.getNeedMore());
            if (page == 1) {
                MyFavUtils.deleteAllThread(this);
            }
            MyFavUtils.saveOrUpdateAllThread(this, forums);
            page++;
            try {
                java.lang.Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateFavArticle() {
        int page = 1;
        boolean hasMore = true;
        while (hasMore) {

            ArticleFavJson json = null;

            try {
                json = BaseHttp.postSync(Url.DOMAIN, ArticleHttp.getArticleFavsParams(this, page), ArticleFavJson.class);
            } catch (Exception e) {
                ZogUtils.showException(e);
            }

            if (json == null) {
                return;
            }
            ArticleFavVariables variables = json.getVariables();
            if (variables == null) {
                return;
            }

            final ArrayList<ArticleFav> articles = variables.getList();
            if (ListUtils.isNullOrContainEmpty(articles)) {
                return;
            }
            hasMore = "1".equals(variables.getNeedMore());
            if (page == 1) {
                MyFavUtils.deleteAllArticle(this);
            }
            MyFavUtils.saveOrUpdateAllArticle(this, articles);
            page++;
            try {
                java.lang.Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
