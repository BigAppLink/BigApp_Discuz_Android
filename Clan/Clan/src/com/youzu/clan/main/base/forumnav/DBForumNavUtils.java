package com.youzu.clan.main.base.forumnav;

import android.content.Context;

import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.DbUtils;
import com.youzu.android.framework.db.sqlite.Selector;
import com.youzu.android.framework.db.sqlite.WhereBuilder;
import com.youzu.clan.base.json.forumnav.NavForum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhao on 15/7/23.
 */
public class DBForumNavUtils {


    private DBForumNavUtils() {

    }

    public static synchronized List<NavForum> getAllNavForum(Context context) {
        DbUtils dbUtils = DbUtils.create(context);
        List<NavForum> forums = new ArrayList<>();
        List<NavForum> allForums = null;
        try {
            allForums = dbUtils.findAll(NavForum.class);
            ZogUtils.printError(ForumnavFragment.class, "getAllNavForum get forums:" + allForums.size());
            NavForum lastforum = null;
            NavForum lastthread = null;
            for (int i = 0; i < allForums.size(); i++) {
                NavForum forum = allForums.get(i);
//                ZogUtils.printError(ForumnavFragment.class, "saveOrUpdateAllForum get forums:" + forum.grade);
                if (forum.grade == 1) {
                    lastforum = forum;
                    forums.add(forum);
                } else if (forum.grade == 2) {
                    lastthread = forum;
                    if (lastforum == null) {
                        continue;
                    }
                    if (lastforum.getForums() == null) {
                        lastforum.setForums(new ArrayList<NavForum>());
                    }
                    lastforum.getForums().add(forum);
                } else {
                    if (lastthread == null) {
                        continue;
                    }
                    if (lastthread.getSubs() == null) {
                        lastthread.setSubs(new ArrayList<NavForum>());
                    }
                    lastthread.getSubs().add(forum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forums;
    }

    public static synchronized NavForum getNavForumById(Context context, String forumId) {
        DbUtils dbUtils = DbUtils.create(context);
        try {
            return dbUtils.findFirst(Selector.from(NavForum.class).where("fid", "=", forumId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized void saveOrUpdateForum(Context context, NavForum forum) {
        DbUtils dbUtils = DbUtils.create(context);
        try {
            dbUtils.saveOrUpdate(forum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void saveOrUpdateAllForum(final Context context, final List<NavForum> forums) {
        saveOrUpdateAllForum(context, forums, null);
    }

    public static synchronized void saveOrUpdateAllForum(final Context context, final List<NavForum> forums, final DoSomeThing doSomeThing) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ZogUtils.printError(ForumnavFragment.class, "saveOrUpdateAllForum get forums:" + forums.size());
                    List<NavForum> allForums = new ArrayList<>();
                    for (int i = 0; i < forums.size(); i++) {
                        NavForum forum = forums.get(i);
                        if (forum == null) {
                            continue;
                        }
                        forum.grade = 1;
                        allForums.add(forum);
                        for (int j = 0; forum.getForums() != null && j < forum.getForums().size(); j++) {
                            NavForum thread = forum.getForums().get(j);
                            if (thread == null) {
                                continue;
                            }
                            thread.grade = 2;
                            allForums.add(thread);
                            if (thread.getSubs() != null) {
                                for (int k = 0; k < thread.getSubs().size(); k++) {
                                    NavForum subThread = thread.getSubs().get(k);
                                    if (subThread != null) {
                                        subThread.grade = 3;
                                        allForums.add(subThread);
                                    }
                                }
                            }
                        }
                    }
                    DbUtils dbUtils = DbUtils.create(context);
                    ZogUtils.printError(DBForumNavUtils.class, "saveOrUpdateAllForum get forums size:" + allForums.size());
                    dbUtils.saveOrUpdateAll(allForums);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (doSomeThing != null) {
                        doSomeThing.execute();
                    }
                }
            }
        }).start();
    }

    public static synchronized void deleteForum(Context context, String forumId) {
        DbUtils dbUtils = DbUtils.create(context);
        try {
            dbUtils.delete(NavForum.class, WhereBuilder.b("id", "=", forumId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void deleteAllForum(Context context) {
        DbUtils dbUtils = DbUtils.create(context);
        try {
            dbUtils.dropTable(NavForum.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
