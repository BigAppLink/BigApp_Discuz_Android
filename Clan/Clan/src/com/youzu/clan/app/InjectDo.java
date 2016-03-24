package com.youzu.clan.app;

/**
 * Created by Zhao on 15/7/10.
 */
public interface InjectDo<T> {


    /**
     * 如果返回为true 继续执行下一步,如弹出toast
     * @param baseJson
     * @return
     */
    public boolean doSuccess(T baseJson);

    /**
     * 如果返回为true 继续执行下一步，如弹出toast
     * @param baseJson
     * @param tag
     * @return
     */
    public boolean doFail(T baseJson,String tag);
}
