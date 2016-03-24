package com.youzu.clan.base.json.config.content;

import java.io.Serializable;

/**
 * Created by wjwu on 2015/12/17.
 */
public class BlogConf implements Serializable {
    /***
     * 是否启用日志广场
     */
    private int enable_blog_square;
    /***
     * 默认展示哪个tab，0：日志广场，1：朋友圈
     */
    private int default_function;
    /***
     * 日志广场的标题
     */
    private String blog_square_title;
    /***
     * 是否启用朋友圈
     */
    private int enable_friend_blog;
    /***
     * 朋友圈的标题
     */
    private String friend_blog;
    /***
     * 日志排序类别 'dateline','replynum','hot' 默认是'dateline'
     */
    private String order;
    /***
     * 排序生效时间范围 0表示一周， 1表示1个月，2表示90天，3表示180天
     */
    private int time_range_type;
    /***
     * 是否启用日志标题自动生成
     */
    private int enable_title_autogen;
    /***
     * 自动截取前N个字符作为日志标题，客户端在发表日志时，如果是自动生成日志标题，请自动截取前N个字符作为subject传递到服务器
     */
    private int blog_title_length;
    /***
     * 是否展示日志标题
     */
    private int enable_title_display;

    public int getEnable_blog_square() {
        return enable_blog_square;
    }

    public void setEnable_blog_square(int enable_blog_square) {
        this.enable_blog_square = enable_blog_square;
    }

    public int getDefault_function() {
        return default_function;
    }

    public void setDefault_function(int default_function) {
        this.default_function = default_function;
    }

    public String getBlog_square_title() {
        return blog_square_title;
    }

    public void setBlog_square_title(String blog_square_title) {
        this.blog_square_title = blog_square_title;
    }

    public int getEnable_friend_blog() {
        return enable_friend_blog;
    }

    public void setEnable_friend_blog(int enable_friend_blog) {
        this.enable_friend_blog = enable_friend_blog;
    }

    public String getFriend_blog() {
        return friend_blog;
    }

    public void setFriend_blog(String friend_blog) {
        this.friend_blog = friend_blog;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getTime_range_type() {
        return time_range_type;
    }

    public void setTime_range_type(int time_range_type) {
        this.time_range_type = time_range_type;
    }

    public int getEnable_title_autogen() {
        return enable_title_autogen;
    }

    public void setEnable_title_autogen(int enable_title_autogen) {
        this.enable_title_autogen = enable_title_autogen;
    }

    public int getBlog_title_length() {
        return blog_title_length;
    }

    public void setBlog_title_length(int blog_title_length) {
        this.blog_title_length = blog_title_length;
    }

    public int getEnable_title_display() {
        return enable_title_display;
    }

    public void setEnable_title_display(int enable_title_display) {
        this.enable_title_display = enable_title_display;
    }
}
