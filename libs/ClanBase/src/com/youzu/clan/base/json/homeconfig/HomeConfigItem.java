package com.youzu.clan.base.json.homeconfig;

/**
 * Created by Zhao on 15/6/30.
 */
public class HomeConfigItem {
    private String id;
    private String title;
    private String pic;
    private String url;
    private String type;
    private String desc;



    /**
     * 当type=2的时候 标识forumid
     * 当type=3的时候 标识threadid
     * 当type=4的时候 标识articleid
     * 当type=5的时候 标识频道id
     */
    private String pid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
