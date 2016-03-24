package com.youzu.clan.base.util.view.threadandarticle;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.DbUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.json.article.ReadArticle;
import com.youzu.clan.base.json.forumdisplay.Thread;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.json.thread.BaseThread;
import com.youzu.clan.base.json.thread.ReadThread;
import com.youzu.clan.base.util.AppUSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.main.base.forumnav.DBForumNavUtils;
import com.youzu.clan.thread.ThreadPublishActivity;
import com.youzu.clan.threadandarticle.ForumNameOCL;

import java.util.List;

/**
 * 设置文章和帖子列表
 * Created by Zhao on 15/11/5.
 */
public class ThreadAndArticleItemUtils extends ContentUtils {

    /**
     * 设置板块名
     *
     * @param context
     * @param thread
     * @param tvForumName
     */
    public static void setForumName(Context context, com.youzu.clan.base.json.forumdisplay.Thread thread, TextView tvForumName) {
        tvForumName.setTextColor(ThemeUtils.getThemeColor(context));
        tvForumName.setText(thread.getForumName());
        ForumNameOCL forumNameOCL = new ForumNameOCL(context, thread.getForumName(), thread.getFid());
        tvForumName.setOnClickListener(forumNameOCL);

    }


    /**
     * 控制显示精华等标签图片
     *
     * @param thread
     * @param tag1
     * @param tag2
     * @param tag3
     */
    public static void showTags(Thread thread, ImageView tag1, ImageView tag2, ImageView tag3) {
        //精华
        if (isHaveDigest(thread)) {
            ZogUtils.printError(ThreadAndArticleItemUtils.class, thread.getSubject() + " " + thread.getDigest());
            tag1.setVisibility(View.VISIBLE);
        } else {
            tag1.setVisibility(View.GONE);
        }

        //热帖
        if (isHaveHotInTitle(thread)) {
            ZogUtils.printError(ThreadAndArticleItemUtils.class, thread.getSubject() + " " + thread.getDigest());
            tag2.setVisibility(View.VISIBLE);
        } else {
            tag2.setVisibility(View.GONE);
        }

        //推荐
        if (isHaveRecommendInTitle(thread)) {
            ZogUtils.printError(ThreadAndArticleItemUtils.class, thread.getSubject() + " " + thread.getDigest());
            tag3.setVisibility(View.VISIBLE);
        } else {
            tag3.setVisibility(View.GONE);
        }

    }


    /**
     * 过滤要显示的tag
     *
     * @param context
     * @param thread
     * @return 返回为false的时候是表示不显示
     */
    public static boolean filterTagInTitle(Context context, BaseThread thread) {


        if (!StringUtils.isEmptyOrNullOrNullStr(thread.getIcon())
                && !isHaveDigestIconInTitle(thread)
                && !isHaveHotInTitle(thread)
                && !isHaveRecommendInTitle(thread)
                && Integer.parseInt(thread.getIcon()) >= 0) {
            return true;
        }

        if (isHaveDigestIconInTitle(thread)
                && !isShowDigetstInTitle(context, thread)) {
            return false;
        }

        if (isHaveHotInTitle(thread) &&
                !isShowHotInTitle(context, thread)) {
            return false;
        }

        if (isHaveRecommendInTitle(thread)
                && !isShowRecommendInTitle(context, thread)) {
            return false;
        }


        return false;
    }


    /////////////////////////

    /**
     * 是否启用显示热帖
     *
     * @param context
     * @param thread
     * @return
     */
    public static boolean isShowRecommendInTitle(Context context, BaseThread thread) {

        if (!AppUSPUtils.isUShowRecommendInTitle(context))
            return false;

        if (isHaveRecommendInTitle(thread)) {
            return true;
        }

        return false;
    }

    /**
     * 标题是否包含ret
     *
     * @param thread
     * @return
     */
    public static boolean isHaveRecommendInTitle(BaseThread thread) {
        if (thread == null)
            return false;

        if (!StringUtils.isEmptyOrNullOrNullStr(thread.getIcon())
                && (Integer.parseInt(thread.getIcon()) == 5 || Integer.parseInt(thread.getIcon()) == 14)) {
            return true;
        }

        return false;
    }


    /////////////////////////

    /**
     * 是否启用显示热帖
     *
     * @param context
     * @param thread
     * @return
     */
    public static boolean isShowHotInTitle(Context context, BaseThread thread) {

        if (!AppUSPUtils.isUShowHotInTitle(context))
            return false;

        if (isHaveHotInTitle(thread)) {
            return true;
        }

        return false;
    }

