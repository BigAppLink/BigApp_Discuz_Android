package com.youzu.clan.base.json.blog;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wjwu on 2015/12/18.
 */
public class BlogInfo implements Serializable {
    //日志ID
    private String blogid;
    //作者ID
    private String uid;
    private String avatar;
    //日志缩略图，如果为空字符串，代表没有图片
    private String pic;
    //数组，日志的标签，该字段不存在时，表示没有标签
    private ArrayList<String> tag;
    //日志摘要
    private String message;
    //作者用户名
    private String username;
    //日志标题
    private String subject;
    //用户自定义分类的ID号
    private String classid;
    //系统分类的ID号
    private String catid;
    //查看次数
    private String viewnum;
    //回复次数
    private String replynum;
    //热度
    private String hot;
    //发表日期
    private String dateline;
    private String picflag;
    //是否允许回复，如果该字段为1，客户端不要展示回复按钮
    private String noreply;
    //个人定义的分类名称，“随便看看”和"好友的日志"中为空字符串
    private String class_name;
    //系统定义的分类名称
    private String cat_name;
    //该日志的图片列表（仅限jpg/jpeg/png格式）
    private ArrayList<String> image_list;
    //什么人可见，客户端可以考虑根据friendsname展示给用户看--什么情况下允许查看日志
    private BlogFriend friend;
    //当friend为凭密码可见时，本字段表示密码，其他情况无效或不存在，注意，DZ的逻辑中，密码为本地校验，
    // 因此，客户端需要弹框让用户输入密码，以确定用户输入的密码是否和服务器返回的密码一致，一致时再调用详情接口，否则拒绝。
    private String password;
    private String favtimes;
    private String sharetimes;
    private String status;
    private String click1;
    private String click2;
    private String click3;
    private String click4;
    private String click5;
    private String click6;
    private String click7;
    private String click8;
    private String postip;
    private String port;
    private String related;
    private String relatedtime;
    private ArrayList<String> target_ids;
    private String hotuser;
    private String magiccolor;
    private String magicpaper;
    private String pushedaid;
    private String link;
    private String edit;
    private String delete;

    public String getBlogid() {
        return blogid;
    }

    public void setBlogid(String blogid) {
        this.blogid = blogid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getViewnum() {
        return viewnum;
    }

    public void setViewnum(String viewnum) {
        this.viewnum = viewnum;
    }

    public String getReplynum() {
        return replynum;
    }

    public void setReplynum(String replynum) {
        this.replynum = replynum;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getPicflag() {
        return picflag;
    }

    public void setPicflag(String picflag) {
        this.picflag = picflag;
    }

    public String getNoreply() {
        return noreply;
    }

    public void setNoreply(String noreply) {
        this.noreply = noreply;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public ArrayList<String> getImage_list() {
        return image_list;
    }

    public void setImage_list(ArrayList<String> image_list) {
        this.image_list = image_list;
    }

    public BlogFriend getFriend() {
        return friend;
    }

    public void setFriend(BlogFriend friend) {
        this.friend = friend;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFavtimes() {
        return favtimes;
    }

    public void setFavtimes(String favtimes) {
        this.favtimes = favtimes;
    }

    public String getSharetimes() {
        return sharetimes;
    }

    public void setSharetimes(String sharetimes) {
        this.sharetimes = sharetimes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClick1() {
        return click1;
    }

    public void setClick1(String click1) {
        this.click1 = click1;
    }

    public String getClick2() {
        return click2;
    }

    public void setClick2(String click2) {
        this.click2 = click2;
    }

    public String getClick3() {
        return click3;
    }

    public void setClick3(String click3) {
        this.click3 = click3;
    }

    public String getClick4() {
        return click4;
    }

    public void setClick4(String click4) {
        this.click4 = click4;
    }

    public String getClick5() {
        return click5;
    }

    public void setClick5(String click5) {
        this.click5 = click5;
    }

    public String getClick6() {
        return click6;
    }

    public void setClick6(String click6) {
        this.click6 = click6;
    }

    public String getClick7() {
        return click7;
    }

    public void setClick7(String click7) {
        this.click7 = click7;
    }

    public String getClick8() {
        return click8;
    }

    public void setClick8(String click8) {
        this.click8 = click8;
    }

    public String getPostip() {
        return postip;
    }

    public void setPostip(String postip) {
        this.postip = postip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public String getRelatedtime() {
        return relatedtime;
    }

    public void setRelatedtime(String relatedtime) {
        this.relatedtime = relatedtime;
    }

    public ArrayList<String> getTarget_ids() {
        return target_ids;
    }

    public void setTarget_ids(ArrayList<String> target_ids) {
        this.target_ids = target_ids;
    }

    public String getHotuser() {
        return hotuser;
    }

    public void setHotuser(String hotuser) {
        this.hotuser = hotuser;
    }

    public String getMagiccolor() {
        return magiccolor;
    }

    public void setMagiccolor(String magiccolor) {
        this.magiccolor = magiccolor;
    }

    public String getMagicpaper() {
        return magicpaper;
    }

    public void setMagicpaper(String magicpaper) {
        this.magicpaper = magicpaper;
    }

    public String getPushedaid() {
        return pushedaid;
    }

    public void setPushedaid(String pushedaid) {
        this.pushedaid = pushedaid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
