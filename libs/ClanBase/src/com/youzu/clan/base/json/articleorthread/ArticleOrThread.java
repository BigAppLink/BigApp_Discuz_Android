package com.youzu.clan.base.json.articleorthread;


import com.kit.utils.StringUtils;
import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.article.Article;

/**
 * Created by tangh on 2015/10/10.
 */
public class ArticleOrThread extends com.youzu.clan.base.json.forumdisplay.Thread {

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

    private String contents;
    private String content;

    private String summary;
    private String pic;
    private String allowcomment;
    private String owncomment;
    private String status;
    private String dateline;

    public Article getArticle() {
        Article article = new Article();
        article.setAid(aid);
        article.setBid(bid);
        article.setCatid(catid);
        article.setUid(uid);
        article.setUsername(username);
        article.setFavid(favid);
        article.setTitle(title);
        article.setAuthor(author);
        article.setHighlight(highlight);
        article.setFrom(from);
        article.setShareUrl(shareUrl);
        article.setUrl(url);
        article.setContent(content);
        article.setContents(contents);
        article.setFromurl(fromurl);
        article.setSummary(summary);
        article.setPic(pic);
        article.setAllowcomment(allowcomment);
        article.setOwncomment(owncomment);
        article.setDateline(dateline);
        article.setStatus(status);
        article.setAttachmentUrls(getAttachmentUrls());
        return article;
    }

    public boolean isArticle() {
        if (StringUtils.isEmptyOrNullOrNullStr(aid)) {
            return false;
        } else {
            return true;
        }
    }

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

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getFavid() {
        return favid;
    }

    public void setFavid(String favid) {
        this.favid = favid;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    @JSONField(name = "share_url")
    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public String getDateline() {
        return dateline;
    }

    @Override
    public void setDateline(String dateline) {
        this.dateline = dateline;
    }
}
