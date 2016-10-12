package com.typedef;

public class Versions {
    public String ver;
    public String vertype;
    public String uploader;
    public String desc;
    public String file;
    public int showlog;

    public int formatVersion(){
        String ver = this.ver.replace(".", "");
        return Integer.parseInt(ver);
    }

    public static int formatVersion(String version){
        String ver = version.replace(".","");
        return Integer.parseInt(ver);

    }
}