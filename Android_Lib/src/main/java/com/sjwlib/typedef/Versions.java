package com.sjwlib.typedef;

public class Versions {
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String ver;
    private String vertype;
    private String userid;
    private String uploader;
    private String file;
    private String desc;

    public String getVer() {
        return ver;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private int showlog;

    public String getVertype() {
        return vertype;
    }

    public void setVertype(String vertype) {
        this.vertype = vertype;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getShowlog() {
        return showlog;
    }

    public void setShowlog(int showlog) {
        this.showlog = showlog;
    }
}