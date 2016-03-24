package com.youzu.clan.threadandarticle.model;

public class PostData {
//        tid: 帖子id,
//        fid: 板块id,
//        page: 页码,
//        authorid: 作者id,
//        totalPage: 最大页码,
//        maxposition: 最大楼层数

    private String api;

    private String tid;
    private String pid;
    private String fid;

    private String uid;

    private String page;
    private String authorid;
    private int totalPage;
    private int maxposition;

    private String src;



    //跳楼时候要传的楼层数目
    private String postno;

    //Toast的内容
    private String text;
    private String type;



    //点击图片时候的的内容
    private String[] imgArr;
    private int current;




    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getMaxposition() {
        return maxposition;
    }

    public void setMaxposition(int maxposition) {
        this.maxposition = maxposition;
    }

    public String getPostno() {
        return postno;
    }

    public void setPostno(String postno) {
        this.postno = postno;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }




    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String[] getImgArr() {
        return imgArr;
    }

    public void setImgArr(String[] imgArr) {
        this.imgArr = imgArr;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }


    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
