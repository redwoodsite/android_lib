package com.sjwlib.typedef;

/**
 * Created by yangzhixi on 2016/4/30.
 */
public abstract class RequestCallbackBase {
    private boolean showProgress = true;

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

    public void onFail(String error){

    }

}
