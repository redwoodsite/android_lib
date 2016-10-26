package com.sjwlib.widget.versionupgrade;

import android.content.Context;

import com.sjwlib.core.typedef.URLData;

/**
 * Created by Administrator on 2016/10/26.
 */
public class VersionContext {
    private Context context;
    private String userid;
    private String appName;
    private String verName;
    private URLData checkUrlData;
    private URLData upgradeUrlData;

    public void setContext(Context context) {
        this.context = context;
    }

    public VersionContext(Context context){
        this.context = context;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    private String applicationId;

    public URLData getCheckUrlData() {
        return checkUrlData;
    }

    public void setCheckUrlData(URLData checkUrlData) {
        this.checkUrlData = checkUrlData;
    }


    public URLData getUpgradeUrlData() {
        return upgradeUrlData;
    }

    public void setUpgradeUrlData(URLData upgradeUrlData) {
        this.upgradeUrlData = upgradeUrlData;
    }

    public Context getContext() {
        return context;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }
}
