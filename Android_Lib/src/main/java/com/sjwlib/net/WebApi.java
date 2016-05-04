package com.sjwlib.net;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sjwlib.activity.WorkProgressActivity;
import com.sjwlib.typedef.RequestCallbackBase;
import com.sjwlib.typedef.RequestDataCallback;
import com.sjwlib.typedef.RequestDataParamsPageCallback;
import com.sjwlib.typedef.RequestDataParamsCallback;
import com.sjwlib.typedef.RequestParamsCallback;
import com.sjwlib.typedef.ResponseJsonError;
import com.sjwlib.typedef.RequestStringCallback;
import com.sjwlib.typedef.URLData;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;

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
    private RequestCall requestCall = null;


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
                       final RequestCallbackBase callbackBase) {
        // 根据key从url.xml中找到url结点
        final URLData urlData = UrlConfigManager.findURL(context, apiKey);
        if (urlData == null) {
            String error = "没有找到API!";
            if(callbackBase.isShowError()) // 未找到api强制显示错误
                new AlertDialog.Builder(context)
                        .setTitle("出错了").setMessage(error)
                        .setPositiveButton("确定", null).show();
            callbackBase.onFail(error);
            return;
        }
        // 配置请求参数
        String url = urlData.getUrl().toLowerCase();
        String netType = urlData.getNetType().toLowerCase();

        final WorkProgressActivity workProgressActivity = new WorkProgressActivity(context);
        if (callbackBase.isShowProgress()) // 打开进度
            workProgressActivity.showTips();

        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (callbackBase.isShowProgress()) // 关闭进度
                    workProgressActivity.dismiss();
                String error = e.getMessage();
                boolean isShowSysteError = !error.equals("Socket closed")
                        && !error.equals("Canceled")
                        && !error.equals("Socket is Closed");
                if(callbackBase.isShowError() && isShowSysteError)
                    new AlertDialog.Builder(context)
                            .setTitle("出错了").setMessage(error)
                            .setPositiveButton("确定", null).show();
                callbackBase.onFail(error);
            }
            @Override
            public void onResponse(String result) {
                if (callbackBase.isShowProgress()) // 关闭进度
                    workProgressActivity.dismiss();
                if (callbackBase != null) {
                    if (result.contains(resp_succ)) { // 处理服务端返回成功的数据()
                        JSONObject jsonObject = JSON.parseObject(result);
                        // [response_params]
                        String params = jsonObject.containsKey(WebApi.response_params)
                                ?jsonObject.getString("response_params")
                                :"";
                        // [response_page]
                        String page = jsonObject.containsKey(WebApi.response_page)
                                ?jsonObject.getString("response_page")
                                :"";
                        // [response_data]
                        String data = jsonObject.containsKey(WebApi.response_data)
                                ?jsonObject.getString("response_data")
                                :"";

                        if(false == params.equals("")){
                            params = params.replaceAll("(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})T(\\d{1,2}:\\d{1,2}:\\d{1,2})","$1 $2");
                        }
                        if(false == page.equals("")){
                            page = page.replaceAll("(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})T(\\d{1,2}:\\d{1,2}:\\d{1,2})","$1 $2");
                        }
                        if(false == data.equals("")){
                            data = data.replaceAll("(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})T(\\d{1,2}:\\d{1,2}:\\d{1,2})","$1 $2");
                        }

                        // 无参回调必定执行
                        callbackBase.onSuccess();
                        // RequestStringCallBack
                        if(callbackBase.getClass().getSuperclass().equals(RequestStringCallback.class)){
                            RequestStringCallback stringCallback = (RequestStringCallback)callbackBase;
                            stringCallback.onSuccess(result);
                            stringCallback.onSuccess(result,params);
                            stringCallback.onSuccess(result, params, data);
                        } if(callbackBase.getClass().getSuperclass().equals(RequestParamsCallback.class)){ // RequestParamsCallback
                            java.lang.reflect.Type genericParamsType = getGenericType(callbackBase, 0);
                            Object paramsObject = JSON.parseObject(params, genericParamsType);//
                            RequestParamsCallback paramsCallback = (RequestParamsCallback)callbackBase;
                            paramsCallback.onSuccess(paramsObject);
                        }else if(callbackBase.getClass().getSuperclass().equals(RequestDataCallback.class)){ // RequestDataCallback
                            java.lang.reflect.Type genericDataType = getGenericType(callbackBase, 0);
                            Object dataObject = JSON.parseObject(data, genericDataType);//
                            RequestDataCallback dataCallback = (RequestDataCallback)callbackBase;
                            dataCallback.onSuccess(dataObject);
                        }
                        else if(callbackBase.getClass().getSuperclass().equals(RequestDataParamsCallback.class)){ // RequestDataParamsCallback
                            java.lang.reflect.Type genericParamsType = getGenericType(callbackBase, 0);
                            Object paramsObject = JSON.parseObject(params, genericParamsType);
                            java.lang.reflect.Type genericDataType = getGenericType(callbackBase, 1);
                            Object dataObject = JSON.parseObject(data, genericDataType);//
                            RequestDataParamsCallback dataCallback = (RequestDataParamsCallback)callbackBase;
                            dataCallback.onSuccess(paramsObject, dataObject);
                        }
                        else if(callbackBase.getClass().getSuperclass().equals(RequestDataParamsPageCallback.class)){ // RequestDataParamsPageCallback
                            java.lang.reflect.Type genericParamsType = getGenericType(callbackBase, 0);
                            Object paramsObject = JSON.parseObject(params, genericParamsType);
                            java.lang.reflect.Type genericPageType = getGenericType(callbackBase, 1);
                            Object pageObject = JSON.parseObject(page, genericPageType);
                            java.lang.reflect.Type genericDataType = getGenericType(callbackBase, 2);
                            Object dataObject = JSON.parseObject(data, genericDataType);
                            RequestDataParamsPageCallback dataCallback = (RequestDataParamsPageCallback)callbackBase;
                            dataCallback.onSuccess(paramsObject, pageObject, dataObject);
                        }
                    } else if(result.contains(resp_fail)) { // 处理服务端返回失败的数据
                        ResponseJsonError error = JSON.parseObject(result, ResponseJsonError.class);
                        if(callbackBase.isShowError())
                            new AlertDialog.Builder(context)
                                    .setTitle("出错了").setMessage(error.error_msg)
                                    .setPositiveButton("确定", null).show();
                        callbackBase.onFail(error.error_msg);
                    }else { // 接口没有按规约设计
                        String error = "接口不符合规约!";
                            if(callbackBase.isShowError())
                                new AlertDialog.Builder(context)
                                        .setTitle("出错了").setMessage(error)
                                        .setPositiveButton("确定", null).show();
                        callbackBase.onFail(error);
                    }
                }
            }
        };
        // 执行get请求
        if (params == null) {
            params = new HashMap();
        }

        try{
            /*if(requestCall!=null)
                requestCall.cancel();*/
            requestCall = null;
            if (netType.equals("get")) {
                requestCall = OkHttpUtils.get().url(url).params((Map) params).build();
            }
            if (netType.equals("post")) {
                requestCall = OkHttpUtils.post().url(url).params((Map) params).build();
            }
            requestCall.writeTimeOut(30000);
            requestCall.execute(callback);
        }catch (Exception ex){
            String error ="执行Api出现异常:" + ex.getMessage();
            if(callbackBase.isShowProgress()){
                Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
            }
            callbackBase.onFail(error);
        }
    }

    public void cancelRequest(){
        if(requestCall!=null)
            requestCall.cancel();
    }
}