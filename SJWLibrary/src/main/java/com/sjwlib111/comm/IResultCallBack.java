package com.sjwlib111.comm;

/**
 * 结果回调函数
 */
public interface IResultCallBack {

    public void onSuccess();
    public void onFail(String error);
}
