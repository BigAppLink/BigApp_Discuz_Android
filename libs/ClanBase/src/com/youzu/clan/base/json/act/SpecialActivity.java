package com.youzu.clan.base.json.act;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zhao on 15/11/20.
 */
public class SpecialActivity implements Serializable {
//    "tid": "769",
//    "uid": "1",
//    "aid": "0",
//    "cost": "100",
//    "starttimefrom": "2015-11-28 14:42",
//    "starttimeto": "0",
//    "place": "上海",
//    "class": "自驾出行",
//    "gender": "1",
//    "number": "111",
//    "applynumber": "0",
//    "expiration": "2016-2-11 14:42",
//    "ufield": {

    private String tid;
    private String uid;
    private String aid;
    private String cost;
    private String starttimefrom;
    private String starttimeto;
    private String place;
    private String classAct;
    private String gender;
    private String number;
    private String applynumber;
    private String expiration;
    private UField ufield;
    private String credit;
    private String credit_title;
    private String thumbattachurl;
    private String allapplynum;
    private String aboutmembers;

    private ArrayList<JoinField> joinFields;
    private String closed;
    private String status;
    private String button;


    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getStarttimefrom() {
        return starttimefrom;
    }

    public void setStarttimefrom(String starttimefrom) {
        this.starttimefrom = starttimefrom;
    }

    public String getStarttimeto() {
        return starttimeto;
    }

    public void setStarttimeto(String starttimeto) {
        this.starttimeto = starttimeto;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getClassAct() {
        return classAct;
    }

    public void setClassAct(String classAct) {
        this.classAct = classAct;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getApplynumber() {
        return applynumber;
    }

    public void setApplynumber(String applynumber) {
        this.applynumber = applynumber;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCredit_title() {
        return credit_title;
    }

    public void setCredit_title(String credit_title) {
        this.credit_title = credit_title;
    }

    public String getThumbattachurl() {
        return thumbattachurl;
    }

    public void setThumbattachurl(String thumbattachurl) {
        this.thumbattachurl = thumbattachurl;
    }

    public String getAllapplynum() {
        return allapplynum;
    }

    public void setAllapplynum(String allapplynum) {
        this.allapplynum = allapplynum;
    }

    public String getAboutmembers() {
        return aboutmembers;
    }

    public void setAboutmembers(String aboutmembers) {
        this.aboutmembers = aboutmembers;
    }

    public ArrayList<JoinField> getJoinFields() {
        return joinFields;
    }

    @JSONField(name = "joinfield")
    public void setJoinFields(ArrayList<JoinField> joinFields) {
        this.joinFields = joinFields;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public UField getUfield() {
        return ufield;
    }

    public void setUfield(UField ufield) {
        this.ufield = ufield;
    }
}
