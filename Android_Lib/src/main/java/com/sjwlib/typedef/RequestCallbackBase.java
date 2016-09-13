package com.sjwlib.typedef;

/**
 * Created by yangzhixi on 2016/4/30.
 */
public abstract class RequestCallbackBase {
    private boolean showProgress = true;
    private String address = "";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String tips = "";

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public boolean isShowError() {
        return showError;
    }

    public void setShowError(boolean showError) {
        this.showError = showError;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    private boolean showError = true;

    public void onSuccess(){

    }

    public void onError(String error){

    }

}
