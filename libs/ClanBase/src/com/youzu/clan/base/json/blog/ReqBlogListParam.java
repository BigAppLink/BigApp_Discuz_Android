package com.youzu.clan.base.json.blog;

import java.io.Serializable;

/**
 * Created by wjwu on 2015/12/18.
 */
public class ReqBlogListParam implements Serializable {
    /***
     * we：好友的日志（朋友圈）me：我的日志 all：全部日志（日志广场）
     * 当view=all时，加参数order=hot，表示按热度值排序，order=relynum时，表示按回复数排序，默认按发表时间排序
     */
    public String view;
    /***
     * 当view=all时，加参数order=hot，表示按热度值排序，order=relynum时，表示按回复数排序，默认按发表时间排序
     */
    public String order;
    /***
     * 当view=me且uid不为空时，表示查看某用户的日志，即‘他的日志’
     */
    public String uid;
    /***
     * 站点分类ID号
     */
    public String catid;
    /***
     * 用户分类ID号
     */
    public String classid;

    public ReqBlogListParam(String view, String order, String uid, String catid, String classid) {
        this.view = view;
        this.order = order;
        this.uid = uid;
        this.catid = catid;
        this.classid = classid;
    }
}
