package com.youzu.clan.base.json.thread;

import com.youzu.clan.base.json.model.Variables;

/**
 * Created by wjwu on 2015/12/15.
 */
public class ThreadPayVariables extends Variables {
    /**
     * 本篇主题的售价
     */
    private String price;
    /***
     * 本片主题售出后作者可以得到多少钱（税后收入）
     */
    private String netprice;
    /***
     * 购买本主题后，用户还剩下多少钱
     */
    private String balance;
    /***
     * 消耗的积分类型，比如 金钱、威望等
     */
    private String title;
    /***
     * 作者ID号
     */
    private String authorid;
    /***
     * 作者用户名
     */
    private String author;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNetprice() {
        return netprice;
    }

    public void setNetprice(String netprice) {
        this.netprice = netprice;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