    /**
     * 标题是否包含热帖
     *
     * @param thread
     * @return
     */
    public static boolean isHaveHotInTitle(BaseThread thread) {
        if (thread == null)
            return false;

        if (!StringUtils.isEmptyOrNullOrNullStr(thread.getIcon())
                && (Integer.parseInt(thread.getIcon()) == 1 || Integer.parseInt(thread.getIcon()) == 10)) {
            return true;
        }

        return false;
    }


    /////////////////////////

    /**
     * 是否启用显示精华
     *
     * @param context
     * @param thread
     * @return
     */
    public static boolean isShowDigetst(Context context, BaseThread thread) {

        if (!AppUSPUtils.isUShowDigetstInTitle(context))
            return false;

        if (isHaveDigest(thread)) {
            return true;
        }

        return false;
    }


    /**
     * 是否在标题启用显示精华
     *
     * @param context
     * @param thread
     * @return
     */
    public static boolean isShowDigetstInTitle(Context context, BaseThread thread) {

        if (!AppUSPUtils.isUShowDigetstInTitle(context))
            return false;

        if (isHaveDigestIconInTitle(thread)) {
            return true;
        }

        return false;
    }

    /**
     * 标题是否包含精华
     * <p/>
     * 精华比较特别，icon中包含有精华，并且有digest字段标识精华，digest区分精华1、精华2、精华3，但icon中不区分
     *
     * @param thread
     * @return
     */
    public static boolean isHaveDigest(BaseThread thread) {
        if (thread == null)
            return false;

        if (!StringUtils.isEmptyOrNullOrNullStr(thread.getDigest())
                && ((Integer.parseInt(thread.getDigest()) > 0) || isHaveDigestIconInTitle(thread))) {
            return true;
        }

        return false;
    }

    /**
     * ICON类型标题是否包含精华
     *
     * @param thread
     * @return
     */
    public static boolean isHaveDigestIconInTitle(BaseThread thread) {
        if (thread == null)
            return false;

        if (!StringUtils.isEmptyOrNullOrNullStr(thread.getIcon())
                && (Integer.parseInt(thread.getIcon()) == 0 || Integer.parseInt(thread.getIcon()) == 9)) {
            return true;
        }

        return false;
    }


    /////////////////////////

    /**
     * 保存已读
     *
     * @param context
     * @param tid
     */
    public static void saveReadTid(Context context, String tid) {
        ReadThread thread = new ReadThread();
        thread.setTid(tid);
        thread.setTs(System.currentTimeMillis());
        try {
            DbUtils.create(context).saveOrUpdate(thread);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取已读
     *
     * @param context
     * @param tid
     * @return
     */
    public static boolean hasRead(Context context, String tid) {
        //好不容易做完已读，结果又尼玛去掉了！！！！！！
//
//        try {
//            DbUtils dbUtils = DbUtils.create(context);
//            ReadThread thread = (ReadThread) dbUtils.findFirst(Selector.from(
//                    ReadThread.class).where("tid", "=", tid));
//            if (thread != null) {
//                long nowTs = System.currentTimeMillis();
//                if (nowTs - thread.getTs() > 24 * 60 * 60 * 1000) {
//                    dbUtils.dropTable(ReadThread.class);
//                    return false;
//                }
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return false;
    }


    /**
     * @param context
     * @param aid
     */
    public synchronized static void saveReadAid(Context context, String aid) {
        ReadArticle article = new ReadArticle();
        article.setAid(aid);
        article.setTs(System.currentTimeMillis());
        try {
            DbUtils.create(context).saveOrUpdate(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判定文章是否已读
     *
     * @param context
     * @param aid
     * @return
     */
    public static boolean articleHasRead(Context context, String aid) {
//        try {
//            DbUtils dbUtils = DbUtils.create(context);
//            ReadArticle article = dbUtils.findFirst(Selector.from(
//                    ReadArticle.class).where("aid", "=", aid));
//            if (article != null) {
//                long nowTs = System.currentTimeMillis();
//                if (nowTs - article.getTs() > 24 * 60 * 60 * 1000) {
//                    dbUtils.dropTable(ReadArticle.class);
//                    return false;
//                }
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return false;
    }


    public static void addThread(Activity context) {
        if (ClanUtils.isToLogin(context, null, Activity.RESULT_OK, false)) {
            return;
        }
        List<NavForum> forums = DBForumNavUtils.getAllNavForum(context);
        if (ListUtils.isNullOrContainEmpty(forums)) {
            ToastUtils.mkShortTimeToast(context, context.getString(R.string.wait_a_moment));
        } else
            IntentUtils.gotoNextActivity(context, ThreadPublishActivity.class);
    }

}
