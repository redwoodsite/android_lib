package com.sjw.example.okhttpapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.sjw.example.okhttpapp.R;
import com.sjw.example.okhttpapp.bean.Gps;
import com.sjwlib.typedef.RequestDataCallback;
import com.sjwlib.typedef.RequestParamsCallback;
import com.sjwlib.typedef.SuccessCallback;
import com.sjwlib.net.WebApi;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OkHttpActivity extends AppDataActivity {

    @Bind(R.id.edtResult) EditText edtResult;
    @Bind(R.id.btn_none) Button btn_none;
    @Bind(R.id.btn_string_result) Button btn_string_result;
    @Bind(R.id.btn_string_result_params) Button btn_string_result_params;
    @Bind(R.id.btn_string_result_params_data) Button btn_string_result_params_data;
    @Bind(R.id.btn_object_params) Button btn_object_params;
    @Bind(R.id.btn_object_params_data) Button btn_object_params_data;

    @OnClick(R.id.btn_none) void string_none_click(){
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cphs", "豫J12345:13598870467");
        params.put("top", "1");
        // callback
        SuccessCallback callback = new SuccessCallback() {
            @Override
            public void onSuccess() {
                edtResult.setText("执行成功了!");
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gettopgps", params, callback,true);
    }
    @OnClick(R.id.btn_string_result) void string_result_click(){
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cphs", "豫J12345:13598870467");
        params.put("top", "1");
        // callback
        SuccessCallback callback = new SuccessCallback() {
            @Override
            public void onCallback(String result) {
                edtResult.setText(result);
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gettopgps", params, callback,true);
    }
    @OnClick(R.id.btn_string_result_params) void string_result_params(){
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cphs", "豫J12345:13598870467");
        params.put("top", "1");
        // callback
        SuccessCallback callback = new SuccessCallback() {
            @Override
            public void onCallback(String result, String params) {
                String value = String.format("result=%s\r\n\r\nparams=", result, params);
                edtResult.setText(value);
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gettopgps", params, callback,true);
    }
    @OnClick(R.id.btn_string_result_params_data) void string_result_params_data_click(){
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cphs", "豫J12345:13598870467");
        params.put("top", "1");
        // callback
        SuccessCallback callback = new SuccessCallback() {
            @Override
            public void onCallback(String result, String params, String data) {
                String value = String.format("result=%s\r\n\r\nparams=%s\r\n\r\ndata=%s", result, params, data);
                edtResult.setText(value);
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gettopgps", params, callback,true);
    }
    @OnClick(R.id.btn_object_params) void object_params_click() {
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cphs", "豫J12345:13598870467");
        params.put("top", "1");
        // callback
        RequestParamsCallback<HashMap<String,String>> callback = new RequestParamsCallback<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> params) {
                String value = JSON.toJSONString(params);
                edtResult.setText("params="+value);
            }
        };
        // invoke webapi
        WebApi.getInstance().invoke(this, "gps.gettopgps", params, callback,true);
    }
    @OnClick(R.id.btn_object_params_data) void object_params_data_click() {
        // params
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cphs", "豫J12345:13598870467");
        params.put("top", "0");
        // callback--bean
        RequestDataCallback<HashMap<String,String>, List<Gps>> callback = new RequestDataCallback<HashMap<String, String>, List<Gps>>() {
            @Override
            public void onSuccess(HashMap<String, String> params, List<Gps> dataList) {
                String strParams = JSON.toJSONString(params);
                String strData = JSON.toJSONString(dataList);
                String value = String.format("params=%s\r\n\r\ndata=%s", strParams, strData);
                edtResult.setText(value);
            }
        };
        // invoke api
        WebApi.getInstance().invoke(this, "gps.gettopgps", params, callback, true);

    }
    @OnClick(R.id.btnXUtils) void xutils_click(){
        Intent activtiy = new Intent(this, XUtilsActivity.class);
        startActivity(activtiy);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_okhttp);
        ButterKnife.bind(this);
        btn_none.setText("onSuccess()");
        btn_string_result.setText("String: onSuccess(String result)");
        btn_string_result_params.setText("String: onSuccess(String result, String params)");
        btn_string_result_params_data.setText("String: onSuccess(String result, String params, String data)");
        btn_object_params.setText("Object：onSuccess(T params)");
        btn_object_params_data.setText("Object：onSuccess(E params, F data)");

    }

    @Override
    protected void loadData() {

    }

}
