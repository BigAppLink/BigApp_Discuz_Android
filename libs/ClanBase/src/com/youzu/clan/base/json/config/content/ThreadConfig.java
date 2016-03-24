package com.youzu.clan.base.json.config.content;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/8/18.
 */
public class ThreadConfig {

    private ThreadConfigItem newThread;
    private ThreadConfigItem hotThread;
    private ThreadConfigItem favoriteThread;


    public ThreadConfigItem getNewThread() {
        return newThread;
    }

    @JSONField(name = "new")
    public void setNewThread(ThreadConfigItem newThread) {
        this.newThread = newThread;
    }

    public ThreadConfigItem getHotThread() {
        return hotThread;
    }

    @JSONField(name = "hot")
    public void setHotThread(ThreadConfigItem hotThread) {
        this.hotThread = hotThread;
    }

    public ThreadConfigItem getFavoriteThread() {
        return favoriteThread;
    }

    @JSONField(name = "favorite")
    public void setFavoriteThread(ThreadConfigItem favoriteThread) {
        this.favoriteThread = favoriteThread;
    }

    class ThreadConfigItem{
        private String enable;
        private String sort;

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }
    }
}
