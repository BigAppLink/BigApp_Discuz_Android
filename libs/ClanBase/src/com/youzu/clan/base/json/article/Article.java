package com.youzu.clan.base.json.article;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tangh on 2015/8/26.
 */
public class Article implements Serializable {

    private String aid;
    private String bid;
    private String catid;
    private String uid;
    private String username;

    private String favid;

    private String title;
    private String author;

    private String highlight;

    private String from;
    private String fromurl;
    private String shareUrl;

    private String url;
    private String summary;
    private String pic;

    private String contents;
    private String content;
    private String allowcomment;
    private String owncomment;


    private String tag;
    private String dateline;
    private ArrayList<String> attachmentUrls;
    private String status;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromurl() {
        return fromurl;
    }

    public void setFromurl(String fromurl) {
        this.fromurl = fromurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAllowcomment() {
        return allowcomment;
    }

    public void setAllowcomment(String allowcomment) {
        this.allowcomment = allowcomment;
    }

    public String getOwncomment() {
        return owncomment;
    }

    public void setOwncomment(String owncomment) {
        this.owncomment = owncomment;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getFavid() {
        return favid;
    }

    public void setFavid(String favid) {
        this.favid = favid;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    @JSONField(name = "share_url")
    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public ArrayList<String> getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(ArrayList<String> attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
