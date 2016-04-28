package com.sjwlib.net;

import android.app.AlertDialog;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sjwlib.activity.WorkProgressActivity;
import com.sjwlib.typedef.ResponseJsonError;
import com.sjwlib.typedef.ResponseResult;
import com.sjwlib.typedef.SuccessCallback;
import com.sjwlib.typedef.URLData;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by yangzhixi on 2016/4/15.
 */
public class WebApi {
    private static final String resp_succ = "resp_status_succ";
    private static final String resp_error = "resp_status_fail";
    private static WebApi service = null;

    private WebApi() {

    }

    public static synchronized WebApi getInstance() {
        if (WebApi.service == null) {
            WebApi.service = new WebApi();
        }
        return WebApi.service;
    }

    public void invoke(final Context context,
                       final String apiKey,
                       Map<String, String> params,
                       final StringCallback callback) {
        invoke(context, apiKey, params, callback,false);
    }

    public void invoke(final Context context,
                       final String apiKey,
                       Map<String, String> params,
                       final StringCallback stringCallback,
                       final boolean showProgress) {
        // 根据key从url.xml中找到url结点
        final URLData urlData = UrlConfigManager.findURL(context, apiKey);
        if(urlData == null){
            new AlertDialog.Builder(context)
                    .setTitle("出错啦").setMessage("没有找到API!")
                    .setPositiveButton("确定", null).show();
            return;
        }
        // 配置请求参数
        String url = urlData.getUrl().toLowerCase();
        String netType = urlData.getNetType().toLowerCase();

        final WorkProgressActivity workProgressActivity = new WorkProgressActivity(context);
        if(showProgress)
            workProgressActivity.showTips();

        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if(showProgress)
                    workProgressActivity.dismiss();
                if(stringCallback!=null)
                    stringCallback.onError(call,e);
            }

            @Override
            public void onResponse(String result) {
                if(showProgress)
                    workProgressActivity.dismiss();
                if(stringCallback!=null)
                    stringCallback.onResponse(result);
            }
        };
        if(params == null) {
            params = new HashMap();
        }

        if(netType.equals("get")) {
            OkHttpUtils.get().url(url).params((Map)params).build().execute(callback);
        }

        if(netType.equals("post")) {
            OkHttpUtils.post().url(url).params((Map)params).build().execute(callback);
        }
    }

    public void invoke(final Context context,
                       final String apiKey,
                       final Map<String,String> params,
                       final SuccessCallback callback) {
        invoke(context, apiKey, params, callback, true);
    }

    public void invoke(final Context context,
                       final String apiKey,
                       Map<String,String> params,
                       final SuccessCallback successCallback,
                       final boolean showProgress) {
        // 根据key从url.xml中找到url结点
        final URLData urlData = UrlConfigManager.findURL(context, apiKey);
        if(urlData == null){
            new AlertDialog.Builder(context)
                    .setTitle("出错了").setMessage("没有找到API!")
                    .setPositiveButton("确定", null).show();
            return;
        }
        // 配置请求参数
        String url = urlData.getUrl().toLowerCase();
        String netType = urlData.getNetType().toLowerCase();

        final WorkProgressActivity workProgressActivity = new WorkProgressActivity(context);
        if(showProgress){
            workProgressActivity.showTips();
        }

        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if(showProgress)
                    workProgressActivity.dismiss();
                new AlertDialog.Builder(context)
                        .setTitle("出错了").setMessage(e.getMessage())
                        .setPositiveButton("确定", null).show();
            }
            @Override
            public void onResponse(String result) {
                if(showProgress)
                    workProgressActivity.dismiss();
                if(successCallback!=null){ // 处理默认错误
                    if(result.contains(resp_error)){
                        ResponseJsonError error = JSON.parseObject(result, ResponseJsonError.class);
                        new AlertDialog.Builder(context)
                                .setTitle("出错了").setMessage(error.error_msg)
                                .setPositiveButton("确定", null).show();
                    }else // 回传给客户端
                    {
                        if(result.contains(resp_succ)){
                            JSONObject jsonObject = JSON.parseObject(result);
                            String params = jsonObject.getString("response_params");
                            String data =  false == jsonObject.containsKey("response_data")
                                    ? "" : jsonObject.getString("response_data");
                            HashMap<String,String> resp_params
                                    = params==null||params.equals("")
                                    ? new HashMap<String, String>()
                                    : JSON.parseObject(params, new TypeReference<HashMap<String, String>>(){});
                            ArrayList<HashMap<String,String>> resp_data
                                    = data==null||data.equals("")
                                    ? new ArrayList<HashMap<String,String>>()
                                    : JSON.parseObject(data, new TypeReference<ArrayList<HashMap<String, String>>>(){});
                            successCallback.onCallback();
                            successCallback.onCallback(result);
                            successCallback.onCallback(result,params);
                            successCallback.onCallback(result,params,data);
                            successCallback.onCallback(resp_params);
                            successCallback.onCallback(resp_params,resp_data);
                            successCallback.onCallback(params,data,resp_params,resp_data);
                        }else{
                            ResponseJsonError error = JSON.parseObject(result, ResponseJsonError.class);
                            new AlertDialog.Builder(context)
                                    .setTitle("出错了").setMessage("接口定义错误!")
                                    .setPositiveButton("确定", null).show();
                        }
                    }
                }
            }
        };
        // 执行get请求
        if(params == null) {
            params = new HashMap();
        }

        if(netType.equals("get")) {
            OkHttpUtils.get().url(url).params((Map)params).build().execute(callback);
        }

        if(netType.equals("post")) {
            OkHttpUtils.post().url(url).params((Map)params).build().execute(callback);
        }
    }
}
