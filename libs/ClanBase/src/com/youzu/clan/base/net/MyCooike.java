package com.youzu.clan.base.net;

import org.apache.http.cookie.Cookie;

import java.util.Date;

/**
 * Created by Zhao on 15/6/23.
 */
public class MyCooike implements Cookie {

    private String name;
    private String value;
    private String comment;
    private String commentURL;
    private Date expiryDate;
    private boolean isPersistent;
    private String domain;
    private String path;
    private int[] ports;
    private boolean isSecure;
    private int version;
    private boolean isExpired;


    public MyCooike(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getCommentURL() {
        return commentURL;
    }

    public void setCommentURL(String commentURL) {
        this.commentURL = commentURL;
    }

    @Override
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean isPersistent() {
        return isPersistent;
    }

    public void setIsPersistent(boolean isPersistent) {
        this.isPersistent = isPersistent;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int[] getPorts() {
        return ports;
    }

    public void setPorts(int[] ports) {
        this.ports = ports;
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    public void setIsSecure(boolean isSecure) {
        this.isSecure = isSecure;
    }

    @Override
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isExpired() {
        return isExpired;
    }

    @Override
    public boolean isExpired(Date date) {
        return false;
    }

    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }
}
