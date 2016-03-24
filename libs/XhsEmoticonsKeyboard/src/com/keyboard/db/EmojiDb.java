package com.keyboard.db;

import android.content.Context;
import android.util.Log;

import com.keyboard.bean.EmoticonBean;
import com.keyboard.bean.EmoticonSetBean;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

public class EmojiDb {
    private static final String DB_NAME = "smiley.db";
    private static final int DB_VERSION = 1;

    private EmojiDb() {
    }

    public static void addAllEmoji(Context context, List<EmoticonBean> beans) {
        DbUtils dbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, null);
        try {
            dbUtils.saveAll(beans);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static List<EmoticonBean> getAllEmojis(Context context) {
        DbUtils dbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, null);
        try {
            return dbUtils.findAll(EmoticonBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<EmoticonBean> getEmojiGroup(Context context, String group) {
        DbUtils dbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, null);
        try {
            return dbUtils.findAll(Selector.from(EmoticonBean.class).where("groupName", "=", group));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static EmoticonBean getEmojiByUnicode(Context context, String unicode) {
        DbUtils dbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, null);
        try {
            return dbUtils.findFirst(Selector.from(EmoticonBean.class).where("content", "=", unicode));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EmoticonBean getEmojiByShortname(Context context, String shortname) {
        DbUtils dbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, null);
        try {
            return dbUtils.findFirst(Selector.from(EmoticonBean.class).where("shortname", "=", shortname));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addEmojiSet(Context context, EmoticonSetBean setBean) {
        DbUtils dbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, null);
        try {
            Log.e("APP", "setBean.getDescription:" + setBean.getDescription());
            dbUtils.save(setBean);
            List<EmoticonBean> beans = setBean.getEmoticonList();
            addAllEmoji(context, beans);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    @Deprecated
    public static List<EmoticonSetBean> getEmojiSets(Context context) {
        DbUtils dbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, null);
        try {
            EmoticonSetBean setBean = dbUtils.findFirst(EmoticonSetBean.class);
            List<EmoticonBean> beans = getAllEmojis(context);
            setBean.setEmoticonList(beans);
            List<EmoticonSetBean> list = new ArrayList<EmoticonSetBean>();
            list.add(setBean);
            return list;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<EmoticonSetBean> getAllEmojiSet(Context context) {
        DbUtils dbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, null);
        try {
            List<EmoticonSetBean> setBean = dbUtils.findAll(EmoticonSetBean.class);
            return setBean;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<EmoticonSetBean> getEmojiSetsByName(Context context, String name) {
        DbUtils dbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, null);
        try {
            EmoticonSetBean setBean = dbUtils.findFirst(Selector.from(EmoticonSetBean.class).where("name", "=", name));
            List<EmoticonBean> beans = getAllEmojis(context);
            setBean.setEmoticonList(beans);
            List<EmoticonSetBean> list = new ArrayList<EmoticonSetBean>();
            list.add(setBean);
            return list;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<EmoticonSetBean> getEmojiLibraryByGroup(Context context, String name, String group) {
        DbUtils dbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, null);
        try {
            EmoticonSetBean setBean = dbUtils.findFirst(Selector.from(EmoticonSetBean.class).where("name", "=", name));
            List<EmoticonBean> beans = getEmojiGroup(context, group);
            setBean.setEmoticonList(beans);
            List<EmoticonSetBean> list = new ArrayList<EmoticonSetBean>();
            list.add(setBean);
            return list;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clear(Context context) {
        DbUtils dbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, null);
        try {
            dbUtils.dropTable(EmoticonSetBean.class);
            dbUtils.dropTable(EmoticonBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
