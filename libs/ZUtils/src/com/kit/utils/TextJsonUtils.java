package com.kit.utils;

import com.google.gson.Gson;
import com.kit.app.enums.CharsetName;
import com.kit.config.AppConfig;

import java.io.File;
import java.util.List;

/**
 * Created by Zhao on 14-10-13.
 */
public class TextJsonUtils extends TextUtils {
    private static TextJsonUtils instance;
    private String FILE_NAME;
    private File FILE;

//    private Class clazz;


    public static TextJsonUtils getInstance() {

//        if (null == instance) {
        instance = new TextJsonUtils();
//        }
        return instance;
    }

    public TextJsonUtils setFile(String filename) {
        this.FILE_NAME = filename;
        return instance;
    }

    public TextJsonUtils setFile(File file) {
        this.FILE = file;
        return instance;
    }

    /**
     * 把Objgect Json化之后存入txt文件中
     *
     * @param obj
     */
    public void saveData(Object obj, Class clazz) {

        if (StringUtils.isNullOrEmpty(FILE_NAME)) {
            FILE_NAME = AppConfig.CACHE_DATA_DIR + clazz.getSimpleName();
        }

        ZogUtils.printLog(TextJsonUtils.class, FILE_NAME);

        Gson gson = new Gson();
        String str = gson.toJson(obj);


        TextUtils.writeStr2File(str, FILE_NAME, "UTF-8");

    }


    /**
     * 把List Json化之后存入txt文件中
     */
    public void saveDataList(List list, Class listIncludeClazz) {

        if (StringUtils.isNullOrEmpty(FILE_NAME)) {
            FILE_NAME = AppConfig.CACHE_DATA_DIR + listIncludeClazz.getSimpleName() + "List";
        }

        ZogUtils.printLog(TextJsonUtils.class, FILE_NAME);

        Gson gson = new Gson();
        String str = gson.toJson(list);


        TextUtils.writeStr2File(str, FILE_NAME, "UTF-8");

    }

    /**
     * 从Json化的txt文件中获取Json字符串
     *
     * @return
     */
    public Object getData(Class clazz) {
        if (StringUtils.isNullOrEmpty(FILE_NAME)) {
            FILE_NAME = AppConfig.CACHE_DATA_DIR + clazz.getSimpleName();
        }

        ZogUtils.printLog(TextJsonUtils.class, FILE_NAME);

        String str = TextUtils.readTxtFromLocal(FILE_NAME, CharsetName.UTF_8);

        Gson gson = new Gson();
        Object object = gson.fromJson(str, clazz);

        return object;
    }


    /**
     * 从Json化的txt文件中获取Json字符串
     *
     * Json直接转list貌似不能直接用 所以返回String
     * 自行在外面解析
     *
     * @return
     */
    public String getDataList(Class listIncludeClazz) {
        if (StringUtils.isNullOrEmpty(FILE_NAME)) {
            FILE_NAME = AppConfig.CACHE_DATA_DIR + listIncludeClazz.getSimpleName() + "List";
        }

        ZogUtils.printLog(TextJsonUtils.class, FILE_NAME);

        String str = TextUtils.readTxtFromLocal(FILE_NAME, CharsetName.UTF_8);
//        Gson gson = new Gson();
//        List list = gson.fromJson(str,
//                new TypeToken<List>() {
//                }.getType());
//
//        LogUtils.printLog(TextJsonUtils.class, "list.size():" + list.size());
        return str;
    }
}
