package com.sjwlib.widget.versionupgrade;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import com.sjwlib.core.net.WebApi;
import com.sjwlib.core.typedef.RequestStringCallback;

import java.util.HashMap;
import java.util.Map;

public class VersionManager {

    /*----------------------- 检查新版本 ----------------------*/

    public static void checkVersion(final VersionContext versionContext, final VersionCallback versionCallback){
        Map<String,String> params = new HashMap<String, String>();
        params.put("app", versionContext.getAppName());
        params.put("ver", versionContext.getVerName());
        // callback
        RequestStringCallback callback = new RequestStringCallback() {
            @Override
            public void onError(String error) {
                if(versionCallback!=null)
                    versionCallback.onCheckFail("检查新版本失败!");
            }
            @Override
            public void onSuccess(String result) {
                VersionBean versionBean = null;
                try {
                    String response_params = JSON.parseObject(result).getString("response_params");
                    versionBean = JSON.parseObject(response_params, VersionBean.class);
                } catch (Exception ex) {
                    Toast.makeText(versionContext.getContext(), "查询新版本失败!", Toast.LENGTH_SHORT);
                    return;
                }
                // 软件升级的两种情形：全部升级（userid为空），点对点升级（userid不为空且等于当前userid)
                if (versionBean != null) {
                    if(versionBean.getStatus() == 0){ // status=0不升级
                        if (versionCallback != null)
                            versionCallback.onNonVersion();
                    }else if(versionBean.getStatus() == 1){ // status=1升级
                        // 升级范围：所有用户(userid为null)，特定用户(user不为null，即有1个多个用户标识)
                        if ("".equals(versionBean.getUserid()) || versionBean.getUserid().contains(versionContext.getUserid()))
                            showLogDialog(versionContext, versionBean, versionCallback);
                    }
                } else {
                    if (versionCallback != null)
                        versionCallback.onNonVersion();
                }
            }
        };
        callback.setShowProgress(false);
        callback.setShowError(false);
        // call api
        WebApi.getInstance().invokeOkHttp(versionContext.getContext(), versionContext.getCheckUrlData(), params, callback);
    }

    private static void showLogDialog(final VersionContext versionContext, final VersionBean versionBean, final VersionCallback versionCallback){
        String strLog = "更新于"+versionBean.getUpdateTime()+"\n是否立即升级 %s -> %s？";
        if(versionBean.getShowlog() == 1)
            strLog = String.format( versionBean.getDesc() + "\n\n" + strLog,
                    versionContext.getVerName() + versionBean.getVertype(),
                    versionBean.getVer()+ versionBean.getVertype());
        else
            strLog = String.format(strLog, versionContext.getVerName(),
                    versionBean.getVer());
        Dialog dialogUpdate = new AlertDialog.Builder(versionContext.getContext())
                .setTitle("发现新版本了!")
                .setMessage(strLog)
                // 设置内容
                .setPositiveButton("升级", // 设置确定按钮
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // 进入下载界面
                                Intent intent = new Intent(versionContext.getContext(), DownLoadActivity.class);
                                intent.putExtra("file",versionBean.getFile());
                                intent.putExtra("url", versionContext.getUpgradeUrlData().getUrl());
                                intent.putExtra("applicationId", versionContext.getApplicationId());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                versionContext.getContext().startActivity(intent);
                            }

                        })
                .setNegativeButton("下次再说",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                if(versionCallback!=null)
                                    versionCallback.onJumpUpdate();
                            }
                        }).create();// 创建
        // 显示对话框
        dialogUpdate.show();
    }
}