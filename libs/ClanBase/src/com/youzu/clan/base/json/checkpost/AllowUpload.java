package com.youzu.clan.base.json.checkpost;

public class AllowUpload {
    private String jpg;
    private String jpeg;
    private String gif;
    private String png;
    private String mp3;
    private String txt;
    private String zip;
    private String rar;
    private String pdf;

    public AllowUpload() {
    }

    public AllowUpload(String jpg, String jpeg, String gif, String png, String mp3, String txt, String zip, String rar, String pdf) {
        this.jpg = jpg;
        this.jpeg = jpeg;
        this.gif = gif;
        this.png = png;
        this.mp3 = mp3;
        this.txt = txt;
        this.zip = zip;
        this.rar = rar;
        this.pdf = pdf;
    }

    public String getJpg() {
        return jpg;
    }

    public void setJpg(String jpg) {
        this.jpg = jpg;
    }

    public String getJpeg() {
        return jpeg;
    }

    public void setJpeg(String jpeg) {
        this.jpeg = jpeg;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    public String getPng() {
        return png;
    }

    public void setPng(String png) {
        this.png = png;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getRar() {
        return rar;
    }

    public void setRar(String rar) {
        this.rar = rar;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}

