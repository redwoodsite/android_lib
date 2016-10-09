package com.sjwlib.net;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sjwlib.activity.WorkProgressActivity;
import com.sjwlib.typedef.ApiRequestParams;
import com.sjwlib.typedef.RequestCallbackBase;
import com.sjwlib.typedef.RequestDataCallback;
import com.sjwlib.typedef.RequestDataParamsPageCallback;
import com.sjwlib.typedef.RequestDataParamsCallback;
import com.sjwlib.typedef.RequestParamsCallback;
import com.sjwlib.typedef.ResponseError;
import com.sjwlib.typedef.RequestStringCallback;
import com.sjwlib.typedef.URLData;
import com.sjwlib.utils.ReflectionUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.apache.http.Header;

import java.lang.reflect.ParameterizedType;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final int timeout = 30000;

    private static WebApi service = null;
    private RequestCall requestCall = null;
    // 配置请求参数
    final String[] regexArray = new String[]{
            "\\d+\\.\\d+\\.\\d+\\.\\d+",
            "(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})T(\\d{1,2}:\\d{1,2}:\\d{1,2})(?:\\.\\d+)?"
    };

    public String getAddress(Context context, String apiKey) {
        final URLData urlData = UrlConfigManager.findAddress(context, apiKey);
        if(urlData!=null){
            String url = urlData.getUrl();
            url = match(url, regexArray[0]);
            return url;
        }else{
            return "";
        }
    }

    private WebApi() {

    }

    public static synchronized WebApi getInstance() {
        if (WebApi.service == null) {
            WebApi.service = new WebApi();
        }
        return WebApi.service;
    }

    // index:泛型参数索引,默认从0开始
    private java.lang.reflect.Type getGenericType(Object callback, int index) {
        java.lang.reflect.Type mySupperClass = callback.getClass().getGenericSuperclass();
        java.lang.reflect.Type type = ((ParameterizedType) mySupperClass).getActualTypeArguments()[index];
        return type;
    }

    public void invokeOkHttp(final Context context,
                             final String apiKey,
                             Map<String, String> params,
                             final RequestCallbackBase callbackBase) {
        final ApiRequestParams apiRequestParams = getApiRequestParams(context, apiKey, callbackBase);
        if (apiRequestParams.isParamsOk()) {
            String url = apiRequestParams.getUrl();
            String netType = apiRequestParams.getNetType();
            final StringCallback callback = apiRequestParams.getResultCallback();
            if (params == null) {
                params = new HashMap();
            }

            try {
                requestCall = null;
                if (netType.equals("get")) {
                    requestCall = OkHttpUtils.get().url(url).params((Map) params).build();
                }
                if (netType.equals("post")) {
                    requestCall = OkHttpUtils.post().url(url).params((Map) params).build();
                }
                requestCall.writeTimeOut(timeout);
                requestCall.execute(callback);
            } catch (Exception ex) {
                String error = "执行Api出现异常:" + ex.getMessage();
                if (callbackBase.isShowProgress()) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                callbackBase.onError(error);
            }
        }
    }

    public void invokeVolley(final Context context,
                            final String apiKey,
                            final Map<String, String> params,
                            final RequestCallbackBase callbackBase) {

        final ApiRequestParams apiRequestParams = getApiRequestParams(context, apiKey, callbackBase);
        if (apiRequestParams.isParamsOk()) {
            String url = apiRequestParams.getUrl();
            String netType = apiRequestParams.getNetType();
            final StringCallback callback = apiRequestParams.getResultCallback();

            // 加入？参数
            String strParams = "";
            Set<Map.Entry<String, String>> sets = params.entrySet();
            for (Map.Entry<String, String> entry : sets) {
                if (strParams.equals(""))
                    strParams = URLEncoder.encode(entry.getKey()) + "=" + URLEncoder.encode(entry.getValue());
                else
                    strParams += "&" + URLEncoder.encode(entry.getKey()) + "=" + URLEncoder.encode(entry.getValue());
            }
            if (!strParams.equals(""))
                url += "?" + strParams;

            // 执行get请求
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String result) {
                    apiRequestParams.getResultCallback().onResponse(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String error = volleyError.getMessage();
                    if (error == null) error = "未知异常!";
                    apiRequestParams.getResultCallback().onError(null, new Exception(error));
                }
            });
            stringRequest.setTag("sjwapi");
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestCall.writeTimeOut(timeout);
            try {
                //requestQueue.cancelAll("sjwapi");
                requestQueue.add(stringRequest);
                //勿加此句，requestQueue.add函数中会自动调用start() arequestQueue.start();
            } catch (Exception ex) {
                String error = "执行Api出现异常:" + ex.getMessage();
                if (callbackBase.isShowProgress()) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                callbackBase.onError(error);
            }
        }
    }

    public void invokeAsyncHttp(final Context context, String apiKey, Map<String, String> params, final RequestCallbackBase callbackBase) {
        final ApiRequestParams apiRequestParams = getApiRequestParams(context, apiKey, callbackBase);
        if (apiRequestParams.isParamsOk()) {
            String url = apiRequestParams.getUrl();
            String netType = apiRequestParams.getNetType();
            try {
                if (params == null) {
                    params = new HashMap();
                }
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.setTimeout(timeout);
                if (netType.equals("get")) {
                    asyncHttpClient.get(url, new RequestParams(params), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int code, Header[] headers, byte[] bytes) {
                            String result = new String(bytes);
                            apiRequestParams.getResultCallback().onResponse(result);
                        }
                        @Override
                        public void onFailure(int code, Header[] headers, byte[] bytes, Throwable throwable) {
                            apiRequestParams.getResultCallback().onError(null, new Exception(throwable.getMessage()));
                        }
                    });
                }
                if (netType.equals("post")) {
                    asyncHttpClient.post(url, new RequestParams(params), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int code, Header[] headers, byte[] bytes) {
                            String result = new String(bytes);
                            apiRequestParams.getResultCallback().onResponse(result);
                        }

                        @Override
                        public void onFailure(int code, Header[] headers, byte[] bytes, Throwable throwable) {
                            apiRequestParams.getResultCallback().onError(null, new Exception(throwable.getMessage()));
                        }
                    });
                }
            } catch (Exception var12) {
                String error = "执行Api出现异常:" + var12.getMessage();
                if (callbackBase.isShowProgress()) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT);
                }
                callbackBase.onError(error);
            }
        }
    }

    /*
    得到回调和URL参数
     */

    private ApiRequestParams getApiRequestParams(final Context context, String apiKey, final RequestCallbackBase callbackBase) {
        ApiRequestParams apiRequestParams = new ApiRequestParams();
        // 根据key从url.xml中找到url结点
        final URLData urlData = UrlConfigManager.findURL(context, apiKey);
        if (urlData == null) {
            String error = "没有找到API!";
            if (callbackBase.isShowError()) // 未找到api强制显示错误
                new AlertDialog.Builder(context)
                        .setTitle("出错了").setMessage(error)
                        .setPositiveButton("确定", null).show();
            callbackBase.onError(error);
            apiRequestParams.setParamsOk(false);
        }else {
            String url = urlData.getUrl();
            // 如果指定局部IP地址，则将url.xml中的地址替换为局部的IP地址
            if (!"".equals(callbackBase.getAddress())) {
                url = url.replaceAll(regexArray[0], callbackBase.getAddress());
            }
            String netType = urlData.getNetType().toLowerCase();

            // 默认创建进度
            final WorkProgressActivity workProgressActivity = new WorkProgressActivity(context);
            // 打开进度
            if (callbackBase.isShowProgress()) {
                if (!callbackBase.getTips().equals(""))
                    workProgressActivity.show(callbackBase.getTips());
                else
                    workProgressActivity.showDef();
            }

            StringCallback callback = new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    if (callbackBase.isShowProgress()) // 关闭进度
                        workProgressActivity.dismiss();
                    String error = e.getMessage();
                    if(error == null) error = "";
                    /*boolean isShowSysteError = !error.equals("Socket closed")
                            && !error.equals("Canceled")
                            && !error.equals("Socket is Closed");
                    if (callbackBase.isShowError() && isShowSysteError)*/
                    if(callbackBase.isShowError()){
                        if(error.startsWith("failed to connect to"))
                            Toast.makeText(context, "无法连接到网络,请稍侯重试!", Toast.LENGTH_SHORT);
                        else
                            new AlertDialog.Builder(context)
                                    .setTitle("出错了").setMessage(error)
                                    .setPositiveButton("确定", null).show();
                    }
                    callbackBase.onError(error);
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
                                    ? jsonObject.getString("response_params")
                                    : "";
                            // [response_page]
                            String page = jsonObject.containsKey(WebApi.response_page)
                                    ? jsonObject.getString("response_page")
                                    : "";
                            // [response_data]
                            String data = jsonObject.containsKey(WebApi.response_data)
                                    ? jsonObject.getString("response_data")
                                    : "";

                            if (false == params.equals("")) {
                                params = params.replaceAll(regexArray[1], "$1 $2");
                            }
                            if (false == page.equals("")) {
                                page = page.replaceAll(regexArray[1], "$1 $2");
                            }
                            if (false == data.equals("")) {
                                data = data.replaceAll(regexArray[1], "$1 $2");
                            }

                            // 无参回调必定执行
                            callbackBase.onSuccess();
                            if (callbackBase.getClass().getSuperclass().equals(RequestStringCallback.class)) { // RequestStringCallback
                                RequestStringCallback stringCallback = (RequestStringCallback) callbackBase;
                                stringCallback.onSuccess(result);
                                stringCallback.onSuccess(result, params);
                                stringCallback.onSuccess(result, params, data);
                            }
                            if (callbackBase.getClass().getSuperclass().equals(RequestParamsCallback.class)) { // RequestParamsCallback
                                java.lang.reflect.Type genericParamsType = getGenericType(callbackBase, 0);
                                Object paramsObject = JSON.parseObject(params, genericParamsType);//
                                RequestParamsCallback paramsCallback = (RequestParamsCallback) callbackBase;
                                paramsCallback.onSuccess(paramsObject);
                            } else if (callbackBase.getClass().getSuperclass().equals(RequestDataCallback.class)) { // RequestDataCallback
                                java.lang.reflect.Type genericDataType = getGenericType(callbackBase, 0);
                                Object dataObject = null;
                                try {
                                    if("".equals(data))
                                        dataObject = ReflectionUtil.newInstance(genericDataType);
                                    else
                                        dataObject = JSON.parseObject(data, genericDataType);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                RequestDataCallback dataCallback = (RequestDataCallback) callbackBase;
                                dataCallback.onSuccess(dataObject);
                            } else if (callbackBase.getClass().getSuperclass().equals(RequestDataParamsCallback.class)) { // RequestDataParamsCallback
                                java.lang.reflect.Type genericParamsType = getGenericType(callbackBase, 0);
                                Object paramsObject = JSON.parseObject(params, genericParamsType);
                                java.lang.reflect.Type genericDataType = getGenericType(callbackBase, 1);
                                Object dataObject = null;
                                try {
                                    if("".equals(data))
                                        dataObject = ReflectionUtil.newInstance(genericDataType);
                                    else
                                        dataObject = JSON.parseObject(data, genericDataType);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                RequestDataParamsCallback dataCallback = (RequestDataParamsCallback) callbackBase;
                                dataCallback.onSuccess(paramsObject, dataObject);
                            } else if (callbackBase.getClass().getSuperclass().equals(RequestDataParamsPageCallback.class)) { // RequestDataParamsPageCallback
                                java.lang.reflect.Type genericParamsType = getGenericType(callbackBase, 0);
                                Object paramsObject = JSON.parseObject(params, genericParamsType);
                                java.lang.reflect.Type genericPageType = getGenericType(callbackBase, 1);
                                Object pageObject = JSON.parseObject(page, genericPageType);
                                java.lang.reflect.Type genericDataType = getGenericType(callbackBase, 2);
                                Object dataObject = JSON.parseObject(data, genericDataType);
                                RequestDataParamsPageCallback dataCallback = (RequestDataParamsPageCallback) callbackBase;
                                dataCallback.onSuccess(paramsObject, pageObject, dataObject);
                            }
                        } else if (result.contains(resp_fail)) { // 处理服务端返回失败的数据
                            ResponseError error = JSON.parseObject(result, ResponseError.class);
                            if (callbackBase.isShowError())
                                new AlertDialog.Builder(context)
                                        .setTitle("出错了").setMessage(error.error_msg)
                                        .setPositiveButton("确定", null).show();
                            callbackBase.onError(error.error_msg);
                        } else { // 接口没有按规约设计
                            String error = "接口不符合规约!";
                            if (callbackBase.isShowError())
                                new AlertDialog.Builder(context)
                                        .setTitle("出错了").setMessage(error)
                                        .setPositiveButton("确定", null).show();
                            callbackBase.onError(error);
                        }
                    }
                }
            };
            apiRequestParams.setParamsOk(true);
            apiRequestParams.setUrl(url);
            apiRequestParams.setResultCallback(callback);
            apiRequestParams.setNetType(netType);
        }

        return apiRequestParams;
    }

    public String match(String input, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return input;
        }
    }

}

/**
 * @param tag              标识
 * @param networkLostTouch 无网络访问时的回调接口
public static <T> void addQueue(Request<T> request, String tag, OnNetworkLostTouch networkLostTouch) {
    //如果request为空，则不予加入请求队列
    if (request == null) {
        return;
    }
    //如果tag标识为空或者空字符串，那么应给予warn，但是仍能继续请求网络
    if (StringUtils.isBlank(tag)) {
        LogCus.w("此次的网络请求未设置有效的tag，将无法通过cancleRequest取消网络请求");
    }

    //先检查网络是否可用
    if (!NetWorkUtils.isNetworkConnected()) {
        networkLostTouch.lostTouch(ResourceUtils.getString(R.string.network_not_avaliable));
        return;
    }

    request.setRetryPolicy(
            new DefaultRetryPolicy(
                    DEFAULT_TIMEOUT_MS,//默认超时时间
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
    );

    request.setTag(tag);
    mTags.add(tag);
    mRequestQueue.add(request);

    //我去，妹的纠结了很久的"com.android.volley.NoConnectionError: java.io.InterruptedIOException"原来就是因为增加了这句话
//        mRequestQueue.start();
}*/