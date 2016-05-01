package com.sjwlib.net;

import android.app.AlertDialog;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sjwlib.activity.WorkProgressActivity;
import com.sjwlib.typedef.RequestCallbackBase;
import com.sjwlib.typedef.RequestDataCallback;
import com.sjwlib.typedef.RequestParamsCallback;
import com.sjwlib.typedef.ResponseJsonError;
import com.sjwlib.typedef.SuccessCallback;
import com.sjwlib.typedef.URLData;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by yangzhixi on 2016/4/15.
 */
public class WebApi {
    public static final String resp_succ = "resp_status_succ";
    public static final String resp_fail = "resp_status_fail";
    public static final String response_page = "response_page";
    public static final String response_params = "response_params";
    public static final String response_data = "response_data";
    private static WebApi service = null;

    private WebApi() {

    }

    public static synchronized WebApi getInstance() {
        if (WebApi.service == null) {
            WebApi.service = new WebApi();
        }
        return WebApi.service;
    }

    // index:泛型参数索引,默认从0开始
    private java.lang.reflect.Type getGenericType(Object callback, int index){
        java.lang.reflect.Type mySupperClass = callback.getClass().getGenericSuperclass();
        java.lang.reflect.Type type = ((ParameterizedType)mySupperClass).getActualTypeArguments()[index];
        return type;
    }

    public void invoke(final Context context,
                       final String apiKey,
                       Map<String, String> params,
                       final StringCallback callback) {
        invoke(context, apiKey, params, callback, false);
    }

    public void invoke(final Context context,
                       final String apiKey,
                       Map<String, String> params,
                       final StringCallback stringCallback,
                       final boolean showProgress) {
        // 根据key从url.xml中找到url结点
        final URLData urlData = UrlConfigManager.findURL(context, apiKey);
        if (urlData == null) {
            new AlertDialog.Builder(context)
                    .setTitle("出错啦").setMessage("没有找到API!")
                    .setPositiveButton("确定", null).show();
            return;
        }
        // 配置请求参数
        String url = urlData.getUrl().toLowerCase();
        String netType = urlData.getNetType().toLowerCase();

        final WorkProgressActivity workProgressActivity = new WorkProgressActivity(context);
        if (showProgress)
            workProgressActivity.showTips();

        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (showProgress)
                    workProgressActivity.dismiss();
                if (stringCallback != null)
                    stringCallback.onError(call, e);
            }

            @Override
            public void onResponse(String result) {
                if (showProgress)
                    workProgressActivity.dismiss();
                if (stringCallback != null)
                    stringCallback.onResponse(result);
            }
        };
        if (params == null) {
            params = new HashMap();
        }

        if (netType.equals("get")) {
            OkHttpUtils.get().url(url).params((Map) params).build().execute(callback);
        }

        if (netType.equals("post")) {
            OkHttpUtils.post().url(url).params((Map) params).build().execute(callback);
        }
    }

    public void invoke(final Context context,
                       final String apiKey,
                       final Map<String, String> params,
                       final SuccessCallback callback) {
        invoke(context, apiKey, params, callback, true);
    }

    public void invoke(final Context context,
                       final String apiKey,
                       Map<String, String> params,
                       final RequestCallbackBase callbackBase,
                       final boolean showProgress) {
        // 根据key从url.xml中找到url结点
        final URLData urlData = UrlConfigManager.findURL(context, apiKey);
        if (urlData == null) {
            new AlertDialog.Builder(context)
                    .setTitle("出错了").setMessage("没有找到API!")
                    .setPositiveButton("确定", null).show();
            return;
        }
        // 配置请求参数
        String url = urlData.getUrl().toLowerCase();
        String netType = urlData.getNetType().toLowerCase();

        final WorkProgressActivity workProgressActivity = new WorkProgressActivity(context);
        if (showProgress) {
            workProgressActivity.showTips();
        }

        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (showProgress)
                    workProgressActivity.dismiss();
                new AlertDialog.Builder(context)
                        .setTitle("出错了").setMessage(e.getMessage())
                        .setPositiveButton("确定", null).show();
            }
            @Override
            public void onResponse(String result) {
                if (showProgress)
                    workProgressActivity.dismiss();
                if (callbackBase != null) { // 处理默认错误
                    if (result.contains(resp_fail)) {
                        ResponseJsonError error = JSON.parseObject(result, ResponseJsonError.class);
                        new AlertDialog.Builder(context)
                                .setTitle("出错了").setMessage(error.error_msg)
                                .setPositiveButton("确定", null).show();
                    } else // 回传给客户端
                    {
                        if (result.contains(resp_succ)) {
                            JSONObject jsonObject = JSON.parseObject(result);

                            // [response_params]
                            String params = jsonObject.containsKey(WebApi.response_params)
                                    ?jsonObject.getString("response_params")
                                    :"";
                            // [response_data]
                            String data = jsonObject.containsKey(WebApi.response_data)
                                    ?jsonObject.getString("response_data")
                                    :"";

                            // 无参回调必定执行
                            callbackBase.onSuccess();

                            // RequestStringCallBack
                            if(callbackBase.getClass().getSuperclass().equals(SuccessCallback.class)){
                                SuccessCallback stringCallback = (SuccessCallback)callbackBase;
                                stringCallback.onCallback(result);
                                stringCallback.onCallback(result,params);
                                stringCallback.onCallback(result, params, data);
                            } if(callbackBase.getClass().getSuperclass().equals(RequestParamsCallback.class)){ // RequestParamsCallback
                                java.lang.reflect.Type genericParamsType = getGenericType(callbackBase, 0);
                                Object paramsObject = JSON.parseObject(params, genericParamsType);//
                                RequestParamsCallback paramsCallback = (RequestParamsCallback)callbackBase;
                                paramsCallback.onSuccess(paramsObject);
                            }else if(callbackBase.getClass().getSuperclass().equals(RequestDataCallback.class)){ // RequestDataCallback
                                java.lang.reflect.Type genericParamsType = getGenericType(callbackBase, 0);
                                Object paramsObject = JSON.parseObject(params, genericParamsType);
                                java.lang.reflect.Type genericDataType = getGenericType(callbackBase, 1);
                                Object dataObject = JSON.parseObject(data, genericDataType);//
                                RequestDataCallback dataCallback = (RequestDataCallback)callbackBase;
                                dataCallback.onSuccess(paramsObject, dataObject);
                            }

                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("出错了").setMessage("接口定义错误!")
                                    .setPositiveButton("确定", null).show();
                        }
                    }
                }
            }
        };
        // 执行get请求
        if (params == null) {
            params = new HashMap();
        }

        if (netType.equals("get")) {
            OkHttpUtils.get().url(url).params((Map) params).build().execute(callback);
        }

        if (netType.equals("post")) {
            OkHttpUtils.post().url(url).params((Map) params).build().execute(callback);
        }
    }
}