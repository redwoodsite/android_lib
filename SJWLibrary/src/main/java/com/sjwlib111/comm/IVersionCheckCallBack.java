package com.sjwlib111.comm;

/**
 * Created by Administrator on 2016/1/1.
 */
public interface IVersionCheckCallBack {
    public void onNonVersion(); // 无新版本
    public void onCheckFail(String error); // 版本检查失败
    public void onJumpUpdate(); // 跳过更新
}
